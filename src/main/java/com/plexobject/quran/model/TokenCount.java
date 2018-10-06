package com.plexobject.quran.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TokenCount implements Comparable<TokenCount> {
    private final Token token;
    private int count;
    private final List<Ayat> ayats = new ArrayList<>();
    private final Set<Surat> surats = new HashSet<>();

    public TokenCount(Token token) {
        this.token = token;
    }

    public TokenCount(Ayat ayat, Token token) {
        this.token = token;
        increment(ayat);
    }

    public int increment(Ayat ayat) {
        ayats.add(ayat);
        surats.add(ayat.getSurat());
        count++;
        return count;
    }

    public int getCount() {
        return count;
    }

    public Set<Surat> getSurats() {
        return surats;
    }

    public List<Ayat> getAyats() {
        return ayats;
    }

    public Token getToken() {
        return token;
    }

    @Override
    public int compareTo(TokenCount o) {
        return o.count - count;
    }

    @Override
    public String toString() {
        return token.text + " = " + count;
    }
}
