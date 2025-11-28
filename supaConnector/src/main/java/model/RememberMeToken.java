package model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.OffsetDateTime;

public class RememberMeToken
{
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String id;
    private String series;          // identifier (never changes)
    private String user_id;
    private String token;           // random, changes on each login
    private OffsetDateTime lastUsed;

    // getters & setters
    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getSeries()
    {
        return series;
    }

    public void setSeries(String series)
    {
        this.series = series;
    }

    public String getUser_id()
    {
        return user_id;
    }

    public void setUser_id(String user_id)
    {
        this.user_id = user_id;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public OffsetDateTime getLastUsed()
    {
        return lastUsed;
    }

    public void setLastUsed(OffsetDateTime lastUsed)
    {
        this.lastUsed = lastUsed;
    }
}