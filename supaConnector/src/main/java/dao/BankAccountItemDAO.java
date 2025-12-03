package dao;

import config.SupabaseClient;
import dao.base.AbstractDAODecorator;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import model.BankAccount;
import model.BankAccountItem;
import utils.Config;

import java.io.Serializable;

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

}