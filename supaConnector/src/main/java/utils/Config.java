package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config
{
    private static Properties properties = new Properties();

    static
    {
        initProperties();
    }

    private static void initProperties()
    {
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream("config.properties"))
        {
            if (input == null)
            {
                System.err.println("Impossible de trouver config.properties");
            }
            else
            {
                properties.load(input);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static String get(String key)
    {
        if (properties.isEmpty())
        {
            initProperties();
        }

        return properties.getProperty(key);
    }
}
