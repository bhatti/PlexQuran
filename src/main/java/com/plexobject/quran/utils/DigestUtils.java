package com.plexobject.quran.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

public class DigestUtils {
	public static String digest(File file) throws Exception {
		return digest(file, "MD5");
	}

	public static String digest(File file, String algorithm) throws Exception {
		InputStream fis = new BufferedInputStream(new FileInputStream(file));

		byte[] buffer = new byte[1024];
		MessageDigest complete = MessageDigest.getInstance(algorithm);
		int numRead;

		do {
			numRead = fis.read(buffer);
			if (numRead > 0) {
				complete.update(buffer, 0, numRead);
			}
		} while (numRead != -1);

		fis.close();
		return toHex(complete.digest());
	}

	public static String toHex(byte[] b) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < b.length; i++) {
			sb.append(Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}

}
