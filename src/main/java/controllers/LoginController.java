package controllers;

import Entity.LoginUser;
import Entity.User;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import sql.MySQLUserRepository;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginController {
    LoginUser loginUser = new LoginUser(new MySQLUserRepository());
    private User user;

    @FXML
    private AnchorPane panel;

    @FXML
    private Label infoLabel;

    @FXML
    private Button loginButton;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button regButton;

    private static final String LOGIN_USER = "LOGIN";
    private static final String REG_USER = "REGISTER";

    String regex = "^[a-zA-Z0-9]+$";
    Pattern pattern = Pattern.compile(regex);
    Matcher loginMatcher;
    Matcher passwordMather;

    @FXML
    void initialize() {
        loginButton.setOnAction(actionEvent -> {
            String login = String.valueOf(loginField.getCharacters());
            String password = String.valueOf(passwordField.getCharacters());

            loginMatcher = pattern.matcher(login);
            passwordMather = pattern.matcher(password);

            infoLabel.setAlignment(Pos.CENTER);

            if(!loginMatcher.matches() || !passwordMather.matches()){
                infoLabel.setText("Логін та пароль можуть містити тільки цифри або літери англійського алфавіта");
                return;
            } else if(login.isEmpty() || password.isEmpty()){
                infoLabel.setText("Всі поля повинні бути заповнені!");
                return;
            }
            else {
                user = loginUser.signIn(login, password);
            }


            if(user == null) {
                infoLabel.setText("Такого користувача немає!");
                return;
            }



            try {
                loginButton.getScene().getWindow().hide();
                if(!user.isRole())
                    new SceneController().switchToShopPanel(actionEvent, user);
                else
                    new SceneController().switchToAdminPanel(actionEvent, user);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        regButton.setOnAction(actionEvent -> {
            try {
                new SceneController().switchWindow(actionEvent, "/Main/regPanel.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


}
