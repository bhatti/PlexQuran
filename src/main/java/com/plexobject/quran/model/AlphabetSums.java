package com.plexobject.quran.model;

import com.plexobject.quran.utils.NumberUtils;

public class AlphabetSums {
	private int letters;
	private long sums;
	private long gcd;
	private long lcm;

	AlphabetSums(long sums) {
		this.sums = sums;
	}

	AlphabetSums(int letters, long sums) {
		this.letters = letters;
		this.sums = sums;
	}

	AlphabetSums(AlphabetSums other) {
		this.letters = other.letters;
		this.sums = other.sums;
		this.gcd = other.gcd;
		this.lcm = other.lcm;
	}

	public void add(AlphabetSums other) {
		sums += other.sums;
		letters += other.letters;
	}

	public void add(long sum) {
		sums += sum;
		letters++;
	}

	AlphabetSums(long sums, long[] arr) {
		this.sums = sums;
		this.gcd = NumberUtils.gcd(arr);
		this.lcm = NumberUtils.lcm(arr);
	}

	public long getSums() {
		return sums;
	}

	public void setSums(long sums) {
		this.sums = sums;
	}

	public long getGcd() {
		return gcd;
	}

	public void setGcd(long gcd) {
		this.gcd = gcd;
	}

	public long getLcm() {
		return lcm;
	}

	public void setLcm(long lcm) {
		this.lcm = lcm;
	}

	public int getLetters() {
		return letters;
	}

	public void setLetters(int letters) {
		this.letters = letters;
	}

	@Override
	public String toString() {
		return "AlphabetSums [letters=" + letters + ", sums=" + sums + ", gcd="
		        + gcd + ", lcm=" + lcm + "]";
	}

}
