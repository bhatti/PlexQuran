package com.plexobject.quran.ui;

import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class InitialsView extends JSplitPane {
    private static final long serialVersionUID = 1L;
    private QuranInitialsView mainView;
    private InitialsOptionsView sidePanel;

    public InitialsView() {
        super(JSplitPane.HORIZONTAL_SPLIT);
        mainView = new QuranInitialsView();
        sidePanel = new InitialsOptionsView();
        setTopComponent(sidePanel);
        setBottomComponent(mainView);
        setDividerLocation(300); // 100 ignored in some releases

        sidePanel.getLettersList()
                .addListSelectionListener(new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent e) {
                        Character ch = sidePanel.getSelectedCharacter();
                        if (ch != null) {
                            mainView.search(ch);
                        }
                    }
                });
    }

}
