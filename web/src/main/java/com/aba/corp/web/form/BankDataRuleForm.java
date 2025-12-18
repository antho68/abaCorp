package com.aba.corp.web.form;

import com.aba.corp.utils.Constants;
import com.aba.corp.web.item.EntityItem;
import com.aba.corp.web.utils.CommonUtils;
import dao.BankAccountRuleDAO;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import model.BankAccount;
import model.BankAccountRule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Named("bankDataRuleForm")
@ViewScoped
public class BankDataRuleForm extends AbstractCrudForm<BankAccountRule> implements Serializable
{
    @Inject
    private BankAccountRuleDAO bankAccountRuleDAO;

    private String id;
    private String accountId;
    private String containText;
    private String valueToSet;
    private Collection<SelectItem> valueToSets;
    private String value;
    private Collection<SelectItem> values;

    private EntityItem<BankAccount> selectedBankAccount;
    private Collection<BankAccount> myBankAccounts = new ArrayList<>();

    public BankDataRuleForm()
    {
    }

    @Override
    public void clearForm()
    {
        super.clearForm();

        setId(null);
        setSelectedBankAccount(null);
        setAccountId(null);
        setContainText(null);
        setValueToSet(Constants.BankAccountRuleValueToSet.TYPE);
        setValueToSets(new ArrayList<>());
        setValue(null);
        setValues(new ArrayList<>());

        initLists();
        valueToSetChanged();
    }

    private void initLists()
    {
        Collection<SelectItem> valueToSets = new ArrayList<>();
        for (String rt : Constants.BankAccountRuleValueToSet.all)
        {
            valueToSets.add(new SelectItem(rt, Constants.BankAccountRuleValueToSet.labels.get(rt)));
        }
        setValueToSets(valueToSets);
    }

    @Override
    public void fillForm()
    {
        super.fillForm();

        if (getSelectedData() != null)
        {
            setId(getSelectedData().getId());
            setAccountId(getSelectedData().getAccountId());

            setBankAccountForRule();

            setContainText(getSelectedData().getContainText());
            setValueToSet(getSelectedData().getValueToSet());
            setValue(getSelectedData().getValue());

            valueToSetChanged();
        }
    }

    private void setBankAccountForRule()
    {
        BankAccount bankAccount = getMyBankAccounts().stream()
                .filter(b -> b.getId().equals(getSelectedData().getAccountId()))
                        .findFirst().orElse(null);

        setSelectedBankAccount(null);
        if (bankAccount != null)
        {
            setSelectedBankAccount(new EntityItem<BankAccount>(bankAccount,
                    bankAccount.getName() + " (" + bankAccount.getCode() + ")"));

            getSelectedData().setAccountDescription(bankAccount != null ?
                    bankAccount.getCode() + " (" + bankAccount.getName() + ")" : "");
        }
    }

    @Override
    public void saveForm(String menuId)
    {
        getSelectedData().setId(getId());
        getSelectedData().setAccountId(getSelectedBankAccount().getItem().getId());
        getSelectedData().setContainText(getContainText());
        getSelectedData().setValueToSet(getValueToSet());
        getSelectedData().setValue(getValue());

        getSelectedData().setAccountDescription(getSelectedBankAccount().getLabel());

        try
        {
            if (isAddMode() || isCopyMode())
            {
                setSelectedData(bankAccountRuleDAO.insert(getSelectedData()));
            }
            else
            {
                setSelectedData(bankAccountRuleDAO.update("id", getId(), getSelectedData()));
            }

            setBankAccountForRule();
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

    public Collection<SelectItem> getValueToSets()
    {
        return valueToSets;
    }

    public void setValueToSets(Collection<SelectItem> valueToSets)
    {
        this.valueToSets = valueToSets;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public Collection<SelectItem> getValues()
    {
        return values;
    }

    public void setValues(Collection<SelectItem> values)
    {
        this.values = values;
    }

    public void valueToSetHandler()
    {
        formChanged();
        valueToSetChanged();
    }

    public void valueToSetChanged()
    {
        Collection<SelectItem> values = new ArrayList<>();
        if (getValueToSet() != null)
        {
            switch (getValueToSet())
            {
                case Constants.BankAccountRuleValueToSet.TYPE:
                    for (String type : Constants.BankRecordDataType.allTypes)
                    {
                        values.add(new SelectItem(type, Constants.BankRecordDataType.labels.get(type)));
                    }
                    break;
                case Constants.BankAccountRuleValueToSet.PAYMENT:
                    for (String currency : Constants.BankRecordDataPaymentType.all)
                    {
                        values.add(new SelectItem(currency, Constants.BankRecordDataPaymentType.labels.get(currency)));
                    }
                    break;
                case Constants.BankAccountRuleValueToSet.OWNER:
                    for (String currency : Constants.BankRecordDataOwner.all)
                    {
                        values.add(new SelectItem(currency, Constants.BankRecordDataOwner.labels.get(currency)));
                    }
                    break;
                case Constants.BankAccountRuleValueToSet.SCOPE:
                    for (String currency : Constants.BankRecordDataScope.all)
                    {
                        values.add(new SelectItem(currency, Constants.BankRecordDataScope.labels.get(currency)));
                    }
                    break;
            }
        }

        setValues(values);
    }

    public EntityItem<BankAccount> getSelectedBankAccount()
    {
        return selectedBankAccount;
    }
    public void setSelectedBankAccount(EntityItem<BankAccount> selectedBankAccount)
    {
        this.selectedBankAccount = selectedBankAccount;
    }

    public Collection<BankAccount> getMyBankAccounts()
    {
        return myBankAccounts;
    }
    public void setMyBankAccounts(Collection<BankAccount> myBankAccounts)
    {
        this.myBankAccounts = myBankAccounts;
    }

}
