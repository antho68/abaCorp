package com.aba.corp.web.converter;

import com.aba.corp.utils.Constants;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.inject.Named;
import model.BankAccountRule;

import java.io.Serializable;

@Named("bankAccountRuleValueConverter")
public class BankAccountRuleValueConverter implements Converter, Serializable
{
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s)
    {
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o)
    {
        if (o instanceof BankAccountRule rule)
        {
            switch (rule.getValueToSet())
            {
                case Constants.BankAccountRuleValueToSet.TYPE :
                    return Constants.BankRecordDataType.labels.get(rule.getValue());
                case Constants.BankAccountRuleValueToSet.OWNER:
                    return Constants.BankRecordDataOwner.labels.get(rule.getValue());
                case Constants.BankAccountRuleValueToSet.PAYMENT:
                    return Constants.BankRecordDataPaymentType.labels.get(rule.getValue());
                case Constants.BankAccountRuleValueToSet.SCOPE:
                    return Constants.BankRecordDataScope.labels.get(rule.getValue());
            }
        }

        return "null";
    }
}
