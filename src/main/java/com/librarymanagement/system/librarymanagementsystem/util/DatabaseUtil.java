package com.librarymanagement.system.librarymanagementsystem.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {

    private static DatabaseUtil instance;
    private static Connection connection;

    // Private constructor to prevent instantiation
    private DatabaseUtil() {
        // Initialize the connection only when the class is loaded
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_library_management", "root", "Blastroid@1");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace(); // Ideally, use a logging framework
        }
    }

    // Singleton pattern to get the instance of DatabaseUtil
    public static synchronized DatabaseUtil getInstance() {
        if (instance == null) {
            instance = new DatabaseUtil();
        }
        return instance;
    }

    // Returns a connection to the database
    public Connection getConnection() {
        if (connection == null) {
            throw new IllegalStateException("Connection is not initialized.");
        }
        try {
            if (connection.isClosed()) {
                // Recreate the connection if it is closed
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_library_management", "root", "Blastroid@1");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    // Selorm, Close the database connection
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                connection = null; // Ensure the connection is set to null after closing
            }
        }
    }
}
