package dao;

import config.SupabaseClient;
import dao.base.AbstractDAODecorator;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.RememberMeToken;
import utils.Config;

import java.io.IOException;
import java.io.Serializable;
import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Base64;

@Named("rememberMeTokenDAO")
@ApplicationScoped
public class RememberMeTokenDAO extends AbstractDAODecorator<RememberMeToken> implements Serializable
{
    private static final int TOKEN_LENGTH = 32;
    private static final long EXPIRY_DAYS = 30;
    private static final String COOKIE_PATH = "/";
    private static final boolean COOKIE_SECURE = true;     // always true in production
    private static final boolean COOKIE_HTTP_ONLY = true;  // prevent JS access
    private static final String COOKIE_SAME_SITE = "Lax";

    public RememberMeTokenDAO()
    {
    }

    @PostConstruct
    public void init()
    {
        SupabaseClient client = new SupabaseClient(
                Config.get("supabase.url"),
                Config.get("supabase.apikey")
        );

        initClient(client);
    }

    @Override
    protected String getTableName()
    {
        return "RememberMeToken"; // respecter la casse exacte
    }

    @Override
    protected Class<RememberMeToken> getClazz()
    {
        return RememberMeToken.class;
    }

    public void createToken(String userId, HttpServletResponse response) throws IOException
    {
        String series = randomString();
        String token = randomString();

        String filter = "?user_id=eq." + userId;

        RememberMeToken checkExisting = findByFilter(filter).stream().findFirst().orElse(null);
        if (checkExisting != null)
        {
            delete("id", checkExisting.getId());
        }

        RememberMeToken t = new RememberMeToken();
        t.setSeries(series);
        t.setUser_id(userId);
        t.setToken(token);
        t.setLastUsed(OffsetDateTime.now());

        insert(t);

        addCookie(response, "rememberme_series", series, EXPIRY_DAYS);
        addCookie(response, "rememberme_token", token, EXPIRY_DAYS);
    }

    public String validateToken(HttpServletRequest request) throws IOException
    {
        String series = getCookie(request, "rememberme_series");
        String token = getCookie(request, "rememberme_token");

        if (series == null || token == null)
        {
            return null;
        }

        String filter = "?series=eq." + series;
        RememberMeToken db = findByFilter(filter).stream().findFirst().orElse(null);
        if (db == null || !db.getToken().equals(token))
        {
            // Possible hacking attempt → delete whole series
            if (db != null)
            {
                delete("series", series);
            }

            return null;
        }

        // Refresh token (defense against token theft)
        db.setToken(randomString());
        db.setLastUsed(OffsetDateTime.now());

        update("id", db.getId(), db);

        return db.getUser_id();
    }

    public void removeTokens(String userId, HttpServletResponse response) throws IOException
    {
        if (userId != null)
        {
            delete("user_id", userId);
        }

        deleteCookie(response, "rememberme_series");
        deleteCookie(response, "rememberme_token");
    }

    private String randomString()
    {
        byte[] bytes = new byte[TOKEN_LENGTH];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public void addCookie(HttpServletResponse response, String name, String value, long days)
    {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath(COOKIE_PATH);
        cookie.setMaxAge((int) (days * 24 * 60 * 60)); // seconds
        cookie.setSecure(COOKIE_SECURE);
        cookie.setHttpOnly(COOKIE_HTTP_ONLY);

        // SameSite attribute (not settable via Cookie API until Jakarta EE 10 / Servlet 6)
        // We add it manually as raw header
        String sameSiteHeader = String.format("%s=%s; Path=%s; Max-Age=%d; Secure; HttpOnly; SameSite=%s",
                name, value, COOKIE_PATH, cookie.getMaxAge(), COOKIE_SAME_SITE);

        response.addHeader("Set-Cookie", sameSiteHeader);
        // Also add the normal cookie (some older browsers/CDNs need it)
        response.addCookie(cookie);
    }

    public void deleteCookie(HttpServletResponse response, String name)
    {
        // Method 1: standard way
        Cookie cookie = new Cookie(name, "");
        cookie.setPath(COOKIE_PATH);
        cookie.setMaxAge(0);
        cookie.setSecure(COOKIE_SECURE);
        cookie.setHttpOnly(COOKIE_HTTP_ONLY);
        response.addCookie(cookie);

        // Method 2: raw header with SameSite (guarantees removal even with strict SameSite)
        String deleteHeader = String.format("%s=; Path=%s; Max-Age=0; Expires=Thu, 01 Jan 1970 00:00:00 GMT; Secure; HttpOnly; SameSite=%s",
                name, COOKIE_PATH, COOKIE_SAME_SITE);
        response.addHeader("Set-Cookie", deleteHeader);
    }

    public String getCookie(HttpServletRequest request, String name)
    {
        if (request.getCookies() == null)
        {
            return null;
        }

        return Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals(name))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}