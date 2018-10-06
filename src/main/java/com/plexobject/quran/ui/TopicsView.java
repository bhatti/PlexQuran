package com.plexobject.quran.ui;

import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.plexobject.quran.model.Topic;

public class TopicsView extends JSplitPane {
	private static final long serialVersionUID = 1L;
	private QuranTopicsView mainView;
	private TopicsOptionsView sidePanel;

	public TopicsView() {
		super(JSplitPane.HORIZONTAL_SPLIT);
		mainView = new QuranTopicsView();
		sidePanel = new TopicsOptionsView();
		setTopComponent(sidePanel);
		setBottomComponent(mainView);
		setDividerLocation(300); // 100 ignored in some releases

		sidePanel.getTopicsList().addListSelectionListener(
		        new ListSelectionListener() {
			        public void valueChanged(ListSelectionEvent e) {
				        Topic topic = sidePanel.getSelectedTopic();
				        if (topic != null) {
					        mainView.search(topic.getName());
				        }
			        }
		        });
	}

}
