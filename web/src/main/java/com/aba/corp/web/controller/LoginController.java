package com.aba.corp.web.controller;

import com.aba.corp.web.SessionBean;
import dao.AuthProviderDAO;
import dao.RememberMeTokenDAO;
import dao.UserDAO;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletResponse;
import model.AuthProvider;
import model.User;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.context.PrimeFacesContext;

import java.io.IOException;
import java.io.Serializable;
import java.time.OffsetDateTime;

@Named
@ViewScoped
public class LoginController extends AbstractController implements Serializable
{
    @Inject
    private RememberMeTokenDAO rememberMeTokenDAO;
    @Inject
    private SessionBean sessionBean;
    @Inject
    private ExternalContext externalContext;
    @Inject
    private UserDAO userDAO;
    @Inject
    private AuthProviderDAO authProviderDAO;

    private String username;
    private String email;
    private String password;
    private boolean rememberMe = false;
    private boolean loggedIn = false;

    @PostConstruct
    public void init()
    {
    }

    public void login()
    {
        PrimeFacesContext context = (PrimeFacesContext) PrimeFacesContext.getCurrentInstance();

        if (!StringUtils.isEmpty(getUsername()) && !StringUtils.isEmpty(getPassword()))
        {
            loggedIn = true;
            try
            {
                User userToCheck = userDAO.checkAuth(getUsername(), getPassword());
                if (userToCheck == null)
                {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR
                            , "Error !!!", "No Access Found");
                    PrimeFaces.current().dialog().showMessageDynamic(message);

                    return;
                }

                if (rememberMe)
                {
                    HttpServletResponse response = (HttpServletResponse)
                            externalContext.getResponse();
                    rememberMeTokenDAO.createToken(userToCheck.getId(), response);
                }

                sessionBean.setUser(userToCheck);
                sessionBean.setLastPageCalled(context.getExternalContext().getRequestContextPath()
                        + "/pages/home.xhtml?faces-redirect=true");
                sessionBean.setLoggedIn(true);

                context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath()
                        + "/pages/home.xhtml");
            }
            catch (Exception e)
            {
                System.err.println(e);
                log.error(e);
            }
        }
        else
        {
            loggedIn = false;
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Erreur de connexion", "Nom d’utilisateur ou mot de passe incorrect"));
        }
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public boolean isLoggedIn()
    {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn)
    {
        this.loggedIn = loggedIn;
    }

    public boolean isRememberMe()
    {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe)
    {
        this.rememberMe = rememberMe;
    }

    public void goToSignUp()
    {
        PrimeFacesContext context = (PrimeFacesContext) PrimeFacesContext.getCurrentInstance();

        try
        {
            context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath()
                    + "/pages/login/signUp.xhtml");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void goBackToLogin()
    {
        PrimeFacesContext context = (PrimeFacesContext) PrimeFacesContext.getCurrentInstance();

        try
        {
            context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath()
                    + "/pages/login/login.xhtml");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void signIn()
    {
        try
        {
            boolean isValid = true;

            User userToCheck = userDAO.findFirstByEmail(getEmail());
            if (userToCheck != null)
            {
                isValid = false;

                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR
                        , "Error !!!", "Already an User with this Email !");
                PrimeFaces.current().dialog().showMessageDynamic(message);
            }

            if (isValid && getPassword().contains("*"))
            {
                isValid = false;

                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR
                        , "Error !!!", " '*' is not allowed !");
                PrimeFaces.current().dialog().showMessageDynamic(message);
            }

            if (isValid)
            {
                User user = new User();
                user.setEmail(getEmail());
                user.setName(getUsername());
                user.setUpdated_at(OffsetDateTime.now());

                user = userDAO.insert(user);

                AuthProvider authProvider = new AuthProvider();
                authProvider.setUser_id(user.getId());
                authProvider.setProvider("Email");
                authProvider.setPassword(getPassword());
                authProvider.setUpdated_at(OffsetDateTime.now());

                authProvider = authProviderDAO.insert(authProvider);
            }
        }
        catch (Exception e)
        {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR
                    , "Error !!!", e.getMessage());
            PrimeFaces.current().dialog().showMessageDynamic(message);

            System.err.println(e);
            log.error(e);
        }
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }
}