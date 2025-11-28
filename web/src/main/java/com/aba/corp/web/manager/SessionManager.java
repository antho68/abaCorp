package com.aba.corp.web.manager;


import com.aba.corp.web.SessionBean;

import java.util.Map;

public interface SessionManager
{
    void addSession(String var1, SessionBean var2);

    void removeSession(String var1);

    SessionBean getSession(String var1);

    void clearSessions();

    Map<String, SessionBean> getSessions();

    void runScheduled();
}
