package ru.kozhevnikov.weatherapp.dao;

import ru.kozhevnikov.weatherapp.entity.City;
import ru.kozhevnikov.weatherapp.entity.UserCity;
import ru.kozhevnikov.weatherapp.exception.DaoException;
import ru.kozhevnikov.weatherapp.utils.ConnectionManager;

import java.sql.*;
import java.util.*;

public class UserCityDAO implements DAO<UserCity> {
    private static final UserCityDAO INSTANCE = new UserCityDAO();
    private final UserDAO userDAO = UserDAO.getInstance();
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
    private static final String MAKE_JOURNAL_BY_USER_ID_SQL = """
            SELECT id,user_id,city_id, createat
            FROM usercity
            WHERE user_id = ?
            ORDER BY id
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

    public List<UserCity> findCitiesByUserId(int userID) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(MAKE_JOURNAL_BY_USER_ID_SQL)) {
            preparedStatement.setInt(1, userID);

            ResultSet resultSet = preparedStatement.executeQuery();
            List<UserCity> journal = new ArrayList<>();
            while (resultSet.next()) {
                journal.add(buildUserCity(resultSet));
            }
            return journal;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public City getLastCity(int userId) {
        List<UserCity> journal = findCitiesByUserId(userId);
        return journal.get(journal.size()-1).getCity();
    }

    private UserCity buildUserCity(ResultSet resultSet) throws SQLException {
        return new UserCity(resultSet.getInt("id"),
                userDAO.findById(resultSet.getInt("user_id"), resultSet.getStatement().getConnection()).orElse(null),
                cityDAO.findByID(resultSet.getInt("city_id"), resultSet.getStatement().getConnection()).orElse(null),
                resultSet.getTimestamp("createat").toLocalDateTime());
    }
}
