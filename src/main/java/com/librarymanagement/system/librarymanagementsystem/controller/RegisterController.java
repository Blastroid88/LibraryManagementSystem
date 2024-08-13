package com.librarymanagement.system.librarymanagementsystem.controller;

import com.librarymanagement.system.librarymanagementsystem.MainApp;
import com.librarymanagement.system.librarymanagementsystem.model.User;
import com.librarymanagement.system.librarymanagementsystem.util.LoginService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class RegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private TextField roleField;

    @FXML
    private Button registerButton;

    @FXML
    private Button cancelButton;

    private MainApp mainApp;
    private LoginService loginService = new LoginService();

    // This allows the controller to access the main controller
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {
        registerButton.setOnAction(event -> handleRegister());
        cancelButton.setOnAction(event -> handleCancel());
    }

    @FXML
    private void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String role = roleField.getText();

        if (!password.equals(confirmPassword)) {
            showAlert("Password Mismatch", "Passwords do not match.");
            return;
        }

        if (username.isEmpty() || password.isEmpty() || role.isEmpty()) {
            showAlert("Incomplete Data", "Please fill out all fields.");
            return;
        }

        User newUser = new User(0, username, password, role);
        boolean registrationSuccess = loginService.registerUser(newUser);
        if (registrationSuccess) {
            showAlert("Registration Successful", "User registered successfully.");
            //Selorm remember this redirect to the login view
            try {
                mainApp.showLoginView();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            showAlert("Registration Failed", "Failed to register user.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleCancel() {
        try {
            mainApp.showLoginView();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while switching views.");
        }
    }
}
