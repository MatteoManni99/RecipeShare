package it.unipi.dii.aide.lsmsd.recipeshare;
import java.io.*;
import javafx.scene.image.Image;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Configuration {

    public static final String MONGODB_URL_LOCAL = load("MONGODB_URL_LOCAL");
    public static final String MONGODB_URL = load("MONGODB_URL");
    public static final String MONGODB_DB = load("MONGODB_DB");
    public static final String MONGODB_RECIPE = load("MONGODB_RECIPE");
    public static final String MONGODB_REPORTED_RECIPE = load("MONGODB_REPORTED_RECIPE");
    public static final String MONGODB_AUTHOR = load("MONGODB_AUTHOR");
    public static final String MONGODB_MODERATOR = load("MONGODB_MODERATOR");
    public static final String NEO4J_URL_LOCAL = load("NEO4J_URL_LOCAL");
    public static final String NEO4J_URL = load("NEO4J_URL");
    public static final String NEO4J_USERNAME = load("NEO4J_USERNAME");
    public static final String NEO4J_PASSWORD = load("NEO4J_PASSWORD");
    public static final List<Image> AVATAR;

    static {
        try {
            AVATAR = Arrays.asList(new Image(new FileInputStream(load("AVATAR_1"))), new Image(new FileInputStream(load("AVATAR_2"))),
                    new Image(new FileInputStream(load("AVATAR_3"))), new Image(new FileInputStream(load("AVATAR_4"))),
                    new Image(new FileInputStream(load("AVATAR_5"))), new Image(new FileInputStream(load("AVATAR_6"))),
                    new Image(new FileInputStream(load("AVATAR_7"))), new Image(new FileInputStream(load("AVATAR_8"))));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static String load(String key) {
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