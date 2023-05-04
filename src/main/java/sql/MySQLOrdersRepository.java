package sql;

import Entity.OrderItem;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class MySQLOrdersRepository {
    PreparedStatement preparedStatement;

    public void buy(int id, ArrayList<OrderItem> orderItems) {
        String queryAddOrder = "INSERT INTO `order`(userId, productId, price, count, date) VALUES(?, ?, ?, ?, ?)";

        try {
            preparedStatement = MySQLConnector.getMySQLConnector().getConnection().prepareStatement(queryAddOrder);

            for (OrderItem orderItem : orderItems) {
                preparedStatement.setInt(1, id);
                preparedStatement.setInt(2, orderItem.getProduct().getId());
                preparedStatement.setDouble(3, orderItem.getProduct().getPrice());
                preparedStatement.setDouble(4, orderItem.getProduct().getCount());
                preparedStatement.setDate(5, Date.valueOf(LocalDate.now()));
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
