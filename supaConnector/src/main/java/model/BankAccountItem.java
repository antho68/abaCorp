package model;

import com.aba.corp.utils.Utils;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.beans.Transient;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Date;

public class BankAccountItem extends AbstractModel<String> implements Serializable
{
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String id;
    private String accountId;
    private OffsetDateTime date;
    private OffsetDateTime effectiveDate;
    private String description;
    private Double amout;
    private String type;
    private String paymentType;
    private String owner;
    private String scope;

    // getters & setters
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

    public OffsetDateTime getDate()
    {
        return date;
    }

    public void setDate(OffsetDateTime date)
    {
        this.date = date;
    }

    @Transient
    public String getDateFormatted()
    {
        if (getDate() != null)
        {
            return Utils.getOffsetDateFormatted(getDate());
        }

        return "";
    }

    public OffsetDateTime getEffectiveDate()
    {
        return effectiveDate;
    }

    public void setEffectiveDate(OffsetDateTime effectiveDate)
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