package com.plexobject.quran.model;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import com.plexobject.quran.utils.TextHelper;

public class Surat implements Comparable<Surat> {
	private int number;
	private int startOffset;
	private int ayasCount;
	private int order;
	private int rukusCount;
	private String name;
	private String tName;
	private String eName;
	private String type;
	private List<Integer> juzAyats = new ArrayList<Integer>();
	private List<Integer> manzilAyats = new ArrayList<Integer>();
	private List<Integer> rukuAyats = new ArrayList<Integer>();
	private List<Integer> quarterAyats = new ArrayList<Integer>();
	private AlphabetSums nameSum;
	private AlphabetSums sums;
	private AlphabetSums muqataatSums;
	private AlphabetSums sumsWithoutBismilla;
	private List<Ayat> ayats = new ArrayList<Ayat>();
	private long checksum;

	public Surat() {

	}

	public Surat(int number, int startOffset, int ayasCount, int order,
	        int rukusCount, String name, String tName, String eName, String type) {
		this.number = number;
		this.startOffset = startOffset;
		this.ayasCount = ayasCount;
		this.order = order;
		this.rukusCount = rukusCount;
		this.name = name;
		this.tName = tName;
		this.eName = eName;
		this.type = type;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getStartOffset() {
		return startOffset;
	}

	public void setStartOffset(int startOffset) {
		this.startOffset = startOffset;
	}

	public int getAyasCount() {
		return ayasCount;
	}

	public void setAyasCount(int ayasCount) {
		this.ayasCount = ayasCount;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public int getRukusCount() {
		return rukusCount;
	}

	public void setRukusCount(int rukusCount) {
		this.rukusCount = rukusCount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String gettName() {
		return tName;
	}

	public void settName(String tName) {
		this.tName = tName;
	}

	public String geteName() {
		return eName;
	}

	public void seteName(String eName) {
		this.eName = eName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String toDetailsString() {
		return "Surat [number=" + number + ", startOffset=" + startOffset
		        + ", ayasCount=" + ayasCount + ", order=" + order
		        + ", rukusCount=" + rukusCount + ", name=" + name + ", tName="
		        + tName + ", eName=" + eName + ", type=" + type + "]";
	}

	@Override
	public String toString() {
		return number + " - " + name + " (" + eName + ") ";
	}

	public List<Ayat> getAyats() {
		return ayats;
	}

	public Ayat getAyat(int n) {
		return ayats.get(n);
	}

	public void addAyat(Ayat a) {
		ayats.add(a);
	}

	public void setAyats(List<Ayat> ayats) {
		this.ayats = ayats;
	}

	public List<Integer> getJuzAyats() {
		return juzAyats;
	}

	public void addJuzAyat(int j) {
		this.juzAyats.add(j);
	}

	public List<Integer> getManzilAyats() {
		return manzilAyats;
	}

	public void addManzilAyat(int j) {
		this.manzilAyats.add(j);
	}

	public List<Integer> getRukuAyats() {
		return rukuAyats;
	}

	public void addRukuAyat(int j) {
		this.rukuAyats.add(j);
	}

	public List<Integer> getQuarterAyats() {
		return quarterAyats;
	}

	public void addQuarterAyat(int j) {
		this.quarterAyats.add(j);
	}

	public AlphabetSums getSums() {
		return sums;
	}

	public void setSums(AlphabetSums sums) {
		this.sums = sums;
	}

	@Override
	public int compareTo(Surat o) {
		return number - o.number;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + number;
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
		Surat other = (Surat) obj;
		if (number != other.number)
			return false;
		return true;
	}

	public AlphabetSums getNameSum() {
		return nameSum;
	}

	public void setNameSum(AlphabetSums nameSum) {
		this.nameSum = nameSum;
	}

	public AlphabetSums getSumsWithoutBismilla() {
		return sumsWithoutBismilla;
	}

	public void setSumsWithoutBismilla(AlphabetSums sumsWithoutBismilla) {
		this.sumsWithoutBismilla = sumsWithoutBismilla;
	}

	public AlphabetSums getMuqataatSums() {
		return muqataatSums;
	}

	public void setMuqataatSums(AlphabetSums muqataatSums) {
		this.muqataatSums = muqataatSums;
	}

	public long getChecksum() {
		if (checksum == 0) {
			Checksum crc = new CRC32();
			for (Ayat a : getAyats()) {
				for (char ch : a.getSearchableArabic().toCharArray()) {
					if (ArabicLetter.isArabic(ch)) {
						ArabicLetter letter = TextHelper.ARABIC_LETTERS.get(ch);
						sums.add(letter.getNumber());
						crc.update(letter.getNumber());
					}
				}
			}
			checksum = crc.getValue();
		}
		return checksum;
	}
}