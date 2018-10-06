package com.plexobject.quran.ui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.plexobject.quran.ApplicationDelegate;
import com.plexobject.quran.listeners.QuranSearchListener;
import com.plexobject.quran.model.Ayat;
import com.plexobject.quran.model.SearchMatch;

public class QuranSearchView extends JPanel implements QuranSearchListener {
    private static final long serialVersionUID = 1L;
    private static final String[] COL_NAMES = { "Surat", "Ayat #", "Ayat" };

    private AyatTableModel model;
    private JTable table;
    private JLabel infoL;

    public QuranSearchView() {
        model = new AyatTableModel(COL_NAMES);
        table = new JTable(model);
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(0).setMaxWidth(150);
        table.getColumnModel().getColumn(1).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setMaxWidth(75);
        table.setFocusable(false);
        table.setRowSelectionAllowed(false);
        for (String name : COL_NAMES) {
            table.getColumn(name).setCellRenderer(new AyatRenderer(true));
        }
        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);
        infoL = new JLabel("");
        add(infoL, BorderLayout.NORTH);
        ApplicationDelegate.getInstance().addQuranSearchListener(this);
    }

    @Override
    public void search(String text) {
        List<SearchMatch> result = ApplicationDelegate.getInstance().getQuran()
                .search(text);
        boolean partial = result.size() > 0 ? result.get(0).partial : false;
        boolean fuzzy = result.size() > 0 ? result.get(0).tokens.get(0).soundex
                : false;
        infoL.setText("Found " + result.size() + " ayats "
                + (fuzzy ? "fuzzy " : "") + "matched '" + text + "'"
                + (partial ? " partially" : ""));
        model.setAyats(new ArrayList<Ayat>(result));
        model.fireTableDataChanged();
    }

    @Override
    public void resetSearch() {
        model.setAyats(new ArrayList<Ayat>());
        model.fireTableDataChanged();
        infoL.setText("");
    }

}
