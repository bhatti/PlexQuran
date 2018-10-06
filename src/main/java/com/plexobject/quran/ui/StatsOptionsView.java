package com.plexobject.quran.ui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class StatsOptionsView extends JPanel {
    private static final long serialVersionUID = 1L;
    private JScrollPane statsList;
    private JList<String> typeList;

    public StatsOptionsView() {
        setLayout(new BorderLayout());
        add(addStatsList(), BorderLayout.CENTER);
    }

    public JList<String> getTypeList() {
        return typeList;
    }

    private JComponent addStatsList() {
        List<String> data = new ArrayList<>();
        data.addAll(Arrays.asList("Top Arabic Words", "Top Phrases",
                "Top English Words", "Surat Sums", "Ayats Sums",
                "Arabic Alphabets"));
        typeList = new JList<String>(data.toArray(new String[data.size()]));
        typeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        typeList.setSelectedIndex(0);
        typeList.setVisibleRowCount(10);
        statsList = new JScrollPane(typeList,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        statsList.setBorder(BorderFactory.createTitledBorder("Stats Type"));
        return statsList;
    }

}
