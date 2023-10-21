package ru.kozhevnikov.weatherapp.dao;

import ru.kozhevnikov.weatherapp.entity.City;
import ru.kozhevnikov.weatherapp.entity.UserCity;
import ru.kozhevnikov.weatherapp.exception.DaoException;
import ru.kozhevnikov.weatherapp.utils.ConnectionManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class UserCityDAO implements DAO<UserCity> {
    private static final UserCityDAO INSTANCE = new UserCityDAO();
    private final CityDAO cityDAO = CityDAO.getInstance();
    private static final String SAVE_SQL = """
            INSERT INTO usercity (city_id, user_id)
            VALUES (?,?)
            """;
    private static final String TIME_SQL = """
            SELECT createat
            FROM usercity
            WHERE id = ?
            """;
    private static final String FIND_CITY_BY_USER_ID_SQL = """
            SELECT city_id, createat
            FROM usercity
            WHERE user_id = ?
            """;

    private UserCityDAO() {
    }

    public static UserCityDAO getInstance() {
        return INSTANCE;
    }

    @Override
    public UserCity save(UserCity userCity) {
        try (Connection connection = ConnectionManager.open()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setInt(1, userCity.getCity().getId());
                preparedStatement.setInt(2, userCity.getUser().getId());

                preparedStatement.executeUpdate();

                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    userCity.setId(generatedKeys.getInt("id"));
                }
            }
            try(PreparedStatement preparedStatement = connection.prepareStatement(TIME_SQL)){
                preparedStatement.setInt(1, userCity.getId());
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()){
                    userCity.setCreateAt(resultSet.getTimestamp("createat").toLocalDateTime());
                }
            }
            return userCity;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public City getLastCity(int userId) {
        Map<LocalDateTime,City> cities = findCitiesByUserId(userId);
        if (cities.isEmpty()) return null;
        City lastCity = null;
        for (City city : cities.values()) {
            lastCity = city;
        }
        return lastCity;
    }

    public Map<LocalDateTime, City> findCitiesByUserId(int userID) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_CITY_BY_USER_ID_SQL)) {
            preparedStatement.setInt(1, userID);

            ResultSet resultSet = preparedStatement.executeQuery();
//            List<City> cities = new ArrayList<>();
            Map<LocalDateTime, City> cities = new LinkedHashMap<>();
            while (resultSet.next()) {
                cities.put(resultSet.getTimestamp("createAt").toLocalDateTime(),cityDAO.findByID(resultSet.getInt("city_id"),
                        resultSet.getStatement().getConnection()).orElse(null));
            }
            return cities;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
