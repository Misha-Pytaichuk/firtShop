package sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class MySQLCategoriesRepository {

    static final Connection connection;

    static {
        connection = MySQLConnector.getMySQLConnector().getConnection();
    }
    PreparedStatement preparedStatement;
    ResultSet resultSet;

    public HashMap<String, Integer> getCategory(){
        HashMap<String, Integer> categories = new HashMap<>();
        String queryGetCategory = "SELECT categoriesName, id FROM categories";
        try {
            preparedStatement = connection.prepareStatement(queryGetCategory);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                categories.put(resultSet.getString("categoriesName"), resultSet.getInt("id"));
            }

            return categories;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getCategory(int id){
        String category = null;
        String queryGetCategory = "SELECT categoriesName FROM categories WHERE id = ?";
        try {
            preparedStatement = connection.prepareStatement(queryGetCategory);

            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                category = resultSet.getString(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;
    }
}
