package com.aba.corp.web.controller;

import com.aba.corp.ProcessorAccountItem;
import com.aba.corp.dto.BankRecordDataDto;
import com.aba.corp.utils.Constants;
import com.aba.corp.web.form.BankDataRuleForm;
import com.aba.corp.web.form.BankForm;
import com.aba.corp.web.utils.CommonUtils;
import com.aba.corp.web.utils.CrudMode;
import dao.BankAccountDAO;
import dao.BankAccountItemDAO;
import dao.BankAccountRuleDAO;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import model.BankAccount;
import model.BankAccountItem;
import model.BankAccountRule;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import utils.MessageUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Named
@ViewScoped
public class BankController extends AbstractController<BankForm, BankAccountItem> implements Serializable
{
    @Inject
    private BankForm bankForm;
    @Inject
    private BankAccountRuleDAO bankAccountRuleDAO;
    @Inject
    private BankDataRuleForm bankDataRuleForm;
    @Inject
    private BankAccountDAO bankAccountDAO;
    @Inject
    private BankAccountItemDAO bankAccountItemDAO;

    private UploadedFile file;
    private InputStream inputStream;

    private Collection<BankAccountItem> decryptedBankAccountItems;
    private Collection<BankAccount> myBankAccounts = new ArrayList<>();
    private BankAccountRule selectedBankAccountRule;

    private Map<String, LinkedList<BankAccountRule>> bankAccountRulesMap = new HashMap<>();

    @PostConstruct
    public void init()
    {
        setCrudForm(bankForm);
        search();
    }

