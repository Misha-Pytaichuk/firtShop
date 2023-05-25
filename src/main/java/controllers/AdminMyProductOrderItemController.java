package controllers;

import Entity.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AdminMyProductOrderItemController {

    @FXML
    private Label myProductCountLabel;

    @FXML
    private Label myProductNameLabel;

    @FXML
    private Label myProductCostLabel;

    @FXML
    void initialize() {

    }

    public void setData(Product product){
        myProductNameLabel.setText(product.getName());
        myProductCountLabel.setText(String.valueOf(product.getCount()));
        myProductCostLabel.setText(String.valueOf(product.getPrice()));
    }
}
