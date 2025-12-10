package com.aba.corp.web.converter;

import com.aba.corp.utils.Constants;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.inject.Named;

import java.io.Serializable;

@Named("bankAccountRuleValueToSetConverter")
public class BankAccountRuleValueToSetConverter implements Converter, Serializable
{
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s)
    {
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o)
    {
        return Constants.BankAccountRuleValueToSet.labels.get(o);
    }
}
