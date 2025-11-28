package com.aba.corp.web.controller;

import com.aba.corp.web.SessionBean;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.event.MenuActionEvent;
import org.primefaces.model.menu.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Named
@SessionScoped
public class MenuController implements Serializable
{
    private static final Logger log = LogManager.getLogger(MenuController.class);

    @Inject
    private SessionBean sessionBean;

    private MenuModel model;

    @PostConstruct
    public void init()
    {
        setModel(new DefaultMenuModel());

        DefaultMenuItem mi = new DefaultMenuItem();
        mi.setValue("Home");
        mi.setIcon("pi pi-home");
        mi.setCommand("#{menuController.goToMenu}");
        mi.setParam("menuLink", "/pages/admin/users.xhtml");
        getModel().getElements().add(mi);

        if (sessionBean.getUser() != null)
        {
            buildBankMenu();

            if ("ADMIN".equals(sessionBean.getUser().getRoleType()))
            {
                buildAdminMenu();

            }
        }
    }

    private void buildAdminMenu()
    {
        DefaultSubMenu adminSubMenu = DefaultSubMenu.builder()
                .label("Adminstration")
                .icon("pi pi-wrench")
                .expanded(true)
                .build();

        Map<String, List<String>> params = new java.util.HashMap<>();
        params.put("menuLink", List.of("/pages/admin/users.xhtml"));
        MenuElement item = DefaultMenuItem.builder()
                .value("Utilisateurs")
                .command("#{menuController.goToMenu}")
                .icon("pi pi-users")
                .params(params)
                .build();
        adminSubMenu.getElements().add(item);

        getModel().getElements().add(adminSubMenu);
    }

    private void buildBankMenu()
    {
        DefaultSubMenu bankSubMenu = DefaultSubMenu.builder()
                .label("Banque")
                .icon("pi pi-euro")
                .expanded(true)
                .build();

        Map<String, List<String>> params = new java.util.HashMap<>();
        params.put("menuLink", List.of("/pages/bank/bankAccount.xhtml"));
        MenuElement item = DefaultMenuItem.builder()
                .value("Comptes Bancaires")
                .command("#{menuController.goToMenu}")
                .icon("pi pi-list")
                .params(params)
                .build();
        bankSubMenu.getElements().add(item);

        getModel().getElements().add(bankSubMenu);
    }

    public void goToMenu(ActionEvent event)
    {
        MenuItem menuItem = ((MenuActionEvent) event).getMenuItem();

        String menuLink = menuItem.getParams().get("menuLink").get(0);

        try
        {
            FacesContext fc = FacesContext.getCurrentInstance();
            String path = fc.getExternalContext().getRequestContextPath()
                    + menuLink;

            sessionBean.setLastPageCalled(path);
            fc.getExternalContext().redirect(path);
        }
        catch (Exception e)
        {
            log.error("Get Token error", e);
            throw new RuntimeException(e);
        }
    }

    public MenuModel getModel()
    {
        return model;
    }

    public void setModel(MenuModel model)
    {
        this.model = model;
    }
}