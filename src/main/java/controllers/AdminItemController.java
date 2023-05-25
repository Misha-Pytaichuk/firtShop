package controllers;

import Entity.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdminItemController {

    private Product product;

    @FXML
    private Label nameItemLabel;

    @FXML
    private Label countItemLabel;

    @FXML
    private Label costItemLabel;

    @FXML
    private Button openCardButton = new Button();
    @FXML
    private Button removeItemButton;
    private final List<AdminButtonListener> listeners = new ArrayList<>();

    @FXML
    void initialize() {
        openCardButton.setOnAction(event -> {
            onButtonClicked();
        });
        removeItemButton.setOnAction(event -> {
            onRemoveButtonClicked();
        });
    }

    @FXML
    private void onButtonClicked() {
        for (AdminButtonListener listener : listeners) {
            listener.onButtonClicked(product, this);
        }
    }

    @FXML
    private void onRemoveButtonClicked() {
        for (AdminButtonListener listener : listeners) {
            listener.onRemoveButtonClicked(product, this);
        }
    }



    public void addButtonListener(AdminButtonListener listener) {
        listeners.add(listener);
    }

    public void setData(Product product) {
        this.product = product;
        nameItemLabel.setText(product.getName());
        countItemLabel.setText("В наявності: " + product.getCount());
        costItemLabel.setText("Ціна: " + product.getPrice());
    }

    public void setCount(double count){
        product.setCount(count);
        countItemLabel.setText("В наявності: " + count);
    }

    public Product getProduct() {
        return product;
    }
}

