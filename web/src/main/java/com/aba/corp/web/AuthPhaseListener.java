package com.aba.corp.web;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.PhaseEvent;
import jakarta.faces.event.PhaseId;
import jakarta.faces.event.PhaseListener;
import jakarta.inject.Inject;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class AuthPhaseListener implements PhaseListener
{
    @Inject
    private SessionBean sessionBean;

    protected Collection<String> accessAllowedURLs;

    @PostConstruct
    public void init()
    {
        accessAllowedURLs = new ArrayList<>();
        accessAllowedURLs.add("/pages/login/login.xhtml");
    }

    @Override
    public void beforePhase(PhaseEvent event)
    {
    }

    @Override
    public void afterPhase(PhaseEvent event)
    {
        if (event.getPhaseId() != getPhaseId())
        {
            return;
        }

        if (event.getFacesContext().getViewRoot() == null)
        {
            return;
        }

        FacesContext fc = event.getFacesContext();
        String viewId = fc.getViewRoot().getViewId();
        String redirect = viewId;

        if (StringUtils.isNotEmpty(viewId))
        {
            if (sessionBean != null && !sessionBean.isLoggedIn())
            {
                try
                {
                    fc.getExternalContext().redirect(
                            fc.getExternalContext().getRequestContextPath()
                                    + "/pages/loginGroup/login.xhtml?redirect=" + redirect);
                    return;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public PhaseId getPhaseId()
    {
        return PhaseId.RESTORE_VIEW;
    }
}