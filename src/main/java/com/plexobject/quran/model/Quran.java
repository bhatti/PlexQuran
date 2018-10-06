package com.plexobject.quran.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.plexobject.quran.utils.NumberUtils;
import com.plexobject.quran.utils.TextHelper;

// http://islamtutor.blogspot.com/2011/12/qurans-statistics.html
// http://www.intellaren.com/articles/en/qss
// 
/*
 * Quran has 114 Chapters / Suras.

 Quran has 6236 verses / ayats.

 Quran has 338,606 letters (huruf); 86,430 words (repeated & non repeated kalemat), but in actual words are around 2000.

 Quran has 86 Makkan Chapters and 28 Medinan Chapters.

 Quran has total 4613 Makkan verses and 1623 Medinan Verses.

 Longest chapter of Quran is Chapter 2 - Al Baqarah.

 Shortest chaper of Quran is Chapter 108 - Al-Kauthar.

 Seven Chapters of Quran is named after Prophets.

 Name of 28 Prophets have been mentioned in Quran.

 Sura Anam (the Cattle), is the sixth Chapter of the Quran. It has the names of 18 Prophets in four consecutive verses.

 ALLAH(SWT) used his Name, Allah, in the Quran 2698 times, Rahman 57 times, Rahim 114 times and Ism 19 times. These numbers are other than Bismillahir Rahmanir Rahim.

 Sura Mujadilah is the only Sura of the Quran, where in each verse (Ayat), the name of Allah has been mentioned.

 Longest verse (Ayat) of Quran is Verse 282 of Sura Al-Baqr, the shortest verse is Verse 21 of Sura Mudathir.

 Sura Naml (the Ants), the 27th Chapter of the Quran, has two Bismillahs, one at the beginning, and one in Verse 30.

 Ramadan (the 9th month of the Islamic calendar) is the only month mentioned in the Quran. The name occurs in Verse 85 of Sura Baqr.

 Friday is the only day mentioned in the Quran. Sura 62 has been titled Jumaa (Friday)

 Name of 16 places, cities or countries mentioned in Quran

 Name of 15 eatables or drinks are mentioned in Quran

 Name of 7 colors mentioned in Quran

 The most recited prayer in the world is Surah Al Fatiha. Recited atleast seventeen times a day by billion of Muslims around Globe.

 The Quran was completed in 23 years- from 610 AD to 632 AD.

 Mary or Maryam is the only women named in Quran

 Hazrat Zaid bin Harisa is the only companion of Prophet Muhammad (PBUH) named in Quran

 After the revelation of this Verse, the Prophet of Islam, lived another 80 days in the world. No other revelation came to him during those eighty days. The Message was complete; "the religion had been perfected". The Revelation had thus ended forever.

 The script number_of_verses.php tells us that:
 There are 6236 verses in the Quran, and
 it lists the number of verses in each surah (chapter).
 The largest surah (chapter), (2) Al-Baqarah (The Cow), contains 286 verses, and
 the shortest surah, (108) Al-Kawthar (The Abundance), contains 3 verses.
 The script letter_frequency_uthmani.php tells us that:
 The total number of letters in the Quran is 327293, and
 it lists the number of letters in each surah (chapter), and
 it lists the frequency of each alphabetic letter in the whole quran, and
 finally it lists the frequency each alphabetic letter appears in each surah.
 The largest surah in the Quran, (2) Al-Baqarah (The Cow), contains 25986 letters, and
 the smallest surah, (108) Al-Kawthar (The Abundance), contains 43 letters.
 The three most common letters in the Quran are alpeh, laam, and noon ( ا ل ن ) and they occur 52655, 38102 and 27268 times respectively.
 The script quran_words.php tells us that:
 The number of words in the quran is 77430, and
 the number of unique words in the quran is 14716, and
 the average length of those unique words is 5.30 letters.
 It shows a table with the number of words in each surah, and
 it also shows a table with the frequency words occur in the quran.
 The longest surah, (2) Al-Baqarah (The Cow), contains 6116 words, and
 the shortest surah, (108) Al-Kawthar (The Abundance), contains 10 words.
 The three most common words in the quran are min/men (from/who), Allah (God) and inna (is, if, ...etc) ( من, الله, ان ) and they occur 2763, 2153 and 1604 times respectively.

 */
