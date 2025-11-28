package com.aba.corp.web.utils;

import java.util.Date;

public class CommonContextHolder
{
    private static final ThreadLocal<String> idHolder = new ThreadLocal<String>();
    private static final ThreadLocal<Date> applDateHolder = new ThreadLocal<Date>();

    public CommonContextHolder()
    {
    }

    public static String getId()
    {
        return idHolder.get();
    }

    public static void setId(String id)
    {
        idHolder.set(id);
    }

    public static void setApplDate(Date applDate)
    {
        applDateHolder.set(applDate);
    }

    public static Date getApplDate()
    {
        return applDateHolder.get();
    }

    public static void clearAll()
    {
        clearApplDateHolder();
        clearIdHolder();
    }

    public static void clearIdHolder()
    {
        idHolder.remove();
    }

    public static void clearApplDateHolder()
    {
        applDateHolder.remove();
    }
}

