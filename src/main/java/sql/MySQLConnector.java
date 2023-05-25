package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnector {
    private static MySQLConnector mySQLConnector;
    private static Connection connection;
    private static final String url = "jdbc:mysql://localhost:3306/shop";
    private static final String user = "admin";
    private static final String password = "admin";

    public static synchronized MySQLConnector getMySQLConnector(){
        if(mySQLConnector == null) {
            mySQLConnector = new MySQLConnector();
        }
        return mySQLConnector;
    }

    private MySQLConnector(){
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
