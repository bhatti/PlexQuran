package com.plexobject.quran.ui;

import javax.swing.JSplitPane;

public class SearchView extends JSplitPane {
	private static final long serialVersionUID = 1L;
	private QuranSearchView quranSearchView;
	private SearchOptionsView sidePanel;

	public SearchView() {
		super(JSplitPane.HORIZONTAL_SPLIT);
		quranSearchView = new QuranSearchView();
		sidePanel = new SearchOptionsView();
		setTopComponent(sidePanel);
		setBottomComponent(quranSearchView);
		setDividerLocation(300); // 100 ignored in some releases

	}
}
