package ru.kozhevnikov.weatherapp.utils;

import ru.kozhevnikov.weatherapp.dao.WeatherDAO;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class ConnectionManager {
    static {
        try {
            Class.forName(DbPropertiesUtil.get("driver"));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private ConnectionManager() {
    }

    public static Connection open() {
        try {
            return DriverManager.getConnection(
                    DbPropertiesUtil.get("url"),
                    DbPropertiesUtil.get("name"),
                    DbPropertiesUtil.get("password"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
