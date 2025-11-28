package com.aba.corp.utils;

import com.sun.tools.javac.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Utils
{
    protected static final Logger log = LogManager.getLogger(Utils.class);

    public static String PATTERN_DATE_GERMAN = "dd.MM.yyyy";
    public static String PATTERN_DATE_TIME_GERMAN = "dd.MM.yyyy HH:mm";
    public static String PATTERN_DATE_TIME_GERMAN_SEC = "dd.MM.yyyy HH:mm:ss";
    public static Locale LOCALE_DE_CH = new Locale("de", "CH");
    private static final DateTimeFormatter PATTERN_DATE_TIME_GERMAN_FORMATTER =
            DateTimeFormatter.ofPattern(PATTERN_DATE_TIME_GERMAN);

    public static String getDateFormatted(Date date)
    {
        return getDateFormatted(date, PATTERN_DATE_GERMAN);
    }

    public static String getOffsetDateTimeFormatted(OffsetDateTime offsetDateTime)
    {
        return offsetDateTime.format(PATTERN_DATE_TIME_GERMAN_FORMATTER);
    }

    public static String getDateFormatted(Date date, String pattern)
    {
        if (date == null)
        {
            return "";
        }
        else
        {
            DateFormat dfmt = new SimpleDateFormat(pattern, getLocaleFromContextWithFallback());
            dfmt.setCalendar(getCalendar());
            return dfmt.format(date);
        }
    }

    public static Locale getLocaleFromContextWithFallback()
    {
        Locale locale = LOCALE_DE_CH;
        return locale;
    }

    public static Calendar getCalendar()
    {
        Calendar cal = new GregorianCalendar();
        cal.setTimeZone(getTimeZone());
        cal.setFirstDayOfWeek(2);
        return cal;
    }

    public static TimeZone getTimeZone()
    {
        return getTimeZone("CET");
    }

    public static TimeZone getTimeZone(String timeZone)
    {
        return TimeZone.getTimeZone(timeZone);
    }

    public static String getDateAndTimeLongFormatted(Date date)
    {
        return getDateFormatted(date, PATTERN_DATE_TIME_GERMAN);
    }

    public static Date getDateWithMinutesOffset(Date date, int minutesOffset)
    {
        return getDateWithOffset(date, 12, minutesOffset);
    }

    public static Date getDateWithOffset(Date date, int field, int value)
    {
        if (date == null)
        {
            return null;
        }
        else
        {
            Calendar cal = getCalendar(date);
            cal.add(field, value);
            return cal.getTime();
        }
    }

    public static Calendar getCalendar(Date date)
    {
        if (date == null)
        {
            return null;
        }
        else
        {
            Calendar cal = getCalendar();
            cal.setTime(date);
            return cal;
        }
    }

    public static boolean isCollectionEmpty(Collection<?> collection)
    {
        return collection == null || collection.isEmpty();
    }
}
