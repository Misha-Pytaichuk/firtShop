package controllers;

import Entity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    public static User user;

    public void switchWindow(ActionEvent event, String path) throws IOException {
        root = FXMLLoader.load(getClass().getResource(path));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToShopPanel(ActionEvent event, User user) throws IOException {
        this.user = user;
        root = FXMLLoader.load(getClass().getResource("/Main/shopPanel.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1280, 720);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToAdminPanel(ActionEvent event, User user) throws IOException {
        this.user = user;
        root = FXMLLoader.load(getClass().getResource("/Main/adminPanel.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1280, 720);
        stage.setScene(scene);
        stage.show();
    }
    public static User getUser() {
        return user;
    }
}
