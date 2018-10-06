package com.plexobject.quran.model;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

import com.plexobject.quran.utils.TextHelper;

public class Ayat implements Comparable<Ayat> {
	protected Surat surat;
	protected int number;
	protected String searchableArabic;
	protected String displayableArabic;
	protected String english;
	protected List<Token> arabicWords;
	protected List<Token> englishWords;
	protected List<Token> buckWalterWords;

	protected List<Token> arabicSoundex;
	protected List<Token> englishSoundex;
	protected List<Token> buckWalterSoundex;
	private AlphabetSums sums;

	Ayat() {

	}

	public Ayat(Surat surat, int number, String searchableArabic,
	        String displayableArabic, String english) {
		this.setSurat(surat);
		this.number = number;
		this.searchableArabic = searchableArabic;
		this.displayableArabic = displayableArabic;
		this.english = english;
		arabicWords = tokenize(searchableArabic, true, false);
		englishWords = tokenize(english, false, false);
		buckWalterWords = TextHelper.buckWalter(arabicWords);

		arabicSoundex = TextHelper.arabicSoundex(arabicWords);
		englishSoundex = TextHelper.soundex(englishWords);
		buckWalterSoundex = TextHelper.soundex(buckWalterWords);
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getSearchableArabic() {
		return searchableArabic;
	}

	public void setSearchableArabic(String searchableArabic) {
		this.searchableArabic = searchableArabic;
	}

	public String getDisplayableArabic() {
		return displayableArabic;
	}

	public void setDisplayableArabic(String displayableArabic) {
		this.displayableArabic = displayableArabic;
	}

	public String getEnglish() {
		return english;
	}

	public void setEnglish(String english) {
		this.english = english;
	}

	public List<Token> getArabicWords() {
		return arabicWords;
	}

	public List<Token> getEnglishWords() {
		return englishWords;
	}

	public List<Token> getBuckWalterWords() {
		return buckWalterWords;
	}

	public List<Token> getArabicSoundex() {
		return arabicSoundex;
	}

	public List<Token> getEnglishSoundex() {
		return englishSoundex;
	}

	public List<Token> getBuckWalterSoundex() {
		return buckWalterSoundex;
	}

	private static List<Token> tokenize(String line, boolean arabic,
	        boolean soundex) {
		BreakIterator boundary = BreakIterator.getWordInstance();
		boundary.setText(line);
		List<Token> tokens = new ArrayList<Token>();
		int start = boundary.first();
		for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary
		        .next()) {
			String word = line.substring(start, end);
			tokens.add(new Token(word, arabic, start, end, soundex));
		}
		return tokens;
	}

	public Surat getSurat() {
		return surat;
	}

	public void setSurat(Surat surat) {
		this.surat = surat;
	}

	@Override
	public int compareTo(Ayat o) {
		int cmp = surat.compareTo(o.surat);
		if (cmp == 0) {
			cmp = number - o.number;
		}
		return cmp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + number;
		result = prime * result + ((surat == null) ? 0 : surat.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ayat other = (Ayat) obj;
		if (number != other.number)
			return false;
		if (surat == null) {
			if (other.surat != null)
				return false;
		} else if (!surat.equals(other.surat))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return surat.getName() + " " + surat.getNumber() + "-" + number;
	}

	public AlphabetSums getSums() {
		return sums;
	}

	public void setSums(AlphabetSums sums) {
		this.sums = sums;
	}
}
