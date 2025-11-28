import config.SupabaseClient;
import dao.AuthProviderDAO;
import dao.UserDAO;
import model.AuthProvider;
import model.User;
import utils.Config;

import java.io.IOException;
import java.util.List;

public class TestSupabase
{
    public static void main(String[] args)
    {
        String supabaseUrl = Config.get("supabase.url");
        String apiKey = Config.get("supabase.apikey");

        SupabaseClient client = new SupabaseClient(supabaseUrl, apiKey);

        UserDAO userDAO = new UserDAO();
        userDAO.initClient(client);

        AuthProviderDAO authDAO = new AuthProviderDAO();
        authDAO.initClient(client);

        try
        {
            // --- TEST USERS ---
            System.out.println("Liste des utilisateurs :");
            List<User> users = userDAO.findAll();
            for (User u : users)
            {
                System.out.println(u.getId() + " | " + u.getEmail() + " | last login: " + u.getLast_login());
            }

            // Tester findByEmail
            String testEmail = "test@example.com";
            User user = userDAO.findFirstByEmail(testEmail);
            if (user != null)
            {
                System.out.println("Utilisateur trouvé : " + user.getEmail());
            }
            else
            {
                System.out.println("Utilisateur non trouvé avec email : " + testEmail);
            }

            // --- TEST AUTH PROVIDER ---
            String provider = "google";
            String providerId = "google-123456";

            AuthProvider auth = authDAO.findByProviderId(providerId, provider);
            if (auth != null)
            {
                System.out.println("AuthProvider trouvé pour user_id : " + auth.getUser_id());
            }
            else
            {
                System.out.println("Aucun AuthProvider trouvé pour provider_id : " + providerId);
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}