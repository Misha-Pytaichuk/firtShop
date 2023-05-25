package sql;

import Entity.Order;
import Entity.OrderItem;

import java.sql.*;
import java.util.ArrayList;

public class MySQLOrdersRepository {

    static final Connection connection;
    PreparedStatement preparedStatement;
    CallableStatement callableStatement;
    Statement statement;

    static {
        connection = MySQLConnector.getMySQLConnector().getConnection();
    }

    public ArrayList<Order> getOrdersById(int id){
        ArrayList<Order> orders = new ArrayList<>();
        try {

            String query = "SELECT number, date FROM orders WHERE userId = ? GROUP BY number, date";
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int number = resultSet.getInt("number");
                Timestamp date = resultSet.getTimestamp("date");

                orders.add(new Order(number, date));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public ArrayList<Order> getOrders(){
        ArrayList<Order> orders = new ArrayList<>();
        try {

            String query = "SELECT number, date, userId FROM orders  GROUP BY number, date, userId";
            preparedStatement = connection.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int number = resultSet.getInt("number");
                Timestamp date = resultSet.getTimestamp("date");
                int userId = resultSet.getInt("userId");

                orders.add(new Order(number, date, userId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public ArrayList<Order> getOrdersByDate(Date dateFrom, Date dateTo){
        ArrayList<Order> orders = new ArrayList<>();
        try {

            String query = "{ CALL GetOrdersByDate(?, ?) }";
            callableStatement = connection.prepareCall(query);

                callableStatement.setDate(1, dateFrom);
                callableStatement.setDate(2, dateTo);

            ResultSet resultSet = callableStatement.executeQuery();

            while (resultSet.next()) {
                int number = resultSet.getInt("number");
                Timestamp date = resultSet.getTimestamp("date");
                int userId = resultSet.getInt("userId");

                orders.add(new Order(number, date, userId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public void buy(int id, ArrayList<OrderItem> orderItems) {
        String queryAddOrder = "INSERT INTO `orders`(userId, productId, price, count, date, number) VALUES(?, ?, ?, ?, now(), ?)";

        try {
            preparedStatement = connection.prepareStatement(queryAddOrder);

            int number = getNextOrderNumber();

            for (OrderItem orderItem : orderItems) {
                preparedStatement.setInt(1, id);
                preparedStatement.setInt(2, orderItem.getProduct().getId());
                preparedStatement.setDouble(3, orderItem.getProduct().getPrice());
                preparedStatement.setDouble(4, orderItem.getProduct().getCount());
                preparedStatement.setInt(5, number);
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    // Метод для получения следующего номера заказа
    private int getNextOrderNumber() {
        String query = "SELECT MAX(number) FROM `orders`";
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                int maxOrderNumber = resultSet.getInt(1);
                return maxOrderNumber + 1; // Инкрементируем текущий максимальный номер заказа
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 1; // Если таблица пустая, возвращаем 1
    }
}

