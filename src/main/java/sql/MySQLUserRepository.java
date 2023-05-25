package sql;

import Entity.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MySQLUserRepository implements UserRepository {
    PreparedStatement preparedStatement;
    ResultSet resultSet;

    @Override
    public User save(String login, String password, String name, String lastName, String number) {

        String querySave = "INSERT INTO user(login, password, name, lastName, telephoneNumber, isManager) VALUES(?, ?, ?, ?, ?, ?)";

        try {
            preparedStatement = MySQLConnector.getMySQLConnector().getConnection().prepareStatement(querySave);

            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, lastName);
            preparedStatement.setString(5, number);
            preparedStatement.setBoolean(6, false);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            if (e.getSQLState().equals("45000")) {
                return null;
            }
        }
        return findByUsername(login, password);
    }

    @Override
    public User findByUsername(String username, String password) {
        String queryFindByUsername = "SELECT * FROM user WHERE login = ? AND password = ?";

        try {
            preparedStatement = MySQLConnector.getMySQLConnector().getConnection().prepareStatement(queryFindByUsername);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                return new User(resultSet.getInt(1),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getBoolean(7));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUser(int id){
        String queryFindByUsername = "SELECT * FROM user WHERE id = ?";

        try {
            preparedStatement = MySQLConnector.getMySQLConnector().getConnection().prepareStatement(queryFindByUsername);
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                return new User(resultSet.getInt(1),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public void deleteByUsername(String username) {

    }
}
