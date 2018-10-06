package com.plexobject.quran.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
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
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import com.plexobject.quran.ApplicationDelegate;
import com.plexobject.quran.model.AlphabetSums;
import com.plexobject.quran.model.ArabicLetter;
import com.plexobject.quran.model.Ayat;
import com.plexobject.quran.model.Surat;
import com.plexobject.quran.model.TokenCount;
import com.plexobject.quran.utils.ButtonUtils;

public class StatsView extends JSplitPane {
    private static final long serialVersionUID = 1L;

    static class WordRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;

        public WordRenderer() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
        }

        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row,
                int col) {

            Component c = super.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus, row, col);
            Object valueAt = table.getModel().getValueAt(row, col);
            String s = "";
            if (valueAt != null) {
                s = valueAt.toString();
            }
            setFont(new Font("Arial", Font.BOLD, 16));
            setText(s);
            return c;
        }
    }

    private JLabel infoL;
    private JTextField searchF;
    private JPanel searchP;
    private JTable table;
    private String[] columns;
    private boolean english;
    private JComboBox<String> suratCombo;
    private int selectedOption;

    public StatsView() {
        super(JSplitPane.HORIZONTAL_SPLIT);
        table = new JTable();
        final StatsOptionsView optionsView = new StatsOptionsView();
        setTopComponent(optionsView);
        JPanel bottomP = new JPanel();
        bottomP.setLayout(new BorderLayout());
        bottomP.add(new JScrollPane(table), BorderLayout.CENTER);
        infoL = new JLabel("");
        bottomP.add(infoL, BorderLayout.SOUTH);
        bottomP.add(addSearch(), BorderLayout.NORTH);

        setBottomComponent(bottomP);
        setDividerLocation(200); // 100 ignored in some releases
        addArabicWordsCount(null, null);

        optionsView.getTypeList()
                .addListSelectionListener(new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent e) {
                        selectedOption = optionsView.getTypeList()
                                .getSelectedIndex();
                        doResetSearch();
                        // ApplicationDelegate.getInstance().fireSuratSelected(surat);
                    }
                });

    }

    public void addPhrasesCount(String text, Surat surat) {
        columns = new String[] { "Phrase", "Count", "Sum", "# of Words" };
        List<TokenCount> all = surat != null
                ? ApplicationDelegate.getInstance().getQuran()
                        .getPhraseCounts(surat, 2000)
                : ApplicationDelegate.getInstance().getQuran()
                        .getPhraseCounts(2000);
        english = false;
        configureModel(columns, all, text, surat);
    }

    public void addEnglishWordsCount(String text, Surat surat) {
        columns = new String[] { "English Word", "Count", "Sum" };
        List<TokenCount> all = surat != null
                ? ApplicationDelegate.getInstance().getQuran()
                        .getEnglishWordCounts(surat, 2000)
                : ApplicationDelegate.getInstance().getQuran()
                        .getEnglishWordCounts(2000);
        english = true;
        configureModel(columns, all, text, surat);
    }

    public void addArabicWordsCount(String text, Surat surat) {
        columns = new String[] { "Arabic Word", "Count", "Sum" };
        List<TokenCount> all = surat != null
                ? ApplicationDelegate.getInstance().getQuran()
                        .getArabicWordCounts(surat, 2000)
                : ApplicationDelegate.getInstance().getQuran()
                        .getArabicWordCounts(2000);
        english = false;
        configureModel(columns, all, text, surat);
    }

    public void getSuratSums() {
        columns = new String[] { "Surat", "Letters/Sum",
                "Letters/Sum wo Bismillah", "Muqatta'at Letters/Sum" };
        // "Sum GCD/Sum wo Bismillah GCD",
        infoL.setVisible(false);
        searchP.setVisible(false);

        final List<Surat> surats = ApplicationDelegate.getInstance().getQuran()
                .getSuratWithSums();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setModel(new AbstractTableModel() {
            private static final long serialVersionUID = 1L;

            @Override
            public int getRowCount() {
                return surats.size();
            }

            @Override
            public int getColumnCount() {
                return columns.length;
            }

            @Override
            public String getColumnName(int col) {
                return columns[col];
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                Surat surat = surats.get(rowIndex);
                if (columnIndex == 0) {
                    return surat.getNumber() + " - " + surat.getName();
                    // + " ("
                    // + surat.getNameSum().getLetters() + "/"
                    // + surat.getNameSum().getSums() + ")";
                } else if (columnIndex == 1) {
                    return surat.getSums().getLetters() + "/"
                            + surat.getSums().getSums();
                } else if (columnIndex == 2) {
                    return surat.getSumsWithoutBismilla().getLetters() + "/"
                            + surat.getSumsWithoutBismilla().getSums();
                    // } else if (columnIndex == 3) {
                    // return surat.getSums().getGcd() + "/"
                    // + surat.getSumsWithoutBismilla().getGcd();
                } else {
                    AlphabetSums sums = surat.getMuqataatSums();
                    return sums != null
                            ? sums.getLetters() + "/" + sums.getSums() : "";
                }
            }
        });
        table.getColumnModel().getColumn(0).setCellRenderer(new WordRenderer());
        table.setRowHeight(40);
    }

    public void getAyatSums() {
        infoL.setVisible(false);
        searchP.setVisible(false);

        columns = new String[] { "Surat", "Ayat #", "Letters", "Alphabet Sum" };
        final List<Ayat> ayats = ApplicationDelegate.getInstance().getQuran()
                .getAyatWithSums();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setModel(new AbstractTableModel() {
            private static final long serialVersionUID = 1L;

            @Override
            public int getRowCount() {
                return ayats.size();
            }

            @Override
            public int getColumnCount() {
                return columns.length;
            }

            @Override
            public String getColumnName(int col) {
                return columns[col];
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                Ayat ayat = ayats.get(rowIndex);
                if (columnIndex == 0) {
                    return ayat.getSurat().getName();
                } else if (columnIndex == 1) {
                    return String.valueOf(ayat.getNumber());
                } else if (columnIndex == 2) {
                    return String.valueOf(ayat.getSums().getLetters());
                } else {
                    return String.valueOf(ayat.getSums().getSums());
                }
            }
        });
        table.getColumnModel().getColumn(0).setCellRenderer(new WordRenderer());
        table.setRowHeight(40);
    }

    public void addArabicLettersCount(String text, Surat surat) {
        columns = new String[] { "Letter", "Count", "Sum" };
        List<TokenCount> all = surat != null
                ? ApplicationDelegate.getInstance().getQuran()
                        .getArabicLettersCount(surat)
                : ApplicationDelegate.getInstance().getQuran()
                        .getArabicLettersCount();
        english = false;
        configureModel(columns, all, text, surat);
    }

    public void addEnglishLettersCount(String text, Surat surat) {
        columns = new String[] { "Letter", "Count", "Sum" };
        List<TokenCount> all = surat != null
                ? ApplicationDelegate.getInstance().getQuran()
                        .getEnglishLettersCount(surat)
                : ApplicationDelegate.getInstance().getQuran()
                        .getEnglishLettersCount();
        english = true;
        configureModel(columns, all, text, surat);
    }

    private void configureModel(final String[] columns,
            final List<TokenCount> all, String text, Surat surat) {
        final List<TokenCount> counts = all.stream()
                .filter(t -> suratMatches(text, surat, t))
                .collect(Collectors.toList());
        String textMsg = text != null && text.length() > 0 ? " for " + text
                : "";
        String suratMsg = surat != null ? ", surat " + surat.geteName() : "";
        infoL.setText(
                "Showing " + counts.size() + " words" + textMsg + suratMsg);

        infoL.setVisible(true);
        searchP.setVisible(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setModel(new AbstractTableModel() {
            private static final long serialVersionUID = 1L;

            @Override
            public int getRowCount() {
                return counts.size();
            }

            @Override
            public int getColumnCount() {
                return columns.length;
            }

            @Override
            public String getColumnName(int col) {
                return columns[col];
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                if (columnIndex == 0) {
                    return counts.get(rowIndex).getToken().text;
                } else if (columnIndex == 1) {
                    return counts.get(rowIndex).getCount();
                } else if (columnIndex == 2) {
                    return counts.get(rowIndex).getToken().sum;
                } else {
                    return counts.get(rowIndex).getToken().numWords;
                }
            }
        });
        table.getColumnModel().getColumn(0).setCellRenderer(new WordRenderer());
        table.setRowHeight(40);
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

    protected void doResetSearch() {
        ApplicationDelegate.getInstance().fireResetSearch();
        searchF.setText("");
        suratCombo.setSelectedIndex(0);
        updateData(null, null);
    }

    private boolean suratMatches(String text, Surat surat, TokenCount tc) {
        if (surat == null && (text == null || text.trim().length() == 0)) {
            return true;
        }
        if (surat != null && !tc.getSurats().contains(surat)) {
            return false;
        }
        if (text != null && text.trim().length() > 0) {
            if (english) {
                return tc.getToken().text.contains(text);
            }

            return tc.getToken().text.matches(ArabicLetter.getRex(text));
        }
        return true;
    }

    protected void doSearch() {
        String text = searchF.getText().trim();
        int surahN = suratCombo.getSelectedIndex();
        Surat surat = surahN == 0 ? null
                : ApplicationDelegate.getInstance().getQuran()
                        .getSurat(surahN - 1);
        updateData(text, surat);
    }

    private void updateData(String text, Surat surat) {
        switch (selectedOption) {
        case 0:
            addArabicWordsCount(text, surat);
            break;
        case 1:
            ApplicationDelegate.getInstance().doInBackGround(new Runnable() {
                @Override
                public void run() {
                    addPhrasesCount(text, surat);
                }
            });
            break;
        case 2:
            addEnglishWordsCount(text, surat);
            break;
        case 3:
            getSuratSums();
            break;
        case 4:
            getAyatSums();
            break;
        case 5:
            addArabicLettersCount(text, surat);
            break;
        default:
        }
    }
}
