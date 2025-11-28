package com.aba.corp.web.utils;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public enum SystemStage
{
    DEVELOPMENT("dev", "@DCA", "Development"),
    TEST("tst", "@Customer", "Production"),
    PRODUCTION("prod", "@Customer", "Production");

    private final String code;
    private final String shortDesc;
    private final String jsfProjectStage;
    private static final Map<String, SystemStage> lookup = new HashMap();

    private SystemStage(String code, String shortDesc, String jsfProjectStage)
    {
        this.code = code;
        this.shortDesc = shortDesc;
        this.jsfProjectStage = jsfProjectStage;
    }

    public String getCode()
    {
        return this.code;
    }

    public String getShortDesc()
    {
        return this.shortDesc;
    }

    public String getJsfProjectStage()
    {
        return this.jsfProjectStage;
    }

    public static SystemStage get(String code)
    {
        return lookup.get(code);
    }

    static
    {
        Iterator var0 = EnumSet.allOf(SystemStage.class).iterator();

        while (var0.hasNext())
        {
            SystemStage mode = (SystemStage) var0.next();
            lookup.put(mode.getCode(), mode);
        }
    }
}

