package ru.kozhevnikov.weatherapp.utils;

import ru.kozhevnikov.weatherapp.dao.WeatherDAO;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ApiConnection {
    private ApiConnection() {
    }
    public static String getApiKey(){
        return ApiPropertiesUtil.get("api.key");
    }
}

