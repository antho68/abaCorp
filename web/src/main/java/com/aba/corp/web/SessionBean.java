package com.aba.corp.web;

import dao.RememberMeTokenDAO;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletResponse;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.MessageUtils;

import java.io.Serializable;
import java.util.Locale;

@Named("sessionBean")
@SessionScoped
public class SessionBean implements Serializable
{
    private static final Logger log = LogManager.getLogger(SessionBean.class);
    private static final long serialVersionUID = 2416174991497702624L;

    @Inject
    private ExternalContext externalContext;
    @Inject
    private RememberMeTokenDAO rememberMeTokenDAO;

    private Locale locale = new Locale("fr");
    private boolean loggedIn;
    private String sessionId;
    private User user;
    private String lastPageCalled;

    public void init()
    {
    }

    public void logout()
    {
        try
        {
            HttpServletResponse response = (HttpServletResponse)
                    externalContext.getResponse();
            rememberMeTokenDAO.removeTokens(getUser().getId(), response);

            setUser(null);
            setLastPageCalled(null);
            setLoggedIn(false);

            FacesContext fc = FacesContext.getCurrentInstance();
            fc.getExternalContext().redirect(fc.getExternalContext().getRequestContextPath()
                    + "/pages/loginGroup/login.xhtml");
        }
        catch (Exception e)
        {
            log.error("Get Token error", e);
            throw new RuntimeException(e);
        }
    }

    public boolean isLoggedIn()
    {
        return this.loggedIn;
    }

    public void setLoggedIn(boolean loggedIn)
    {
        this.loggedIn = loggedIn;
    }

    public String getSessionId()
    {
        return this.sessionId;
    }

    public void setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
    }

    public User getUser()
    {
        return this.user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public String getLastPageCalled()
    {
        return lastPageCalled;
    }

    public void setLastPageCalled(String lastPageCalled)
    {
        this.lastPageCalled = lastPageCalled;
    }

    public Locale getLocale()
    {
        return locale;
    }

    public void setLocale(Locale locale)
    {
        this.locale = locale;
    }

    public void resetMessagesListener()
    {
        MessageUtils.clearMessageList();
    }
}