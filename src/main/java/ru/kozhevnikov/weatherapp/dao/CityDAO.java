package ru.kozhevnikov.weatherapp.dao;

import ru.kozhevnikov.weatherapp.model.City;
import ru.kozhevnikov.weatherapp.model.Weather;
import ru.kozhevnikov.weatherapp.utils.ConnectionManager;

import java.sql.*;
import java.util.*;

public class CityDAO {
    private static Connection connection = ConnectionManager.open();

    public List<City> getDataFromDB() {
        List<City> cities = getCities();
        for (City city : cities) {
            Weather weather = initWeather(getCityID(city));
            city.setWeather(weather);
        }
        return cities;
    }

    private Weather initWeather(int cityId) {
        Weather weather = new Weather();
        try (PreparedStatement preparedStatement = connection.prepareStatement("" +
                "SELECT avgTemp, cloud, precipitation FROM Weather WHERE city_id = ?")) {
            preparedStatement.setInt(1, cityId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                weather.setAverageTemperature(resultSet.getInt("avgTemp"));
                weather.setCloud(resultSet.getInt("cloud"));
                weather.setPrecipitation(resultSet.getDouble("precipitation"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return weather;
    }

    public List<City> getCities() {
        List<City> cities = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM City")) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("name");

                cities.add(new City(name));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cities;
    }

    public void acceptToDatabase(City city) {
        if (getCities().contains(city)) {
            update(city.getWeather());
        } else {
            save(city, city.getWeather());
        }
    }

    public void update(Weather weather) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE Weather SET avgTemp=?, cloud = ?, precipitation = ? WHERE city_id = ?"
        )) {
            preparedStatement.setInt(1, weather.getAverageTemperature());
            preparedStatement.setInt(2, weather.getCloud());
            preparedStatement.setDouble(3, weather.getPrecipitation());
            preparedStatement.setInt(4, getCityID(weather.getCity()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void save(City city, Weather weather) {
        save(city);
        save(city.getWeather());
    }

    private void save(City city) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO City"
                + "(name) VALUES (?)")) {
            preparedStatement.setString(1, city.getName());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void save(Weather weather) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Weather " +
                "(city_id, avgTemp, cloud, precipitation) VALUES (?,?,?,?)")) {
            preparedStatement.setInt(1, getCityID(weather.getCity()));
            preparedStatement.setInt(2, weather.getAverageTemperature());
            preparedStatement.setInt(3, weather.getCloud());
            preparedStatement.setDouble(4, weather.getPrecipitation());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int getCityID(City city) {
        int id = -1;
        try (PreparedStatement statement = connection.prepareStatement("SELECT city_id FROM City WHERE name = ?")) {
            statement.setString(1, city.getName());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt("city_id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return id;
    }
}
