package com.plexobject.quran.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.plexobject.quran.model.ArabicLetter;
import com.plexobject.quran.model.Token;

public class TextHelper {
	public static final Map<Character, ArabicLetter> ARABIC_LETTERS = new HashMap<Character, ArabicLetter>();
	static {
		for (ArabicLetter al : ArabicLetter.values()) {
			ARABIC_LETTERS.put(al.arabic, al);
		}
	}

	public static List<Token> combine(List<Token> tokens, Set<String> topWords) {
		List<Token> result = new ArrayList<Token>();
		combine(tokens, 0, 0, result, topWords);
		return result;
	}

	private static void combine(List<Token> tokens, int from, int to,
	        List<Token> result, Set<String> topWords) {
		Map<String, Boolean> matched = new HashMap<String, Boolean>();
		while (from < tokens.size() - 1) {
			StringBuilder sb = new StringBuilder();
			int start = 0;
			int end = 0;
			int count = 0;
			boolean arabic = false;
			for (int i = from; i <= to; i++) {
				Token t = tokens.get(i);
				if (i == from) {
					start = t.start;
				}
				end = t.end;
				arabic = t.arabic;
				if (t.text.length() > 1 && topWords.contains(t.text)) {
					if (sb.length() > 0) {
						sb.append(" ");
					}
					sb.append(t.text);
					count++;
				}
			}
			if (count > 1) {
				String word = sb.toString();
				if (matched.get(word) == null) {
					result.add(new Token(sb.toString(), arabic, start, end,
					        false, to - from + 1));
					matched.put(word, Boolean.TRUE);
				}
			}
			// System.out.println("from " + from + ", to " + to + ", result "
			// + result.size());
			if (from < tokens.size() - 1) {
				if (to >= tokens.size() - 1) {
					from++;
					to = from;
				} else {
					to++;
				}
			}
		}
	}

	public static List<Token> buckWalter(List<Token> tokens) {
		List<Token> buckWalters = new ArrayList<Token>();
		for (Token t : tokens) {
			buckWalters.add(new Token(buckWalter(t.text), t.arabic, t.start,
			        t.end, t.soundex));
		}
		return buckWalters;
	}

	public static String buckWalter(String word) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < word.length(); i++) {
			ArabicLetter al = ARABIC_LETTERS.get(word.charAt(i));
			if (al != null) {
				sb.append(al.buckWalter);
			}
		}
		// System.out.println("Converted " + word + " into " + sb);
		return sb.toString().trim();
	}

	public static List<Token> soundex(List<Token> tokens) {
		List<Token> list = new ArrayList<Token>();
		for (Token t : tokens) {
			list.add(new Token(soundex(t.text), t.arabic, t.start, t.end,
			        true));
		}
		return list;
	}

	public static String soundex(String s) {
		if (s.length() <= 0) {
			return s;
		}
		char[] x = s.toUpperCase().toCharArray();
		char firstLetter = x[0];

		// convert letters to numeric code
		for (int i = 0; i < x.length; i++) {
			switch (x[i]) {
			case 'B':
			case 'F':
			case 'P':
			case 'V': {
				x[i] = '1';
				break;
			}

			case 'C':
			case 'G':
			case 'J':
			case 'K':
			case 'Q':
			case 'S':
			case 'X':
			case 'Z': {
				x[i] = '2';
				break;
			}

			case 'D':
			case 'T': {
				x[i] = '3';
				break;
			}

			case 'L': {
				x[i] = '4';
				break;
			}

			case 'M':
			case 'N': {
				x[i] = '5';
				break;
			}

			case 'R': {
				x[i] = '6';
				break;
			}

			default: {
				x[i] = '0';
				break;
			}
			}
		}

		// remove duplicates
		StringBuilder buffer = new StringBuilder();
		buffer.append(firstLetter);
		for (int i = 1; i < x.length; i++) {
			if (x[i] != x[i - 1] && x[i] != '0') {
				buffer.append(x[i]);
			}
		}
		return buffer.toString();

	}

	public static List<Token> arabicSoundex(List<Token> tokens) {
		List<Token> list = new ArrayList<Token>();
		for (Token t : tokens) {
			list.add(new Token(arabicSoundex(t.text), t.arabic, t.start, t.end,
			        true));
		}
		return list;
	}

	// http://www.codeproject.com/Articles/26880/Arabic-Soundex
	public static String arabicSoundex(String word) {
		switch (word.charAt(0)) {
		case 'ا':
		case 'أ':
		case 'إ':
		case 'آ': {
			word = word.substring(1, word.length() - 1);
		}
			break;

		}
		StringBuilder buffer = new StringBuilder();

		// Size of the word to process
		int size = word.length();
		// Make sure the word is at least two characters in length
		if (size > 1) {

			// Convert the word to character array for faster processing
			char[] chars = word.toCharArray();
			// Buffer to build up with character codes
			// The current and previous character codes
			int prevCode = 0;
			int currCode = 0;
			// Ignore first character and replace it with fixed value
			buffer.append('x');

			// Loop through all the characters and convert them to the proper
			// character code
			for (int i = 1; i < size; i++) {
				switch (chars[i]) {
				case 'ا':
				case 'أ':
				case 'إ':
				case 'آ':
				case 'ح':
				case 'خ':
				case 'ه':
				case 'ع':
				case 'غ':
				case 'ش':
				case 'و':
				case 'ي':
					currCode = 0;
					break;
				case 'ف':
				case 'ب':
					currCode = 1;
					break;

				case 'ج':
				case 'ز':
				case 'س':
				case 'ص':
				case 'ظ':
				case 'ق':
				case 'ك':
					currCode = 2;
					break;
				case 'ت':
				case 'ث':
				case 'د':
				case 'ذ':
				case 'ض':
				case 'ط':
					currCode = 3;
					break;
				case 'ل':
					currCode = 4;
					break;
				case 'م':
				case 'ن':
					currCode = 5;
					break;
				case 'ر':
					currCode = 6;
					break;
				}

				// Check to see if the current code is the same as the last one
				if (currCode != prevCode) {
					// Check to see if the current code is 0 (a vowel); do not
					// process vowels
					if (currCode != 0)
						buffer.append(currCode);
				}
				// Set the new previous character code
				prevCode = currCode;

			}
		}
		return buffer.toString();
	}
}
