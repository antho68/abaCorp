package utils;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

import java.util.Iterator;
import java.util.Locale;


/**
 * helper methods for internationalizing
 */
public class MessageUtils
{
    private static synchronized FacesContext getFacesContext()
    {
        return FacesContext.getCurrentInstance();
    }

    private static Locale getLocale()
    {
        return getLocale(getFacesContext());
    }

    private static Locale getLocale(FacesContext facesContext)
    {
        return facesContext.getViewRoot().getLocale();
    }

    public static void clearMessageList()
    {
        FacesContext facesContext = getFacesContext();
        if (!facesContext.getMessageList().isEmpty())
        {
            Iterator<FacesMessage> messages = facesContext.getMessages();
            while (messages.hasNext())
            {
                messages.next();
                messages.remove();
            }
        }
    }

    public static boolean hasErrorMessage()
    {
        return hasMessageWithSeverity(getFacesContext(), FacesMessage.SEVERITY_ERROR);
    }

    public static boolean hasWarningMessage()
    {
        return hasMessageWithSeverity(getFacesContext(), FacesMessage.SEVERITY_WARN);
    }

    public static boolean hasInfoMessage()
    {
        return hasMessageWithSeverity(getFacesContext(), FacesMessage.SEVERITY_INFO);
    }

    private static boolean hasMessageWithSeverity(FacesContext facesContext, FacesMessage.Severity severity)
    {
        Iterator<FacesMessage> messages = facesContext.getMessages();

        FacesMessage message;
        do
        {
            if (!messages.hasNext())
            {
                return false;
            }

            message = messages.next();
        } while (!severity.equals(message.getSeverity()));

        return true;
    }

    public static void addErrorMessage(String text)
    {
        getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Erreur", text));
    }

    public static void addErrorMessage(String summary, String text)
    {
        getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                summary, text));
    }


}