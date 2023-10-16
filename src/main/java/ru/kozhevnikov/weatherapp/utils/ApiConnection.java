package ru.kozhevnikov.weatherapp.utils;
/**
 * Утилита для получения API-ключа.
 */
public final class ApiConnection {
    private ApiConnection() {
    }
    /**
     * Получает API-ключ из файла api.properties.
     *
     * @return API-ключ
     */
    public static String getApiKey(){
        return ApiPropertiesUtil.get("api.key");
    }
}

