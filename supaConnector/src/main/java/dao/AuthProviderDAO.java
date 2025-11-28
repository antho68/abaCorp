package dao;

import config.SupabaseClient;
import dao.base.AbstractDAODecorator;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import model.AuthProvider;
import utils.Config;

@Named("authProviderDAO")
@ApplicationScoped
public class AuthProviderDAO extends AbstractDAODecorator<AuthProvider>
{
    public AuthProviderDAO()
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
        return "AuthProviders";
    }

    @Override
    protected Class<AuthProvider> getClazz()
    {
        return AuthProvider.class;
    }

    // Méthodes spécifiques à AuthProvider
    public AuthProvider findByProviderId(String providerId, String provider) throws Exception
    {
        return findBy("provider_user_id", providerId).stream()
                .filter(a -> a.getProvider().equals(provider))
                .findFirst()
                .orElse(null);
    }

    public AuthProvider findByUserIdAndProvide(String userId, String provider, String password) throws Exception
    {
        String filter = "?user_id=eq." + userId;
        filter += "&provider=eq." + provider + "";
        filter += "&password=eq." + password + "";

        return findByFilter(filter).stream()
                .findFirst()
                .orElse(null);
    }
}