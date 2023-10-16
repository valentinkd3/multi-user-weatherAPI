package ru.kozhevnikov.weatherapp.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/**
 * Утилита для получения свойств базы данных.
 */
public class DbPropertiesUtil {
    private static final Properties PROPERTIES = new Properties();
    static {
        loadProperties();
    }
    private DbPropertiesUtil() {
    }
    /**
     * Получает значение свойства по ключу.
     *
     * @param key ключ для поиска свойства
     * @return значение свойства, соответствующее ключу, или null, если ключ не найден
     */
    public static String get(String key){
        return PROPERTIES.getProperty(key);
    }
    /**
     * Загружает свойства из файла "database.properties".
     *
     * @throws RuntimeException если происходит ошибка ввода-вывода при загрузке свойств
     */
    private static void loadProperties(){
        try(InputStream stream = DbPropertiesUtil.class.getClassLoader().getResourceAsStream("database.properties")){
            PROPERTIES.load(stream);
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
