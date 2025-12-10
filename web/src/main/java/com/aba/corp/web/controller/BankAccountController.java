package com.aba.corp.web.controller;

import com.aba.corp.ProcessorAccount;
import com.aba.corp.dto.BankAccountDataDto;
import com.aba.corp.web.form.BankAccountForm;
import com.aba.corp.web.utils.CommonUtils;
import dao.BankAccountDAO;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import model.BankAccount;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import utils.MessageUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

@Named
@ViewScoped
public class BankAccountController extends AbstractController<BankAccountForm, BankAccount> implements Serializable
{
    @Inject
    private BankAccountForm bankAccountForm;
    @Inject
    private BankAccountDAO bankAccountDAO;

    private UploadedFile file;
    private InputStream inputStream;
    private Collection<BankAccount> decryptedBankAccounts;

    @PostConstruct
    public void init()
    {
        setCrudForm(bankAccountForm);
        search();
    }

    @Override
    protected void search()
    {
        try
        {
            if (sessionBean != null && sessionBean.getUser() != null)
            {
                setDatas(bankAccountDAO.findBy("userId", sessionBean.getUser().getId()));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected BankAccount initNewData()
    {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setUserId(sessionBean.getUser().getId());

        return bankAccount;
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
            ProcessorAccount processorAccount = new ProcessorAccount();
            try
            {
                Collection<BankAccountDataDto> bankAccountDataDto = processorAccount.doFullRead(getInputStream(), false);
                decryptedBankAccounts = new LinkedList<>();

                Integer i = 1;
                for (BankAccountDataDto d : bankAccountDataDto)
                {
                    if (!CommonUtils.isCollectionEmpty(getDatas())
                        && getDatas().stream().anyMatch(b -> b.getCode().equals(d.getCode())))
                    {
                        continue;
                    }

                    BankAccount bankAccount = new BankAccount();
                    bankAccount.setId(i.toString());
                    bankAccount.setUserId(sessionBean.getUser().getId());
                    bankAccount.setName(d.getName());
                    bankAccount.setCode(d.getCode());
                    bankAccount.setType(d.getType());

                    decryptedBankAccounts.add(bankAccount);
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

    public UploadedFile getFile()
    {
        return file;
    }

    public void setFile(UploadedFile file)
    {
        this.file = file;
    }

    public Collection<BankAccount> getDecryptedBankAccounts()
    {
        return decryptedBankAccounts;
    }

    public void setDecryptedBankAccounts(Collection<BankAccount> decryptedBankAccounts)
    {
        this.decryptedBankAccounts = decryptedBankAccounts;
    }

    public InputStream getInputStream()
    {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream)
    {
        this.inputStream = inputStream;
    }

    public void handleFileUpload(FileUploadEvent event) throws IOException
    {
        setInputStream(event.getFile().getInputStream());
    }

    public void setDeleteImportData(BankAccount toDeleteBankAccount)
    {
        getDecryptedBankAccounts().remove(toDeleteBankAccount);
    }

    public void importActionListener()
    {
        try
        {
            if (getDecryptedBankAccounts() != null && !getDecryptedBankAccounts().isEmpty())
            {
                for (BankAccount b : getDecryptedBankAccounts())
                {
                    b.setId(null);
                    bankAccountDAO.insert(b);
                }

                hideDialog("addFromFileDialog");
                search();
            }
            else
            {
                MessageUtils.addErrorMessage("No data to import.");
                showErrorDialog();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            MessageUtils.addErrorMessage("Error during import: " + e.getMessage());
            showErrorDialog();
        }
    }
}