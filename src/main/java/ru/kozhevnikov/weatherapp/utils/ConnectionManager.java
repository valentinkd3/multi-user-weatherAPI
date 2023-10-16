package ru.kozhevnikov.weatherapp.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * Менеджер подключения к базе данных.
 */
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
    /**
     * Открывает новое подключение к базе данных.
     *
     * @return объект Connection для взаимодействия с базой данных
     * @throws RuntimeException если происходит ошибка при открытии подключения
     */
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
