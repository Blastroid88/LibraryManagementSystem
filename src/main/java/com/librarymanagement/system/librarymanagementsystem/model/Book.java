package com.librarymanagement.system.librarymanagementsystem.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDate;

public class Book {

    private final IntegerProperty id;
    private final StringProperty title;
    private final StringProperty author;
    private final StringProperty publisher;
    private final StringProperty isbn;
    private final ObjectProperty<LocalDate> publishedDate;
    private final StringProperty category;
    private final IntegerProperty quantity;

    public Book() {
        this.id = new SimpleIntegerProperty();
        this.title = new SimpleStringProperty();
        this.author = new SimpleStringProperty();
        this.publisher = new SimpleStringProperty();
        this.isbn = new SimpleStringProperty();
        this.publishedDate = new SimpleObjectProperty<>();
        this.category = new SimpleStringProperty();
        this.quantity = new SimpleIntegerProperty();
    }

    public Book(int id, String title, String author, String publisher, String isbn, LocalDate publishedDate, String category, int quantity) {
        this();
        setId(id);
        setTitle(title);
        setAuthor(author);
        setPublisher(publisher);
        setIsbn(isbn);
        setPublishedDate(publishedDate);
        setCategory(category);
        setQuantity(quantity);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public StringProperty titleProperty() {
        return title;
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public StringProperty authorProperty() {
        return author;
    }

    public String getAuthor() {
        return author.get();
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public StringProperty publisherProperty() {
        return publisher;
    }

    public String getPublisher() {
        return publisher.get();
    }

    public void setPublisher(String publisher) {
        this.publisher.set(publisher);
    }

    public StringProperty isbnProperty() {
        return isbn;
    }

    public String getIsbn() {
        return isbn.get();
    }

    public void setIsbn(String isbn) {
        this.isbn.set(isbn);
    }

    public ObjectProperty<LocalDate> publishedDateProperty() {
        return publishedDate;
    }

    public LocalDate getPublishedDate() {
        return publishedDate.get();
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate.set(publishedDate);
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public String getCategory() {
        return category.get();
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public int getQuantity() {
        return quantity.get();
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    @Override
    // Display the book's title in the ComboBox
    public String toString() {
        return title.get();
    }
}
