package com.librarymanagement.system.librarymanagementsystem;

import com.librarymanagement.system.librarymanagementsystem.controller.LoginController;
import com.librarymanagement.system.librarymanagementsystem.controller.MainController;
import com.librarymanagement.system.librarymanagementsystem.controller.RegisterController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    private Stage primaryStage;
    private MainController mainController;

    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
    @Override
    public void start(Stage stage) throws IOException {
        this.primaryStage = stage;
        showLoginView();
    }

    public void showLoginView() throws IOException {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/librarymanagement/system/librarymanagementsystem/login-view.fxml"));
            Scene scene = new Scene(loader.load(),600,400);
            primaryStage.setTitle("Library Management System - Login");
            primaryStage.setScene(scene);
            primaryStage.show();

            LoginController loginController = loader.getController();
            loginController.setMainApp(this);
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void showMainView() throws IOException {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/librarymanagement/system/librarymanagementsystem/MainView.fxml"));
            Scene scene = new Scene(loader.load(),852,560);
            primaryStage.setTitle("Library Management System - Dashboard");
            primaryStage.setScene(scene);
            primaryStage.show();
            //I just added it here remember this loads the MainController
            MainController mainController = loader.getController();
            mainController.setMainApp(this);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    //Selorm remember, this is to communicate with the Register view
    public void showRegisterView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/librarymanagement/system/librarymanagementsystem/Register.fxml"));
            AnchorPane registerView = loader.load();
            Scene scene = new Scene(registerView);
            primaryStage.setScene(scene);

            RegisterController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    //This will be used to open other UI interfaces
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
