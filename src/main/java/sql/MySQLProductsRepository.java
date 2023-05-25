package sql;

import Entity.Product;

import java.sql.*;
import java.util.ArrayList;

public class MySQLProductsRepository {
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    static final Connection connection;

    static {
        connection = MySQLConnector.getMySQLConnector().getConnection();
    }

    public void saveToBD(Product product){
        String query = "INSERT INTO product(nameProduct, cost, countProduct, categoriesId) VALUES (?, ?, ?, ?)";

        try {
            preparedStatement = connection.prepareStatement(query);

                preparedStatement.setString(1, product.getName());
                preparedStatement.setDouble(2, product.getCount());
                preparedStatement.setDouble(3, product.getPrice());
                preparedStatement.setInt(4, product.getCategory());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Product product){
        String query = "DELETE FROM product WHERE id = ?";

        try {
            preparedStatement = connection.prepareStatement(query);

                preparedStatement.setInt(1, product.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(ArrayList<Product> products) {
        String queryUpdateCount = "UPDATE product SET countProduct = ? WHERE id = ?";

        try {
            preparedStatement = connection.prepareStatement(queryUpdateCount);
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

    public Boolean update(Product product) {
        String queryUpdateCount = "UPDATE product SET nameProduct = ?, cost = ?, countProduct = ? WHERE id = ?";

        try {
            preparedStatement = connection.prepareStatement(queryUpdateCount);

                preparedStatement.setString(1, product.getName());
                preparedStatement.setDouble(2, product.getPrice());
                preparedStatement.setDouble(3, product.getCount());
                preparedStatement.setInt(4, product.getId());
                preparedStatement.addBatch();


            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            if (e.getSQLState().equals("45000")) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<Product> getProducts(){
        ArrayList<Product> products = new ArrayList<>();

        String queryGetProducts = "SELECT * FROM product_view";

        try {
            preparedStatement = connection.prepareStatement(queryGetProducts);
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

        String queryGetProducts = "SELECT * FROM product_view WHERE categoriesId = ?";

        try {
            preparedStatement = connection.prepareStatement(queryGetProducts);

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

    public ArrayList<Product> getProductsByName(String name){
        ArrayList<Product> products = new ArrayList<>();

        String queryGetProducts = "SELECT * FROM product_view WHERE nameProduct LIKE ?";
        try {
            preparedStatement = connection.prepareStatement(queryGetProducts);

            preparedStatement.setString(1, "%" + name + "%");

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

    public ArrayList<Product> getProductASC(String s, int id){
        ArrayList<Product> products = new ArrayList<>();
        String queryASC;

        if (id == 0)
            queryASC = "SELECT * FROM product_view ORDER BY " + s + " ASC";
        else
            queryASC = "SELECT * FROM product_view WHERE categoriesId = " + id + " ORDER BY " + s + " ASC";

        try {
            preparedStatement = connection.prepareStatement(queryASC);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                products.add(new Product(resultSet.getInt(1), resultSet.getString(2), resultSet.getDouble(3), resultSet.getDouble(4), resultSet.getInt(5)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public ArrayList<Product> getProductDESC(String s, int id){
        ArrayList<Product> products = new ArrayList<>();

        String queryDESC;

        if (id == 0)
            queryDESC = "SELECT * FROM product_view ORDER BY " + s + " DESC";
        else
            queryDESC = "SELECT * FROM product_view WHERE categoriesId = " + id + " ORDER BY " + s + " DESC";

        try {
            preparedStatement = connection.prepareStatement(queryDESC);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                products.add(new Product(resultSet.getInt(1), resultSet.getString(2), resultSet.getDouble(3), resultSet.getDouble(4), resultSet.getInt(5)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public ArrayList<Product> getProductFromOrder(int userId, int orderNumber){

        ArrayList<Product> products = new ArrayList<>();

        String query = "SELECT o.productId, p.nameProduct, o.price, o.count FROM orders o \n" +
                "JOIN product p ON o.productId = p.id\n" +
                "WHERE o.userId = ? AND o.number = ?";

        try {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, orderNumber);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                products.add(new Product(resultSet.getInt(1), resultSet.getString(2), resultSet.getDouble(3), resultSet.getDouble(4)));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return products;
    }

    public Boolean checkProductAvailability(int productId, double quantity) {
        String procedure = "{CALL check_product_availability(?, ?, ?)}";
        boolean isAvailable = false;

        try (CallableStatement callableStatement = connection.prepareCall(procedure)) {
            callableStatement.setInt(1, productId);
            callableStatement.setDouble(2, quantity);
            callableStatement.registerOutParameter(3, Types.BOOLEAN);

            callableStatement.execute();

            isAvailable = callableStatement.getBoolean(3);

            if (isAvailable) {
                System.out.println("Товар доступен в достаточном количестве.");
            } else {
                System.out.println("Товар недоступен в достаточном количестве.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isAvailable;
    }
}
