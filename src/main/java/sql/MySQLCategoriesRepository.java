package sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class MySQLCategoriesRepository {
    PreparedStatement preparedStatement;
    ResultSet resultSet;

    public HashMap<String, Integer> getCategory(){
        HashMap<String, Integer> categories = new HashMap<>();
        String queryGetCategory = "SELECT categoriesName, id FROM categories";
        try {
            preparedStatement = MySQLConnector.getMySQLConnector().getConnection().prepareStatement(queryGetCategory);
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
}
