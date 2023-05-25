package Entity;

import java.sql.Timestamp;

public class Order {
    private int number;
    private Timestamp timestamp;
    private int userId;

    public Order(int number, Timestamp timestamp) {
        this.number = number;
        this.timestamp = timestamp;
    }

    public Order(int number, Timestamp timestamp, int userId) {
        this.number = number;
        this.timestamp = timestamp;
        this.userId = userId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
