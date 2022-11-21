package com.aziz_work.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationReader {
    private static Properties properties = new Properties();

    static {

        try {
            FileInputStream file = new FileInputStream("configurations.properties");
            properties.load(file);

        } catch (IOException e) {
            System.out.println("File not found in Configuration properties");        }
    }

    public static String getProperty(String keyWord){

        return  properties.getProperty(keyWord);
    }
}
