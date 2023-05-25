package controllers;

import Entity.Order;
import Entity.Product;

public interface ButtonListener {
    void onButtonClicked(Product product, ItemController itemController);
    void onButtonClicked(Product product, ItemOrderController itemController, ItemController ic);
    void onButtonClicked(MyOrderItemController myOrderItemController, Order order);

}
