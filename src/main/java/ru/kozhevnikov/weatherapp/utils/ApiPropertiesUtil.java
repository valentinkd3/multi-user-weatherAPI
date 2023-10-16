package ru.kozhevnikov.weatherapp.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/**
 * Утилита для доступа к свойствам API из файла "api.properties".
 */
public class ApiPropertiesUtil {
    private static final Properties PROPERTIES = new Properties();
    static {
        loadProperties();
    }
    /**
     * Получает значение свойства по ключу.
     *
     * @param key ключ свойства
     * @return значение свойства
     */
    public static String get(String key){
        return PROPERTIES.getProperty(key);
    }
    private static void loadProperties() {
        try(InputStream stream = ApiPropertiesUtil.class.getClassLoader().getResourceAsStream("api.properties")){
            PROPERTIES.load(stream);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
