package controllers;

import Entity.Order;
import Entity.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.sql.Time;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AdminMyOrderItemController {

    @FXML
    private Label myOrderDateLabel;

    @FXML
    private Button myOrderAboutButton;

    @FXML
    private Label myOrderNumberItemLabel;

    @FXML
    private Label myOrderTimeLabel;

    private Order order;

    private final List<AdminButtonListener> listeners = new ArrayList<>();
    private Product product;

    @FXML
    void initialize() {
        myOrderAboutButton.setOnAction(event -> {
            onButtonClicked();
        });
    }

    @FXML
    private void onButtonClicked() {
        for (AdminButtonListener listener : listeners) {
            listener.onButtonClicked(this, this.order);
        }
    }

    public void addButtonListener(AdminPanelController listener) {
        listeners.add(listener);
    }

    public void setData(Order order) {
        this.order = order;

        LocalDate date = order.getTimestamp().toLocalDateTime().toLocalDate();
        Time time = new Time(order.getTimestamp().getTime());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedDate = date.format(formatter);

        myOrderNumberItemLabel.setText(String.valueOf(order.getNumber()));
        myOrderDateLabel.setText(formattedDate);
        myOrderTimeLabel.setText(String.valueOf(time));
    }
}

