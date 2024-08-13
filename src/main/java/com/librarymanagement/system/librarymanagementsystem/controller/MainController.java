package com.librarymanagement.system.librarymanagementsystem.controller;

import com.librarymanagement.system.librarymanagementsystem.MainApp;
import com.librarymanagement.system.librarymanagementsystem.model.Book;
import com.librarymanagement.system.librarymanagementsystem.model.Member;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.util.Optional;
import javafx.stage.Stage;

public class MainController {
    private MainApp mainApp;

    @FXML
    private Button manageBooksButton;
    @FXML
    private Button manageMembersButton;
    @FXML
    private Button manageLoansButton;
    @FXML
    private Button logoutButton;


    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {
        manageBooksButton.setOnAction(event -> handleManageBooks());
        manageMembersButton.setOnAction(event -> handleManageMembers());
        manageLoansButton.setOnAction(event -> handleManageLoans());
        logoutButton.setOnAction(event -> handleLogout());
    }


    @FXML
    private void handleManageBooks() {
        showInformationAlert("Manage Books", "Manage Books section clicked.");
        try {
            // Remember this loads the book view from the FXML file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("book.fxml"));
            AnchorPane bookView = loader.load();
            // Create a new Scene with the book view.
            Scene bookScene = new Scene(bookView, 1140, 560); // Set preferred size for the scene.
            // Set the new scene on the primary stage.
            Stage primaryStage = mainApp.getPrimaryStage();
            primaryStage.setScene(bookScene);
            primaryStage.setTitle("Library Management System - Manage Books");
            primaryStage.show();
            // Give the controller access to the main app.
            BookController controller = loader.getController();
            controller.setMainApp(mainApp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleManageMembers() {
        showInformationAlert("Manage Members", "Manage Members section clicked.");
        try {
            // Load the member view.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("member.fxml"));
            AnchorPane memberView = loader.load();
            // Create a new Scene with the member view.
            Scene memberScene = new Scene(memberView, 939, 570); // Set preferred size for the scene.
            // Set the new scene on the primary stage.
            Stage primaryStage = mainApp.getPrimaryStage();
            primaryStage.setScene(memberScene);
            primaryStage.setTitle("Library Management System - Manage Members");
            primaryStage.show();
            // Give the controller access to the main app.
            MemberController controller = loader.getController();
            controller.setMainController(mainApp); // Pass the MainController instance
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleManageLoans() {
        showInformationAlert("Issue/Return Books", "Issue/Return Books section clicked.");
        try {
            // Load the loan view.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("loan.fxml"));
            AnchorPane loanView = loader.load();
            // Create a new Scene with the loan view.
            Scene loanScene = new Scene(loanView, 1037, 510); // Set preferred size for the scene.
            // Set the new scene on the primary stage.
            Stage primaryStage = mainApp.getPrimaryStage();
            primaryStage.setScene(loanScene);
            primaryStage.setTitle("Library Management System - Issue/Return Books");
            primaryStage.show();
            // Give the controller access to the main app.
            LoanController controller = loader.getController();
            controller.setMainApp(mainApp); // Pass the MainController instance
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        // Create a confirmation alert.
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Logout");
        confirmAlert.setHeaderText("Are you sure you want to log out?");
        confirmAlert.setContentText("You will be returned to the login screen.");
        // Selorm, this shows how the alert and wait for the user's response.
        Optional<ButtonType> result = confirmAlert.showAndWait();
        // This will check if the user clicked OK or otherwise.
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // This proceeds with logout if the user confirmed.
            try {
                mainApp.showLoginView();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                showInformationAlert("Error", "Error loading login view.");
            }
        } else {
            // If the user did not confirm, do nothing and stay on the current screen.
            showInformationAlert("Logout Cancelled", "You remain logged in.");
        }
    }

    private void showInformationAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
