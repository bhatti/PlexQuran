package com.plexobject.quran.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum ArabicLetter {
    Alif((char) 1575, 'A', -1, 0, 1), Ba((char) 1576, 'b', 1, 1, 2), Ta(
            (char) 1578, 't', 3, 3,
            3), Tha((char) 1579, 'v', -1, 3, 4), Jeem((char) 1580, 'j', 2, 2,
                    5), HHa((char) 1581, 'H', -1, 0, 6), Kha((char) 1582, 'x',
                            2, 2, 7), Dal((char) 1583, 'd', 3, 3, 8), Thal(
                                    (char) 1584, '*', -1, 3,
                                    9), Ra((char) 1585, 'r', 6, 6, 10), Zain(
                                            (char) 1586, 'z', 2, 2,
                                            11), Seen((char) 1587, 's', 2, 2,
                                                    12), Sheen((char) 1588, '$',
                                                            -1, 0, 13), Sad(
                                                                    (char) 1589,
                                                                    'S', 2, 2,
                                                                    14), DDad(
                                                                            (char) 1590,
                                                                            'D',
                                                                            3,
                                                                            3,
                                                                            15), TTa(
                                                                                    (char) 1591,
                                                                                    'T',
                                                                                    3,
                                                                                    3,
                                                                                    16), DTha(
                                                                                            (char) 1592,
                                                                                            'Z',
                                                                                            2,
                                                                                            3,
                                                                                            17), Ain(
                                                                                                    (char) 1593,
                                                                                                    'E',
                                                                                                    -1,
                                                                                                    0,
                                                                                                    18), Ghain(
                                                                                                            (char) 1594,
                                                                                                            'g',
                                                                                                            2,
                                                                                                            0,
                                                                                                            19), Fa((char) 1601,
                                                                                                                    'f',
                                                                                                                    1,
                                                                                                                    1,
                                                                                                                    20), Qaf(
                                                                                                                            (char) 1602,
                                                                                                                            'q',
                                                                                                                            2,
                                                                                                                            2,
                                                                                                                            21), Kaf(
                                                                                                                                    (char) 1603,
                                                                                                                                    'k',
                                                                                                                                    2,
                                                                                                                                    2,
                                                                                                                                    22), Lam(
                                                                                                                                            (char) 1604,
                                                                                                                                            'l',
                                                                                                                                            4,
                                                                                                                                            4,
                                                                                                                                            23), Meem(
                                                                                                                                                    (char) 1605,
                                                                                                                                                    'm',
                                                                                                                                                    5,
                                                                                                                                                    5,
                                                                                                                                                    24), Noon(
                                                                                                                                                            (char) 1606,
                                                                                                                                                            'n',
                                                                                                                                                            5,
                                                                                                                                                            5,
                                                                                                                                                            25), Ha((char) 1607,
                                                                                                                                                                    'h',
                                                                                                                                                                    -1,
                                                                                                                                                                    0,
                                                                                                                                                                    26), Waw(
                                                                                                                                                                            (char) 1608,
                                                                                                                                                                            'w',
                                                                                                                                                                            -1,
                                                                                                                                                                            0,
                                                                                                                                                                            27), Ya((char) 1610,
                                                                                                                                                                                    'y',
                                                                                                                                                                                    -1,
                                                                                                                                                                                    0,
                                                                                                                                                                                    28), Hamza(
                                                                                                                                                                                            (char) 1569,
                                                                                                                                                                                            '\'',
                                                                                                                                                                                            -1,
                                                                                                                                                                                            0,
                                                                                                                                                                                            1), AlifMaksura(
                                                                                                                                                                                                    (char) 1609,
                                                                                                                                                                                                    'Y',
                                                                                                                                                                                                    -1,
                                                                                                                                                                                                    -1,
                                                                                                                                                                                                    0), TaMarbuta(
                                                                                                                                                                                                            (char) 1577,
                                                                                                                                                                                                            'p',
                                                                                                                                                                                                            -1,
                                                                                                                                                                                                            -1,
                                                                                                                                                                                                            0), Tatweel(
                                                                                                                                                                                                                    (char) 1600,
                                                                                                                                                                                                                    '_',
                                                                                                                                                                                                                    -1,
                                                                                                                                                                                                                    -1,
                                                                                                                                                                                                                    0), SmallHighSeen(
                                                                                                                                                                                                                            (char) 1756,
                                                                                                                                                                                                                            ':',
                                                                                                                                                                                                                            -1,
                                                                                                                                                                                                                            -1,
                                                                                                                                                                                                                            0), SmallHighRoundedZero(
                                                                                                                                                                                                                                    (char) 1759,
                                                                                                                                                                                                                                    '@',
                                                                                                                                                                                                                                    -1,
                                                                                                                                                                                                                                    -1,
                                                                                                                                                                                                                                    0), SmallHighUprightRectangularZero(
                                                                                                                                                                                                                                            (char) 1760,
                                                                                                                                                                                                                                            '"',
                                                                                                                                                                                                                                            -1,
                                                                                                                                                                                                                                            -1,
                                                                                                                                                                                                                                            0), SmallHighMeemIsolatedForm(
                                                                                                                                                                                                                                                    (char) 1762,
                                                                                                                                                                                                                                                    '[',
                                                                                                                                                                                                                                                    -1,
                                                                                                                                                                                                                                                    -1,
                                                                                                                                                                                                                                                    0), SmallLowSeen(
                                                                                                                                                                                                                                                            (char) 1763,
                                                                                                                                                                                                                                                            ';',
                                                                                                                                                                                                                                                            -1,
                                                                                                                                                                                                                                                            -1,
                                                                                                                                                                                                                                                            0), SmallWaw(
                                                                                                                                                                                                                                                                    (char) 1765,
                                                                                                                                                                                                                                                                    ',',
                                                                                                                                                                                                                                                                    -1,
                                                                                                                                                                                                                                                                    -1,
                                                                                                                                                                                                                                                                    0), SmallYa(
                                                                                                                                                                                                                                                                            (char) 1766,
                                                                                                                                                                                                                                                                            '.',
                                                                                                                                                                                                                                                                            -1,
                                                                                                                                                                                                                                                                            -1,
                                                                                                                                                                                                                                                                            0), SmallHighNoon(
                                                                                                                                                                                                                                                                                    (char) 1768,
                                                                                                                                                                                                                                                                                    '!',
                                                                                                                                                                                                                                                                                    -1,
                                                                                                                                                                                                                                                                                    -1,
                                                                                                                                                                                                                                                                                    0), EmptyCentreLowStop(
                                                                                                                                                                                                                                                                                            (char) 1770,
                                                                                                                                                                                                                                                                                            '-',
                                                                                                                                                                                                                                                                                            -1,
                                                                                                                                                                                                                                                                                            -1,
                                                                                                                                                                                                                                                                                            0), EmptyCentreHighStop(
                                                                                                                                                                                                                                                                                                    (char) 1771,
                                                                                                                                                                                                                                                                                                    '+',
                                                                                                                                                                                                                                                                                                    -1,
                                                                                                                                                                                                                                                                                                    -1,
                                                                                                                                                                                                                                                                                                    0), RoundedHighStopWithFilledCentre(
                                                                                                                                                                                                                                                                                                            (char) 1772,
                                                                                                                                                                                                                                                                                                            '%',
                                                                                                                                                                                                                                                                                                            -1,
                                                                                                                                                                                                                                                                                                            -1,
                                                                                                                                                                                                                                                                                                            0), SmallLowMeem(
                                                                                                                                                                                                                                                                                                                    (char) 1773,
                                                                                                                                                                                                                                                                                                                    ']',
                                                                                                                                                                                                                                                                                                                    -1,
                                                                                                                                                                                                                                                                                                                    -1,
                                                                                                                                                                                                                                                                                                                    0);
    public final char arabic;
    public final char buckWalter;
    public final int enSoundex;
    public final int arSoundex;
    private final int number;

    private ArabicLetter(char arabic, char buckWalter, int enSoundex,
            int arSoundex, int number) {
        this.arabic = arabic;
        this.buckWalter = buckWalter;
        this.enSoundex = enSoundex;
        this.arSoundex = arSoundex;
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public boolean isSearchable() {
        return number > 0 || this == ArabicLetter.SmallHighSeen;
    }

    public static boolean isArabic(char ch) {
        for (ArabicLetter l : values()) {
            if (ch == l.arabic) {
                return true;
            }
        }
        return false;
    }

    public static String getRex(String english) {
        final StringBuilder regex = new StringBuilder(".*");
        for (char ch : english.toCharArray()) {
            char lch = Character.toLowerCase(ch);
            if (lch >= 'a' && ch <= 'z') {
                switch (lch) {
                case 'a':
                    regex.append("[آ|ا|ع]");
                    break;
                case 'b':
                    regex.append('ب');
                    break;
                case 'c':
                    regex.append("[س|ك‎|ق|ث|ص]");
                    break;
                case 'd':
                    regex.append("[د|ض|ظ|غ]");
                    break;
                case 'e':
                    regex.append("[آ|ا]");
                    break;
                case 'f':
                    regex.append('ف');
                    break;
                case 'g':
                    regex.append("[ج|غ]");
                    break;
                case 'h':
                    regex.append("[ح|ه|ة]");
                    break;
                case 'i':
                    regex.append("[ى|ي]");
                    break;
                case 'j':
                    regex.append("[ج]");
                    break;
                case 'k':
                    regex.append("[س|ك‎|ق|ث|ص]");
                    break;
                case 'l':
                    regex.append("[ل]");
                    break;
                case 'm':
                    regex.append("[م]");
                    break;
                case 'n':
                    regex.append("[ن‎]");
                    break;
                case 'o':
                    regex.append("[و]");
                    break;
                case 'p':
                    regex.append('ب');
                    break;
                case 'q':
                    regex.append("[ك|ق]");
                    break;
                case 'r':
                    regex.append('ر');
                    break;
                case 's':
                    regex.append("[س|ث|ص|ش]");
                    break;
                case 't':
                    regex.append("[ت|ط‎]");
                    break;
                case 'u':
                    regex.append("[ى|ي|و]");
                    break;
                case 'v':
                    regex.append("[و]");
                    break;
                case 'w':
                    regex.append("[و]");
                    break;
                case 'x':
                    break;
                case 'y':
                    regex.append("[و|ى|ي]");
                    break;
                case 'z':
                    regex.append("[ذ|ز|س|ص|ظ]");
                    break;
                default:
                    regex.append(ch);
                }
            } else {
                regex.append(ch);
            }
        }
        regex.append(".*");
        return regex.toString();
    }

    public static List<Character> getSearchableLetters() {
        final List<Character> letters = new ArrayList<Character>();

        for (ArabicLetter l : ArabicLetter.values()) {
            if (l.isSearchable()) {
                letters.add(l.arabic);
            }
        }
        Collections.sort(letters);
        return letters;
    }

    // b, f, p, v => 1
    // c, g, j, k, q, s, x, z => 2
    // d, t => 3
    // l => 4
    // m, n => 5
    // r => 6

}
