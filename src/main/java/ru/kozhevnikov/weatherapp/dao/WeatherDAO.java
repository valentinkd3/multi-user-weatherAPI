package ru.kozhevnikov.weatherapp.dao;

import ru.kozhevnikov.weatherapp.model.Weather;
import ru.kozhevnikov.weatherapp.utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WeatherDAO {
    private static Connection connection = ConnectionManager.open();

    public List<Weather> index() {
        List<Weather> weathers = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            String SQL = "SELECT * FROM WEATHER";
            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {
                String location = resultSet.getString("location");
                Weather weather = new Weather(location);
                weather.setCurrentTemperature(resultSet.getInt("curTemp"));
                weather.setCurrentText(resultSet.getString("curText"));
                weather.setCurrentTemperatureFeelsLike(resultSet.getInt("curFeels"));
                weather.setAverageTemperature(resultSet.getInt("avgTemp"));

                weathers.add(weather);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return weathers;
    }

    public void acceptToDatabase(Weather weather) {
        if (index().contains(weather)) {
            update(weather);
        } else save(weather);
    }

    public void update(Weather weather) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE Weather SET curTemp=?, curText=?, curFeels=?, avgTemp=? WHERE location=?"
            );
            preparedStatement.setInt(1, weather.getCurrentTemperature());
            preparedStatement.setString(2, weather.getCurrentText());
            preparedStatement.setInt(3, weather.getCurrentTemperatureFeelsLike());
            preparedStatement.setInt(4, weather.getAverageTemperature());
            preparedStatement.setString(5, weather.getLocation());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void save(Weather weather) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Weather VALUES (?,?,?,?,?,?)");
            preparedStatement.setInt(1, new Random().nextInt());
            preparedStatement.setString(2, weather.getLocation());
            preparedStatement.setInt(3, weather.getCurrentTemperature());
            preparedStatement.setString(4, weather.getCurrentText());
            preparedStatement.setInt(5, weather.getCurrentTemperatureFeelsLike());
            preparedStatement.setInt(6, weather.getAverageTemperature());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
