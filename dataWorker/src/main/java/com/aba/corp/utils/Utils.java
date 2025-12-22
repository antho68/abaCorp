package com.aba.corp.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Utils
{
    private static final Marker API_REQUEST = MarkerManager.getMarker("API_REQUEST");
    private static final Marker API_RESPONSE = MarkerManager.getMarker("API_RESPONSE");

    protected static final Logger log = LogManager.getLogger();
    protected static final Logger requestLogger = LogManager.getLogger("requestLogger");
    protected static final Logger responceLogger = LogManager.getLogger("responceLogger");

    public static String PATTERN_DATE_GERMAN = "dd.MM.yyyy";
    public static String PATTERN_DATE_TIME_GERMAN = "dd.MM.yyyy HH:mm";
    public static String PATTERN_DATE_TIME_GERMAN_SEC = "dd.MM.yyyy HH:mm:ss";
    public static Locale LOCALE_DE_CH = new Locale("de", "CH");
    private static final DateTimeFormatter PATTERN_DATE_TIME_GERMAN_FORMATTER =
            DateTimeFormatter.ofPattern(PATTERN_DATE_TIME_GERMAN);
    private static final DateTimeFormatter PATTERN_DATE_GERMAN_FORMATTER =
            DateTimeFormatter.ofPattern(PATTERN_DATE_GERMAN);

    public static String getDateFormatted(Date date)
    {
        return getDateFormatted(date, PATTERN_DATE_GERMAN);
    }

    public static String getOffsetDateFormatted(OffsetDateTime offsetDateTime)
    {
        return offsetDateTime.format(PATTERN_DATE_GERMAN_FORMATTER);
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

    public static OffsetDateTime getOffsetDateTimeFromDate(Date date)
    {
        ZoneId paris = ZoneId.of("Europe/Paris");
        return date == null ? null : date.toInstant().atZone(paris).toOffsetDateTime();
    }

    public static String getStringWithoutLanguageChar(String text)
    {
        if (text != null)
        {
            text = text.replaceAll("À", "a").replaceAll("Â", "a");
            text = text.replaceAll("É", "e").replaceAll("È", "e").replaceAll("Ê", "e");
            text = text.replaceAll("Ù", "u").replaceAll("û", "u");
            text = text.replaceAll("Ô", "o");

            text = text.replaceAll("à", "a").replaceAll("â", "a");
            text = text.replaceAll("é", "e").replaceAll("è", "e").replaceAll("ê", "e");
            text = text.replaceAll("ù", "u").replaceAll("û", "u");
            text = text.replaceAll("ô", "o");
        }

        return text;
    }

    public static void logError(String message)
    {
        log.error(message);
    }

    public static void logWarn(String message)
    {
        log.warn(getLogId() + ": " + message);
    }

    public static void logError(Throwable t)
    {
        logError(t.getMessage(), t);
    }

    public static void logError(String message, Throwable t)
    {
        log.error(getLogId() + ": " + (message != null ? message : "message is null!"), t);
    }

    public static void logInfo(String message)
    {
        log.info(message);
    }

    public static void logDebug(String message)
    {
        log.debug(message);
    }

    public static void logTrace(String message)
    {
        log.trace(getLogId() + ": " + message);
    }

    protected static String getLogId()
    {
        return "LOG_ID NOT SET";
    }

    public static void logRequest(String url, String command, String json) {
        requestLogger.debug(API_REQUEST, "Send to {} - {} : {}", url, command, json);
    }

    public static void logResponce(String url, String command, String json) {
        responceLogger.debug(API_RESPONSE, "Send to {} - {} : {}", url, command, json);
    }

    public static boolean isSameDate(Object d1, Object d2) {
        if (d1 == null || d2 == null) {
            return false;
        }

        OffsetDateTime odt1 = null;
        OffsetDateTime odt2 = null;

        if (d1 instanceof Date)
        {
            odt1 = getOffsetDateTimeFromDate((Date) d1);
        }
        else if (d1 instanceof OffsetDateTime)
        {
            odt1 = (OffsetDateTime) d1;
        }

        if (d2 instanceof Date)
        {
            odt2 = getOffsetDateTimeFromDate((Date) d2);
        }
        else if (d2 instanceof OffsetDateTime)
        {
            odt2 = (OffsetDateTime) d2;
        }

        if (!odt1.isEqual(odt2))
        {
            return false;
        }

        return true;
    }
}