package com.databases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:src/main/java/com/databases/document.db";

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
            System.out.println("Kết nối thành công tới SQLite database");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
}