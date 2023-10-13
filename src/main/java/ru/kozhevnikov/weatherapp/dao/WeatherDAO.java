package ru.kozhevnikov.weatherapp.dao;

import ru.kozhevnikov.weatherapp.model.Weather;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class WeatherDAO {
    private static String URL;
    private static String NAME;
    private static String PASSWORD;
    private static Connection connection;

    static {
        Properties properties = new Properties();
        try (InputStream inputStream = WeatherDAO.class.getClassLoader().getResourceAsStream("database.properties")) {
            properties.load(inputStream);
            initProperties(properties);
            connection(URL, NAME, PASSWORD);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void initProperties(Properties properties) {
        try {
            Class.forName(properties.getProperty("driver"));
            URL = properties.getProperty("url");
            NAME = properties.getProperty("user");
            PASSWORD = properties.getProperty("password");
        } catch (ClassNotFoundException e) {
        }
    }
    private static void connection(String URL, String USERNAME, String PASSWORD) {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Weather> index(){
        List<Weather> weathers = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            String SQL = "SELECT * FROM WEATHER";
            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()){
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

    public void acceptToDatabase(Weather weather){
        if (index().contains(weather)) {
            update(weather);
        } else save(weather);
    }
    public void update(Weather weather){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE Weather SET curTemp=?, curText=?, curFeels=?, avgTemp=? WHERE location=?"
            );
            preparedStatement.setInt(1,weather.getCurrentTemperature());
            preparedStatement.setString(2, weather.getCurrentText());
            preparedStatement.setInt(3,weather.getCurrentTemperatureFeelsLike());
            preparedStatement.setInt(4, weather.getAverageTemperature());
            preparedStatement.setString(5, weather.getLocation());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public void save(Weather weather){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Weather VALUES (?,?,?,?,?)");
            preparedStatement.setString(1, weather.getLocation());
            preparedStatement.setInt(2,weather.getCurrentTemperature());
            preparedStatement.setString(3, weather.getCurrentText());
            preparedStatement.setInt(4,weather.getCurrentTemperatureFeelsLike());
            preparedStatement.setInt(5, weather.getAverageTemperature());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        WeatherDAO weatherDAO = new WeatherDAO();
        Weather weather = new Weather("Moon");
        weather.setCurrentTemperature(34);
        weather.setCurrentText("Дождливо");
        weather.setCurrentTemperatureFeelsLike(13);
        weather.setAverageTemperature(47);

        weatherDAO.acceptToDatabase(weather);
    }
}
