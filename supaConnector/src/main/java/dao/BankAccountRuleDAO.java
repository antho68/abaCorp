package dao;

import config.SupabaseClient;
import dao.base.AbstractDAODecorator;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import model.BankAccount;
import model.BankAccountRule;
import model.RememberMeToken;
import utils.Config;

import java.io.Serializable;
import java.util.LinkedList;

@Named("bankAccountRuleDAO")
@ApplicationScoped
public class BankAccountRuleDAO extends AbstractDAODecorator<BankAccountRule> implements Serializable
{
    public BankAccountRuleDAO()
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
        return "BankAccountRule"; // respecter la casse exacte
    }

    @Override
    protected Class<BankAccountRule> getClazz()
    {
        return BankAccountRule.class;
    }

    public BankAccountRule findFirstById(String id) throws Exception
    {
        return findBy("id", id).stream().findFirst().orElse(null);
    }

    public BankAccountRule createBankAccount(BankAccountRule bankAccount) throws Exception
    {
        return insert(bankAccount);
    }

    public LinkedList<BankAccountRule> findByUserId(String userId) throws Exception
    {
        String sFilter = "?select=*,...BankAccount!inner()&BankAccount.userId=eq." + userId;
        return findByFilter(sFilter);
    }

    public LinkedList<BankAccountRule> findByAccountId(String accountId) throws Exception
    {
        return findBy("accountId", accountId);
    }
}