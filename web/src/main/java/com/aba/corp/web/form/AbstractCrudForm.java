package com.aba.corp.web.form;

import com.aba.corp.web.utils.CrudMode;
import model.AbstractModel;
import org.primefaces.event.SelectEvent;

import java.io.Serializable;

public class AbstractCrudForm<D extends AbstractModel<? extends Serializable>>
{
    protected D selectedData;
    private CrudMode mode;
    private Boolean formChanged = false;
    private String dialogName = "crudDialog";

    public AbstractCrudForm()
    {
    }

    public CrudMode getMode()
    {
        return mode;
    }

    public void setMode(CrudMode mode)
    {
        this.mode = mode;
    }

    public D getSelectedData()
    {
        return selectedData;
    }
    public void setSelectedData(D selectedData)
    {
        this.selectedData = selectedData;
    }

    public Boolean getFormChanged()
    {
        return formChanged;
    }
    public void setFormChanged(Boolean formChanged)
    {
        this.formChanged = formChanged;
    }

    public void formChanged(SelectEvent selectEvent)
    {
        this.formChanged = true;
    }

    public void formChanged()
    {
        this.formChanged = true;
    }

    public void resetForm(D selectedData)
    {
        this.selectedData = selectedData;
        this.mode = null;
        this.formChanged = false;

        clearForm();
        fillForm();
    }

    public void clearForm()
    {

    }

    public void fillForm()
    {

    }

    public boolean validateForm()
    {
        return true;
    }

    public void saveForm(String menuId)
    {

    }

    public boolean isAddMode()
    {
        return mode == CrudMode.ADD;
    }

    public boolean isEditMode()
    {
        return mode == CrudMode.EDIT;
    }

    public boolean isDeleteMode()
    {
        return mode == CrudMode.DELETE;
    }

    public boolean isCopyMode()
    {
        return mode == CrudMode.COPY;
    }

    public String getDialogName()
    {
        return dialogName;
    }
    public void setDialogName(String dialogName)
    {
        this.dialogName = dialogName;
    }
}
