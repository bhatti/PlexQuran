package com.plexobject.quran.model;

public class Sajda {
	private int suratNumber;
	private int ayatNumber;
	private String type;
	public static final Sajda SAJDAS[] = new Sajda[] {
			// new Sajda(sura, aya, type),
			new Sajda(7, 206, "recommended"), new Sajda(13, 15, "recommended"),
			new Sajda(16, 50, "recommended"),
			new Sajda(17, 109, "recommended"),
			new Sajda(19, 58, "recommended"), new Sajda(22, 18, "recommended"),
			new Sajda(22, 77, "recommended"), new Sajda(25, 60, "recommended"),
			new Sajda(27, 26, "recommended"), new Sajda(32, 15, "obligatory"),
			new Sajda(38, 24, "recommended"), new Sajda(41, 38, "obligatory"),
			new Sajda(53, 62, "obligatory"), new Sajda(84, 21, "recommended"),
			new Sajda(96, 19, "obligatory") };

	public Sajda(int suratNumber, int ayatNumber, String type) {
		this.suratNumber = suratNumber;
		this.ayatNumber = ayatNumber;
		this.type = type;
	}

	public int getSuratNumber() {
		return suratNumber;
	}

	public void setSuratNumber(int suratNumber) {
		this.suratNumber = suratNumber;
	}

	public int getAyatNumber() {
		return ayatNumber;
	}

	public void setAyatNumber(int ayatNumber) {
		this.ayatNumber = ayatNumber;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
