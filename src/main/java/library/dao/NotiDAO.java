package library.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import library.model.Book;
import library.model.ConcreteBook;
import library.util.DBConnection;

public class NotiDAO implements DAO {
    private final Connection connection;
    private static NotiDAO instance;

    private NotiDAO() {
        this.connection = DBConnection.getInstance().getConnection();
    }

    public static NotiDAO geNotiDAO() {
        if (instance == null) {
            instance = new NotiDAO();
        }
        return instance;
    }

    public List<String> getNotificationsFromAdminToUser(int userId) throws SQLException {
        List<String> notifications = new ArrayList<>();
        String query = "SELECT message FROM noti WHERE users_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                notifications.add(resultSet.getString("message"));
            }
        }
        return notifications;
    }

    public List<String> getNotificationsFromUserToAdmin(int adminId) throws SQLException {
        List<String> notifications = new ArrayList<>();
        String query = "SELECT message FROM noti WHERE users_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, adminId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                notifications.add(resultSet.getString("message"));
            }
        }
        return notifications;
    }

    public void addNotificationFromAdminToUser(int userId, String message) throws SQLException {
        String query = "INSERT INTO noti (users_id, message, is_read, created_at) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setString(2, message);
            statement.setBoolean(3, false);
            statement.setDate(4, new java.sql.Date(System.currentTimeMillis()));
            statement.executeUpdate();
        }
    }

    public void addNotificationFromUserToAdmin(int adminId, String message) throws SQLException {
        String query = "INSERT INTO noti (users_id, message, is_read, created_at) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, adminId);
            statement.setString(2, message);
            statement.setBoolean(3, false);
            statement.setDate(4, new java.sql.Date(System.currentTimeMillis()));
            statement.executeUpdate();
        }
    }

    public void deleteNoti(String message) throws SQLException {
        String query = "DELETE FROM noti WHERE message = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, message);
            statement.executeUpdate();
        }
    }

    public boolean isUserHasNotification(int id) {
        String query = "SELECT * FROM noti WHERE users_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
