package com.aba.corp.web.utils;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public enum CrudMode
{
    ADD("1", "hinzufugen"),
    EDIT("2", "bearbeiten"),
    COPY("3", "kopieren"),
    DELETE("4", "loschen"),
    SHOW("5", "zeigen");

    private final String code;
    private final String shortDesc;
    private static final Map<String, CrudMode> lookup = new HashMap();

    private CrudMode(String code, String shortDesc)
    {
        this.code = code;
        this.shortDesc = shortDesc;
    }

    public String getCode()
    {
        return this.code;
    }

    public String getShortDesc()
    {
        return this.shortDesc;
    }

    public static CrudMode get(String code)
    {
        return lookup.get(code);
    }

    static
    {
        Iterator var0 = EnumSet.allOf(CrudMode.class).iterator();

        while (var0.hasNext())
        {
            CrudMode mode = (CrudMode) var0.next();
            lookup.put(mode.getCode(), mode);
        }

    }
}
