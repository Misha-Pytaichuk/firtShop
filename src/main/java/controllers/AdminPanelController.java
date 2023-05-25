package controllers;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import Entity.Order;
import Entity.OrderItem;
import Entity.Product;
import Entity.User;
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
import sql.MySQLCategoriesRepository;
import sql.MySQLOrdersRepository;
import sql.MySQLProductsRepository;
import sql.MySQLUserRepository;

public class AdminPanelController implements AdminButtonListener{

    private AdminItemController adminItemController = new AdminItemController();
    private AdminMyOrderItemController myOrderItemController = new AdminMyOrderItemController();
    private AdminMyProductOrderItemController myProductOrderItemController = new AdminMyProductOrderItemController();

    private MySQLUserRepository mySQLUserRepository = new MySQLUserRepository();
    private MySQLCategoriesRepository categoriesRepository = new MySQLCategoriesRepository();
    private MySQLProductsRepository productsRepository = new MySQLProductsRepository();
    private MySQLOrdersRepository ordersRepository = new MySQLOrdersRepository();

    private HashMap<String, Integer> categories = categoriesRepository.getCategory(); // Для категорій
    private Set<String> keysCategory = categories.keySet(); // Для категорій

    private ArrayList<Product> products = productsRepository.getProducts(); // Отримуємо всі товари
    private ArrayList<Product> filterProducts = products;
    private ArrayList<Product> changeProducts = new ArrayList<>();
    private ArrayList<OrderItem> orderItems = new ArrayList<>();
    private ArrayList<MyOrderItemController> myOrderItemControllers = new ArrayList<>();

    private boolean isMenuHidden = true;
    private int countRow = 1;
    private String radio;
    private int categoryID = 0;

    @FXML
    private Label categoryLaber;

    @FXML
    private AnchorPane addPanel;

    @FXML
    private Button searchButtonForField;

    @FXML
    private GridPane myOrderGridPane;

    @FXML
    private Button addBackButton;

    @FXML
    private AnchorPane myOrdersPanel;

    @FXML
    private RadioButton costRadioButton;

    @FXML
    private AnchorPane panelItemCard;

    @FXML
    private AnchorPane myProductOrdersPanel;

    @FXML
    private Button myOrderButton;

    @FXML
    private Button myOrdersBackButton;

    @FXML
    private GridPane myProductOrderGridPane;

    @FXML
    private ChoiceBox<String> categoryChoiceBox;

    @FXML
    private ChoiceBox<String> editCategoryChoiceBox;

    @FXML
    private Button dropFiltersButton;

    @FXML
    private GridPane gridPane;

    @FXML
    private RadioButton hasRadioButton;

    @FXML
    private Button searchButton;

    @FXML
    private Label sortLabel;

    @FXML
    private Pane searchPanel;

    @FXML
    private AnchorPane leftPanel;

    @FXML
    private Pane backgroundForRightPanel;

    @FXML
    private TextField addNameProductField;

    @FXML
    private Button addButton;

    @FXML
    private TextField countField;

    @FXML
    private Button confirmAddButton;

    @FXML
    private TextField addCostField;

    @FXML
    private TextField searchFiled;

    @FXML
    private Button backButton;

    @FXML
    private Label infoItemLabel;

    @FXML
    private TextField costField;

    @FXML
    private TextField nameField;

    @FXML
    private Pane backgroundForCard;

    @FXML
    private ChoiceBox<String> sortChoiceBox;

    @FXML
    private Button editButton;

    @FXML
    private TextField addCountField;

    @FXML
    private RadioButton nameRadioButton;

    @FXML
    private ChoiceBox<String> addCategoryChoiceBox;

    @FXML
    private Button myProductOrdersBackButton;

    @FXML
    private Label addLabel;

    @FXML
    private AnchorPane clientPanel;

    @FXML
    private Label clientOrderLabel;

    @FXML
    private Label numberOrderLabel;

    @FXML
    private Button refreshButton;

    @FXML
    private Button searchForDateButton;

    @FXML
    private DatePicker dateFromPicker;

    @FXML
    private DatePicker dateToPicker;

