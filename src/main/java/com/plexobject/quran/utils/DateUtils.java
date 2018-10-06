package com.plexobject.quran.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	private static final ThreadLocal<SimpleDateFormat> T_FORMAT = new ThreadLocal<SimpleDateFormat>() {
		protected synchronized SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); // //
																		// 2010-09-30T17:30:31Z
		}
	};
	private static final ThreadLocal<SimpleDateFormat> DISPLAY_DATE_FORMAT = new ThreadLocal<SimpleDateFormat>() {
		protected synchronized SimpleDateFormat initialValue() {
			return new SimpleDateFormat("EEE, MMM d, ''yy h:mm:ss a");
		}
	};
	private static final ThreadLocal<SimpleDateFormat> SLASH_DATE_FORMAT = new ThreadLocal<SimpleDateFormat>() {
		protected synchronized SimpleDateFormat initialValue() {
			return new SimpleDateFormat("MM/dd/yy");
		}
	};

	public static DateFormat getTDateFormat() {
		return T_FORMAT.get();
	}

	public static DateFormat getDisplayDateFormat() {
		return DISPLAY_DATE_FORMAT.get();
	}

	public static String slashDateFormat(Date date) {
		return SLASH_DATE_FORMAT.get().format(date);
	}

	public static long parseSlashDate(String sdate) {
		DateFormat dateF = DateFormat.getDateInstance(DateFormat.SHORT);
		dateF.setLenient(true);
		long date = 0;
		try {
			date = dateF.parse(sdate).getTime();
		} catch (java.text.ParseException e) {
			try {
				java.util.Calendar cal = java.util.Calendar.getInstance();
				int begin;
				int end;

				end = sdate.indexOf('-');
				if (end == -1)
					end = sdate.indexOf('/');
				int month = Integer.parseInt(sdate.substring(0, end)) - 1;

				begin = sdate.indexOf('-');
				if (begin == -1)
					begin = sdate.indexOf('/');
				end = sdate.lastIndexOf('-');
				if (end == -1)
					end = sdate.lastIndexOf('/');
				int day = Integer.parseInt(sdate.substring(begin + 1, end));

				end = sdate.lastIndexOf('-');
				if (end == -1)
					sdate.lastIndexOf('/');
				int year = Integer.parseInt(sdate.substring(end + 1));

				if (year < 100) {
					if (year > 90)
						year += 1900;
					else
						year += 2000;
				}
				cal.set(year, month, day);
				date = cal.getTime().getTime();
			} catch (Exception ee) {
				throw new IllegalArgumentException("Illegal date format "
						+ sdate);
			}
		}
		return date;
	}

}
