package com.aba.corp.web.manager;

import com.aba.corp.web.SessionBean;
import com.aba.corp.web.utils.CommonContextHolder;
import com.aba.corp.web.utils.CommonUtils;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.*;

@Named("sessionManager")
public class SessionManagerImpl implements Serializable, SessionManager
{
    private static final long serialVersionUID = -6209027022706211101L;
    private Map<String, SessionBean> sessionBeans = new HashMap();

    public SessionManagerImpl()
    {
    }

    public void addSession(String sessionId, SessionBean sessionBean)
    {
        this.sessionBeans.put(sessionId, sessionBean);
    }

    public void removeSession(String sessionId)
    {
        this.sessionBeans.remove(sessionId);
    }

    public SessionBean getSession(String sessionId)
    {
        return this.sessionBeans.get(sessionId);
    }

    public void clearSessions()
    {
        this.sessionBeans.clear();
    }

    public Map<String, SessionBean> getSessions()
    {
        return this.sessionBeans;
    }

    public void runScheduled()
    {
        CommonContextHolder.setId("SessionManager");

        Date checkDateLoggedIn = CommonUtils.getDateWithMinutesOffset(new Date()
                , -1 * 60);
        Date checkDateLoggedOut = CommonUtils.getDateWithMinutesOffset(new Date()
                , -1 * 60);

        Collection<String> sessionIdsToRemove = new ArrayList();
        Iterator<SessionBean> it = this.sessionBeans.values().iterator();

        while (true)
        {
            SessionBean sessionBean;
            do
            {
                if (!it.hasNext())
                {
                    Iterator var7 = sessionIdsToRemove.iterator();

                    while (var7.hasNext())
                    {
                        String sessionId = (String) var7.next();
                        this.removeSession(sessionId);
                    }

                    CommonContextHolder.clearAll();
                    return;
                }

                sessionBean = it.next();
            } while (sessionBean.isLoggedIn());

            sessionIdsToRemove.add(sessionBean.getSessionId());
            sessionBean.setLoggedIn(false);
        }
    }
}

