package com.plexobject.quran.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TokenCounter {
	Map<String, TokenCount> tokens = new TreeMap<String, TokenCount>();

	public int add(Ayat ayat, Token t, int min) {
		if (t.text.length() > min) {
			TokenCount count = tokens.get(t.text);
			if (count == null) {
				count = new TokenCount(ayat, t);
				tokens.put(t.text, count);
				return 1;
			} else {
				return count.increment(ayat);
			}
		}
		return 0;
	}

	public int getSize() {
		return tokens.size();
	}

	public List<TokenCount> getCounts() {
		return getCounts(tokens.size());
	}

	public List<TokenCount> getCounts(int max) {
		List<TokenCount> counts = new ArrayList<TokenCount>(tokens.values());
		Collections.sort(counts);

		while (counts.size() > max) {
			counts.remove(counts.size() - 1);
		}
		return counts;
	}
}
