package com.aba.corp.web.controller;

import org.primefaces.PrimeFaces;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

public abstract class AbstractBaseController implements Serializable
{
    private static final long serialVersionUID = 8196417318793225252L;
    protected String menuId;

    protected AbstractBaseController()
    {
    }

    public String getMenuId()
    {
        return this.menuId;
    }

    public static synchronized void executeOnRequestContext(String executeCommand)
    {
        PrimeFaces context = PrimeFaces.current();
        if (context != null)
        {
            context.executeScript(executeCommand);
        }
    }

    public static synchronized void updateOnRequestContext(String clientId)
    {
        PrimeFaces context = PrimeFaces.current();
        if (context != null)
        {
            context.ajax().update(clientId);
        }
    }

    public static synchronized void updateOnRequestContext(Collection<String> clientIds)
    {
        PrimeFaces context = PrimeFaces.current();
        if (context != null)
        {
            context.ajax().update(clientIds);
        }
    }

    public static void hideDialog(String dialogName)
    {
        executeOnRequestContext("PF('" + dialogName + "').hide(); if(typeof setFocusToFilter == 'function') { setFocusToFilter(); };");
    }

    public static void hideDialogWithoutFocus(String dialogName)
    {
        executeOnRequestContext("PF('" + dialogName + "').hide();");
    }

    public static void showDialog(String dialogName)
    {
        executeOnRequestContext("PF('" + dialogName + "').show();");
    }

    public static void showErrorDialog()
    {
        showDialog("errorDialog");
    }

    public static void showWarningDialog()
    {
        showDialog("warningDialog");
    }

    public static void showInfoDialog()
    {
        showDialog("infoDialog");
    }

    protected ParameterizedType getParameterizedType()
    {
        Class<?> clazz = null;

        Type type;
        for (type = null; !(type instanceof ParameterizedType); type = clazz.getGenericSuperclass())
        {
            if (clazz == null)
            {
                clazz = this.getClass();
            }
            else
            {
                clazz = clazz.getSuperclass();
            }

            if (clazz == null)
            {
                break;
            }
        }

        if (!(type instanceof ParameterizedType))
        {
            throw new IllegalStateException(type + " is not ParameterizedType");
        }
        else
        {
            return (ParameterizedType) type;
        }
    }
}

