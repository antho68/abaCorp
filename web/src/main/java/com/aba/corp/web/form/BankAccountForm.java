package com.aba.corp.web.form;

import com.aba.corp.utils.Constants;
import com.aba.corp.web.utils.CommonUtils;
import dao.BankAccountDAO;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import model.BankAccount;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Named("bankAccountForm")
@ViewScoped
public class BankAccountForm extends AbstractCrudForm<BankAccount> implements Serializable
{
    @Inject
    private BankAccountDAO bankAccountDAO;

    private String id;
    private String userId;
    private String name;
    private String code;
    private String description;
    private Collection<SelectItem> accountTypes;
    private String accountType;

    public BankAccountForm()
    {
    }

    @Override
    public void clearForm()
    {
        super.clearForm();

        initTypes();

        setId(null);
        setName(null);
        setUserId(null);
        setDescription(null);
        setCode(null);
        setAccountType(null);
    }

    private void initTypes()
    {
        Collection<SelectItem> accountTypes = new ArrayList<>();
        for (String rt : Constants.AccountType.allTypes)
        {
            accountTypes.add(new SelectItem(rt, Constants.AccountType.labels.get(rt)));
        }
        setAccountTypes(accountTypes);
    }

    @Override
    public void fillForm()
    {
        super.fillForm();

        if (getSelectedData() != null)
        {
            setId(getSelectedData().getId());
            setName(getSelectedData().getName());
            setUserId(getSelectedData().getUserId());
            setDescription(getSelectedData().getDescription());
            setCode(getSelectedData().getCode());
            setAccountType(getSelectedData().getType());
        }
    }

    @Override
    public void saveForm(String menuId)
    {
        super.saveForm(menuId);

        getSelectedData().setId(getId());
        getSelectedData().setName(getName());
        getSelectedData().setUserId(getUserId());
        getSelectedData().setDescription(getDescription());
        getSelectedData().setCode(getCode());
        getSelectedData().setType(getAccountType());

        try
        {
            if (isAddMode() || isCopyMode())
            {
                bankAccountDAO.insert(getSelectedData());
            }
            else
            {
                bankAccountDAO.update("id", getId(), getSelectedData());
            }
        }
        catch (Exception e)
        {
            CommonUtils.logError(e);
        }
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Collection<SelectItem> getAccountTypes()
    {
        if (CommonUtils.isCollectionEmpty(accountTypes))
        {
            initTypes();
        }

        return accountTypes;
    }

    public void setAccountTypes(Collection<SelectItem> accountTypes)
    {
        this.accountTypes = accountTypes;
    }

    public String getAccountType()
    {
        return accountType;
    }

    public void setAccountType(String accountType)
    {
        this.accountType = accountType;
    }
}
