package model;

import java.time.OffsetDateTime;

public class AuthProvider
{
    private String id;
    private String user_id;
    private String provider;   // "email", "google", "apple"
    private String provider_user_id; // l’identifiant fourni par le provider
    private String password;
    private OffsetDateTime created_at;
    private OffsetDateTime updated_at;

    // getters & setters
    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getUser_id()
    {
        return user_id;
    }

    public void setUser_id(String user_id)
    {
        this.user_id = user_id;
    }

    public String getProvider()
    {
        return provider;
    }

    public void setProvider(String provider)
    {
        this.provider = provider;
    }

    public String getProvider_user_id()
    {
        return provider_user_id;
    }

    public void setProvider_user_id(String provider_user_id)
    {
        this.provider_user_id = provider_user_id;
    }

    public OffsetDateTime getCreated_at()
    {
        return created_at;
    }

    public void setCreated_at(OffsetDateTime created_at)
    {
        this.created_at = created_at;
    }

    public OffsetDateTime getUpdated_at()
    {
        return updated_at;
    }

    public void setUpdated_at(OffsetDateTime updated_at)
    {
        this.updated_at = updated_at;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}