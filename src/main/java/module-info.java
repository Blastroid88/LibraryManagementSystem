module com.librarymanagement.system.librarymanagementsystem {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.base;


    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.desktop;
    requires java.sql;
    requires mysql.connector.j;


    opens com.librarymanagement.system.librarymanagementsystem.model to javafx.base; // Open this package to javafx.base
    exports com.librarymanagement.system.librarymanagementsystem;
    opens com.librarymanagement.system.librarymanagementsystem to javafx.fxml;
    exports com.librarymanagement.system.librarymanagementsystem.controller;
    opens com.librarymanagement.system.librarymanagementsystem.controller to javafx.fxml;
}