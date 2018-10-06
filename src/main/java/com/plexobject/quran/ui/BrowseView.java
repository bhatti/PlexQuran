package com.plexobject.quran.ui;

import javax.swing.JSplitPane;

public class BrowseView extends JSplitPane {
	private static final long serialVersionUID = 1L;
	private QuranBrowseView quranBrowseView;
	private BrowseOptionsView sidePanel;

	public BrowseView() {
		super(JSplitPane.HORIZONTAL_SPLIT);
		quranBrowseView = new QuranBrowseView();
		sidePanel = new BrowseOptionsView();
		setTopComponent(sidePanel);
		setBottomComponent(quranBrowseView);
		setDividerLocation(300); // 100 ignored in some releases

	}
}
