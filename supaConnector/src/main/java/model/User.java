package model;

import com.aba.corp.utils.Utils;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.beans.Transient;
import java.io.Serializable;
import java.time.OffsetDateTime;

public class User extends AbstractModel<String> implements Serializable
{
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String id;

    private String email;
    private String name;
    private String roleType;
    private OffsetDateTime last_login;
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

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public OffsetDateTime getLast_login()
    {
        return last_login;
    }

    public void setLast_login(OffsetDateTime last_login)
    {
        this.last_login = last_login;
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

    public String getRoleType()
    {
        return roleType;
    }

    public void setRoleType(String roleType)
    {
        this.roleType = roleType;
    }

    @Transient
    public String getUpdatedAtFormatted()
    {
        if (getUpdated_at() != null)
        {
            return Utils.getOffsetDateTimeFormatted(getUpdated_at());
        }

        return "";
    }
}