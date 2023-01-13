package com.example.demo1;
import java.io.*;
import java.util.Properties;

public class Configuration {
    public static final String MONGODB_URL = load("MONGODB_URL");
    public static final String MONGODB_DB = load("MONGODB_DB");
    public static final String MONGODB_RECIPE = load("MONGODB_RECIPE");
    public static final String MONGODB_REPORTED_RECIPE = load("MONGODB_REPORTED_RECIPE");
    public static final String MONGODB_REVIEW = load("MONGODB_REVIEW");
    public static final String MONGODB_AUTHOR = load("MONGODB_AUTHOR");
    public static final String MONGODB_MODERATOR = load("MONGODB_MODERATOR");
    
    public static String load(String key) {
        Properties prop = new Properties();
        try {
            String configFilePath = "src/main/resources/config.properties";
            FileInputStream propsInput = new FileInputStream(configFilePath);
            prop.load(propsInput);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop.getProperty(key);
    }

}