package com.aba.corp.web.utils;

import com.aba.corp.utils.Utils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CommonUtils extends Utils
{
    public static String getLog4JFileLocation(String configFileLocation)
    {
        String log4JFileLocation = null;

        try
        {
            Properties properties = getConfigFileProperties(configFileLocation);
            if (properties != null)
            {
                log4JFileLocation = properties.getProperty("log4j.fileLocation", (String) null);
            }
        }
        catch (IOException var4)
        {
            logError(var4.getMessage(), var4);
        }

        return log4JFileLocation;
    }

    public static Properties getConfigFileProperties(String configFileParam) throws IOException
    {
        Properties properties = null;
        File file = getConfigFile(configFileParam);
        if (file != null)
        {
            InputStream inp = null;

            try
            {
                inp = new FileInputStream(file);
                if (inp != null)
                {
                    properties = new Properties();
                    properties.load(inp);
                }
            }
            finally
            {
                if (inp != null)
                {
                    inp.close();
                }

            }
        }
        else
        {
            logWarn("No valid config file given!");
        }

        return properties;
    }

    protected static String getLogId()
    {
        return CommonContextHolder.getId() != null ? CommonContextHolder.getId() : "LOG_ID NOT SET";
    }

    public static File getConfigFile(String systemPropertyPathToFile)
    {
        String fileLocation = System.getProperty(systemPropertyPathToFile);
        if (fileLocation != null)
        {
            File file = getFile(fileLocation);
            if (file != null)
            {
                return file;
            }
        }

        return null;
    }

    public static File getFile(String pathToFile)
    {
        if (!StringUtils.isEmpty(pathToFile))
        {
            pathToFile = pathToFile.replaceAll("file:", "");
            File file = new File(pathToFile);
            if (file != null && file.exists())
            {
                return file;
            }
        }

        return null;
    }
}
