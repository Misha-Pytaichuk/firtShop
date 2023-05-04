package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Entity.LoginUser;
import Entity.User;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sql.MySQLUserRepository;

public class RegistrationController extends LoginController{
    LoginUser loginUser = new LoginUser(new MySQLUserRepository());
    User user;
    private Stage stage;


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label infoLabel;

    @FXML
    private AnchorPane regPanel;

    @FXML
    private TextField numberField;

    @FXML
    private Button backButton;

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField loginField;

    @FXML
    private Button regButton;

    @FXML
    void initialize() {
        backToLoginWindow();
        registration();
    }

    private void backToLoginWindow(){
        backButton.setOnAction(actionEvent -> {

            try {
                new SceneController().switchWindow(actionEvent, "/Main/loginPanel.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    private void registration(){
        regButton.setOnAction(actionEvent -> {
            infoLabel.setAlignment(Pos.CENTER);
            String login = String.valueOf(loginField.getCharacters());
            String password = String.valueOf(passwordField.getCharacters());
            String name = String.valueOf(nameField.getCharacters());
            String lastName = String.valueOf(lastNameField.getCharacters());
            String number = String.valueOf(numberField.getCharacters());

            if(login.isEmpty() || password.isEmpty() || name.isEmpty() || lastName.isEmpty() || number.isEmpty()){
                infoLabel.setText("Всі поля повинні бути заповнені!");
                return;
            } else {
                user = loginUser.signUp(login, password, name, lastName, number);
            }

            if(user == null){
                infoLabel.setText("Такий користувач вже є!");
                return;
            }

            try {
                regButton.getScene().getWindow().hide();
                new SceneController().switchToShopPanel(actionEvent, user);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}


