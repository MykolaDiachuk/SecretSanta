package org.example.resource;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    public static Properties loadConfig() {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("C:\\Users\\PC\\IdeaProjects\\SecretSanta\\src\\main\\resources\\file.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }
}