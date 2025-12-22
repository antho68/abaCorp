package dao;

import config.SupabaseClient;
import dao.base.AbstractDAODecorator;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import model.BankAccountItem;
import utils.Config;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;

@Named("bankAccountItemDAO")
@ApplicationScoped
public class BankAccountItemDAO extends AbstractDAODecorator<BankAccountItem> implements Serializable
{
    public BankAccountItemDAO()
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
        return "BankAccountItem"; // respecter la casse exacte
    }

    @Override
    protected Class<BankAccountItem> getClazz()
    {
        return BankAccountItem.class;
    }

    public BankAccountItem findFirstById(String id) throws Exception
    {
        return findBy("id", id).stream().findFirst().orElse(null);
    }

    public Map<String, Collection<String>> importData(Collection<BankAccountItem> bankAccountItems)
    {
        Map<String, Collection<String>> messages = new HashMap<>();

        for (BankAccountItem bankAccountItem : bankAccountItems)
        {
            try
            {
                LocalDate date = bankAccountItem.getDate().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                String start = date.atStartOfDay(ZoneOffset.UTC).toString();          // 2025-12-19T00:00:00Z
                String end = date.plusDays(1).atStartOfDay(ZoneOffset.UTC).toString();

                String sFilter = "?date=gte.'" + start + "'"
                        + "&date=lt.'" + end + "'"
                        + "&description=eq.'" + bankAccountItem.getDescription() + "'"
                        + "&amout=eq." + bankAccountItem.getAmout();

                Boolean existingItem = findByFilter(sFilter).size() > 0;
                if (!existingItem)
                {
                    insert(bankAccountItem);
                }
                else
                {
                    messages.putIfAbsent("alreadyIn", new ArrayList<>());
                    messages.get("alreadyIn").add("Item :" + bankAccountItem.getDateFormatted() + " -> "
                            + bankAccountItem.getDescription());
                }
            }
            catch (Exception e)
            {
                System.out.println(e);
                messages.putIfAbsent("errors", new ArrayList<>());
                messages.get("errors").add("Error importing item with id " + bankAccountItem.getId() + ": " + e.getMessage());
            }
        }

        return messages;
    }

    public LinkedList<BankAccountItem> findByUserId(String userId, LocalDate fromDate) throws Exception
    {
        String start = fromDate.atStartOfDay(ZoneOffset.UTC).toString();          // 2025-12-19T00:00:00Z

        String sFilter = "?select=*,...BankAccount!inner()&BankAccount.userId=eq." + userId +
                "&date=gte.'" + start;
        return findByFilter(sFilter);
    }
}