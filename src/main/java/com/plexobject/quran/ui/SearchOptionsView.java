package com.plexobject.quran.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.plexobject.quran.ApplicationDelegate;
import com.plexobject.quran.utils.ButtonUtils;

public class SearchOptionsView extends JPanel {
    private static final long serialVersionUID = 1L;
    private JTextField searchF;
    private JPanel searchP;

    private JComponent addSearch() {
        searchP = new JPanel();
        searchP.setLayout(new FlowLayout());
        searchP.setBorder(BorderFactory.createTitledBorder("Search"));

        searchF = new JTextField(15);
        searchF.setMinimumSize(searchF.getPreferredSize());
        setupAutoComplete(new ArrayList<String>());

        searchP.add(searchF);
        JButton searchB = new JButton();
        searchB.setToolTipText("Search Quran");
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

    private void setupAutoComplete(final Collection<String> items) {
        final DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        int i = 0;
        for (String item : items) {
            model.addElement(item);
            if (++i > 20) {
                break;
            }
        }
        final JComboBox<String> cbInput = new JComboBox<String>(model) {
            private static final long serialVersionUID = 1L;

            public Dimension getPreferredSize() {
                return new Dimension(super.getPreferredSize().width, 0);
            }
        };
        cbInput.setMinimumSize(new Dimension(200, 200));

        setAdjusting(cbInput, false);
        cbInput.setSelectedItem(null);
        cbInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isAdjusting(cbInput)) {
                    if (cbInput.getSelectedItem() != null) {
                        searchF.setText(cbInput.getSelectedItem().toString());
                    }
                }
            }
        });

        searchF.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                setAdjusting(cbInput, true);
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    cbInput.setPopupVisible(false);
                    doResetSearch();
                    // dbActions.resetSearch();
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (cbInput.getSelectedItem() != null) {
                        searchF.setText(cbInput.getSelectedItem().toString());
                    }
                    cbInput.setPopupVisible(false);
                    doSearch();
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (cbInput.isPopupVisible()) {
                        e.setKeyCode(KeyEvent.VK_ENTER);
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_UP
                        || e.getKeyCode() == KeyEvent.VK_DOWN) {
                    e.setSource(cbInput);
                    cbInput.dispatchEvent(e);
                }
                setAdjusting(cbInput, false);
            }
        });
        searchF.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updateList();
            }

            public void removeUpdate(DocumentEvent e) {
                updateList();
            }

            public void changedUpdate(DocumentEvent e) {
                updateList();
            }

            private void updateList() {
                setAdjusting(cbInput, true);
                model.removeAllElements();
                String input = searchF.getText();
                if (input.length() > 2) {
                    List<String> result = ApplicationDelegate.getInstance()
                            .getQuran().lookup(input);
                    for (String t : result) {
                        model.addElement(t);
                    }
                }
                cbInput.setPopupVisible(model.getSize() > 0);
                setAdjusting(cbInput, false);
            }
        });
        searchF.setLayout(new BorderLayout());
        searchF.add(cbInput, BorderLayout.SOUTH);
    }

    private static boolean isAdjusting(JComboBox<String> cbInput) {
        if (cbInput.getClientProperty("is_adjusting") instanceof Boolean) {
            return (Boolean) cbInput.getClientProperty("is_adjusting");
        }
        return false;
    }

    private static void setAdjusting(JComboBox<String> cbInput,
            boolean adjusting) {
        cbInput.putClientProperty("is_adjusting", adjusting);
    }

    protected void doSearch() {
        String text = searchF.getText().trim();
        if (text.length() > 0) {
            ApplicationDelegate.getInstance().fireSearch(text);
        } else {
            ApplicationDelegate.getInstance().fireResetSearch();
        }
    }

    protected void doResetSearch() {
        ApplicationDelegate.getInstance().fireResetSearch();
        searchF.setText("");
    }

    public SearchOptionsView() {
        setLayout(new BorderLayout());
        add(addSearch(), BorderLayout.NORTH);
    }
}