    @Override
    protected void search()
    {
        try
        {
            setMyBankAccounts(new ArrayList<>());

            if (sessionBean != null && sessionBean.getUser() != null)
            {
                setMyBankAccounts(bankAccountDAO.findBy("userId", sessionBean.getUser().getId()));

                bankDataRuleForm.setMyBankAccounts(getMyBankAccounts());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void addFromFileActionListener()
    {
        setFile(null);
        setInputStream(null);

        showDialog("addFromFileDialog");
        updateOnRequestContext("addFromFileDialogForm");
    }

    public void processDataFile()
    {
        if (getInputStream() != null)
        {
            ProcessorAccountItem processorAccountItem = new ProcessorAccountItem();
            try
            {
                Collection<BankRecordDataDto> bankRecordDataDtos = processorAccountItem.doFullRead(getInputStream()
                        , false, false);

                decryptedBankAccountItems = new LinkedList<>();

                Integer i = 1;
                for (BankRecordDataDto d : bankRecordDataDtos)
                {
                    if (!CommonUtils.isCollectionEmpty(getDatas())
                            && getDatas().stream().anyMatch(b -> b.getDate().equals(d.getDate())
                            && b.getDescription().equals(d.getDescription()) && b.getAmout().equals(d.getAmout())))
                    {
                        continue;
                    }

                    BankAccountItem bankAccountItem = new BankAccountItem();

                    bankAccountItem.setId(i.toString());
                    bankAccountItem.setDate(CommonUtils.getOffsetDateTimeFromDate(d.getDate()));
                    bankAccountItem.setEffectiveDate(CommonUtils.getOffsetDateTimeFromDate(d.getEffectiveDate()));
                    bankAccountItem.setDescription(d.getDescription());
                    bankAccountItem.setAmout(d.getAmout());

                    bankAccountItem.setAccountId(null);
                    if (getMyBankAccounts() != null && getMyBankAccounts().size() > 1)
                    {
                        BankAccount matchedBankAccount = getMyBankAccounts().stream()
                                .filter(b -> d.getAccountCode() != null && ("10278 " + d.getAccountCode()).equals(b.getCode()))
                                .findFirst().orElse(null);

                        if (matchedBankAccount != null)
                        {
                            bankAccountItem.setAccountId(matchedBankAccount.getId());
                        }
                    }

                    processRulesForItem(bankAccountItem);

                    decryptedBankAccountItems.add(bankAccountItem);
                    i++;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                MessageUtils.addErrorMessage("Error processing file: " + e.getMessage());
                showErrorDialog();
            }
        }
        else
        {
            MessageUtils.addErrorMessage("No file uploaded.");
            showErrorDialog();
        }
    }

    public void processRulesForItem(BankAccountItem bankAccountItem)
    {
        if (bankAccountItem.getAccountId() != null
                && !getBankAccountRulesMap().containsKey(bankAccountItem.getAccountId()))
        {
            try
            {
                LinkedList<BankAccountRule> bankAccountRules =
                        bankAccountRuleDAO.findByAccountId(bankAccountItem.getAccountId());

                getBankAccountRulesMap().put(bankAccountItem.getAccountId(), bankAccountRules);
            }
            catch (Exception e)
            {
                CommonUtils.logError("Error loading bank account rules for accountId "
                        + bankAccountItem.getAccountId(), e);
            }
        }

        if (bankAccountItem.getAccountId() != null
                && getBankAccountRulesMap().containsKey(bankAccountItem.getAccountId()))
        {
            LinkedList<BankAccountRule> allRulesForAccount = getBankAccountRulesMap().get(bankAccountItem.getAccountId());

            if (!CommonUtils.isCollectionEmpty(allRulesForAccount))
            {
                Collection<BankAccountRule> rulesForItem = allRulesForAccount.stream()
                        .filter(r -> bankAccountItem.getDescription().contains(r.getContainText()))
                        .toList();

                if (!CommonUtils.isCollectionEmpty(rulesForItem))
                {
                    for (BankAccountRule rule : rulesForItem)
                    {
                        switch (rule.getValueToSet())
                        {
                            case Constants.BankAccountRuleValueToSet.TYPE:
                                bankAccountItem.setType(rule.getValue());
                                bankAccountItem.setPresetByRule(true);
                                break;
                            case Constants.BankAccountRuleValueToSet.PAYMENT:
                                bankAccountItem.setPaymentType(rule.getValue());
                                bankAccountItem.setPresetByRule(true);
                                break;
                            case Constants.BankAccountRuleValueToSet.OWNER:
                                bankAccountItem.setOwner(rule.getValue());
                                bankAccountItem.setPresetByRule(true);
                                break;
                            case Constants.BankAccountRuleValueToSet.SCOPE:
                                bankAccountItem.setScope(rule.getValue());
                                bankAccountItem.setPresetByRule(true);
                                break;
                        }
                    }
                }
            }
        }
    }

    public void setDeleteImportData(BankAccountItem toDeleteBankAccountItem)
    {
        getDecryptedBankAccountItems().remove(toDeleteBankAccountItem);
    }

    public Collection<BankAccountItem> getDecryptedBankAccountItems()
    {
        return decryptedBankAccountItems;
    }

    public void setDecryptedBankAccountItems(Collection<BankAccountItem> decryptedBankAccountItems)
    {
        this.decryptedBankAccountItems = decryptedBankAccountItems;
    }

    public void handleFileUpload(FileUploadEvent event) throws IOException
    {
        setInputStream(event.getFile().getInputStream());
    }

    public UploadedFile getFile()
    {
        return file;
    }

    public void setFile(UploadedFile file)
    {
        this.file = file;
    }

    public InputStream getInputStream()
    {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream)
    {
        this.inputStream = inputStream;
    }

    public void setAddNewRuleData(BankAccountItem bankAccountItem)
    {
        bankDataRuleForm.setDialogName("bankDataRuleDialog");

        BankAccountRule bankAccountRule = new BankAccountRule();
        bankAccountRule.setAccountId(bankAccountItem.getAccountId());
        bankAccountRule.setContainText(bankAccountItem.getDescription());
        bankAccountRule.setValue(Constants.BankAccountRuleValueToSet.TYPE);
        bankAccountRule.setValueToSet(null);
        setSelectedBankAccountRule(bankAccountRule);

        bankDataRuleForm.resetForm(bankAccountRule);
        bankDataRuleForm.setMode(CrudMode.ADD);

        showDialog(bankDataRuleForm.getDialogName());
        updateOnRequestContext(bankDataRuleForm.getDialogName() + "Form");
    }

    public Collection<BankAccount> getMyBankAccounts()
    {
        return myBankAccounts;
    }

    public void setMyBankAccounts(Collection<BankAccount> myBankAccounts)
    {
        this.myBankAccounts = myBankAccounts;
    }

    public void resetBankDataRuleFormActionListener()
    {
        bankDataRuleForm.resetForm(getSelectedBankAccountRule());
        MessageUtils.clearMessageList();
    }

    public BankAccountRule getSelectedBankAccountRule()
    {
        return selectedBankAccountRule;
    }

    public void setSelectedBankAccountRule(BankAccountRule selectedBankAccountRule)
    {
        this.selectedBankAccountRule = selectedBankAccountRule;
    }

    public void saveBankDataRuleFormActionListener()
    {
        saveFormActionListener();
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

    public Map<String, LinkedList<BankAccountRule>> getBankAccountRulesMap()
    {
        return bankAccountRulesMap;
    }

    public void setBankAccountRulesMap(Map<String, LinkedList<BankAccountRule>> bankAccountRulesMap)
    {
        this.bankAccountRulesMap = bankAccountRulesMap;
    }

    public void importActionListener()
    {
        if (!CommonUtils.isCollectionEmpty(getDecryptedBankAccountItems()))
        {
            Map<String, Collection<String>> messages = bankAccountItemDAO.importData(getDecryptedBankAccountItems());

            for (String k : messages.keySet())
            {
                for (String m : messages.get(k))
                {
                    System.out.println(m);
                }
            }
        }
        else
        {
            MessageUtils.addErrorMessage("Aucune donnée à importer.");
            showErrorDialog();
        }
    }
}