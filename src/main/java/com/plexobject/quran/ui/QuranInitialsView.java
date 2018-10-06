package com.plexobject.quran.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import com.plexobject.quran.ApplicationDelegate;
import com.plexobject.quran.model.ArabicLetter;
import com.plexobject.quran.model.Surat;
import com.plexobject.quran.model.TokenCount;
import com.plexobject.quran.ui.StatsView.WordRenderer;
import com.plexobject.quran.utils.ButtonUtils;

public class QuranInitialsView extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final String[] COL_NAMES = { "Word", "Count", "Sum" };
    private List<TokenCount> counts;
    private JTable table;
    private JLabel infoL;
    private JTextField searchF;
    private JPanel searchP;
    private JComboBox<String> suratCombo;
    private char lastChar;

    AbstractTableModel model = new AbstractTableModel() {
        private static final long serialVersionUID = 1L;

        @Override
        public int getRowCount() {
            return counts != null ? counts.size() : 0;
        }

        @Override
        public int getColumnCount() {
            return COL_NAMES.length;
        }

        @Override
        public String getColumnName(int col) {
            return COL_NAMES[col];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                return counts.get(rowIndex).getToken().text;
            } else if (columnIndex == 1) {
                return counts.get(rowIndex).getCount();
            } else {
                return counts.get(rowIndex).getToken().sum;
            }
        }
    };

    public QuranInitialsView() {
        table = new JTable(model);
        table.getColumnModel().getColumn(0).setCellRenderer(new WordRenderer());
        table.setRowHeight(40);
        table.setFocusable(false);
        table.setRowSelectionAllowed(false);
        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);
        infoL = new JLabel("");
        add(infoL, BorderLayout.SOUTH);
        add(addSearch(), BorderLayout.NORTH);
    }

    private JComponent addSearch() {
        searchP = new JPanel();
        searchP.setLayout(new FlowLayout());
        searchP.setBorder(BorderFactory.createTitledBorder("Search"));

        String[] surahNames = new String[ApplicationDelegate.getInstance()
                .getQuran().getSurats().size() + 1];
        surahNames[0] = "--";
        for (int i = 0; i < ApplicationDelegate.getInstance().getQuran()
                .getSurats().size(); i++) {
            surahNames[i + 1] = (i + 1) + " "
                    + ApplicationDelegate.getInstance().getQuran().getSurats()
                            .get(i).getName()
                    + " " + ApplicationDelegate.getInstance().getQuran()
                            .getSurats().get(i).geteName();
        }
        suratCombo = new JComboBox<String>(surahNames);
        searchP.add(suratCombo);

        searchF = new JTextField(15);
        searchF.setMinimumSize(searchF.getPreferredSize());

        searchP.add(searchF);
        JButton searchB = new JButton();
        searchB.setToolTipText("Filter Search");
        searchB.setActionCommand("search");
        searchB.setIcon(ButtonUtils.loadImage("search.png"));

        searchB.setBorder(BorderFactory.createEmptyBorder());

        searchB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doSearch();
            }
        });
        searchP.add(searchB);

        JButton resetB = new JButton();
        resetB.setToolTipText("Reset search");
        resetB.setIcon(ButtonUtils.loadImage("cancel.png"));

        resetB.setActionCommand("reset");
        resetB.setBorder(BorderFactory.createEmptyBorder());

        resetB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doResetSearch();
            }
        });
        searchP.add(resetB);
        searchF.requestFocusInWindow();

        return searchP;
    }

    public void doResetSearch() {
        ApplicationDelegate.getInstance().fireResetSearch();
        searchF.setText("");
        suratCombo.setSelectedIndex(0);
        doSearch();
    }

    public void search(char ch) {
        lastChar = ch;
        searchF.setText("");
        suratCombo.setSelectedIndex(0);
        doSearch();
    }

    private void doSearch() {
        int surahN = suratCombo.getSelectedIndex();
        Surat surat = surahN == 0 ? null
                : ApplicationDelegate.getInstance().getQuran()
                        .getSurat(surahN - 1);

        counts = ApplicationDelegate.getInstance().getQuran()
                .getArabicCountsByInitial(surat, lastChar);
        String text = searchF.getText().trim();
        if (text.length() > 0) {
            counts = counts.stream().filter(
                    t -> t.getToken().text.matches(ArabicLetter.getRex(text)))
                    .collect(Collectors.toList());
        }
        String suratMsg = surat != null ? ", surat " + surat.geteName() : "";
        infoL.setText("Showing " + counts.size() + " words" + suratMsg);
        model.fireTableDataChanged();
    }

}
