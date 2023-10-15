package ru.kozhevnikov.weatherapp.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
