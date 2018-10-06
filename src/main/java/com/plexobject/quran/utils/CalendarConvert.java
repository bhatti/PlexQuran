package com.plexobject.quran.utils;

import java.util.Locale;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.IslamicCalendar;
import com.ibm.icu.util.TimeZone;

public class CalendarConvert {
    public static void main(String[] args) {
        TimeZone tz = TimeZone.getTimeZone("AST");
        Calendar cal = IslamicCalendar.getInstance(tz);
        cal.set(1, 1, 1);
        DateFormat fmt = DateFormat.getDateInstance(DateFormat.MEDIUM, new Locale("ar", "SA"));
        System.out.println(fmt.format(cal.getTime()));
        fmt = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
        System.out.println(fmt.format(cal.getTime()));
    }
}
