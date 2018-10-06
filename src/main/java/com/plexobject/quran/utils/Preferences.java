package com.plexobject.quran.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Preferences {
	private static final String PLEXQURAN_PROPERTIES = "plexquran.properties";
	private static final String PREF_FILE = System.getProperty("user.home")
			+ System.getProperty("file.separator") + PLEXQURAN_PROPERTIES;
	private static Properties preferences;
	private static String propertiesFile;

	public static String get(String name, String defaultValue) {
		String retVal = preferences.getProperty(name, defaultValue);
		return retVal;
	}

	public static int getInt(String name, int defaultValue) {
		String cfgVal = preferences.getProperty(name);
		int retVal = defaultValue;
		try {
			retVal = Integer.parseInt(cfgVal);
		} catch (NumberFormatException e) {

		}

		return retVal;
	}

	public static String get(String name) {
		return get(name, null);
	}

	public static void set(String name, String value) {
		preferences.setProperty(name, value);
	}

	public static void load() throws IOException {
		propertiesFile = System.getProperty(PLEXQURAN_PROPERTIES);
		if (propertiesFile == null || propertiesFile.trim().equals("")) {
			propertiesFile = PREF_FILE;
		}

		// Create propertiesFile directories if it doesn't exist
		File prefs = new File(propertiesFile);
		prefs.getParentFile().mkdirs();
		preferences = new Properties();
		if (prefs.exists()) {
			preferences.load(new FileInputStream(propertiesFile));
		}
	}

	public static void save() throws IOException {
		preferences.store(new FileOutputStream(propertiesFile),
				"Universal Password Manager Preferences");
	}
}
