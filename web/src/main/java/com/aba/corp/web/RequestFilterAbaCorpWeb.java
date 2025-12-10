package com.aba.corp.web;

import dao.RememberMeTokenDAO;
import dao.UserDAO;
import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;


/**
 * Filter for all requests
 *
 * @author mala
 */
@WebFilter(urlPatterns = "/*", dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.FORWARD})
public class RequestFilterAbaCorpWeb implements Filter
{
    private static final Logger log = LogManager.getLogger(RequestFilterAbaCorpWeb.class);

    @Inject
    private SessionBean sessionBean;
    @Inject
    private UserDAO userDAO;
    @Inject
    private RememberMeTokenDAO rememberMeTokenDAO;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException
    {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // Skip if already logged in
        if (sessionBean.isLoggedIn())
        {
            chain.doFilter(req, res);
            return;
        }

        String uri = request.getRequestURI();

        String token = getCookieValue(request, "rememberme_token");
        if (token != null)
        {
            try
            {
                String userId = rememberMeTokenDAO.validateToken(request);
                if (userId != null)
                {
                    User user = userDAO.findFirstById(userId);
                    if (user != null)
                    {
                        sessionBean.setLoggedIn(true);
                        sessionBean.setUser(user);

                        if (sessionBean.getLastPageCalled() != null)
                        {
                            response.sendRedirect(request.getContextPath() + sessionBean.getLastPageCalled());
                        }
                        else
                        {
                            response.sendRedirect(request.getContextPath() + "/pages/home.xhtml");
                        }

                        return;
                    }
                }
                else
                {
                    rememberMeTokenDAO.removeTokens(null, response);

                    response.sendRedirect(request.getContextPath() + "/pages/loginGroup/login.xhtml");
                    return;
                }
            }
            catch (Exception e)
            {
                log.error("Get Token error", e);
                throw new RuntimeException(e);
            }
        }
        else if (uri.equals(request.getContextPath() + "/") || uri.equals(request.getContextPath()))
        {
            response.sendRedirect(request.getContextPath() + "/pages/loginGroup/login.xhtml");
            return;
        }
        else if (!uri.equals("/web/pages/loginGroup/login.xhtml") && uri.contains("/web/pages/"))
        {
            response.sendRedirect(request.getContextPath() + "/pages/loginGroup/login.xhtml");
            return;
        }

        chain.doFilter(req, res);
    }

    private String getCookieValue(HttpServletRequest req, String name)
    {
        return Arrays.stream(req.getCookies() != null ? req.getCookies() : new Cookie[0])
                .filter(c -> c.getName().equals(name))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}