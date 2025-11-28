package com.aba.corp.web.controller;

import com.aba.corp.web.SessionBean;
import com.aba.corp.web.form.AbstractCrudForm;
import com.aba.corp.web.utils.CommonUtils;
import com.aba.corp.web.utils.CrudMode;
import com.sun.tools.javac.Main;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import model.AbstractModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.component.datatable.DataTable;
import utils.MessageUtils;

import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.util.LinkedList;

public class AbstractController<CF extends AbstractCrudForm<D>, D extends AbstractModel<? extends Serializable>>
        extends AbstractBaseController
{
    protected static final Logger log = LogManager.getLogger(Main.class);

    @Inject
    protected SessionBean sessionBean;

    protected CF crudForm;
    protected LinkedList<D> datas;
    protected LinkedList<D> filteredDatas;
    protected D selectedData;

    public AbstractController()
    {
    }

    protected void search()
    {
    }

    public void searchActionListener()
    {
        MessageUtils.clearMessageList();

        this.search();
        this.updateColumns();
    }

    public void updateColumns()
    {
        UIComponent table = FacesContext.getCurrentInstance().getViewRoot().findComponent(":form:dataTable");
        if (table != null && table instanceof DataTable dataTable)
        {
            dataTable.setFirst(0);
            dataTable.resetValue();
        }
    }

    public LinkedList<D> getDatas()
    {
        return datas;
    }

    public void setDatas(LinkedList<D> datas)
    {
        this.datas = datas;
    }

    public D getSelectedData()
    {
        return selectedData;
    }

    public void setSelectedData(D selectedData)
    {
        this.selectedData = selectedData;
    }

    public void setEditSelectedDto(D selectedData)
    {
        setSelectedData(selectedData);
        this.crudForm.setMode(CrudMode.EDIT);
        this.crudForm.resetForm(selectedData);

        showDialog("crudDialog");
        updateOnRequestContext("crudDialogForm");
    }

    public CF getCrudForm()
    {
        return crudForm;
    }

    public void setCrudForm(CF crudForm)
    {
        this.crudForm = crudForm;
    }

    public void handleCrudDialogClose()
    {
        if (getCrudForm().getFormChanged())
        {
            showDialog("closeConfirmationDialog");
        }

        hideDialog("crudDialog");
    }

    public void dismissChangesActionListener()
    {
        hideDialog("crudDialog");
    }

    public void resetFormActionListener(ActionEvent actionEvent)
    {
        resetForm(true);
    }

    protected void resetForm(boolean clearMessages)
    {
        getCrudForm().resetForm(getSelectedData());

        if (clearMessages)
        {
            MessageUtils.clearMessageList();
        }
    }

    public void saveFormActionListener()
    {
        this.saveFormAction(true);
    }

    public void saveFormAction(boolean closeTheDialog)
    {
        MessageUtils.clearMessageList();

        if (getCrudForm().validateForm())
        {
            try
            {
                getCrudForm().saveForm(getMenuId());
                if (datas == null)
                {
                    datas = new LinkedList<D>();
                }

                if (getCrudForm().isAddMode() || getCrudForm().isCopyMode())
                {
                    datas.add(selectedData);

                    if (filteredDatas != null)
                    {
                        filteredDatas.add(selectedData);
                    }

                    doAfterSave();
                    updateColumns();
                }
                else
                {
                    if (!CommonUtils.isCollectionEmpty(datas))
                    {
                        int index = datas.indexOf(selectedData);
                        if (index >= 0)
                        {
                            datas.set(index, selectedData);
                        }
                    }

                    doAfterSave();
                }

                if (closeTheDialog)
                {
                    hideDialog(getCrudForm().getDialogName());
                    getCrudForm().clearForm();
                }
                else
                {
                    if (getCrudForm().isAddMode() || getCrudForm().isCopyMode())
                    {
                        getCrudForm().setMode(CrudMode.EDIT);
                        getCrudForm().fillForm();
                    }
                }

                return;
            }
            catch (Exception e)
            {
                CommonUtils.logDebug(e.getMessage());
            }
        }

        if (MessageUtils.hasErrorMessage())
        {
            showErrorDialog();
        }
    }

    public void addActionListener()
    {
        D data = initNewData();
        this.crudForm.setMode(CrudMode.ADD);
        this.crudForm.resetForm(data);

        showDialog("crudDialog");
        updateOnRequestContext("crudDialogForm");
    }

    protected D initNewData()
    {
        return null;
    }

    public LinkedList<D> getFilteredDatas()
    {
        return filteredDatas;
    }

    public void setFilteredDatas(LinkedList<D> filteredDatas)
    {
        this.filteredDatas = filteredDatas;
    }

    protected void doAfterSave()
    {
    }
}
