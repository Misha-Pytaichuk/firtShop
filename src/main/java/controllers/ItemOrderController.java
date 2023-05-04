package controllers;

import Entity.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;

public class ItemOrderController {

    @FXML
    private Label nameItemLabel;

    @FXML
    private Label costItemLabel;

    @FXML
    private Label countItemLabel;

    @FXML
    private Button deleteFromOrderButton;

    @FXML
    private Button openOrderCardButton;

    ItemController itemController;

    private final List<ButtonListener> listeners = new ArrayList<>();
    private Product product;

    @FXML
    void initialize() {
        deleteFromOrderButton.setOnAction(event -> {
            onButtonClicked();
        });
    }

    @FXML
    private void onButtonClicked() {
        for (ButtonListener listener : listeners) {
            listener.onButtonClicked(product, this, itemController);
        }
    }

    public void addButtonListener(ButtonListener listener) {
        listeners.add(listener);
    }

    public void setData(Product product, ItemController itemController) {
        this.product = product;
        this.itemController = itemController;
        nameItemLabel.setText(product.getName());
        countItemLabel.setText(String.valueOf(product.getCount()));
        costItemLabel.setText(String.valueOf(product.getPrice()));
    }
}
