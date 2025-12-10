package com.aba.corp.web.item;

import jakarta.faces.model.SelectItem;
import model.AbstractModel;

import java.io.Serializable;

public class EntityItem<D extends AbstractModel<?>> extends SelectItem implements Serializable, LabelItem
{
    private static final long serialVersionUID = 2555800659036924670L;
    private D item;
    private String label;
    private String style;

    public EntityItem(D item, String label)
    {
        this.label = label;
        this.item = item;
    }

    public D getItem()
    {
        return this.item;
    }

    public void setItem(D item)
    {
        this.item = item;
    }

    @Override
    public String getLabel()
    {
        return this.label;
    }

    @Override
    public void setLabel(String label)
    {
        this.label = label;
    }

    public int hashCode()
    {
        int result = 1;
        result = 31 * result + (this.item == null ? 0 : this.item.hashCode());
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
        else if (this.getClass() != obj.getClass())
        {
            return false;
        }
        else
        {
            EntityItem<D> other = (EntityItem) obj;
            if (this.getItem() == null)
            {
                if (other.getItem() != null)
                {
                    return false;
                }
            }
            else
            {
                if (other.getItem() == null)
                {
                    return false;
                }

                if (!this.getItem().getId().equals(other.getItem().getId()))
                {
                    return false;
                }
            }

            return true;
        }
    }

    public String getStyle()
    {
        return style;
    }

    public void setStyle(String style)
    {
        this.style = style;
    }
}

