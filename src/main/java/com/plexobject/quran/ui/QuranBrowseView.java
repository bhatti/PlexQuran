package com.plexobject.quran.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;

import com.plexobject.quran.ApplicationDelegate;
import com.plexobject.quran.listeners.SuratSelectionListener;
import com.plexobject.quran.model.Surat;

public class QuranBrowseView extends JPanel implements SuratSelectionListener {
	private static final long serialVersionUID = 1L;
	private static final String[] COL_NAMES = { "Number", "Ayat" };

	private AyatTableModel model;
	private JTable table;
	private JLabel suratNumberL;
	private JLabel suratNameL;
	private JLabel typeL;
	private JPanel infoP;

	public QuranBrowseView() {
		model = new AyatTableModel(COL_NAMES);
		table = new JTable(model);
		table.getColumnModel().getColumn(0).setPreferredWidth(50);
		table.getColumnModel().getColumn(0).setMaxWidth(75);

		for (String name : COL_NAMES) {
			table.getColumn(name).setCellRenderer(new AyatRenderer(false));
		}
		setLayout(new BorderLayout());
		add(new JScrollPane(table), BorderLayout.CENTER);
		infoP = new JPanel();
		infoP.setLayout(new FlowLayout());
		suratNumberL = new JLabel();
		suratNameL = new JLabel();
		typeL = new JLabel();
		infoP.add(suratNumberL);
		infoP.add(new JSeparator());
		infoP.add(suratNameL);
		infoP.add(new JSeparator());
		infoP.add(typeL);
		add(infoP, BorderLayout.NORTH);
		ApplicationDelegate.getInstance().addSuratSelectionListener(this);
	}

	@Override
	public void selectedSurat(Surat surat) {
		suratNumberL.setText(String.valueOf(surat.getNumber()));
		suratNameL.setText(surat.getName() + " (" + surat.geteName() + ")");
		typeL.setText(surat.getType());
		model.setSurat(surat);
		model.fireTableDataChanged();
	}
}
