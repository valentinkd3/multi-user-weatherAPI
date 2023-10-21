package ru.kozhevnikov.weatherapp.dao;

import ru.kozhevnikov.weatherapp.entity.City;
import ru.kozhevnikov.weatherapp.entity.Weather;
import ru.kozhevnikov.weatherapp.exception.DaoException;
import ru.kozhevnikov.weatherapp.utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CityDAO implements DAO<City>{
    private static final CityDAO INSTANCE = new CityDAO();

    private CityDAO() {
    }

    public static CityDAO getInstance() {
        return INSTANCE;
    }

    private static final String SAVE_SQL = """
            INSERT INTO city (name)
            VALUES (?)
            """;
    public static final String FIND_ALL_SQL = """
            SELECT id,
                name
            FROM city
            """;
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?
            """;
    private static final String FIND_BY_NAME_SQL = FIND_ALL_SQL + """
            WHERE name = ?
            """;
    private static final String DELETE_SQL = """
            DELETE FROM city
            WHERE name = ?
            """;
    public Optional<City> findByName(String name){
        try(Connection connection = ConnectionManager.open();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_NAME_SQL)) {
            preparedStatement.setString(1, name);
            City city = null;
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                city = buildCity(resultSet);
            }
            return Optional.ofNullable(city);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
    public List<City> findAll(){
        try(Connection connection = ConnectionManager.open();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            List<City> cities = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                cities.add(buildCity(resultSet));
            }
            return cities;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
    public Optional<City> findByID(Integer id) {
        try(Connection connection = ConnectionManager.open()){
            return findByID(id,connection);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
    public Optional<City> findByID(Integer id, Connection connection) {
        try(PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            City city = null;
            if (resultSet.next()){
                city = buildCity(resultSet);
            }
            return Optional.ofNullable(city);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
    private static City buildCity(ResultSet resultSet) throws SQLException {
        return new City(
                resultSet.getInt("id"),
                resultSet.getString("name")
        );
    }
    public boolean deleteByName(String name){
        try(Connection connection = ConnectionManager.open();
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setString(1, name);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
    @Override
    public City save(City city) {
        if (checkCity(city)) return null;
        try (Connection connection = ConnectionManager.open();
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, city.getName());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if(generatedKeys.next()){
                city.setId(generatedKeys.getInt("id"));
            }
        return city;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
    private boolean checkCity(City city){
        return findAll().contains(city);
    }
}
