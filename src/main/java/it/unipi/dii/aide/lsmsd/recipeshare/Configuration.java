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
    public static final Image LOGO = loadImage("LOGO");

    public static final List<Image> AVATAR = Arrays.asList(loadImage("AVATAR_1"), loadImage("AVATAR_2"),
            loadImage("AVATAR_3"), loadImage("AVATAR_4"), loadImage("AVATAR_5"),
            loadImage("AVATAR_6"),loadImage("AVATAR_7"), loadImage("AVATAR_8"));

    private static String load(String key) {
        Properties prop = new Properties();
        try {prop.load(new FileInputStream("src/main/resources/config.properties"));}
        catch (IOException e) {e.printStackTrace();}
        return prop.getProperty(key);
    }

    private static Image loadImage(String string) {
        try {return new Image(new FileInputStream(load(string)));}
        catch (FileNotFoundException e) {throw new RuntimeException(e);}
    }

}