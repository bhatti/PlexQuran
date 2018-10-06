package com.plexobject.quran.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.plexobject.quran.model.Ayat;

public class AyatRenderer implements TableCellRenderer {
	MultilineHeader mlh;
	boolean displaySimpleArabic;

	public AyatRenderer(boolean displaySimpleArabic) {
		this.displaySimpleArabic = displaySimpleArabic;
		mlh = new MultilineHeader();
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
	        boolean isSelected, boolean hasFocus, int row, int col) {
		Ayat ayat = (Ayat) value;
		boolean arabic = row % 2 == 0;
		int height = ((ayat.getEnglish().length() / 80) + 1) * 25 + 15;

		table.setRowHeight(row, height);
		if (arabic) {
			mlh.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			mlh.setFont(new Font("Arial", Font.BOLD, 16));
		} else {
			mlh.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			mlh.setFont(new Font("Arial", Font.PLAIN, 16));
		}

		if (isSelected && col == 1) {
			mlh.setBackground(Color.decode("#9999FF"));
		} else {
			mlh.setBackground(table.getBackground());
		}
		String text = null;
		String colName = table.getColumnName(col);

		if ("Surat".equalsIgnoreCase(colName)) {
			text = arabic ? "" : ayat.getSurat().getNumber() + " - "
			        + ayat.getSurat().getName();
		} else if ("Ayat".equalsIgnoreCase(colName)) {
			text = arabic ? displaySimpleArabic ? ayat.getSearchableArabic()
			        : ayat.getDisplayableArabic() : ayat.getEnglish();
		} else if ("Number".equalsIgnoreCase(colName)
		        || "Ayat #".equalsIgnoreCase(colName)) {
			text = arabic ? "" : String.valueOf(ayat.getNumber());
		}
		mlh.setText("<b><p style=vertical-align:\"center\">" + text
		        + "</p></b>");
		// mlh.setText(text);
		return mlh;
	}
}