public class Quran {
    private final List<Surat> surats;
    private List<TokenCount> cachedArabicWordCounts;
    private List<TokenCount> cachedPhraseCounts;
    private List<TokenCount> cachedArabicLetterCounts;
    private Map<ArabicLetter, Map<String, TokenCount>> cachedArabicWordsByInitials;
    private List<TokenCount> cachedEnglishWordCounts;
    private List<TokenCount> cachedEnglishLetterCounts;
    private Map<Character, Map<String, TokenCount>> cachedEnglishWordsByInitials;
    private List<Topic> cachedTopics;

    public Quran() {
        surats = SuratParser.getSurats();
        if (surats.size() != 114) {
            throw new RuntimeException("Incorrect size " + surats.size());
        }
    }

    public List<Surat> getSurats() {
        return surats;
    }

    public int getSuratCount() {
        return surats.size();
    }

    public List<Topic> getTopics() {
        if (cachedTopics == null) {
            cachedTopics = TopicParser.parse();
        }
        return cachedTopics;
    }

    public Surat getSurat(int n) {
        if (surats.size() <= n) {
            throw new IllegalArgumentException(
                    "Number of surats " + surats.size() + " <= " + n);
        }
        return surats.get(n);
    }

    public List<Ayat> getAyats() {
        List<Ayat> ayats = new ArrayList<>();
        for (Surat surat : surats) {
            for (Ayat ayat : surat.getAyats()) {
                ayats.add(ayat);
            }
        }
        return ayats;
    }

    public List<TokenCount> getArabicWordCounts(int max) {
        if (cachedArabicWordCounts == null) {
            TokenCounter counter = new TokenCounter();
            for (Ayat ayat : getAyats()) {
                for (Token t : ayat.getArabicWords()) {
                    counter.add(ayat, t, 1);
                }
            }
            cachedArabicWordCounts = counter.getCounts(max);
        }
        return cachedArabicWordCounts;
    }

    public List<TokenCount> getArabicWordCounts(Surat surat, int max) {
        TokenCounter counter = new TokenCounter();
        for (Ayat ayat : surat.getAyats()) {
            for (Token t : ayat.getArabicWords()) {
                counter.add(ayat, t, 1);
            }
        }
        return counter.getCounts(max);
    }

    public List<TokenCount> getEnglishWordCounts(int max) {
        if (cachedEnglishWordCounts == null) {
            TokenCounter counter = new TokenCounter();
            for (Ayat ayat : getAyats()) {
                for (Token t : ayat.getEnglishWords()) {
                    counter.add(ayat, t, 1);
                }
            }
            cachedEnglishWordCounts = counter.getCounts(max);
        }
        return cachedEnglishWordCounts;
    }

    public List<TokenCount> getEnglishWordCounts(Surat surat, int max) {
        TokenCounter counter = new TokenCounter();
        for (Ayat ayat : surat.getAyats()) {
            for (Token t : ayat.getEnglishWords()) {
                counter.add(ayat, t, 1);
            }
        }
        return counter.getCounts(max);
    }

    public Map<ArabicLetter, Map<String, TokenCount>> getArabicCountsByInitial(
            Surat surat) {
        Map<ArabicLetter, Map<String, TokenCount>> counts = new HashMap<ArabicLetter, Map<String, TokenCount>>();

        for (Ayat ayat : surat.getAyats()) {
            for (Token t : ayat.getArabicWords()) {
                getArabicLetterCounts(ayat, t, counts);

            }
        }
        return counts;
    }

    public List<TokenCount> getArabicCountsByInitial(Surat surat,
            Character ch) {
        Map<ArabicLetter, Map<String, TokenCount>> countMap = surat != null
                ? getArabicCountsByInitial(surat) : getArabicCountsByInitial();
        ArabicLetter initial = TextHelper.ARABIC_LETTERS.get(ch);
        Map<String, TokenCount> counts = countMap.get(initial);
        if (counts == null) {
            // System.out.println("No words for " + ch);
            return new ArrayList<TokenCount>();
        }
        List<TokenCount> list = new ArrayList<TokenCount>(counts.values());
        Collections.sort(list, new Comparator<TokenCount>() {
            @Override
            public int compare(TokenCount o1, TokenCount o2) {
                // return o1.getToken().text.compareTo(o2.getToken().text);
                return new Integer(o2.getCount())
                        .compareTo(new Integer(o1.getCount()));
            }
        });
        return list;
    }

