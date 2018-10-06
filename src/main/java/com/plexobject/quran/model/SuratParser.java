package com.plexobject.quran.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SuratParser {
	private SuratParser() {
	}

	public static List<Surat> getSurats() {
		InputStream in = SuratParser.class
		        .getResourceAsStream("/quran-data.xml");
		if (in == null) {
			throw new RuntimeException("Failed to find /quran-data.xml");
		}
		try {
			List<Surat> surats = new ArrayList<Surat>();

			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
			        .newDocumentBuilder();
			Document doc = dBuilder.parse(in);

			if (doc.hasChildNodes()) {
				parse(doc.getChildNodes(), surats);
			}
			if (surats.size() != 114) {
				throw new RuntimeException("Parsing error " + surats.size());
			}
			AyatParser.addAyats(surats);

			return surats;
		} catch (IOException e) {
			throw new RuntimeException("Failed to get meta data for surats", e);
		} catch (SAXException e) {
			throw new RuntimeException("Failed to get meta data for surats", e);

		} catch (ParserConfigurationException e) {
			throw new RuntimeException("Failed to get meta data for surats", e);

		} catch (FactoryConfigurationError e) {
			throw new RuntimeException("Failed to get meta data for surats", e);
		}
	}

	private static int getInt(NamedNodeMap nodeMap, String name) {
		String value = getString(nodeMap, name);
		return Integer.parseInt(value);
	}

	private static String getString(NamedNodeMap nodeMap, String name) {
		return nodeMap.getNamedItem(name).getNodeValue();
	}

	private static void parse(NodeList nodeList, List<Surat> surats) {
		for (int count = 0; count < nodeList.getLength(); count++) {
			Node node = nodeList.item(count);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.getNodeName().equals("sura") && node.hasAttributes()) {
					NamedNodeMap nodeMap = node.getAttributes();
					// <sura index="1" ayas="7" start="0" name="الفاتحة"
					// tname="Al-Faatiha" ename="The Opening" type="Meccan"
					// order="5" rukus="1"/>
					Surat sura = new Surat();
					sura.setNumber(getInt(nodeMap, "index"));
					sura.setAyasCount(getInt(nodeMap, "ayas"));
					sura.setStartOffset(getInt(nodeMap, "start"));
					sura.setName(getString(nodeMap, "name"));
					sura.seteName(getString(nodeMap, "ename"));
					sura.settName(getString(nodeMap, "tname"));
					sura.setOrder(getInt(nodeMap, "order"));
					sura.setType(getString(nodeMap, "type"));
					sura.setRukusCount(getInt(nodeMap, "rukus"));
					surats.add(sura);
				} else if (node.getNodeName().equals("juz")
				        && node.hasAttributes()) {
					NamedNodeMap nodeMap = node.getAttributes();
					// <juz index="1" sura="1" aya="1"/>
					int suraNum = getInt(nodeMap, "sura");
					int ayaNum = getInt(nodeMap, "aya");

					surats.get(suraNum - 1).addJuzAyat(ayaNum);
				} else if (node.getNodeName().equals("quarter")
				        && node.hasAttributes()) {
					NamedNodeMap nodeMap = node.getAttributes();
					// <quarter index="1" sura="1" aya="1"/>

					int suraNum = getInt(nodeMap, "sura");
					int ayaNum = getInt(nodeMap, "aya");

					surats.get(suraNum - 1).addQuarterAyat(ayaNum);
				} else if (node.getNodeName().equals("manzil")
				        && node.hasAttributes()) {
					NamedNodeMap nodeMap = node.getAttributes();
					// <manzil index="1" sura="1" aya="1"/>

					int suraNum = getInt(nodeMap, "sura");
					int ayaNum = getInt(nodeMap, "aya");

					surats.get(suraNum - 1).addManzilAyat(ayaNum);
				} else if (node.getNodeName().equals("ruku")
				        && node.hasAttributes()) {
					NamedNodeMap nodeMap = node.getAttributes();
					// <ruku index="1" sura="1" aya="1"/>

					int suraNum = getInt(nodeMap, "sura");
					int ayaNum = getInt(nodeMap, "aya");

					surats.get(suraNum - 1).addRukuAyat(ayaNum);
				}

				if (node.hasChildNodes()) {
					parse(node.getChildNodes(), surats);
				}
			}

		}

	}
}
