package controllers;

import Entity.Product;

public interface ButtonListener {
    void onButtonClicked(Product product, ItemController itemController);
    void onButtonClicked(Product product, ItemOrderController itemController, ItemController ic);

}
