package com.librarymanagement.system.librarymanagementsystem.controller;

import com.librarymanagement.system.librarymanagementsystem.MainApp;
import com.librarymanagement.system.librarymanagementsystem.model.User;
import com.librarymanagement.system.librarymanagementsystem.util.LoginService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button clearButton;

    @FXML
    private Button registerButton;

    private MainApp mainApp;
    private LoginService loginService = new LoginService();

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {
        loginButton.setOnAction(event -> handleLogin());
        clearButton.setOnAction(event -> handleClear());
        registerButton.setOnAction(event -> handleRegister());
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User authenticatedUser = loginService.authenticateUser(username, password);
        if (authenticatedUser != null) {
            showAlert("Login Successful!!", "User authenticated: Welcome, " + authenticatedUser.getUsername()+"!");
            try {
                mainApp.showMainView();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            showAlert("Login Failed", "Authentication failed");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleClear() {
        usernameField.clear();
        passwordField.clear();
    }

    @FXML
    private void handleRegister() {
        mainApp.showRegisterView();
    }
}
