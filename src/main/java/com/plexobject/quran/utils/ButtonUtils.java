package com.plexobject.quran.utils;

import java.net.URL;

import javax.swing.ImageIcon;

public class ButtonUtils {
	public static ImageIcon loadImage(String name) {
		URL imageURL = ButtonUtils.class.getResource("/images/" + name);
		return new ImageIcon(imageURL);
	}
}