    public List<TokenCount> getEnglishCountsByInitial(Character ch) {
        Map<Character, Map<String, TokenCount>> countMap = getEnglishCountsByInitial();
        Map<String, TokenCount> counts = countMap.get(ch);
        if (counts == null) {
            // System.out.println("No words for " + ch);
            return new ArrayList<TokenCount>();
        }
        List<TokenCount> list = new ArrayList<TokenCount>(counts.values());
        Collections.sort(list, new Comparator<TokenCount>() {
            @Override
            public int compare(TokenCount o1, TokenCount o2) {
                return o1.getToken().text.compareTo(o2.getToken().text);
            }
        });
        return list;
    }

    public Map<Character, Map<String, TokenCount>> getEnglishCountsByInitial() {
        if (cachedEnglishWordsByInitials == null) {
            cachedEnglishWordsByInitials = new HashMap<Character, Map<String, TokenCount>>();

            for (Surat surat : surats) {
                for (Ayat ayat : surat.getAyats()) {
                    for (Token t : ayat.getEnglishWords()) {
                        getEnglishLetterCounts(ayat, t,
                                cachedEnglishWordsByInitials);

                    }
                }
            }
            // cachedLetterCounts = counter.getCounts(100);
        }
        return cachedEnglishWordsByInitials;
    }

    public Map<ArabicLetter, Map<String, TokenCount>> getArabicCountsByInitial() {
        if (cachedArabicWordsByInitials == null) {
            cachedArabicWordsByInitials = new HashMap<ArabicLetter, Map<String, TokenCount>>();

            for (Surat surat : surats) {
                for (Ayat ayat : surat.getAyats()) {
                    for (Token t : ayat.getArabicWords()) {
                        getArabicLetterCounts(ayat, t,
                                cachedArabicWordsByInitials);

                    }
                }
            }
            // cachedLetterCounts = counter.getCounts(100);
        }
        return cachedArabicWordsByInitials;
    }

    private void getEnglishLetterCounts(Ayat ayat, Token t,
            Map<Character, Map<String, TokenCount>> cache) {
        for (char ch : t.text.toCharArray()) {
            if (ArabicLetter.isArabic(ch)) {
                Map<String, TokenCount> counters = cache.get(ch);
                if (counters == null) {
                    counters = new HashMap<String, TokenCount>();
                    cache.put(ch, counters);
                }
                TokenCount count = counters.get(t.text);
                if (count == null) {
                    count = new TokenCount(ayat, t);
                    counters.put(t.text, count);
                } else {
                    count.increment(ayat);
                }
                break; // only first initial
            }
        }
    }

    private void getArabicLetterCounts(Ayat ayat, Token t,
            Map<ArabicLetter, Map<String, TokenCount>> cache) {
        for (char ch : t.text.toCharArray()) {
            if (ArabicLetter.isArabic(ch)) {
                ArabicLetter initial = TextHelper.ARABIC_LETTERS.get(ch);
                Map<String, TokenCount> counters = cache.get(initial);
                if (counters == null) {
                    counters = new HashMap<String, TokenCount>();
                    cache.put(initial, counters);
                }
                TokenCount count = counters.get(t.text);
                if (count == null) {
                    count = new TokenCount(ayat, t);
                    counters.put(t.text, count);
                } else {
                    count.increment(ayat);
                }
                break; // only first initial
            }
        }
    }

    public List<TokenCount> getEnglishLettersCount() {
        if (cachedEnglishLetterCounts == null) {
            TokenCounter counter = new TokenCounter();
            for (Ayat ayat : getAyats()) {
                for (Token t : ayat.getEnglishWords()) {
                    for (char ch : t.text.toCharArray()) {
                        counter.add(ayat, new Token(String.valueOf(ch)), 0);
                    }
                }
            }
            cachedEnglishLetterCounts = counter.getCounts(100);
        }
        return cachedEnglishLetterCounts;
    }

    public List<TokenCount> getEnglishLettersCount(Surat surat) {
        TokenCounter counter = new TokenCounter();
        for (Ayat ayat : surat.getAyats()) {
            for (Token t : ayat.getEnglishWords()) {
                for (char ch : t.text.toCharArray()) {
                    counter.add(ayat, new Token(String.valueOf(ch)), 0);
                }
            }
        }
        return counter.getCounts(100);
    }

