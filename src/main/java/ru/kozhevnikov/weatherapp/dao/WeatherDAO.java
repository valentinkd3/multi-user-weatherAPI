package ru.kozhevnikov.weatherapp.dao;


import ru.kozhevnikov.weatherapp.entity.City;
import ru.kozhevnikov.weatherapp.entity.Weather;
import ru.kozhevnikov.weatherapp.exception.DaoException;
import ru.kozhevnikov.weatherapp.utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WeatherDAO implements DAO<Weather> {
    private static final WeatherDAO INSTANCE = new WeatherDAO();
    private final CityDAO cityDAO = CityDAO.getInstance();
    private static final String SAVE_SQL = """
            INSERT INTO weather (city_id, text, curtemp, feelslike, cloud, precipitation)
            VALUES (?,?,?,?,?,?)
            """;
    private static final String TIME_SQL = """
            SELECT createat
            FROM weather
            WHERE id = ?
            """;
    private static final String WEATHERS_SQL = """
            SELECT id,
            city_id,
            text,
            curtemp,
            feelslike,
            cloud,
            precipitation,
            createat
            FROM weather
            WHERE city_id = ?
            """;
    private WeatherDAO() {
    }

    public static WeatherDAO getInstance() {
        return INSTANCE;
    }

    @Override
    public Weather save(Weather weather) {
        try (Connection connection = ConnectionManager.open()) {
            City city = weather.getCity();
            try (PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {

                preparedStatement.setInt(1, city.getId());
                preparedStatement.setString(2, weather.getText());
                preparedStatement.setInt(3, weather.getCurTemp());
                preparedStatement.setInt(4, weather.getFeelsLike());
                preparedStatement.setInt(5, weather.getCloud());
                preparedStatement.setDouble(6, weather.getPrecipitation());


                preparedStatement.executeUpdate();

                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    weather.setId(generatedKeys.getInt("id"));
                }
            }
            try(PreparedStatement preparedStatement = connection.prepareStatement(TIME_SQL)){
                preparedStatement.setInt(1, weather.getId());

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()){
                    weather.setCreateAt(resultSet.getTimestamp("createat").toLocalDateTime());
                }
                return weather;
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
    public List<Weather> getWeatherHistory(Integer cityId){
        try(Connection connection = ConnectionManager.open();
            PreparedStatement preparedStatement = connection.prepareStatement(WEATHERS_SQL)) {
            preparedStatement.setInt(1, cityId);

            List<Weather> weathers = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                weathers.add(buildWeather(resultSet));
            }
            return weathers;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private Weather buildWeather(ResultSet resultSet) throws SQLException {
        return new Weather(
            resultSet.getInt("id"),
            cityDAO.findByID(resultSet.getInt("city_id"),resultSet.getStatement().getConnection()).orElse(null),
            resultSet.getString("text"),
            resultSet.getInt("curtemp"),
            resultSet.getInt("feelslike"),
            resultSet.getInt("cloud"),
            resultSet.getDouble("precipitation"),
            resultSet.getTimestamp("createat").toLocalDateTime()
    );
    }
}
