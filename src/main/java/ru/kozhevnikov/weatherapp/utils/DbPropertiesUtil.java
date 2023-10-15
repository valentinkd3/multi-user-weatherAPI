package ru.kozhevnikov.weatherapp.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DbPropertiesUtil {
    private static final Properties PROPERTIES = new Properties();
    static {
        loadProperties();
    }
    private DbPropertiesUtil() {
    }
    public static String get(String key){
        return PROPERTIES.getProperty(key);
    }
    private static void loadProperties(){
        try(InputStream stream = DbPropertiesUtil.class.getClassLoader().getResourceAsStream("database.properties")){
            PROPERTIES.load(stream);
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
