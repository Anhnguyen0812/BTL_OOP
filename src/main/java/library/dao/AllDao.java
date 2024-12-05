package library.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

import library.util.DBConnection;

public class AllDao implements DAO {
    private static Connection connection;

    public AllDao() {
        this.connection = DBConnection.getInstance().getConnection();
    }

    public int getTotalBooks() {
        int totalBooks = 0;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM books");
            if (resultSet.next()) {
                totalBooks = resultSet.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalBooks;
    }

    public int getTotalUsers() {
        int totalUsers = 0;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM users");
            if (resultSet.next()) {
                totalUsers = resultSet.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalUsers;
    }

    public int getTotalBorrowRecords() {
        int totalBorrowRecords = 0;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM borrow_records");
            if (resultSet.next()) {
                totalBorrowRecords = resultSet.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalBorrowRecords;
    }

    public int getTotalReturnedRecords() {
        int totalReturnedRecords = 0;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM borrow_records WHERE status = 3");
            if (resultSet.next()) {
                totalReturnedRecords = resultSet.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalReturnedRecords;
    }

    public int getTotalBorrowPendingRecords() {
        int totalBorrowPendingRecords = 0;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM borrow_records WHERE status = 1");
            if (resultSet.next()) {
                totalBorrowPendingRecords = resultSet.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalBorrowPendingRecords;
    }

    public int getTotalBorrowedRecords() {
        int totalBorrowedRecords = 0;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM borrow_records WHERE status = 2");
            if (resultSet.next()) {
                totalBorrowedRecords = resultSet.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalBorrowedRecords;
    }

    public int getTotalOverdueBooks() {
        int totalOverdueBooks = 0;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement
                    .executeQuery(
                            "SELECT COUNT(*) FROM borrow_records WHERE status = 1 AND return_date < CURRENT_DATE");
            if (resultSet.next()) {
                totalOverdueBooks = resultSet.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalOverdueBooks;
    }

    public static void main(String[] args) {
        AllDao allDao = new AllDao();
        System.out.println("Total books: " + allDao.getTotalBooks());
        System.out.println("Total users: " + allDao.getTotalUsers());
        System.out.println("Total borrow records: " + allDao.getTotalBorrowRecords());

    }

    public int getBooksBorrowedOnDate(LocalDate date) {
        int booksBorrowedOnDate = 0;
        try {
            Statement statement = connection.createStatement();
            String d = String.valueOf(Date.valueOf(date).getTime());
            ResultSet resultSet = statement
                    .executeQuery("SELECT COUNT(*) FROM borrow_records WHERE borrow_date = '" + d + "'");
            if (resultSet.next()) {
                booksBorrowedOnDate = resultSet.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return booksBorrowedOnDate;
    }

    public int getNewUsersOnDate(LocalDate date) {
        int newUsersOnDate = 0;
        try {
            Statement statement = connection.createStatement();
            String d = String.valueOf(Date.valueOf(date).getTime());
            // System.out.println(d);
            ResultSet resultSet = statement
                    .executeQuery("SELECT COUNT(*) FROM users WHERE created_at = '" + d + "'");
            if (resultSet.next()) {
                newUsersOnDate = resultSet.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newUsersOnDate;
    }
}
