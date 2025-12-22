package com.aba.corp.web.converter;


import com.aba.corp.utils.Constants;
import com.aba.corp.web.item.EntityItem;
import dao.BankAccountDAO;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import model.BankAccount;
import model.BankAccountRule;

import java.io.Serializable;


/**
 * @author aba
 */
@Named("bankAccountItemTypeConverter")
@Singleton
@SuppressWarnings("rawtypes")
public class BankAccountItemTypeConverter implements Converter, Serializable
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
        return Constants.BankRecordDataType.labels.get(o);
    }
}
