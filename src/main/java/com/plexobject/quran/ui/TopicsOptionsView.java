package com.plexobject.quran.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.plexobject.quran.ApplicationDelegate;
import com.plexobject.quran.model.Topic;
import com.plexobject.quran.utils.ButtonUtils;

public class TopicsOptionsView extends JPanel {
	private static final long serialVersionUID = 1L;
	private JScrollPane initialsScrollList;
	private JList<Topic> topicsL;
	private JTextField searchF;
	private JPanel searchP;

	private List<Topic> allTopics = ApplicationDelegate.getInstance()
	        .getQuran().getTopics();
	private List<Topic> topics = new ArrayList<Topic>(allTopics);

	public JList<Topic> getTopicsList() {
		return topicsL;
	}

	public Topic getSelectedTopic() {
		int n = topicsL.getSelectedIndex();
		if (n == -1) {
			return null;
		}
		return topics.get(n);
	}

	private JComponent addList() {
		topicsL = new JList<Topic>();
		topicsL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		topicsL.setSelectedIndex(0);
		topicsL.setVisibleRowCount(10);
		topicsL.setListData(topics.toArray(new Topic[topics.size()]));
		initialsScrollList = new JScrollPane(topicsL,
		        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		initialsScrollList
		        .setBorder(BorderFactory.createTitledBorder("Topics"));
		topicsL.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				if (topicsL.getModel().getSize() > 0
				        && topicsL.getSelectedIndex() == -1) {
					topicsL.setSelectionInterval(0, 0);
				}
			}
		});

		return initialsScrollList;
	}

	private JComponent addSearch() {
		searchP = new JPanel();
		searchP.setLayout(new FlowLayout());
		searchP.setBorder(BorderFactory.createTitledBorder("Filter"));

		searchF = new JTextField(15);
		searchF.setMinimumSize(searchF.getPreferredSize());
		searchF.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				// This method never seems to be called
			}

			public void insertUpdate(DocumentEvent e) {
			}

			public void removeUpdate(DocumentEvent e) {
			}
		});
		searchF.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					doResetSearch();
					// dbActions.resetSearch();
				} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					doSearch();
				}
			}
		});
		searchP.add(searchF);
		JButton searchB = new JButton();
		searchB.setToolTipText("Filter List");
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

	protected void doSearch() {
		String text = searchF.getText().trim().toLowerCase();
		topics.clear();
		if (text.length() > 0) {
			for (Topic t : allTopics) {
				if (t.getName().toLowerCase().contains(text)) {
					topics.add(t);
				}
			}
		} else {
			topics.addAll(allTopics);
		}
		topicsL.setListData(topics.toArray(new Topic[topics.size()]));
	}

	protected void doResetSearch() {
		topics.clear();
		topics.addAll(allTopics);
		topicsL.setListData(topics.toArray(new Topic[topics.size()]));
		searchF.setText("");
	}

	public TopicsOptionsView() {
		setLayout(new BorderLayout());
		add(addList(), BorderLayout.CENTER);
		add(addSearch(), BorderLayout.NORTH);
	}

}
