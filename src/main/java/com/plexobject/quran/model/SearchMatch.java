package com.plexobject.quran.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchMatch extends Ayat {
	public final List<Token> tokens = new ArrayList<Token>();
	public final boolean partial;

	public SearchMatch(Ayat ayat, boolean partial) {
		this.surat = ayat.getSurat();
		this.number = ayat.number;
		this.searchableArabic = ayat.searchableArabic;
		this.displayableArabic = ayat.displayableArabic;
		this.english = ayat.english;
		this.arabicWords = ayat.arabicWords;
		this.englishWords = ayat.englishWords;
		this.buckWalterWords = ayat.buckWalterWords;
		this.arabicSoundex = ayat.arabicSoundex;
		this.englishSoundex = ayat.englishSoundex;
		this.buckWalterSoundex = ayat.buckWalterSoundex;
		this.partial = partial;
	}

	public void add(Token t) {
		this.tokens.add(t);
		Collections.sort(tokens);
	}

	@Override
	public String getSearchableArabic() {
		List<Token> arTokens = new ArrayList<Token>();
		for (Token t : tokens) {
			if (t.arabic) {
				arTokens.add(t);
			}
		}
		if (arTokens.size() == 0) {
			return super.getSearchableArabic();
		}
		return getMatching(super.getSearchableArabic(), arTokens);
	}

	public String getEnglish() {
		List<Token> enTokens = new ArrayList<Token>();
		for (Token t : tokens) {
			if (!t.arabic) {
				enTokens.add(t);
			}
		}
		if (enTokens.size() == 0) {
			return super.getEnglish();
		}
		return getMatching(super.getEnglish(), enTokens);
	}

	private static String getMatching(String line, List<Token> tokens) {
		StringBuilder sb = new StringBuilder();
		int prev = 0;
		for (Token t : tokens) {
			String prefix = line.substring(prev, t.start);
			sb.append(prefix);
			sb.append("<i><big><font color=\"#336600\">"
			        + line.substring(t.start, t.end) + "</font></big></i>");
			prev = t.end;
		}
		return sb.toString();
	}

	@Override
	public int compareTo(Ayat o) {
		int cmp = surat.compareTo(o.surat);
		if (cmp == 0) {
			cmp = number - o.number;
		}
		if (cmp == 0 && o instanceof SearchMatch) {
			SearchMatch other = (SearchMatch) o;
			for (int i = 0; i < tokens.size() && i < other.tokens.size(); i++) {
				cmp = tokens.get(i).compareTo(other.tokens.get(i));
				if (cmp != 0) {
					break;
				}
			}
		}
		return cmp;
	}
}
