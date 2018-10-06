package com.plexobject.quran.model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.plexobject.quran.ApplicationDelegate;

public class TopicParser {
	private TopicParser() {
	}

	public static List<Topic> parse() {
		InputStream in = SuratParser.class.getResourceAsStream("/topics.xml");
		if (in == null) {
			throw new RuntimeException("Failed to find /topics.xml");
		}
		try {
			Map<String, Topic> idMap = new HashMap<String, Topic>();

			List<Topic> topics = new ArrayList<Topic>();

			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
			        .newDocumentBuilder();
			Document doc = dBuilder.parse(in);

			if (doc.hasChildNodes()) {
				parse(doc.getChildNodes(), idMap, topics);
			}
			Collections.sort(topics);
			return topics;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to get topics", e);
		}
	}

	private static String getString(NamedNodeMap nodeMap, String name) {
		Node node = nodeMap.getNamedItem(name);
		return node != null ? node.getNodeValue() : null;
	}

	private static void parse(NodeList nodeList, Map<String, Topic> idMap,
	        List<Topic> topics) {
		for (int count = 0; count < nodeList.getLength(); count++) {
			Node node = nodeList.item(count);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.getNodeName().equals("topic") && node.hasAttributes()) {
					NamedNodeMap nodeMap = node.getAttributes();
					// <topic id="4" name="Ablution" ayats="4:43,5:6"/>
					String name = getString(nodeMap, "name");
					if (name.length() <= 1) {
						continue;
					}
					Topic topic = new Topic();
					topic.setId(getString(nodeMap, "id"));
					topic.setName(name);
					String parent = getString(nodeMap, "parent");
					if (parent != null) {
						topic.setParent(idMap.get(parent));
					}
					idMap.put(topic.getId(), topic);
					String suratAyatBuf = getString(nodeMap, "ayats");
					String[] suratAyatToks = suratAyatBuf.split(",");
					for (String num : suratAyatToks) {
						if (num.length() == 0) {
							continue;
						}
						String[] tokens = num.split(":");
						int suratNum = Integer.parseInt(tokens[0]) - 1;
						Surat surat = ApplicationDelegate.getInstance()
						        .getQuran().getSurat(suratNum);
						String[] ayatNums = tokens[1].split("-");
						int start = Integer.parseInt(ayatNums[0]);
						int end = ayatNums.length > 1 ? Integer
						        .parseInt(ayatNums[1]) : start;
						for (int i = start; i <= end; i++) {
							if (surat.getAyasCount() < i - 1 - 1) {
								// System.out.println("Failed to get ayat "
								// + (i - 1) + " for " + surat + " max "
								// + surat.getAyasCount() + ", node "
								// + node + ", ayats " + num + "\n");
								continue;
							}
							Ayat ayat = surat.getAyat(i - 1);
							topic.getAyats().add(ayat);
						}
					}
					if (!topics.contains(topic)) {
						topics.add(topic);
					}
				}

				if (node.hasChildNodes()) {
					parse(node.getChildNodes(), idMap, topics);
				}
			}

		}

	}
}
