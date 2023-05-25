package controllers;

import Entity.Order;
import Entity.Product;

public interface AdminButtonListener {
    void onButtonClicked(Product product, AdminItemController itemController);
    void onRemoveButtonClicked(Product product, AdminItemController itemController);
    void onButtonClicked(AdminMyOrderItemController myOrderItemController, Order order);

}
