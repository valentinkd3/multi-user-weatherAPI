package ru.kozhevnikov.weatherapp.utils;

public final class ApiConnection {
    private ApiConnection() {
    }
    public static String getApiKey(){
        return ApiPropertiesUtil.get("api.key");
    }
}

