package com.plexobject.quran.persistence;

import java.io.IOException;
import java.util.Map;

public interface DatabaseManager {
	public String[] getAllDatabases() throws IOException;

	public boolean deleteDatabase(String database) throws IOException;

	boolean closeDatabase(String dbname) throws IOException;

	public Map<String, String> getInfo(final String database)
			throws IOException;
}
