package com.plexobject.quran.ui;

import javax.swing.AbstractListModel;

import com.plexobject.quran.ApplicationDelegate;
import com.plexobject.quran.model.Surat;

public class SuratTableModel extends AbstractListModel<Surat> {
	private static final long serialVersionUID = 1L;

	@Override
	public int getSize() {
		return ApplicationDelegate.getInstance().getQuran().getSuratCount();
	}

	@Override
	public Surat getElementAt(int index) {
		return ApplicationDelegate.getInstance().getQuran().getSurat(index);
	}

}
