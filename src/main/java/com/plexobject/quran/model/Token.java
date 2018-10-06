package com.plexobject.quran.model;

import com.plexobject.quran.utils.TextHelper;

public class Token implements Comparable<Token> {
    public final String text;
    public final boolean arabic;
    public final int start;
    public final int end;
    public final int numWords;
    public final boolean soundex;
    public final long sum;

    public Token(String word) {
        this(word, false, 0, 0, true, 0);
    }

    public Token(String word, boolean arabic, int start, int end,
            boolean soundex) {
        this(word, arabic, start, end, soundex, 0);
    }

    public Token(String word, boolean arabic, int start, int end,
            boolean soundex, int numWords) {
        this.text = word;
        this.arabic = arabic;
        this.start = start;
        this.end = end;
        this.soundex = soundex;
        this.numWords = numWords;
        if (arabic) {
            AlphabetSums sums = new AlphabetSums(0);
            for (char ch : text.toCharArray()) {
                ArabicLetter letter = TextHelper.ARABIC_LETTERS.get(ch);
                if (letter != null) {
                    sums.add(letter.getNumber());
                } else if (ch >= 'ا') {
                    // System.out.println("No letter for ch " + ch);
                    sums.add(ch - 'ا');
                }
            }
            this.sum = sums.getSums();
        } else {
            long sums = 0;
            for (char ch : text.toCharArray()) {
                sums += Character.toLowerCase(ch) - 'a';
            }
            this.sum = sums;
        }
    }

    @Override
    public int compareTo(Token o) {
        int cmp = Boolean.valueOf(arabic).compareTo(Boolean.valueOf(o.arabic));
        if (cmp == 0) {
            cmp = text.compareTo(o.text);
            if (cmp == 0) {
                cmp = start - o.start;
            }
        }
        return cmp;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (arabic ? 1231 : 1237);
        result = prime * result + end;
        result = prime * result + numWords;
        result = prime * result + (soundex ? 1231 : 1237);
        result = prime * result + start;
        result = prime * result + ((text == null) ? 0 : text.hashCode());
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
        Token other = (Token) obj;
        if (arabic != other.arabic)
            return false;
        if (end != other.end)
            return false;
        if (numWords != other.numWords)
            return false;
        if (soundex != other.soundex)
            return false;
        if (start != other.start)
            return false;
        if (text == null) {
            if (other.text != null)
                return false;
        } else if (!text.equals(other.text))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return text + ", arabic " + arabic;
    }

}