    public List<TokenCount> getArabicLettersCount() {
        if (cachedArabicLetterCounts == null) {
            TokenCounter counter = new TokenCounter();
            for (Ayat ayat : getAyats()) {
                for (Token t : ayat.getArabicWords()) {
                    for (char ch : t.text.toCharArray()) {
                        if (ArabicLetter.isArabic(ch)) {
                            counter.add(ayat, new Token(String.valueOf(ch)), 0);
                        }
                    }
                }
            }
            cachedArabicLetterCounts = counter.getCounts(100);
        }
        return cachedArabicLetterCounts;
    }

    public List<TokenCount> getArabicLettersCount(Surat surat) {
        TokenCounter counter = new TokenCounter();
        for (Ayat ayat : surat.getAyats()) {
            for (Token t : ayat.getArabicWords()) {
                for (char ch : t.text.toCharArray()) {
                    if (ArabicLetter.isArabic(ch)) {
                        counter.add(ayat, new Token(String.valueOf(ch)), 0);
                    }
                }
            }
        }
        return counter.getCounts(100);
    }

    public List<Ayat> getAyatWithSums() {
        getSuratWithSums();
        List<Ayat> ayats = new ArrayList<Ayat>();
        for (Surat surat : surats) {
            for (Ayat ayat : surat.getAyats()) {
                ayats.add(ayat);
            }
        }
        return ayats;
    }

    private AlphabetSums getSumAndCount(Ayat ayat, boolean skipBismillah) {
        AlphabetSums sums = new AlphabetSums(0);

        for (int n = skipBismillah ? 22 : 0; n < ayat.getSearchableArabic()
                .length(); n++) {
            char ch = ayat.getSearchableArabic().charAt(n);
            if (ArabicLetter.isArabic(ch)) {
                ArabicLetter letter = TextHelper.ARABIC_LETTERS.get(ch);
                // if (skipBismillah && ayat.getSurat().getNumber() == 1) {
                // System.out.println("Adding " + letter + " for " + ayat
                // + ", count " + ayat.getSearchableArabic().length());
                // }
                sums.add(letter.getNumber());
            }
        }
        return sums;
    }

    private AlphabetSums getMuqataatSumAndCount(Ayat ayat) {
        AlphabetSums sums = new AlphabetSums(0);

        for (int n = 22; n < ayat.getSearchableArabic().length(); n++) {
            char ch = ayat.getSearchableArabic().charAt(n);
            if (ArabicLetter.isArabic(ch)) {
                ArabicLetter letter = TextHelper.ARABIC_LETTERS.get(ch);
                sums.add(letter.getNumber());
            } else if (ch == 'ۚ' || ch == 1754) {
                break;
            }
        }
        return sums;
    }

    private void setSuratNameSum(Surat surat) {
        AlphabetSums sums = new AlphabetSums(0);
        surat.setNameSum(sums);
        for (char ch : surat.getName().toCharArray()) {
            if (ArabicLetter.isArabic(ch)) {
                ArabicLetter letter = TextHelper.ARABIC_LETTERS.get(ch);
                sums.add(letter.getNumber());
            }
        }
    }

    public List<Surat> getSuratWithSums() {
        List<Integer> muqataatSuras = Arrays.asList(2, 3, 7, 10, 11, 12, 13, 14,
                15, 19, 20, 26, 27, 28, 29, 30, 31, 32, 36, 38, 40, 41, 42, 43,
                44, 45, 46, 50, 68);
        if (surats.get(0).getSums() == null) {
            for (Surat surat : surats) {
                setSuratNameSum(surat);

                AlphabetSums firstAyatCounts = getSumAndCount(
                        surat.getAyats().get(0), false);
                AlphabetSums firstAyatCountsWithoutBismilla = getSumAndCount(
                        surat.getAyats().get(0),
                        surat.getNumber() != 9 ? true : false);
                surat.getAyat(0).setSums(new AlphabetSums(firstAyatCounts));
                if (muqataatSuras.contains(surat.getNumber())) {
                    surat.setMuqataatSums(
                            getMuqataatSumAndCount(surat.getAyat(0)));
                }
                long[] sums = new long[surat.getAyasCount()];
                long[] sumsWithoutBismilla = new long[surat.getAyasCount()];

                sums[0] = firstAyatCounts.getSums();
                sumsWithoutBismilla[0] = firstAyatCountsWithoutBismilla
                        .getSums() == 0 ? 1
                                : firstAyatCountsWithoutBismilla.getSums();
                AlphabetSums abSums = new AlphabetSums(firstAyatCounts);
                AlphabetSums abSumsWithoutBismilla = new AlphabetSums(
                        firstAyatCountsWithoutBismilla);

                for (int i = 1; i < surat.getAyats().size(); i++) {
                    Ayat ayat = surat.getAyat(i);
                    AlphabetSums counts = getSumAndCount(
                            surat.getAyats().get(i), false);
                    ayat.setSums(counts);
                    abSums.add(counts);
                    abSumsWithoutBismilla.add(counts);
                    sums[i] = counts.getSums();
                    sumsWithoutBismilla[i] = counts.getSums();
                }
                abSums.setGcd(NumberUtils.gcd(sums));
                abSumsWithoutBismilla
                        .setGcd(NumberUtils.gcd(sumsWithoutBismilla));
                surat.setSums(abSums);
                surat.setSumsWithoutBismilla(abSumsWithoutBismilla);
            }
        }
        return surats;
    }

