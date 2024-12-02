package library.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import library.util.DBConnection;

public class NotiDAO {
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

}