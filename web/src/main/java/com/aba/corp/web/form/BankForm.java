package com.aba.corp.web.form;

import com.aba.corp.utils.Constants;
import com.aba.corp.web.utils.CommonUtils;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import model.BankAccountItem;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Named("bankForm")
@ViewScoped
public class BankForm extends AbstractCrudForm<BankAccountItem> implements Serializable
{
    private String id;
    private String accountId;
    private OffsetDateTime date;
    private OffsetDateTime effectiveDate;
    private String description;
    private Double amout;

    private String type;
    private Collection<SelectItem> itemTypes;
    private String paymentType;
    private Collection<SelectItem> paymentTypes;
    private String owner;
    private Collection<SelectItem> owners;
    private String scope;
    private Collection<SelectItem> scopes;

    public BankForm()
    {
    }

    @Override
    public void clearForm()
    {
        super.clearForm();

        setId(null);
        setAccountId(null);
        setDate(null);
        setEffectiveDate(null);
        setDescription(null);
        setAmout(null);
        setType(null);
        setItemTypes(null);
        setPaymentType(null);
        setPaymentTypes(null);
        setOwner(null);
        setOwners(null);
        setScope(null);
        setScopes(null);

        initLists();
    }

    private void initLists()
    {
        Collection<SelectItem> itemTypes = new ArrayList<>();
        for (String rt : Constants.BankRecordDataType.allTypes)
        {
            itemTypes.add(new SelectItem(rt, Constants.BankRecordDataType.labels.get(rt)));
        }
        setItemTypes(itemTypes);

        Collection<SelectItem> paymentTypes = new ArrayList<>();
        for (String rt : Constants.BankRecordDataPaymentType.all)
        {
            paymentTypes.add(new SelectItem(rt, Constants.BankRecordDataPaymentType.labels.get(rt)));
        }
        setPaymentTypes(paymentTypes);

        Collection<SelectItem> owners = new ArrayList<>();
        for (String rt : Constants.BankRecordDataOwner.all)
        {
            owners.add(new SelectItem(rt, Constants.BankRecordDataOwner.labels.get(rt)));
        }
        setOwners(owners);

        Collection<SelectItem> scopes = new ArrayList<>();
        for (String rt : Constants.BankRecordDataScope.all)
        {
            scopes.add(new SelectItem(rt, Constants.BankRecordDataScope.labels.get(rt)));
        }
        setScopes(scopes);
    }

    @Override
    public void fillForm()
    {
        super.fillForm();

        if (getSelectedData() != null)
        {
        }
    }

    @Override
    public void saveForm(String menuId)
    {
        super.saveForm(menuId);

    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public Collection<SelectItem> getScopes()
    {
        if (CommonUtils.isCollectionEmpty(scopes))
        {
            initLists();
        }

        return scopes;
    }

    public void setScopes(Collection<SelectItem> scopes)
    {
        this.scopes = scopes;
    }

    public String getScope()
    {
        return scope;
    }

    public void setScope(String scope)
    {
        this.scope = scope;
    }

    public Collection<SelectItem> getOwners()
    {
        if (CommonUtils.isCollectionEmpty(owners))
        {
            initLists();
        }

        return owners;
    }

    public void setOwners(Collection<SelectItem> owners)
    {
        this.owners = owners;
    }

    public String getOwner()
    {
        return owner;
    }

    public void setOwner(String owner)
    {
        this.owner = owner;
    }

    public Collection<SelectItem> getPaymentTypes()
    {
        if (CommonUtils.isCollectionEmpty(paymentTypes))
        {
            initLists();
        }

        return paymentTypes;
    }

    public void setPaymentTypes(Collection<SelectItem> paymentTypes)
    {
        this.paymentTypes = paymentTypes;
    }

    public String getPaymentType()
    {
        return paymentType;
    }

    public void setPaymentType(String paymentType)
    {
        this.paymentType = paymentType;
    }

    public Collection<SelectItem> getItemTypes()
    {
        if (CommonUtils.isCollectionEmpty(itemTypes))
        {
            initLists();
        }

        return itemTypes;
    }

    public void setItemTypes(Collection<SelectItem> itemTypes)
    {
        this.itemTypes = itemTypes;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public Double getAmout()
    {
        return amout;
    }

    public void setAmout(Double amout)
    {
        this.amout = amout;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public OffsetDateTime getEffectiveDate()
    {
        return effectiveDate;
    }

    public void setEffectiveDate(OffsetDateTime effectiveDate)
    {
        this.effectiveDate = effectiveDate;
    }

    public OffsetDateTime getDate()
    {
        return date;
    }

    public void setDate(OffsetDateTime date)
    {
        this.date = date;
    }

    public String getAccountId()
    {
        return accountId;
    }

    public void setAccountId(String accountId)
    {
        this.accountId = accountId;
    }
}
