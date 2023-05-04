package controllers;

import java.io.IOException;
import java.util.*;

import Entity.OrderItem;
import Entity.Product;
import Entity.User;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.util.Duration;
import sql.MySQLCategoriesRepository;
import sql.MySQLOrdersRepository;
import sql.MySQLProductsRepository;

public class OrderController implements ButtonListener{

    private User user;
    private ItemController itemController = new ItemController();
    private ItemOrderController itemOrderController = new ItemOrderController();
    private MySQLCategoriesRepository categoriesRepository = new MySQLCategoriesRepository();
    private MySQLProductsRepository productsRepository = new MySQLProductsRepository();

    private HashMap<String, Integer> categories = categoriesRepository.getCategory(); // Для категорій
    private Set<String> keysCategory = categories.keySet(); // Для категорій

    private ArrayList<Product> products = productsRepository.getProducts(); // Отримуємо всі товари
    private ArrayList<Product> filterProducts = products;
    private ArrayList<Product> changeProducts = new ArrayList<>();
    private ArrayList<OrderItem> orderItems = new ArrayList<>();

    private boolean isMenuHidden = true;
    private int countRow = 1;

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
    void initialize() {
        pushRadioButtonsToGroup();
        pushToScrollPanel(products);
        pushCategoriesToChoiceBox();
        pushToSortChoiceBox();
        choiceSortKind();
        setUserToRightPanel();
        choiceCategory();
        completeOrder();
    }

    public void pushRadioButtonsToGroup(){
        ToggleGroup group = new ToggleGroup();

        costRadioButton.setToggleGroup(group);
        nameRadioButton.setToggleGroup(group);
        hasRadioButton.setToggleGroup(group);
    }

    public void pushCategoriesToChoiceBox(){
        categoryChoiceBox.getItems().addAll(keysCategory);
        categoryChoiceBox.getItems().add("Всі");
    }

    public void choiceCategory(){
        categoryChoiceBox.setOnAction(event -> {
            gridPane.getChildren().clear();
            if(categoryChoiceBox.getValue().equals("Всі")) {
                filterProducts = products;
                pushToScrollPanel(filterProducts);
            }
            for (String str:keysCategory) {
                if(categoryChoiceBox.getValue().equals(str))
                    filterProducts = productsRepository.getProductsByCategory(categories.get(str));
                    pushToScrollPanel(filterProducts);
            }
        });
    }

    public void pushToSortChoiceBox(){
        sortChoiceBox.getItems().add("за зростанням");
        sortChoiceBox.getItems().add("за спаданням");
        sortChoiceBox.getItems().add("Без сортування");
    }

    public void choiceSortKind(){
        sortChoiceBox.setOnAction(event -> {
            gridPane.getChildren().clear();
            if(sortChoiceBox.getValue().equals("за зростанням")){

            }
            if(sortChoiceBox.getValue().equals("за спаданням")){
                pushToScrollPanel(productsRepository.getProductDESC());
            }
            if(sortChoiceBox.getValue().equals("Без сортування")){

                pushToScrollPanel(products);
            }
        });
    }

    public void setUserToRightPanel(){
        userPanel.setVisible(true);
        user = SceneController.getUser();

        nameLabel.setText(user.getName() + " " + user.getLastName());
        numberLabel.setText(user.getNumber());
        idLabel.setText("ID: " + user.getId());

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
            if(fieldCount > oldCount) {
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

    public void completeOrder(){
        checkoutButton.setOnAction(event -> {
            new MySQLOrdersRepository().buy(user.getId(), orderItems);
            new MySQLProductsRepository().add(changeProducts);
            orderItems.clear();
            orderGridPane.getChildren().clear();
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
}