    public List<TokenCount> getPhraseCounts(Surat surat, int max) {
        return PhraseCountParser.parse(surat);
    }

    public List<TokenCount> getPhraseCounts(int max) {
        if (cachedPhraseCounts == null) {
            cachedPhraseCounts = PhraseCountParser.parse(null);
            // List<TokenCount> topWordsCount = getWordCounts(max);
            // Set<String> topWords = new HashSet<String>();
            // for (TokenCount tc : topWordsCount) {
            // topWords.add(tc.getToken().text);
            // }
            //
            // TokenCounter counter = new TokenCounter();
            // for (Surat surat : surats) {
            // for (Ayat ayat : surat.getAyats()) {
            // List<Token> words = ayat.getArabicWords();
            // List<Token> phrases = TextHelper.combine(words, topWords);
            // for (Token t : phrases) {
            // counter.add(ayat, t, 1);
            // // System.out.println(" ayat " + ayat + ", adding " + t
            // // + ", count " + count);
            // }
            // }
            // }
            // cachedPhraseCounts = counter.getCounts(max);
            // try {
            // PrintWriter out = new PrintWriter(new FileWriter("out.xml"));
            // for (TokenCount t : cachedPhraseCounts) {
            // StringBuilder sb = new StringBuilder();
            // for (Ayat a : t.getAyats()) {
            // if (sb.length() > 0) {
            // sb.append(",");
            // }
            // sb.append(a.getSurat().getNumber() + ":"
            // + a.getNumber());
            // }
            // out.println("<phrase ayats=\"" + sb + "\" text=\""
            // + t.getToken().text + "\" start=\""
            // + t.getToken().start + "\" end=\""
            // + t.getToken().end + "\" count=\"" + t.getCount()
            // + "\" words=\"" + t.getToken().numWords + "\"/>");
            // }
            // out.close();
            // } catch (IOException e) {
            // e.printStackTrace();
            // }
        }
        return cachedPhraseCounts;
    }

    public List<String> lookup(String word) {
        boolean arabic = false;
        for (char ch : word.toCharArray()) {
            if (ArabicLetter.isArabic(ch)) {
                arabic = true;
                break;
            }
        }
        String arSoundexWord = arabic ? TextHelper.arabicSoundex(word) : null;
        String enSoundexWord = TextHelper.soundex(word);
        String bwSoundexWord = TextHelper.soundex(TextHelper.buckWalter(word));
        if (bwSoundexWord.length() == 0) {
            bwSoundexWord = TextHelper.soundex(word);
        }
        Set<String> result = new HashSet<>();
        for (Surat surat : surats) {
            for (Ayat ayat : surat.getAyats()) {
                if (arabic && arSoundexWord.length() > 0) {
                    result.addAll(searchArabicSoundex(false, arSoundexWord,
                            null, ayat));
                } else {
                    // if (bwSoundexWord.length() > 0) {
                    // result.addAll(searchBuckWalterSoundex(false,
                    // bwSoundexWord, null, ayat));
                    // }
                    if (enSoundexWord.length() > 0) {
                        result.addAll(searchEnglishSoundex(false, enSoundexWord,
                                null, ayat));
                    }
                }

            }
        }
        //
        List<String> list = new ArrayList<>(result);
        Collections.sort(list);
        return list;
    }

