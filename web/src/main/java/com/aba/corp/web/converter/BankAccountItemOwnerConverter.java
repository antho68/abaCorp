package com.aba.corp.web.converter;


import com.aba.corp.utils.Constants;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

import java.io.Serializable;


/**
 * @author aba
 */
@Named("bankAccountItemOwnerConverter")
@Singleton
@SuppressWarnings("rawtypes")
public class BankAccountItemOwnerConverter implements Converter, Serializable
{
    private static final long serialVersionUID = -5685508572774214248L;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s)
    {
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o)
    {
        return Constants.BankRecordDataOwner.labels.get(o);
    }
}
