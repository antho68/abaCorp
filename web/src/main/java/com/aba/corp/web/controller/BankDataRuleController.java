package com.aba.corp.web.controller;

import com.aba.corp.utils.Utils;
import com.aba.corp.web.form.BankDataRuleForm;
import com.aba.corp.web.item.EntityItem;
import com.aba.corp.web.utils.CommonUtils;
import dao.BankAccountDAO;
import dao.BankAccountRuleDAO;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import model.BankAccount;
import model.BankAccountRule;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Named
@ViewScoped
public class BankDataRuleController extends AbstractController<BankDataRuleForm, BankAccountRule> implements Serializable
{
    @Inject
    private BankDataRuleForm bankDataRuleForm;
    @Inject
    private BankAccountRuleDAO bankAccountRuleDAO;
    @Inject
    private BankAccountDAO bankAccountDAO;

    private Collection<BankAccount> myBankAccounts = new ArrayList<>();

    @PostConstruct
    public void init()
    {
        setCrudForm(bankDataRuleForm);
        search();
    }

    @Override
    protected void search()
    {
        try
        {
            if (sessionBean != null && sessionBean.getUser() != null)
            {
                setDatas(bankAccountRuleDAO.findByUserId(sessionBean.getUser().getId()));
                setMyBankAccounts(bankAccountDAO.findBy("userId", sessionBean.getUser().getId()));

                getCrudForm().setMyBankAccounts(getMyBankAccounts());
                getCrudForm().setDialogName("bankDataRuleDialog");

                for (BankAccountRule bankAccountRule : getDatas())
                {
                    BankAccount bankAccount = getMyBankAccounts().stream()
                            .filter(ba -> ba.getId().equals(bankAccountRule.getAccountId()))
                            .findFirst().orElse(null);
                    bankAccountRule.setAccountDescription(bankAccount != null ?
                            bankAccount.getCode() + " (" + bankAccount.getName() + ")" : "");
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected BankAccountRule initNewData()
    {
        return new BankAccountRule();
    }

    public Collection<BankAccount> getMyBankAccounts()
    {
        return myBankAccounts;
    }

    public void setMyBankAccounts(Collection<BankAccount> myBankAccounts)
    {
        this.myBankAccounts = myBankAccounts;
    }

    public Collection<EntityItem<BankAccount>> completeBankAccount(String searchTerm)
    {
        Collection<EntityItem<BankAccount>> suggestions = new ArrayList<EntityItem<BankAccount>>();
        String searchTermWithoutLanguage = Utils.getStringWithoutLanguageChar(searchTerm);

        if (myBankAccounts != null)
        {
            for (BankAccount bankAccount : myBankAccounts)
            {
                String text = bankAccount.getName() + " (" + bankAccount.getCode() + ")";
                String textWithoutLanguage = Utils.getStringWithoutLanguageChar(text);

                if (StringUtils.containsIgnoreCase(textWithoutLanguage, searchTermWithoutLanguage))
                {
                    EntityItem<BankAccount> bankAccountItem = new EntityItem<BankAccount>(bankAccount, text);
                    suggestions.add(bankAccountItem);
                }
            }
        }

        return suggestions;
    }

    public void handleBankDataRuleDialogClose()
    {
        if (getCrudForm().getFormChanged())
        {
            showDialog("closeConfirmationBankDataRuleDialog");
        }
        else
        {
            hideDialog("bankDataRuleDialog");
        }
    }

    public void dismissBankDataRuleChangesActionListener()
    {
        hideDialog("bankDataRuleDialog");
    }

    public void setEditRuleSelectedDto(BankAccountRule selectedData)
    {
        setEditSelectedDto(selectedData, "bankDataRuleDialog");
    }

    @Override
    public void addActionListener()
    {
        addAction("bankDataRuleDialog");
    }

    public void resetBankDataRuleFormActionListener()
    {
        resetForm(true);
    }

    public void saveBankDataRuleFormActionListener()
    {
        saveFormActionListener();
    }
}