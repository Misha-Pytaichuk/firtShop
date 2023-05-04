package controllers;

import Entity.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ItemController {

    private Product product;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label nameItemLabel;

    @FXML
    private Label countItemLabel;

    @FXML
    private Label costItemLabel;

    @FXML
    private Button openCardButton = new Button();

    private final List<ButtonListener> listeners = new ArrayList<>();

    @FXML
    void initialize() {
        openCardButton.setOnAction(event -> {
            onButtonClicked();
        });
    }

    @FXML
    private void onButtonClicked() {
        for (ButtonListener listener : listeners) {
            listener.onButtonClicked(product, this);
        }
    }

    public void addButtonListener(ButtonListener listener) {
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

