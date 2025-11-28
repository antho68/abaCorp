package com.aba.corp.web.controller;

import com.aba.corp.web.form.BankForm;
import com.aba.corp.web.form.UserForm;
import dao.UserDAO;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import model.BankItem;
import model.User;
import org.primefaces.model.file.UploadedFile;

import java.io.Serializable;

@Named
@ViewScoped
public class BankController extends AbstractController<BankForm, BankItem> implements Serializable
{
    @Inject
    private BankForm bankForm;

    private UploadedFile file;

    @PostConstruct
    public void init()
    {
        setCrudForm(bankForm);
    }

    public void upload() {
        if (file != null) {
            FacesMessage message = new FacesMessage("Successful", file.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public UploadedFile getFile() {
        return file;
    }
    public void setFile(UploadedFile file) {
        this.file = file;
    }
}