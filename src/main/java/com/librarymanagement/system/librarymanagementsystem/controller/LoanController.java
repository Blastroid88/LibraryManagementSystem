package com.librarymanagement.system.librarymanagementsystem.controller;

import com.librarymanagement.system.librarymanagementsystem.model.Loan;
import com.librarymanagement.system.librarymanagementsystem.model.Book;
import com.librarymanagement.system.librarymanagementsystem.model.Member;
import com.librarymanagement.system.librarymanagementsystem.MainApp;
import com.librarymanagement.system.librarymanagementsystem.util.DatabaseUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class LoanController {

    @FXML
    private TableView<Loan> loanTable;
    @FXML
    private TableColumn<Loan, Integer> idColumn;
    @FXML
    private TableColumn<Loan, String> bookNameColumn;
    @FXML
    private TableColumn<Loan, String> memberNameColumn;
    @FXML
    private TableColumn<Loan, LocalDate> issueDateColumn;
    @FXML
    private TableColumn<Loan, LocalDate> dueDateColumn;
    @FXML
    private TableColumn<Loan, LocalDate> returnDateColumn;
    @FXML
    private TableColumn<Loan, String> statusColumn;

    @FXML
    private ComboBox<Book> bookComboBox;
    @FXML
    private ComboBox<Member> memberComboBox;
    @FXML
    private DatePicker issueDatePicker;
    @FXML
    private DatePicker dueDatePicker;
    @FXML
    private DatePicker returnDatePicker;
    @FXML
    private ComboBox<String> statusComboBox;
    @FXML
    private Button backButton2;

    private ObservableList<Loan> loanData = FXCollections.observableArrayList();
    private ObservableList<Book> books = FXCollections.observableArrayList();
    private ObservableList<Member> members = FXCollections.observableArrayList();

    private MainApp mainApp;

    public LoanController() {
    }

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        bookNameColumn.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        memberNameColumn.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        issueDateColumn.setCellValueFactory(new PropertyValueFactory<>("issueDate"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        statusComboBox.getItems().addAll("Issued", "Returned");

        backButton2.setOnAction(event -> handleBack2());
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        loadBookData();
        loadMemberData();
        loadLoanData(); // Load loan data from database
    }

    @FXML
    private void handleBack2() {
        try {
            mainApp.showMainView();
        } catch (IOException e) {
            showErrorAlert("Navigation Error", "Error navigating back to the main view.");
        }
    }

    private void loadBookData() {
        books.clear();
        books.addAll(getBookDataFromDatabase());
        bookComboBox.setItems(books);
    }

    private void loadMemberData() {
        members.clear();
        members.addAll(getMemberDataFromDatabase());
        memberComboBox.setItems(members);
    }

    private ObservableList<Book> getBookDataFromDatabase() {
        ObservableList<Book> bookData = FXCollections.observableArrayList();
        Connection connection = DatabaseUtil.getInstance().getConnection();
        try {
            String query = "SELECT id, title FROM book";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getInt("id"));
                book.setTitle(resultSet.getString("title"));
                bookData.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Database Error", "An error occurred while retrieving book data.");
        }
        return bookData;
    }

    private ObservableList<Member> getMemberDataFromDatabase() {
        ObservableList<Member> memberData = FXCollections.observableArrayList();
        Connection connection = DatabaseUtil.getInstance().getConnection();
        try {
            String query = "SELECT id, name FROM member";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Member member = new Member();
                member.setId(resultSet.getInt("id"));
                member.setName(resultSet.getString("name"));
                memberData.add(member);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Database Error", "An error occurred while retrieving member data.");
        }
        return memberData;
    }

    private void loadLoanData() {
        Connection connection = DatabaseUtil.getInstance().getConnection();
        try {
            String query = "SELECT * FROM loan";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Loan loan = new Loan(
                        resultSet.getInt("id"),
                        resultSet.getString("book_name"),
                        resultSet.getString("member_name"),
                        resultSet.getDate("issue_date").toLocalDate(),
                        resultSet.getDate("due_date").toLocalDate(),
                        resultSet.getDate("return_date") != null ? resultSet.getDate("return_date").toLocalDate() : null,
                        resultSet.getString("status")
                );
                loanData.add(loan);
            }
            loanTable.setItems(loanData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddLoan() {
        try {
            Book selectedBook = bookComboBox.getSelectionModel().getSelectedItem();
            Member selectedMember = memberComboBox.getSelectionModel().getSelectedItem();
            LocalDate issueDate = issueDatePicker.getValue();
            LocalDate dueDate = dueDatePicker.getValue();
            LocalDate returnDate = returnDatePicker.getValue();
            String status = statusComboBox.getValue();

            if (selectedBook == null || selectedMember == null || issueDate == null || dueDate == null || status == null) {
                showErrorAlert("Input Error", "Please provide all required fields.");
                return;
            }

            Connection connection = DatabaseUtil.getInstance().getConnection();

            // Get the next ID
            String getMaxIdQuery = "SELECT MAX(id) FROM loan";
            PreparedStatement getMaxIdStmt = connection.prepareStatement(getMaxIdQuery);
            ResultSet rs = getMaxIdStmt.executeQuery();
            int nextId = 1; // Default to 1 if no records exist
            if (rs.next()) {
                int maxId = rs.getInt(1);
                if (!rs.wasNull()) {
                    nextId = maxId + 1;
                }
            }

            // Insert the new loan record
            String query = "INSERT INTO loan (id, book_name, member_name, issue_date, due_date, return_date, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, nextId);
            statement.setString(2, selectedBook.getTitle());
            statement.setString(3, selectedMember.getName());
            statement.setDate(4, java.sql.Date.valueOf(issueDate));
            statement.setDate(5, java.sql.Date.valueOf(dueDate));
            statement.setDate(6, returnDate != null ? java.sql.Date.valueOf(returnDate) : null);
            statement.setString(7, status);
            statement.executeUpdate();

            Loan newLoan = new Loan(
                    nextId,  // Use the new ID
                    selectedBook.getTitle(),
                    selectedMember.getName(),
                    issueDate,
                    dueDate,
                    returnDate,
                    status
            );
            loanData.add(newLoan);
            clearFields();
        } catch (SQLException e) {
            showErrorAlert("Error", "An error occurred while adding the loan.");
            e.printStackTrace();
        }
    }


    @FXML
    private void handleEditLoan() {
        Loan selectedLoan = loanTable.getSelectionModel().getSelectedItem();
        if (selectedLoan != null) {
            try {
                selectedLoan.setBookName(bookComboBox.getValue().getTitle());
                selectedLoan.setMemberName(memberComboBox.getValue().getName());
                selectedLoan.setIssueDate(issueDatePicker.getValue());
                selectedLoan.setDueDate(dueDatePicker.getValue());
                selectedLoan.setReturnDate(returnDatePicker.getValue());
                selectedLoan.setStatus(statusComboBox.getValue());
                loanTable.refresh();

                Connection connection = DatabaseUtil.getInstance().getConnection();
                String query = "UPDATE loan SET book_name = ?, member_name = ?, issue_date = ?, due_date = ?, return_date = ?, status = ? WHERE id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, selectedLoan.getBookName());
                statement.setString(2, selectedLoan.getMemberName());
                statement.setDate(3, java.sql.Date.valueOf(selectedLoan.getIssueDate()));
                statement.setDate(4, java.sql.Date.valueOf(selectedLoan.getDueDate()));
                statement.setDate(5, selectedLoan.getReturnDate() != null ? java.sql.Date.valueOf(selectedLoan.getReturnDate()) : null);
                statement.setString(6, selectedLoan.getStatus());
                statement.setInt(7, selectedLoan.getId());
                statement.executeUpdate();

                clearFields();
            } catch (SQLException e) {
                showErrorAlert("Error", "An error occurred while editing the loan.");
                e.printStackTrace();
            }
        } else {
            showErrorAlert("No Selection", "Please select a loan to edit.");
        }
    }

    @FXML
    private void handleDeleteLoan() {
        int selectedIndex = loanTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Loan loanToDelete = loanTable.getItems().get(selectedIndex);

            try {
                Connection connection = DatabaseUtil.getInstance().getConnection();
                String query = "DELETE FROM loan WHERE id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, loanToDelete.getId());
                statement.executeUpdate();

                loanTable.getItems().remove(selectedIndex);
            } catch (SQLException e) {
                showErrorAlert("Error", "An error occurred while deleting the loan.");
                e.printStackTrace();
            }
        } else {
            showErrorAlert("No Selection", "Please select a loan to delete.");
        }
    }

    private void clearFields() {
        bookComboBox.getSelectionModel().clearSelection();
        memberComboBox.getSelectionModel().clearSelection();
        issueDatePicker.setValue(null);
        dueDatePicker.setValue(null);
        returnDatePicker.setValue(null);
        statusComboBox.getSelectionModel().clearSelection();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
