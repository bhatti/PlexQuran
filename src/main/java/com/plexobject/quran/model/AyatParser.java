package com.plexobject.quran.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AyatParser {
	private AyatParser() {
	}

	public static void addAyats(List<Surat> surats) throws IOException {
		List<String> displayableArabic = read("/quran-uthmani.txt");
		List<String> searchableArabic = read("/quran-simple-clean.txt");
		List<String> english = read("/en.yusufali.txt");
		if (searchableArabic.size() != 6236) {
			throw new RuntimeException("Incorrect number of arabic ayats "
			        + searchableArabic.size());
		}
		if (displayableArabic.size() != 6236) {
			throw new RuntimeException("Incorrect number of arabic ayats "
			        + displayableArabic.size());
		}
		if (english.size() != 6236) {
			throw new RuntimeException("Incorrect number of english ayats "
			        + english.size());
		}
		for (int i = 0; i < english.size(); i++) {
			String enLine = english.get(i);
			String arLine = searchableArabic.get(i);
			String arDisplayLine = displayableArabic.get(i);
			String[] tokens = enLine.split("\\|");

			int suratNumber = Integer.parseInt(tokens[0]);
			int ayatNumber = Integer.parseInt(tokens[1]);
			Surat surat = surats.get(suratNumber - 1);
			Ayat ayat = new Ayat(surat, ayatNumber, arLine, arDisplayLine,
			        tokens[2]);
			surat.addAyat(ayat);
		}
		for (Surat s : surats) {
			if (s.getAyasCount() != s.getAyats().size()) {
				throw new RuntimeException("Surat " + s.getNumber()
				        + " should have had " + s.getAyasCount()
				        + " ayats but found " + s.getAyats().size());
			}
		}
	}

	private static List<String> read(String name) throws IOException {
		InputStream in = AyatParser.class.getResourceAsStream(name);

		if (in == null) {
			throw new FileNotFoundException("Failed to find " + name);
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(in,
		        "UTF-8"));
		List<String> lines = new ArrayList<String>();
		String line;
		while ((line = reader.readLine()) != null) {
			lines.add(line.trim());
		}
		reader.close();
		return lines;
	}

}
