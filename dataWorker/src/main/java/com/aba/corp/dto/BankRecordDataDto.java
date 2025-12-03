package com.aba.corp.dto;

import com.aba.corp.utils.Utils;

import java.util.Date;

public class BankRecordDataDto
{
    private String accountCode;

    private Date date;
    private Date effectiveDate;
    private String description;
    private Double amout;

    private String type;
    private String paymentType;
    private String owner;
    private String scope;

    public BankRecordDataDto()
    {
    }

    public String getAccountCode()
    {
        return accountCode;
    }

    public void setAccountCode(String accountCode)
    {
        this.accountCode = accountCode;
    }

    public String getDataFromRecordDataDto()
    {
        return Utils.getDateFormatted(date) + " " + getDescription() + " " + getAmout();
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public Date getEffectiveDate()
    {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate)
    {
        this.effectiveDate = effectiveDate;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Double getAmout()
    {
        return amout;
    }

    public void setAmout(Double amout)
    {
        this.amout = amout;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getPaymentType()
    {
        return paymentType;
    }

    public void setPaymentType(String paymentType)
    {
        this.paymentType = paymentType;
    }

    public String getOwner()
    {
        return owner;
    }

    public void setOwner(String owner)
    {
        this.owner = owner;
    }

    public String getScope()
    {
        return scope;
    }

    public void setScope(String scope)
    {
        this.scope = scope;
    }
}
