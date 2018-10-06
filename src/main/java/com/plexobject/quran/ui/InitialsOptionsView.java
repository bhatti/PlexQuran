package com.plexobject.quran.ui;

import java.awt.BorderLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import com.plexobject.quran.model.ArabicLetter;

public class InitialsOptionsView extends JPanel {
    private static final long serialVersionUID = 1L;
    private JScrollPane initialsScrollList;
    private JList<Character> lettersL;
    List<Character> letters = new ArrayList<Character>();

    public JList<Character> getLettersList() {
        return lettersL;
    }

    public Character getSelectedCharacter() {
        int n = lettersL.getSelectedIndex();
        if (n == -1) {
            return null;
        }
        return letters.get(n);
    }

    private JComponent addList() {
        AbstractListModel<Character> lettersM = new AbstractListModel<Character>() {
            private static final long serialVersionUID = 1L;
            {
                letters = ArabicLetter.getSearchableLetters();
            }

            @Override
            public int getSize() {
                return letters.size();
            }

            @Override
            public Character getElementAt(int index) {
                return letters.get(index);
            }
        };

        lettersL = new JList<Character>(lettersM);
        lettersL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lettersL.setSelectedIndex(0);
        lettersL.setVisibleRowCount(10);
        initialsScrollList = new JScrollPane(lettersL,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        initialsScrollList.setBorder(BorderFactory.createTitledBorder("Arabic Letter(s)"));
        lettersL.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (lettersL.getModel().getSize() > 0
                        && lettersL.getSelectedIndex() == -1) {
                    lettersL.setSelectionInterval(0, 0);
                }
            }
        });

        return initialsScrollList;
    }

    public InitialsOptionsView() {
        setLayout(new BorderLayout());
        add(addList(), BorderLayout.CENTER);
    }

}
