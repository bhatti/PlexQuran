package com.plexobject.quran.model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.plexobject.quran.ApplicationDelegate;

public class PhraseCountParser {
    private PhraseCountParser() {
    }

    public static List<TokenCount> parse(Surat surat) {
        InputStream in = SuratParser.class.getResourceAsStream("/phrases.xml");
        if (in == null) {
            throw new RuntimeException("Failed to find /phrases.xml");
        }
        try {
            List<TokenCount> counts = new ArrayList<TokenCount>();

            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            Document doc = dBuilder.parse(in);

            if (doc.hasChildNodes()) {
                parse(surat, doc.getChildNodes(), counts);
            }
            Collections.sort(counts);
            return counts;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get topics", e);
        }
    }

    private static String getString(NamedNodeMap nodeMap, String name) {
        Node node = nodeMap.getNamedItem(name);
        return node != null ? node.getNodeValue() : null;
    }

    private static int getInt(NamedNodeMap nodeMap, String name) {
        String value = getString(nodeMap, name);
        return Integer.parseInt(value);
    }

    private static void parse(final Surat filterSurat, NodeList nodeList,
            List<TokenCount> counts) {
        for (int count = 0; count < nodeList.getLength(); count++) {
            Node node = nodeList.item(count);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (node.getNodeName().equals("phrase")
                        && node.hasAttributes()) {
                    NamedNodeMap nodeMap = node.getAttributes();
                    // <phrase text="الله من شيء" start="44" end="56" count="10"
                    // words="6"/>
                    String text = getString(nodeMap, "text");
                    String[] ayatsT = getString(nodeMap, "ayats").split(",");

                    int start = getInt(nodeMap, "start");
                    int end = getInt(nodeMap, "end");
                    int words = getInt(nodeMap, "words");
                    Token token = new Token(text, true, start, end, false,
                            words);
                    TokenCount tc = new TokenCount(token);
                    for (String at : ayatsT) {
                        String[] sa = at.split(":");
                        int suratNum = Integer.parseInt(sa[0]);
                        Surat surat = ApplicationDelegate.getInstance()
                                .getQuran().getSurat(suratNum - 1);
                        int aya = Integer.parseInt(sa[1]);
                        if (filterSurat == null || filterSurat == surat) {
                            Ayat ayat = surat.getAyat(aya - 1);
                            tc.increment(ayat);
                        }
                    }
                    counts.add(tc);
                }

                if (node.hasChildNodes()) {
                    parse(filterSurat, node.getChildNodes(), counts);
                }
            }
        }
    }
}
