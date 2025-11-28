package dao;

import config.SupabaseClient;
import dao.base.AbstractDAODecorator;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import model.AuthProvider;
import model.User;
import utils.Config;

import java.io.IOException;
import java.io.Serializable;

@Named("userDAO")
@ApplicationScoped
public class UserDAO extends AbstractDAODecorator<User> implements Serializable
{
    public UserDAO()
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
        return "Users"; // respecter la casse exacte
    }

    @Override
    protected Class<User> getClazz()
    {
        return User.class;
    }

    // Méthodes spécifiques à User
    public User findFirstByEmail(String email) throws Exception
    {
        return findBy("email", email).stream().findFirst().orElse(null);
    }

    public User findFirstById(String id) throws Exception
    {
        return findBy("id", id).stream().findFirst().orElse(null);
    }

    public User checkAuth(String username, String password) throws Exception
    {
        User user = findBy("name", username).stream().findFirst().orElse(null);
        if (user != null)
        {
            AuthProviderDAO authProviderDAO = new AuthProviderDAO();
            authProviderDAO.initClient(this.client);

            AuthProvider authProvider = authProviderDAO.findByUserIdAndProvide(user.getId(), "Email", password);
            if (authProvider == null)
            {
                user = null;
            }
        }

        return user;
    }

    public User updateLastLogin(String userId) throws IOException
    {
        String json = "{\"last_login\":\"now()\"}";
        String response = client.patch("/Users?id=eq." + userId, json);
        return mapper.readValue(response, User.class);
    }

    public User createUser(User user) throws Exception
    {
        return insert(user);
    }
}