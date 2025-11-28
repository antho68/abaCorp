package dao;

import config.SupabaseClient;
import dao.base.AbstractDAODecorator;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import model.BankAccount;
import utils.Config;

import java.io.Serializable;

@Named("bankAccountDAO")
@ApplicationScoped
public class BankAccountDAO extends AbstractDAODecorator<BankAccount> implements Serializable
{
    public BankAccountDAO()
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
        return "BankAccount"; // respecter la casse exacte
    }

    @Override
    protected Class<BankAccount> getClazz()
    {
        return BankAccount.class;
    }

    public BankAccount findFirstById(String id) throws Exception
    {
        return findBy("id", id).stream().findFirst().orElse(null);
    }

    public BankAccount createBankAccount(BankAccount bankAccount) throws Exception
    {
        return insert(bankAccount);
    }
}