    public List<SearchMatch> search(String word) {
        boolean arabic = false;
        for (char ch : word.toCharArray()) {
            if (ArabicLetter.isArabic(ch)) {
                arabic = true;
                break;
            }
        }
        // String arSoundexWord = arabic ? TextHelper.arabicSoundex(word) :
        // null;
        String enSoundexWord = TextHelper.soundex(word);
        String bwSoundexWord = TextHelper.soundex(TextHelper.buckWalter(word));
        if (bwSoundexWord.length() == 0) {
            bwSoundexWord = TextHelper.soundex(word);
        }

        Map<Ayat, SearchMatch> result = new HashMap<Ayat, SearchMatch>();
        for (Surat surat : surats) {
            for (Ayat ayat : surat.getAyats()) {
                if (arabic) {
                    searchArabicWord(word, result, ayat);
                } else {
                    if (bwSoundexWord.length() > 0) {
                        searchBuckWalterWord(word, result, ayat);
                    }
                    if (enSoundexWord.length() > 0) {
                        searchEnglishWord(word, result, ayat);
                    }
                }

            }
        }
        List<SearchMatch> list = new ArrayList<SearchMatch>(result.values());
        Collections.sort(list);
        return list;
    }

    private void searchEnglishWord(String word, Map<Ayat, SearchMatch> result,
            Ayat ayat) {
        for (Token t : ayat.getEnglishWords()) {
            if (t.text.equalsIgnoreCase(word)) {
                addToken(result, ayat, t, false);
            }
        }
    }

    private void searchArabicWord(String word, Map<Ayat, SearchMatch> result,
            Ayat ayat) {
        for (Token t : ayat.getArabicWords()) {
            if (t.text.equalsIgnoreCase(word)) {
                addToken(result, ayat, t, false);
            }
        }
    }

    private void searchBuckWalterWord(String word,
            Map<Ayat, SearchMatch> result, Ayat ayat) {
        for (Token t : ayat.getBuckWalterWords()) {
            if (t.text.equalsIgnoreCase(word)) {
                addToken(result, ayat, t, false);
            }
        }
    }

    private Collection<String> searchEnglishSoundex(boolean partial,
            String enSoundexWord, Map<Ayat, SearchMatch> result, Ayat ayat) {
        Collection<String> tokens = new HashSet<>();

        for (Token t : ayat.getEnglishSoundex()) {
            if ((partial && t.text.startsWith(enSoundexWord))
                    || (!partial && t.text.equalsIgnoreCase(enSoundexWord))) {
                addToken(result, ayat, t, partial);
                // System.out.println("English Matched " + t.text + ", soundex "
                // + enSoundexWord + " with token " + t + ", ayat "
                // + ayat.number);
                tokens.add(
                        ayat.english.substring(t.start, t.end).toLowerCase());
            }
        }
        return tokens;
    }

    private Collection<String> searchBuckWalterSoundex(boolean partial,
            String bwSoundexWord, Map<Ayat, SearchMatch> result, Ayat ayat) {
        Collection<String> tokens = new HashSet<>();

        for (Token t : ayat.getBuckWalterSoundex()) {
            if ((partial && t.text.startsWith(bwSoundexWord))
                    || (!partial && t.text.equalsIgnoreCase(bwSoundexWord))) {
                // System.out.println("BuckWalter Matched " + t.text
                // + ", soundex " + bwSoundexWord + " with token " + t
                // + ", ayat " + ayat.number);
                addToken(result, ayat, t, partial);
                tokens.add(
                        ayat.english.substring(t.start, t.end).toLowerCase());
            }
        }
        return tokens;
    }

    private Collection<String> searchArabicSoundex(boolean partial,
            String arSoundexWord, Map<Ayat, SearchMatch> result, Ayat ayat) {
        Collection<String> tokens = new HashSet<>();
        for (Token t : ayat.getArabicSoundex()) {
            if ((partial && t.text.startsWith(arSoundexWord))
                    || (!partial && t.text.equalsIgnoreCase(arSoundexWord))) {
                // System.out.println("Arabic Soundex Matched " + t.text
                // + ", soundex " + arSoundexWord + " with token " + t
                // + ", ayat " + ayat.number);
                addToken(result, ayat, t, partial);
                tokens.add(ayat.searchableArabic.substring(t.start, t.end)
                        .toLowerCase());
            }
        }
        return tokens;
    }

    private void addToken(Map<Ayat, SearchMatch> result, Ayat ayat, Token t,
            boolean partial) {
        if (result == null) {
            return;
        }
        SearchMatch match = result.get(ayat);
        if (match == null) {
            match = new SearchMatch(ayat, partial);
            result.put(ayat, match);
            match.add(t);
        }
    }
}