    @FXML
    void initialize() {
        pushInitItems();
        pushToScrollPanel(products);
        choiceSortKind();
        choiceCategory();
        checkMyOrders();
        searchProduct();
        dropFilters();
        openMyOrders();
        openAddCard();
        searchOrders();
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

        categoryChoiceBox.setValue("Всі");

        addCategoryChoiceBox.getItems().addAll(keysCategory);
        editCategoryChoiceBox.getItems().addAll(keysCategory);
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
                        for (Product c:changeProducts) {
                            if(product.getId() == c.getId()) product.setCount(c.getCount());
                        }
                        list.add(product);
                    }
                    pushToScrollPanel(list);
                    break;
                case "desc":
                    for (Product product: productsRepository.getProductDESC(radio, categoryID)) {
                        for (Product c:changeProducts) {
                            if(product.getId() == c.getId()) product.setCount(c.getCount());
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
                filterProducts = products;
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

            pushToScrollPanel(products);
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

    public void openMyOrders(){
        myOrderButton.setOnAction(event -> {
            pushMyOrders(ordersRepository.getOrders());
            myOrdersPanel.setVisible(true);
            backgroundForCard.setVisible(true);
        });
        myOrdersBackButton.setOnAction(event -> {
            myOrdersPanel.setVisible(false);
            backgroundForCard.setVisible(false);
        });
    }

    public void searchOrders(){
        searchForDateButton.setOnAction(event -> {

            LocalDate localDateFrom = dateFromPicker.getValue();
            LocalDate localDateTo = dateToPicker.getValue();

            LocalDate nextDateTo = localDateTo.plusDays(1);

            Date dateFrom = Date.valueOf(localDateFrom);
            Date dateTo = Date.valueOf(nextDateTo);

            pushMyOrders(ordersRepository.getOrdersByDate(dateFrom, dateTo));

        });
        refreshButton.setOnAction(event -> {
            dateFromPicker.setValue(null);
            dateToPicker.setValue(null);
            pushMyOrders(ordersRepository.getOrders());
        });
    }

    public void pushMyOrders(ArrayList<Order> orders) {
        myOrderGridPane.getChildren().clear();

        try {
            for (Order order: orders) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/Main/adminMyOrderItem.fxml"));
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
                loader.setLocation(getClass().getResource("/Main/adminMyProductOrderItem.fxml"));
                AnchorPane anchorPane = loader.load();
                myProductOrderItemController = loader.getController();

                myProductOrderItemController.setData(product);

                myProductOrderGridPane.add(anchorPane, 0, countRow++);
                myProductOrderGridPane.setMargin(anchorPane, new Insets(1));
            }

            myProductOrdersBackButton.setOnAction(event -> {
                myProductOrderGridPane.getChildren().clear();
                myProductOrdersPanel.setVisible(false);
                clientPanel.setVisible(false);
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
                loader.setLocation(getClass().getResource("/Main/adminItem.fxml"));
                AnchorPane anchorPane = loader.load();

                adminItemController = loader.getController();
                adminItemController.setData(product);
                adminItemController.addButtonListener(this);

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

    public void openItemCard(Product product){ // відкриваєемо повну версію картки товару
        countField.clear();

        nameField.setText(product.getName());
        countField.setText(String.valueOf(product.getCount()));
        costField.setText(String.valueOf(product.getPrice()));
        editCategoryChoiceBox.setValue(categoriesRepository.getCategory(product.getCategory()));
        editButton.setOnAction(event -> {
            product.setName(nameField.getText());
            product.setPrice(Double.parseDouble(costField.getText()));
            product.setCount(Double.parseDouble(countField.getText()));

            for (String str:keysCategory) {
                if(editCategoryChoiceBox.getValue().equals(str)){
                    categoryID = categories.get(str);
                }
            }
            product.setCategory(categoryID);
            if(productsRepository.update(product)){
                infoItemLabel.setText("Успіх");
                products = productsRepository.getProducts();
                gridPane.getChildren().clear();
                pushToScrollPanel(products);
            } else {
                infoItemLabel.setText("Поля повинні бути більше 0");
            }
        });
        backButton.setOnAction(event -> {
            infoItemLabel.setText("");
            panelItemCard.setVisible(false);
            backgroundForCard.setVisible(false);
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

    public void openAddCard(){
        addButton.setOnAction(event -> {
            backgroundForCard.setVisible(true);
            addPanel.setVisible(true);
            addProduct();
        });
        addBackButton.setOnAction(event -> {
            backgroundForCard.setVisible(false);
            addPanel.setVisible(false);
            addLabel.setVisible(false);
        });
    }

    public void addProduct(){
        addCategoryChoiceBox.setValue("М'ясо");
        confirmAddButton.setOnAction(event -> {
            String name = addNameProductField.getText();
            double count = Double.parseDouble(addCountField.getText());
            double price = Double.parseDouble(addCostField.getText());
            int category = 0;
            for (String str:keysCategory) {
                if(addCategoryChoiceBox.getValue().equals(str)){
                    category = categories.get(str);
                }
            }

            addLabel.setVisible(true);

            productsRepository.saveToBD(new Product(name, count, price, category));

            products = productsRepository.getProducts();
            gridPane.getChildren().clear();
            pushToScrollPanel(products);
        });
    }

    public void removeProduct(Product product){
        productsRepository.delete(product);
        products = productsRepository.getProducts();
        gridPane.getChildren().clear();
        pushToScrollPanel(products);
    }

    @Override
    public void onButtonClicked(Product product, AdminItemController itemController) {
        panelItemCard.setVisible(true);
        backgroundForCard.setVisible(true);
        openItemCard(product);
    }

    @Override
    public void onRemoveButtonClicked(Product product, AdminItemController itemController) {
        removeProduct(product);
    }

    @Override
    public void onButtonClicked(AdminMyOrderItemController myOrderItemController, Order order) {
        myProductOrdersPanel.setVisible(true);
        clientPanel.setVisible(true);
        User user = mySQLUserRepository.getUser(order.getUserId());
        clientOrderLabel.setText(user.getName() + " " + user.getLastName());
        numberOrderLabel.setText(user.getNumber());
        ArrayList <Product> products = productsRepository.getProductFromOrder(order.getUserId(), order.getNumber());
        pushMyProductOrders(products);
    }
}
