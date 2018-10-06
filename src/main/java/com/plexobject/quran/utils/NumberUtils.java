package com.plexobject.quran.utils;

import java.text.NumberFormat;
import java.util.Random;

public class NumberUtils {
	private static final ThreadLocal<NumberFormat> PERCENT_FORMAT = new ThreadLocal<NumberFormat>() {
		protected synchronized NumberFormat initialValue() {
			return NumberFormat.getPercentInstance();
		}
	};

	private static final Random random = new Random();

	public static double randomUpdate(double value) {
		return value + (random.nextInt(50) * 0.1);
	}

	public static String formatPercent(double v) {
		return PERCENT_FORMAT.get().format(v);
	}

	public static long gcd(long a, long b) {
		while (b > 0) {
			long temp = b;
			b = a % b; // % is remainder
			a = temp;
		}
		return a;
	}

	public static long gcd(long[] input) {
		long result = input[0];
		for (int i = 1; i < input.length; i++)
			result = gcd(result, input[i]);
		return result;
	}

	public static long lcm(long a, long b) {
		return a * (b / gcd(a, b));
	}

	public static long lcm(long[] input) {
		long result = input[0];
		for (int i = 1; i < input.length; i++)
			result = lcm(result, input[i]);
		return result;
	}
}
