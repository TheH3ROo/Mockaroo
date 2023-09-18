package main.java;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {
    private static final Properties properties;

    static {
        properties = new Properties();
        try (InputStream input = new FileInputStream("src\\database.properties")) {
            properties.load(input);
        } catch (Exception e) {
            System.out.println("The following exception occurred: " + e.getMessage());
        }
    }

    public static String getUrl() {
        return properties.getProperty("db.url");
    }

    public static String getUsername() {
        return properties.getProperty("db.username");
    }

    public static String getPassword() {
        return properties.getProperty("db.password");
    }
}