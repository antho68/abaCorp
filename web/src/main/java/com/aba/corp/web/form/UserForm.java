package com.aba.corp.web.form;

import com.aba.corp.utils.Constants;
import com.aba.corp.web.utils.CommonUtils;
import dao.UserDAO;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Named("userForm")
@ViewScoped
public class UserForm extends AbstractCrudForm<User> implements Serializable
{
    @Inject
    private UserDAO userDAO;

    private String id;
    private String email;
    private String name;

    private Collection<SelectItem> roleTypes;
    private String roleType;

    public UserForm()
    {
    }

    @Override
    public void clearForm()
    {
        super.clearForm();

        Collection<SelectItem> roleTypes = new ArrayList<>();
        for (String rt : Constants.UserRoleType.allRoles)
        {
            roleTypes.add(new SelectItem(rt, Constants.UserRoleType.roleLabels.get(rt)));
        }
        setRoleTypes(roleTypes);

        setId(null);
        setEmail(null);
        setName(null);
        setRoleType(null);
    }

    @Override
    public void fillForm()
    {
        super.fillForm();

        if (getSelectedData() != null)
        {
            setId(getSelectedData().getId());
            setEmail(getSelectedData().getEmail());
            setName(getSelectedData().getName());
            setRoleType(getSelectedData().getRoleType());
        }
    }

    @Override
    public void saveForm(String menuId)
    {
        super.saveForm(menuId);

        getSelectedData().setId(getId());
        getSelectedData().setEmail(getEmail());
        getSelectedData().setName(getName());
        getSelectedData().setRoleType(getRoleType());

        try
        {
            if (isAddMode() || isCopyMode())
            {
                userDAO.insert(getSelectedData());
            }
            else
            {
                userDAO.update("id", getId(), getSelectedData());
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

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Collection<SelectItem> getRoleTypes()
    {
        return roleTypes;
    }

    public void setRoleTypes(Collection<SelectItem> roleTypes)
    {
        this.roleTypes = roleTypes;
    }

    public String getRoleType()
    {
        return roleType;
    }

    public void setRoleType(String roleType)
    {
        this.roleType = roleType;
    }
}
