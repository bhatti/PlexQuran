package com.plexobject.quran.ui;

import java.awt.BorderLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.plexobject.quran.ApplicationDelegate;
import com.plexobject.quran.model.Surat;

public class BrowseOptionsView extends JPanel {
	private static final long serialVersionUID = 1L;
	private JScrollPane accountsScrollList;

	private JComponent addSuratList() {
		AbstractListModel<Surat> suratsM = new SuratTableModel();

		final JList<Surat> suratsL = new JList<Surat>(suratsM);
		suratsL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		suratsL.setSelectedIndex(0);
		suratsL.setVisibleRowCount(10);
		accountsScrollList = new JScrollPane(suratsL,
		        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		accountsScrollList.setBorder(BorderFactory.createTitledBorder("Ayats"));
		suratsL.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				if (suratsL.getModel().getSize() > 0
				        && suratsL.getSelectedIndex() == -1) {
					suratsL.setSelectionInterval(0, 0);
				}
			}
		});
		suratsL.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				int n = suratsL.getSelectedIndex();
				Surat surat = ApplicationDelegate.getInstance().getQuran()
				        .getSurat(n);
				ApplicationDelegate.getInstance().fireSuratSelected(surat);
			}
		});

		suratsL.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					// viewAccountMenuItem.doClick();
				}
			}
		});
		return accountsScrollList;
	}

	public BrowseOptionsView() {
		setLayout(new BorderLayout());
		add(addSuratList(), BorderLayout.CENTER);
	}

}
