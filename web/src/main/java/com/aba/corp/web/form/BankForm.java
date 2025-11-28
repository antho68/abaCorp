package com.aba.corp.web.form;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import model.BankItem;

import java.io.Serializable;

@Named("bankForm")
@ViewScoped
public class BankForm extends AbstractCrudForm<BankItem> implements Serializable
{

    public BankForm()
    {
    }

    @Override
    public void clearForm()
    {
        super.clearForm();


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

}
