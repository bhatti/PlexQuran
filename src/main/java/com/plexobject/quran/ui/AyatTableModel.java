package com.plexobject.quran.ui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.plexobject.quran.model.Ayat;
import com.plexobject.quran.model.Surat;

public class AyatTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private String[] columns;
	private List<Ayat> ayats;

	public AyatTableModel(String[] columns) {
		this.columns = columns;
	}

	public void setSurat(Surat surat) {
		this.ayats = surat.getAyats();
	}

	public void setAyats(List<Ayat> ayats) {
		this.ayats = ayats;
	}

	@Override
	public int getRowCount() {
		return ayats == null ? 0 : ayats.size() * 2;
	}

	@Override
	public int getColumnCount() {
		return columns.length;
	}

	@Override
	public Object getValueAt(int row, int col) {
		int num = row / 2;
		return ayats.get(num);
	}

	@Override
	public Class<?> getColumnClass(int col) {
		return String.class;
	}

	@Override
	public String getColumnName(int col) {
		return columns[col];
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}
}
