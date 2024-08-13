package com.librarymanagement.system.librarymanagementsystem.controller;

import com.librarymanagement.system.librarymanagementsystem.MainApp;
import com.librarymanagement.system.librarymanagementsystem.model.Book;
import com.librarymanagement.system.librarymanagementsystem.util.DatabaseUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class BookController {

    @FXML
    private TableView<Book> bookTable;
    @FXML
    private TableColumn<Book, Integer> idColumn;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, String> publisherColumn;
    @FXML
    private TableColumn<Book, String> isbnColumn;
    @FXML
    private TableColumn<Book, LocalDate> publishedDateColumn;
    @FXML
    private TableColumn<Book, String> categoryColumn;
    @FXML
    private TableColumn<Book, Integer> quantityColumn;

    @FXML
    private TextField titleField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField publisherField;
    @FXML
    private TextField isbnField;
    @FXML
    private DatePicker publishedDateField;
    @FXML
    private TextField categoryField;
    @FXML
    private TextField quantityField;
    @FXML
    private Button backButton;

    private MainApp mainApp;
    private ObservableList<Book> bookData = FXCollections.observableArrayList();

    public BookController() {
    }

    @FXML
    private void initialize() {
        backButton.setOnAction(event -> handleBack());
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        publishedDateColumn.setCellValueFactory(new PropertyValueFactory<>("publishedDate"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        loadBookData();
        bookTable.setItems(bookData);
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    private void handleBack() {
        try {
            mainApp.showMainView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadBookData() {
        bookData.clear(); // Clear existing data to avoid duplication
        try (Connection conn = DatabaseUtil.getInstance().getConnection()) {
            String query = "SELECT * FROM book";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                bookData.add(new Book(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("publisher"),
                        resultSet.getString("isbn"),
                        resultSet.getDate("publishedDate").toLocalDate(),
                        resultSet.getString("category"),
                        resultSet.getInt("quantity")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddBook() {
        String title = titleField.getText();
        String author = authorField.getText();
        String publisher = publisherField.getText();
        String isbn = isbnField.getText();
        LocalDate publishedDate = publishedDateField.getValue();
        String category = categoryField.getText();
        int quantity;

        // Validate and parse quantity
        try {
            quantity = Integer.parseInt(quantityField.getText());
        } catch (NumberFormatException e) {
            showErrorAlert("Invalid Input", "Quantity Error", "Please enter a valid quantity.");
            return;
        }

        if (publishedDate == null) {
            showErrorAlert("Invalid Date", "Date Error", "Please select a valid published date.");
            return;
        }

        try (Connection conn = DatabaseUtil.getInstance().getConnection()) {
            // Query to get the highest existing book ID
            String getMaxIdQuery = "SELECT MAX(id) FROM book";
            PreparedStatement getMaxIdStmt = conn.prepareStatement(getMaxIdQuery);
            ResultSet rs = getMaxIdStmt.executeQuery();

            int nextId = 1; // Default to 1 if no records exist
            if (rs.next()) {
                int maxId = rs.getInt(1);
                if (!rs.wasNull()) {
                    nextId = maxId + 1;
                }
            }
            // Query to insert a new book with the calculated next ID
            String query = "INSERT INTO book (id, title, author, publisher, isbn, publishedDate, category, quantity) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, nextId);
            statement.setString(2, title);
            statement.setString(3, author);
            statement.setString(4, publisher);
            statement.setString(5, isbn);
            statement.setDate(6, Date.valueOf(publishedDate)); // Converts LocalDate to java.sql.Date
            statement.setString(7, category);
            statement.setInt(8, quantity);
            statement.executeUpdate();

            loadBookData();
            bookTable.refresh();
            clearBookFields();
            // Show success message
            showInformationAlert("Success", "Book Saved", "The book has been successfully added to the database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void handleEditBook() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            // Update the book object with values from text fields
            selectedBook.setTitle(titleField.getText());
            selectedBook.setAuthor(authorField.getText());
            selectedBook.setPublisher(publisherField.getText());
            selectedBook.setIsbn(isbnField.getText());
            selectedBook.setPublishedDate(publishedDateField.getValue());
            selectedBook.setCategory(categoryField.getText());
            selectedBook.setQuantity(Integer.parseInt(quantityField.getText()));

            // Selorm this will update the database
            try (Connection conn = DatabaseUtil.getInstance().getConnection()) {
                String query = "UPDATE book SET title = ?, author = ?, publisher = ?, isbn = ?, publishedDate = ?, category = ?, quantity = ? WHERE id = ?";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setString(1, selectedBook.getTitle());
                statement.setString(2, selectedBook.getAuthor());
                statement.setString(3, selectedBook.getPublisher());
                statement.setString(4, selectedBook.getIsbn());
                statement.setDate(5, Date.valueOf(selectedBook.getPublishedDate()));
                statement.setString(6, selectedBook.getCategory());
                statement.setInt(7, selectedBook.getQuantity());
                statement.setInt(8, selectedBook.getId());
                statement.executeUpdate();

                // This for the refresh of the table and clear fields
                loadBookData();
                bookTable.refresh();
                clearBookFields();
            } catch (SQLException e) {
                e.printStackTrace();
                showErrorAlert("Database Error", "Error updating book", "An error occurred while updating the book in the database.");
            }
        } else {
            showErrorAlert("No Selection", "No Book Selected", "Please select a book in the table.");
        }
    }

    @FXML
    private void handleDeleteBook() {
        int selectedIndex = bookTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Book selectedBook = bookTable.getSelectionModel().getSelectedItem();

            // This will show a confirmation alert whether to delete or not
            Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Deletion");
            confirmAlert.setHeaderText("Are you sure you want to delete this book?");
            confirmAlert.setContentText("This action cannot be undone.");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Selorm, if user clicked OK, proceed with deletion
                try (Connection conn = DatabaseUtil.getInstance().getConnection()) {
                    String query = "DELETE FROM book WHERE id = ?";
                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.setInt(1, selectedBook.getId());
                    statement.executeUpdate();

                    bookTable.getItems().remove(selectedIndex);
                    bookTable.refresh();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            showErrorAlert("No Selection", "No Book Selected", "Please select a book in the table.");
        }
    }


    private void clearBookFields() {
        titleField.clear();
        authorField.clear();
        publisherField.clear();
        isbnField.clear();
        publishedDateField.setValue(null);
        categoryField.clear();
        quantityField.clear();
    }

    private void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    //Selorm, this will handle messages
    private void showInformationAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
