package controllers;

import Entity.Order;
import Entity.OrderItem;
import Entity.Product;
import Entity.User;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.util.Duration;
import sql.MySQLCategoriesRepository;
import sql.MySQLOrdersRepository;
import sql.MySQLProductsRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class OrderController implements ButtonListener{

    public Label nameItemLabel1;
    private User user;
    private ItemController itemController = new ItemController();
    private ItemOrderController itemOrderController = new ItemOrderController();
    private MyOrderItemController myOrderItemController = new MyOrderItemController();
    private MyProductOrderItemController myProductOrderItemController = new MyProductOrderItemController();

    private MySQLCategoriesRepository categoriesRepository = new MySQLCategoriesRepository();
    private MySQLProductsRepository productsRepository = new MySQLProductsRepository();
    private MySQLOrdersRepository ordersRepository = new MySQLOrdersRepository();

    private HashMap<String, Integer> categories = categoriesRepository.getCategory(); // Для категорій
    private Set<String> keysCategory = categories.keySet(); // Для категорій

    private ArrayList<Product> products = productsRepository.getProducts(); // Отримуємо всі товари
    private ArrayList<Product> containerForProducts = products; // Контейнер
    private ArrayList<Product> filterProducts = products;
    private ArrayList<Product> changeProducts = new ArrayList<>();
    private ArrayList<OrderItem> orderItems = new ArrayList<>();
    private ArrayList<MyOrderItemController> myOrderItemControllers = new ArrayList<>();

    private boolean isMenuHidden = true;
    private int countRow = 1;
    private String radio;
    private int categoryID = 0;

    @FXML
    private RadioButton costRadioButton;

    @FXML
    private Label categoryLaber;

    @FXML
    private Button searchButton;

    @FXML
    private Label sortLabel;

    @FXML
    private AnchorPane leftPanel;

    @FXML
    private AnchorPane mainPanel;

    @FXML
    private ChoiceBox<String> sortChoiceBox = new ChoiceBox<>();

    @FXML
    private RadioButton hasRadioButton;

    @FXML
    private TextField searchFiled;

    @FXML
    private RadioButton nameRadioButton;

    @FXML
    private ChoiceBox<String> categoryChoiceBox = new ChoiceBox<>();;

    @FXML
    private Text filterLabel;

    @FXML
    private Pane searchPanel;

    @FXML
    private Button searchButtonForField;

    @FXML
    private AnchorPane userPanel;

    @FXML
    private AnchorPane contentOrdersPanel;

    @FXML
    private Button checkoutButton;

    @FXML
    private Label idLabel;

    @FXML
    private Label numberLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Button helpButton;

    @FXML
    private GridPane gridPane;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Pane backgroundForCard;

    @FXML
    private AnchorPane panelItemCard;

    @FXML
    private Button buyButton;

    @FXML
    private Label nameItemLabel;

    @FXML
    private Label costItemLabel;

    @FXML
    private Button backButton;

    @FXML
    private TextField countField;

    @FXML
    private Label countItemLabel;

    @FXML
    private GridPane orderGridPane;

    @FXML
    private ScrollPane orderScrollPane;

    @FXML
    private Button hideButton;

    @FXML
    private Pane backgroundForRightPanel;

    @FXML
    private Label infoItemLabel;

    @FXML
    private Button confirmBackButton;

    @FXML
    private Label confirmSumLabel;

    @FXML
    private Button confirmButton;

    @FXML
    private AnchorPane confirmPanel;

    @FXML
    private Button myOrderButton;

    @FXML
    private Button myOrdersBackButton;

    @FXML
    private ScrollPane myOrderScrollPane;

    @FXML
    private GridPane myOrderGridPane;

    @FXML
    private AnchorPane myOrdersPanel;

    @FXML
    private Button dropFiltersButton;

    @FXML
    private AnchorPane myProductOrdersPanel;

    @FXML
    private ScrollPane myProductOrderScrollPanel;

    @FXML
    private GridPane myProductOrderGridPane;

    @FXML
    private Button myProductOrdersBackButton;

    @FXML
    void initialize() {
        pushInitItems();
        pushToScrollPanel(products);
        choiceSortKind();
        setUserToRightPanel();
        choiceCategory();
        confirmOrder();
        checkMyOrders();
        searchProduct();
        dropFilters();
    }

    public void pushInitItems(){
        ToggleGroup group = new ToggleGroup();

        costRadioButton.setToggleGroup(group);
        nameRadioButton.setToggleGroup(group);
        hasRadioButton.setToggleGroup(group);

        sortChoiceBox.getItems().add("За зростанням");
        sortChoiceBox.getItems().add("За спаданням");
        sortChoiceBox.getItems().add("Без сортування");

        sortChoiceBox.setValue("Без сортування");

        categoryChoiceBox.getItems().addAll(keysCategory);
        categoryChoiceBox.getItems().add("Всі");
    }
    public void choiceSortKind(){
        sortChoiceBox.setOnAction(event -> {
            if(sortChoiceBox.getValue().equals("За зростанням")){
                choiceFilter("asc");
            }
            if(sortChoiceBox.getValue().equals("За спаданням")){
                choiceFilter("desc");
            }
            if(sortChoiceBox.getValue().equals("Без сортування")){
                choiceFilter("none");
            }
        });
    }
    public void choiceFilter(String s){
        searchButton.setOnAction(event -> {
            ArrayList<Product> list = new ArrayList<>();
            gridPane.getChildren().clear();
            if(nameRadioButton.isSelected()) {
                radio = "nameProduct";
            }
            else if(costRadioButton.isSelected()) {
                radio = "cost";
            }
            else if(hasRadioButton.isSelected()) {
                radio = "countProduct";
            }
            switch (s){
                case "asc":
                    for (Product product: productsRepository.getProductASC(radio, categoryID)) {
                        for (Product filter:changeProducts) {
                            if(product.getId() == filter.getId()) product.setCount(filter.getCount());
                        }
                        list.add(product);
                    }
                    pushToScrollPanel(list);
                    break;
                case "desc":
                    for (Product product: productsRepository.getProductDESC(radio, categoryID)) {
                        for (Product filter:changeProducts) {
                            if(product.getId() == filter.getId()) product.setCount(filter.getCount());
                        }
                        list.add(product);
                    }
                    pushToScrollPanel(list);
                    break;
                case "none":
                    pushToScrollPanel(filterProducts);
                    break;
            }
            System.out.println(radio + " " + s + " " + categoryID);
        });
    }

    public void choiceCategory(){
        categoryChoiceBox.setOnAction(event -> {
            gridPane.getChildren().clear();
            if(categoryChoiceBox.getValue().equals("Всі")){
                filterProducts = containerForProducts;
                categoryID = 0;
            }

            for (String str:keysCategory) {
                if(categoryChoiceBox.getValue().equals(str)){
                    categoryID = categories.get(str);
                    filterProducts = productsRepository.getProductsByCategory(categoryID);
                }
            }
            pushToScrollPanel(filterProducts);
            searchButton.fire();
        });
    }

    public void dropFilters(){
        dropFiltersButton.setOnAction(event -> {
            gridPane.getChildren().clear();

            nameRadioButton.setSelected(false);
            costRadioButton.setSelected(false);
            hasRadioButton.setSelected(false);

            categoryChoiceBox.setValue("Всі");
            sortChoiceBox.setValue("Без сортування");

            pushToScrollPanel(containerForProducts);
        });
    }

    public void searchProduct(){
        searchButtonForField.setOnAction(event -> {
            ArrayList<Product> list = new ArrayList<>();

            String text = String.valueOf(searchFiled.getCharacters());

            System.out.println(text);
            if(text.isEmpty()) {
                pushToScrollPanel(filterProducts);
            } else {
                gridPane.getChildren().clear();
                ArrayList<Product> searchProducts = productsRepository.getProductsByName(text);

                for (Product filter:filterProducts) {
                    for (Product search: searchProducts) {
                        if(filter.getName().equals(search.getName())) {
                            search.setCount(filter.getCount());
                            list.add(search);
                        }
                    }
                }
            }
            pushToScrollPanel(list);
        });
    }

    public void setUserToRightPanel(){
        userPanel.setVisible(true);
        user = SceneController.getUser();

        nameLabel.setText(user.getName() + " " + user.getLastName());
        numberLabel.setText(user.getNumber());
        idLabel.setText("ID: " + user.getId());

        pushMyOrders();

        userPanel.setTranslateX(userPanel.getWidth()+360);
        helpButton.setOnAction(actionEvent -> {
            hideRightPanel();
        });
        if(isMenuHidden){
            hideButton.setOnAction(event -> {
                hideRightPanel();
            });
        }
    }

    public void pushMyOrders() {
        myOrderGridPane.getChildren().clear();
        ArrayList<Order> orders = ordersRepository.getOrdersById(user.getId());

        try {
            for (Order order: orders) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/Main/myOrderItem1.fxml"));
                AnchorPane anchorPane = loader.load();
                myOrderItemController = loader.getController();

                myOrderItemController.setData(order);

                myOrderItemController.addButtonListener(this);

                myOrderGridPane.add(anchorPane, 0, countRow++);
                myOrderGridPane.setMargin(anchorPane, new Insets(1));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (Order order: orders) {
            System.out.println(order.getNumber() + " " + order.getTimestamp());
        }
    }

    public void pushMyProductOrders(ArrayList<Product> products) {

        try {
            for (Product product: products) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/Main/myProductOrderItem.fxml"));
                AnchorPane anchorPane = loader.load();
                myProductOrderItemController = loader.getController();

                myProductOrderItemController.setData(product);

                myProductOrderGridPane.add(anchorPane, 0, countRow++);
                myProductOrderGridPane.setMargin(anchorPane, new Insets(1));
            }

            myProductOrdersBackButton.setOnAction(event -> {
                myProductOrderGridPane.getChildren().clear();
                myProductOrdersPanel.setVisible(false);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void pushToScrollPanel(ArrayList<Product> products){
        int column = 0;
        int row = 1;
        try {
            for (Product product: products) {

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/Main/item.fxml"));
                AnchorPane anchorPane = loader.load();

                itemController = loader.getController();
                itemController.setData(product);
                itemController.addButtonListener(this);

                if(column == 4) {
                    column = 0;
                    row++;
                }

                gridPane.add(anchorPane, column++, row);
                gridPane.setMaxWidth(Region.USE_COMPUTED_SIZE);
                gridPane.setMinWidth(Region.USE_COMPUTED_SIZE);
                gridPane.setPrefWidth(Region.USE_COMPUTED_SIZE);

                gridPane.setMaxHeight(Region.USE_COMPUTED_SIZE);
                gridPane.setMinHeight(Region.USE_COMPUTED_SIZE);
                gridPane.setPrefHeight(Region.USE_COMPUTED_SIZE);

                gridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void hideRightPanel() {
        TranslateTransition menuTransition = new TranslateTransition(Duration.millis(500), userPanel);

        if (isMenuHidden) {
            menuTransition.setToX(0);
            isMenuHidden = false;
            backgroundForRightPanel.setVisible(true);
        } else {
            menuTransition.setToX(360);
            isMenuHidden = true;
            backgroundForRightPanel.setVisible(false);
        }
        menuTransition.play();
    }

    public void openItemCard(Product product, ItemController itemController){ // відкриваєемо повну версію картки товару
        countField.clear();

        nameItemLabel.setText(product.getName());
        countItemLabel.setText("Кількість: " + product.getCount());
        costItemLabel.setText("Ціна: " + product.getPrice());
        buyButton.setOnAction(event -> {
            double fieldCount;
            double oldCount;
            double orderPrice;

            oldCount = product.getCount();
            fieldCount = Double.parseDouble(countField.getText());
            infoItemLabel.setTextFill(Paint.valueOf("RED"));
            if(countField.getText().isEmpty()) {
                infoItemLabel.setText("Введіть кількість!");
                return;
            }
            if(!productsRepository.checkProductAvailability(product.getId(), fieldCount)) {
                infoItemLabel.setText("Нажаль, такої кількісті немає.");
                return;
            }
            infoItemLabel.setTextFill(Paint.valueOf("BLACK"));
            infoItemLabel.setText("Успішно!");

            product.setCount(oldCount-fieldCount);
            countItemLabel.setText("Кількість: " + product.getCount());
            orderPrice = fieldCount*product.getPrice();

            for (OrderItem orderItem : orderItems) {
                if(product.getId() == orderItem.getProduct().getId()){
                    itemController.setData(product);
                    toStackOrderElements(itemController, orderItem, fieldCount, orderPrice);
                    return;
                }
            }

            addProductToOrder(new Product(product.getId(), product.getName(), fieldCount, orderPrice), itemController, product);
        });

        backButton.setOnAction(event -> {
            infoItemLabel.setText("");
            panelItemCard.setVisible(false);
            backgroundForCard.setVisible(false);
        });
    }

    public void addProductToOrder(Product orderProduct, ItemController itemController, Product oldProduct){ // Додаємо товар до "кошика"

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Main/itemOrder.fxml"));
            AnchorPane anchorPane = loader.load();
            itemOrderController = loader.getController();
            orderItems.add(new OrderItem(itemOrderController, orderProduct, anchorPane));


            System.out.println(oldProduct.getName() + " " + oldProduct.getCount());
            for (Product product: containerForProducts) {
                if(product.getId() == oldProduct.getId()) {
                    product.setCount(oldProduct.getCount());
                    System.out.println(product.getName() + " " + product.getCount());
                }
            }
            for (Product product: filterProducts) {
                if(product.getId() == oldProduct.getId()) {
                    product.setCount(oldProduct.getCount());
                    System.out.println(product.getName() + " " + product.getCount());
                }
            }
            changeProducts.add(oldProduct); // Товар, який зміниться після оформлення заказу
            itemController.setData(oldProduct); // Сетаєм дані для картки товару в мейн ролл пейні

            itemOrderController.setData(orderProduct, itemController); // Сетаєм дані для картки товару в ордер ролл пейні

            itemOrderController.addButtonListener(this);

            orderGridPane.add(anchorPane, 0, countRow++);
            orderGridPane.setMargin(anchorPane, new Insets(1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void toStackOrderElements(ItemController ic, OrderItem orderItem, double fieldCount, double newPrice){
        orderItem.getProduct().setCount(orderItem.getProduct().getCount()+fieldCount);
        orderItem.getProduct().setPrice(orderItem.getProduct().getPrice()+newPrice);
        orderItem.getItemOrderController().setData(orderItem.getProduct(), ic);
    }

    public void removeFromOrder(Product product, ItemOrderController ioc, ItemController ic){
        Iterator<Product> iterator = changeProducts.iterator();

        while (iterator.hasNext()) {
            Product changeProduct = iterator.next();
            if (changeProduct.getId() == product.getId()) {
                product.setCount(product.getCount()+changeProduct.getCount());
                ic.setCount(product.getCount());
                iterator.remove();
            }
        }

        Iterator<OrderItem> orderIterator = orderItems.iterator();

        while (orderIterator.hasNext()) {
            OrderItem orderItem = orderIterator.next();
            if (orderItem.getItemOrderController().equals(ioc)) {
                orderGridPane.getChildren().remove(orderItem.getAnchorPane());
                orderIterator.remove();
            }
        }
    }

    public void confirmOrder(){
        checkoutButton.setOnAction(event -> {
            confirmPanel.setVisible(true);
            backgroundForCard.setVisible(true);
            double sum = 0;
            for (OrderItem orderItem: orderItems) {
                sum += orderItem.getProduct().getPrice();
            }
            confirmSumLabel.setText(String.valueOf(sum));
            confirmButton.setOnAction(confirmEvent->{
                ordersRepository.buy(user.getId(), orderItems);
                productsRepository.update(changeProducts);
                pushMyOrders();
                orderItems.clear();
                orderGridPane.getChildren().clear();
                confirmPanel.setVisible(false);
                backgroundForCard.setVisible(false);
            });
            confirmBackButton.setOnAction(backEvent -> {
                confirmPanel.setVisible(false);
                backgroundForCard.setVisible(false);
            });
        });
    }

    public void checkMyOrders(){
        myOrderButton.setOnAction(event -> {
            myOrdersPanel.setVisible(true);
            backgroundForCard.setVisible(true);
        });
        myOrdersBackButton.setOnAction(event -> {
            myOrdersPanel.setVisible(false);
            backgroundForCard.setVisible(false);
        });
    }

    @Override
    public void onButtonClicked(Product product, ItemController itemController) {
        panelItemCard.setVisible(true);
        backgroundForCard.setVisible(true);
        openItemCard(product, itemController);
    }

    @Override
    public void onButtonClicked(Product product, ItemOrderController ioc, ItemController ic) {
        removeFromOrder(product, ioc, ic);
    }

    @Override
    public void onButtonClicked(MyOrderItemController myOrderItemController, Order order) {
        myProductOrdersPanel.setVisible(true);
        ArrayList <Product> products = productsRepository.getProductFromOrder(user.getId(), order.getNumber());
        pushMyProductOrders(products);
    }
}
