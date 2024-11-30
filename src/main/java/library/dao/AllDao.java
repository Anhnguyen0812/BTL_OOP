package library.dao;

import java.sql.Connection;

public class AllDao {
    private static Connection connection;

    // public AllDao() {
    //     this.connection = DBConnection.getInstance().getConnection();
    // }

    // public int getTotalBooks() {
    //     int totalBooks = 0;
    //     try {
    //         Statement statement = connection.createStatement();
    //         ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM books");
    //         if (resultSet.next()) {
    //             totalBooks = resultSet.getInt(1);
    //         }
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    //     return totalBooks;
    // }

    // public int getTotalUsers() {
    //     int totalUsers = 0;
    //     try {
    //         Statement statement = connection.createStatement();
    //         ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM users");
    //         if (resultSet.next()) {
    //             totalUsers = resultSet.getInt(1);
    //         }
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    //     return totalUsers;
    // }

    // public int getTotalBorrowRecords() {
    //     int totalBorrowRecords = 0;
    //     try {
    //         Statement statement = connection.createStatement();
    //         ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM borrow_records");
    //         if (resultSet.next()) {
    //             totalBorrowRecords = resultSet.getInt(1);
    //         }
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    //     return totalBorrowRecords;
    // }

    // public static void main(String[] args) {
    //     AllDao allDao = new AllDao();
    //     System.out.println("Total books: " + allDao.getTotalBooks());
    //     System.out.println("Total users: " + allDao.getTotalUsers());
    //     System.out.println("Total borrow records: " + allDao.getTotalBorrowRecords());

    // }
}
