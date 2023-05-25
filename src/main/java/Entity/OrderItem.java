package Entity;

import controllers.ItemOrderController;
import javafx.scene.layout.AnchorPane;

public class OrderItem {
    private ItemOrderController itemOrderController;
    private Product product;
    private AnchorPane anchorPane;

    public OrderItem(ItemOrderController itemOrderController, Product product, AnchorPane anchorPane) {
        this.itemOrderController = itemOrderController;
        this.product = product;
        this.anchorPane = anchorPane;
    }

    public ItemOrderController getItemOrderController() {
        return itemOrderController;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public AnchorPane getAnchorPane() {
        return anchorPane;
    }

    public void setAnchorPane(AnchorPane anchorPane) {
        this.anchorPane = anchorPane;
    }
}
