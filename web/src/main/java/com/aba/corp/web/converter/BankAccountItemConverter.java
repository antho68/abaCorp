package com.aba.corp.web.converter;


import com.aba.corp.web.item.EntityItem;
import dao.BankAccountDAO;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import model.BankAccount;

import java.io.Serializable;


/**
 * @author aba
 */
@Named("bankAccountItemConverter")
@Singleton
@SuppressWarnings("rawtypes")
public class BankAccountItemConverter implements Converter, Serializable
{
    private static final long serialVersionUID = -5685508572774214248L;

    @Inject
    private BankAccountDAO bankAccountDAO;

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value)
    {
        if (value == null || ("" + value).length() == 0)
        {
            return "";
        }
        EntityItem<?> entityItem = (EntityItem<?>) value;
        if (entityItem.getItem() == null || entityItem.getItem().getId() == null)
        {
            return "";
        }
        return entityItem.getItem().getId().toString();
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value)
    {
        try
        {
            String pk = value;
            BankAccount obj = null;
            if (pk != null)
            {
                try
                {
                    obj = bankAccountDAO.findFirstById(pk);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }

            EntityItem<BankAccount> result = new EntityItem<BankAccount>(obj, getLabelForEntity(obj));
            return result;
        }
        catch (NumberFormatException e)
        {
            // no need to log - user typed a string into autocomplete field
            //CommonUtils.logError(CommonConstants.ERROR_LOG, e.getMessage(), e);
        }
        return null;
    }

    public static String getLabelForEntity(BankAccount bankAccount)
    {
        if (bankAccount != null)
        {
            return bankAccount.getName() + " (" + bankAccount.getCode() + ")";
        }
        return "";
    }

}
