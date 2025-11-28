package com.aba.corp.web.controller;

import com.aba.corp.web.form.UserForm;
import dao.UserDAO;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import model.User;

import java.io.Serializable;

@Named
@ViewScoped
public class UserController extends AbstractController<UserForm, User> implements Serializable
{
    @Inject
    private UserDAO userDAO;
    @Inject
    private UserForm userForm;

    @PostConstruct
    public void init()
    {
        setCrudForm(userForm);

        try
        {
            setDatas(userDAO.findAll());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}