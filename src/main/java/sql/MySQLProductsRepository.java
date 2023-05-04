package sql;

import Entity.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MySQLProductsRepository {
    PreparedStatement preparedStatement;
    ResultSet resultSet;

    public void saveToBD(Product product){

    }

    public void add(ArrayList<Product> products) {
        String queryUpdateCount = "UPDATE product SET countProduct = ? WHERE id = ?";

        try {
            preparedStatement = MySQLConnector.getMySQLConnector().getConnection().prepareStatement(queryUpdateCount);
            for (Product product: products) {
                preparedStatement.setDouble(1, product.getCount());
                preparedStatement.setInt(2, product.getId());
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Product> getProducts(){
        ArrayList<Product> products = new ArrayList<>();

        String queryGetProducts = "SELECT * FROM product";

        try {
            preparedStatement = MySQLConnector.getMySQLConnector().getConnection().prepareStatement(queryGetProducts);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                products.add(new Product(resultSet.getInt(1), resultSet.getString(2), resultSet.getDouble(3), resultSet.getDouble(4), resultSet.getInt(5)));
            }

            return products;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<Product> getProductsByCategory(int cId){
        ArrayList<Product> products = new ArrayList<>();

        String queryGetProducts = "SELECT * FROM product WHERE categoriesId = ?";

        try {
            preparedStatement = MySQLConnector.getMySQLConnector().getConnection().prepareStatement(queryGetProducts);

            preparedStatement.setInt(1, cId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                products.add(new Product(resultSet.getInt(1), resultSet.getString(2), resultSet.getDouble(3), resultSet.getDouble(4), resultSet.getInt(5)));
            }

            return products;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<Product> getProductASC(){
        return null;
    }

    public ArrayList<Product> getProductDESC(){
        ArrayList<Product> products = new ArrayList<>();

        String queryDESC = "SELECT * FROM product ORDER BY cost DESC";

        try {
            preparedStatement = MySQLConnector.getMySQLConnector().getConnection().prepareStatement(queryDESC);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                products.add(new Product(resultSet.getInt(1), resultSet.getString(2), resultSet.getDouble(3), resultSet.getDouble(4), resultSet.getInt(5)));
            }
            return products;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
