package model;

import java.io.Serializable;

public abstract class AbstractModel<PK extends Serializable> implements Serializable, Cloneable
{
    private static final long serialVersionUID = -1919854131578047489L;
    protected PK id;

    protected AbstractModel()
    {
    }

    public PK getId()
    {
        return id;
    }

    public void setId(PK id)
    {
        this.id = id;
    }

    public int hashCode()
    {
        int result = 1;
        result = 31 * result + (this.id == null ? 0 : this.id.hashCode());
        return result;
    }

    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        else if (obj == null)
        {
            return false;
        }
        else if (!this.getClass().isInstance(obj))
        {
            return false;
        }
        else
        {
            AbstractModel<Serializable> other = (AbstractModel) obj;
            if (this.id == null)
            {
                if (other.getId() != null)
                {
                    return false;
                }
            }
            else if (!this.id.equals(other.getId()))
            {
                return false;
            }

            return true;
        }
    }

    public Object clone()
    {
        Object clone = null;

        try
        {
            clone = super.clone();
        }
        catch (Exception var3)
        {
        }

        return clone;
    }
}

