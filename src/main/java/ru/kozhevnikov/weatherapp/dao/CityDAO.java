package ru.kozhevnikov.weatherapp.dao;

import ru.kozhevnikov.weatherapp.model.City;
import ru.kozhevnikov.weatherapp.model.Weather;
import ru.kozhevnikov.weatherapp.utils.ConnectionManager;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
/**
 * Класс {@code CityDAO} предоставляет методы для взаимодействия с базой данных и выполнения операций с моделями
 * {@code City} и {@code Weather}.
 */
public class CityDAO {
    private static Connection connection = ConnectionManager.open();
    /**
     * Метод принимает объект типа City и добавляет его в базу данных.
     * Если город уже существует в базе, метод проверяет наличие данных о погоде на сегодня.
     * Если данные сегодняшней даты уже присутствуют, то они обновляются, иначе создается новая запись погоды.
     *
     * @param city объект типа City, который необходимо добавить в базу данных
     */
    public void acceptToDatabase(City city) {
        if (getCities().contains(city)){
            if (isContainsTodayData(city)){
                updateWeather(city.getWeather());
            }
            else {
                saveWeather(city.getWeather());
            }
        }
        else {
            saveCity(city);
            saveWeather(city.getWeather());
        }
    }
    /**
     * Метод возвращает список объектов City, для которых имеются данные о погоде на текущую дату.
     *
     * @return cписок объектов City с данными о погоде на текущую дату
     */
    public List<City> getTodayDateFromDb() {
        List<City> cities = new ArrayList<>();
        LocalDate todayDate = LocalDate.now();

        for (City cityFromDb : getCities()) {
            int idFromDb = getCityID(cityFromDb);
            List<Weather> weathersFromDb = getWeathersForCity(idFromDb);
            for (Weather weatherFromDb : weathersFromDb) {
                if (weatherFromDb.getDate().equals(todayDate)){
                    cityFromDb.setWeather(weatherFromDb);
                    cities.add(cityFromDb);
                }
            }
        }
        return cities;
    }
    private boolean isContainsTodayData(City city){
        int id = getCityID(city);
        for(Weather weatherFromDb : getWeathersForCity(id)){
            if (weatherFromDb.getDate().equals(city.getWeather().getDate())) return true;
        }
        return false;
    }
    private List<Weather> getWeathersForCity(int cityId){
        List<Weather> weathers = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement("" +
                "SELECT avgTemp, cloud, precipitation, date, text FROM Weather Where city_id = ?")){
            statement.setInt(1, cityId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                Weather weather = new Weather();
                weather.setAverageTemperature(resultSet.getInt("avgTemp"));
                weather.setCloud(resultSet.getInt("cloud"));
                weather.setPrecipitation(resultSet.getDouble("precipitation"));
                weather.setDate(resultSet.getDate("date").toLocalDate());
                weather.setCurrentText(resultSet.getString("text"));

                weathers.add(weather);
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
        return weathers;
    }
    private List<City> getCities() {
        List<City> cities = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement("SELECT name FROM City")) {
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
    private void updateWeather(Weather weather) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE Weather SET avgTemp=?, cloud = ?, precipitation = ?, text = ? WHERE city_id = ?"
        )) {
            preparedStatement.setInt(1, weather.getAverageTemperature());
            preparedStatement.setInt(2, weather.getCloud());
            preparedStatement.setDouble(3, weather.getPrecipitation());
            preparedStatement.setString(4, weather.getCurrentText());
            preparedStatement.setInt(5, getCityID(weather.getCity()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveCity(City city) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO City"
                + "(name) VALUES (?)")) {

            preparedStatement.setString(1, city.getName());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveWeather(Weather weather) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Weather " +
                "(city_id, avgTemp, cloud, precipitation, date, text) VALUES (?,?,?,?,?,?)")) {

            preparedStatement.setInt(1, getCityID(weather.getCity()));
            preparedStatement.setInt(2, weather.getAverageTemperature());
            preparedStatement.setInt(3, weather.getCloud());
            preparedStatement.setDouble(4, weather.getPrecipitation());
            preparedStatement.setDate(5, Date.valueOf(weather.getDate()));
            preparedStatement.setString(6, weather.getCurrentText());

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
