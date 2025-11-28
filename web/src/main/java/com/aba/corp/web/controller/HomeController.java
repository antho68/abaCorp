package com.aba.corp.web.controller;

import config.SupabaseClient;
import dao.UserDAO;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import model.User;
import utils.Config;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class HomeController implements Serializable
{
    @Inject
    private UserDAO userDAO;

    private List<User> users;

    @PostConstruct
    public void init()
    {
        SupabaseClient client = new SupabaseClient(
                Config.get("supabase.url"),
                Config.get("supabase.apikey")
        );
        userDAO = new UserDAO();
        userDAO.initClient(client);

        try
        {
            users = userDAO.findAll();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public List<User> getUsers()
    {
        return users;
    }
}