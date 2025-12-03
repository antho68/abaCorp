package com.aba.corp.web.controller;

import com.aba.corp.ProcessorAccountItem;
import com.aba.corp.dto.BankRecordDataDto;
import com.aba.corp.web.form.BankForm;
import com.aba.corp.web.utils.CommonUtils;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import model.BankAccount;
import model.BankAccountItem;
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
public class BankController extends AbstractController<BankForm, BankAccountItem> implements Serializable
{
    @Inject
    private BankForm bankForm;

    private UploadedFile file;
    private InputStream inputStream;

    private Collection<BankAccountItem> decryptedBankAccountItems;

    @PostConstruct
    public void init()
    {
        setCrudForm(bankForm);
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
                        , false);

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

                    //TODO ABA
                    BankAccountItem bankAccountItem = new BankAccountItem();
                    bankAccountItem.setId(i.toString());

                    bankAccountItem.setDate(CommonUtils.getOffsetDateTimeFromDate(d.getDate()));
                    bankAccountItem.setEffectiveDate(CommonUtils.getOffsetDateTimeFromDate(d.getEffectiveDate()));
                    bankAccountItem.setDescription(d.getDescription());
                    bankAccountItem.setAmout(d.getAmout());
                    bankAccountItem.setType(d.getType());
                    bankAccountItem.setPaymentType(d.getPaymentType());
                    bankAccountItem.setOwner(d.getOwner());
                    bankAccountItem.setScope(d.getScope());

                    bankAccountItem.setAccountId(null);

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
}