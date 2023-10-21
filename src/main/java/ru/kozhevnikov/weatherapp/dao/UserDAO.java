package ru.kozhevnikov.weatherapp.dao;

import ru.kozhevnikov.weatherapp.entity.User;
import ru.kozhevnikov.weatherapp.exception.DaoException;
import ru.kozhevnikov.weatherapp.utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO implements DAO<User>{
    private static final UserDAO INSTANCE = new UserDAO();
    private static final String DELETE_SQL = """
            DELETE FROM users
            WHERE id = ?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO users (username, password) 
            VALUES (?,?);
            """;
    private static final String UPDATE_SQL = """
            UPDATE users
            SET username = ?,
                password = ?
            WHERE id = ?
            """;
    private static final String FIND_ALL_SQL = """
            SELECT id,
                username,
                password
            FROM users
            """;
    private static final String FIND_BY_NAME_SQL = """
            SELECT id,
            username,
            password
            FROM users
            WHERE username = ?
            """;
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?
            """;
    private UserDAO() {
    }

    public static UserDAO getInstance() {
        return INSTANCE;
    }
    @Override
    public User save(User element){
        try(Connection connection = ConnectionManager.open();
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1, element.getUsername());
            preparedStatement.setString(2, element.getPassword());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()){
                element.setId(generatedKeys.getInt("id"));
            }
            return element;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
    public Optional<User> findById(Integer id){
        try(Connection connection = ConnectionManager.open()){
            return findById(id,connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Optional<User> findById(Integer id, Connection connection){
        try(PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setInt(1, id);
            User user = null;
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                user = buildUser(resultSet);
            }
            return Optional.ofNullable(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Optional<User> findByName(String username){
        try(Connection connection = ConnectionManager.open();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_NAME_SQL)) {

            preparedStatement.setString(1, username);
            User user = null;
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                user = buildUser(resultSet);
            }
            return Optional.ofNullable(user);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
    public void update(User user){
        try(Connection connection = ConnectionManager.open();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setInt(3, user.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
    public boolean delete(int id) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }public List<User> findAll(){
        try(Connection connection = ConnectionManager.open();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            List<User> users = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                users.add(buildUser(resultSet));
            }
            return users;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
    private static User buildUser(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getInt("id"),
                resultSet.getString("username"),
                resultSet.getString("password")
        );
    }
}
