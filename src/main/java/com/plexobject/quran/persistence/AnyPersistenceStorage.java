package com.plexobject.quran.persistence;

import com.plexobject.quran.model.PersistentText;

public class AnyPersistenceStorage extends PersistenceStorageBdb<PersistentText> {

	public AnyPersistenceStorage(String dbname) {
		super(dbname);
	}
}
