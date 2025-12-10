package model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

public class BankAccountRule extends AbstractModel<String> implements Serializable
{
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String id;
    private String accountId;
    private String containText;
    private String valueToSet;
    private String value;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getAccountId()
    {
        return accountId;
    }

    public void setAccountId(String accountId)
    {
        this.accountId = accountId;
    }

    public String getContainText()
    {
        return containText;
    }

    public void setContainText(String containText)
    {
        this.containText = containText;
    }

    public String getValueToSet()
    {
        return valueToSet;
    }

    public void setValueToSet(String valueToSet)
    {
        this.valueToSet = valueToSet;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

}