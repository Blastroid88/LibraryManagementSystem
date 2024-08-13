package com.librarymanagement.system.librarymanagementsystem.controller;

import com.librarymanagement.system.librarymanagementsystem.model.Member;
import com.librarymanagement.system.librarymanagementsystem.MainApp;
import com.librarymanagement.system.librarymanagementsystem.util.DatabaseUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class MemberController {

    @FXML
    private TableView<Member> memberTable;
    @FXML
    private TableColumn<Member, Integer> idColumn;
    @FXML
    private TableColumn<Member, String> nameColumn;
    @FXML
    private TableColumn<Member, String> emailColumn;
    @FXML
    private TableColumn<Member, String> phoneColumn;
    @FXML
    private TableColumn<Member, String> addressColumn;
    @FXML
    private TableColumn<Member, LocalDate> membershipDateColumn;
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField membershipDateField;
    @FXML
    private Button backButton3;

    private MainApp mainApp;
    private ObservableList<Member> memberData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Initialize table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        membershipDateColumn.setCellValueFactory(new PropertyValueFactory<>("membershipDate"));
        // Load member data
        loadMemberData();
        // Set up event handler for the back button
        backButton3.setOnAction(event -> handleBack());
    }

    public void setMainController(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    private void handleBack() {
        try {
            mainApp.showMainView();
        } catch (IOException e) {
            showErrorAlert("Navigation Error", "Error navigating back to the main view.");
        }
    }

    private void loadMemberData() {
        String query = "SELECT * FROM member";
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            memberData.clear(); // Clear existing data
            while (resultSet.next()) {
                Member member = new Member(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("address"),
                        resultSet.getDate("membership_date").toLocalDate()
                );
                memberData.add(member); // Add new member to the list
            }
            memberTable.setItems(memberData); // Set the list to the TableView
        } catch (SQLException e) {
            showErrorAlert("Database Error", "An error occurred while loading member data.");
        }
    }

    @FXML
    private void handleAddMember() {
        // Retrieve input values
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();
        LocalDate membershipDate;

        // Validate and parse membership date
        try {
            membershipDate = LocalDate.parse(membershipDateField.getText());
        } catch (Exception e) {
            showErrorAlert("Invalid Date", "Please enter a valid membership date.");
            return;
        }

        // Validate other fields
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            showErrorAlert("Input Error", "Please provide all required fields.");
            return;
        }

        try (Connection connection = DatabaseUtil.getInstance().getConnection()) {
            // Query to get the highest existing member ID
            String getMaxIdQuery = "SELECT MAX(id) FROM member";
            PreparedStatement getMaxIdStmt = connection.prepareStatement(getMaxIdQuery);
            ResultSet rs = getMaxIdStmt.executeQuery();

            int nextId = 1; // Default to 1 if no records exist
            if (rs.next()) {
                int maxId = rs.getInt(1);
                if (!rs.wasNull()) {
                    nextId = maxId + 1;
                }
            }

            // Query to insert a new member with the calculated next ID
            String query = "INSERT INTO member (id, name, email, phone, address, membership_date) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, nextId);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, phone);
            preparedStatement.setString(5, address);
            preparedStatement.setDate(6, java.sql.Date.valueOf(membershipDate));

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                loadMemberData(); // Reload member data to reflect changes
                clearFields();
                showInformationAlert("Success", "Member added successfully.");
            } else {
                showErrorAlert("Database Error", "An error occurred while adding the member.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Database Error", "An error occurred while adding the member.");
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Error", "An error occurred while adding the member.");
        }
    }



    @FXML
    private void handleEditMember() {
        Member selectedMember = memberTable.getSelectionModel().getSelectedItem();
        if (selectedMember != null) {
            try {
                // Retrieve updated values from input fields
                String name = nameField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();
                String address = addressField.getText();
                LocalDate membershipDate;

                // Validate fields
                if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                    showErrorAlert("Input Error", "Please provide all required fields.");
                    return;
                }
                try {
                    membershipDate = LocalDate.parse(membershipDateField.getText());
                } catch (Exception e) {
                    showErrorAlert("Invalid Date", "Please enter a valid membership date.");
                    return;
                }

                // Prepare SQL update query
                String query = "UPDATE member SET name = ?, email = ?, phone = ?, address = ?, membership_date = ? WHERE id = ?";
                try (Connection connection = DatabaseUtil.getInstance().getConnection();
                     PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                    // Set parameters for the prepared statement
                    preparedStatement.setString(1, name);
                    preparedStatement.setString(2, email);
                    preparedStatement.setString(3, phone);
                    preparedStatement.setString(4, address);
                    preparedStatement.setDate(5, java.sql.Date.valueOf(membershipDate));
                    preparedStatement.setInt(6, selectedMember.getId());

                    // Execute update query
                    int affectedRows = preparedStatement.executeUpdate();
                    if (affectedRows > 0) {
                        loadMemberData(); // Reload member data to reflect changes
                        clearFields(); // Clear input fields
                        showInformationAlert("Success", "Member details updated successfully.");
                    } else {
                        showErrorAlert("Database Error", "An error occurred while updating the member.");
                    }
                }
            } catch (SQLException e) {
                showErrorAlert("Database Error", "An error occurred while updating the member.");
                e.printStackTrace();
            } catch (Exception e) {
                showErrorAlert("Error", "An unexpected error occurred.");
                e.printStackTrace();
            }
        } else {
            showErrorAlert("No Selection", "Please select a member to edit.");
        }
    }


    @FXML
    private void handleDeleteMember() {
        int selectedIndex = memberTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            // Retrieve selected member
            Member selectedMember = memberTable.getItems().get(selectedIndex);

            // Confirm deletion
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirm Deletion");
            confirmationAlert.setHeaderText("Are you sure you want to delete this member?");
            confirmationAlert.setContentText("This action cannot be undone.");

            // Show confirmation dialog and wait for user response
            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Selorm, this proceeds with deletion
                String query = "DELETE FROM member WHERE id = ?";
                try (Connection connection = DatabaseUtil.getInstance().getConnection();
                     PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                    preparedStatement.setInt(1, selectedMember.getId());

                    int affectedRows = preparedStatement.executeUpdate();
                    if (affectedRows > 0) {
                        memberTable.getItems().remove(selectedIndex); // Remove member from TableView
                        showInformationAlert("Success", "Member deleted successfully.");
                    } else {
                        showErrorAlert("Database Error", "An error occurred while deleting the member.");
                    }
                } catch (SQLException e) {
                    showErrorAlert("Error", "An error occurred while deleting the member.");
                }
            } else {
                showInformationAlert("Cancellation", "Member deletion was cancelled.");
            }
        } else {
            showErrorAlert("No Selection", "Please select a member to delete.");
        }
    }

    private void clearFields() {
        nameField.clear();
        emailField.clear();
        phoneField.clear();
        addressField.clear();
        membershipDateField.clear();
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    //Selorm, this will handle messages
    private void showInformationAlert(String title, String header) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(alert.getContentText());
        alert.showAndWait();
    }
}
