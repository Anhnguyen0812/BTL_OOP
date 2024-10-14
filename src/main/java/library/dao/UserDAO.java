package library.dao;

import library.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import library.util.DBConnection;

public class UserDAO {
    private static Connection connection;
    private static final String tail = "bao";

    public UserDAO() {
        UserDAO.connection = DBConnection.getInstance().getConnection();
    }

    public UserDAO(Connection connection) {
        UserDAO.connection = connection;
    }

    public static String getSalt() throws NoSuchAlgorithmException {
        // Tạo ra salt ngẫu nhiên với SecureRandom
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        // Mã hóa salt thành chuỗi base64 để dễ lưu trữ
        return Base64.getEncoder().encodeToString(salt);
    }

    // Hàm hash mật khẩu với salt
    public static String hashPassword(String password, String salt) throws NoSuchAlgorithmException {
        // Kết hợp mật khẩu với salt
        String saltedPassword = password + salt;
        
        // Sử dụng thuật toán SHA-256 để băm
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = md.digest(saltedPassword.getBytes());
        
        // Mã hóa thành chuỗi base64 để dễ lưu trữ
        return Base64.getEncoder().encodeToString(hashedBytes);
    }

    public void addUser(User user) throws SQLException, NoSuchAlgorithmException {
        String salt = getSalt();
        String query = "INSERT INTO users (name, email, password, salt, role) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, user.getName());
        stmt.setString(2, user.getEmail());
        stmt.setString(3, hashPassword(user.getPassword(), salt));
        stmt.setString(4, user.getRole());
        stmt.executeUpdate();
    }
    

    public User getUserById(int id) throws SQLException {
        String query = "SELECT * FROM users WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            if (rs.getString("role").equals("Librarian")) {
                return new ConcreteUser(rs.getInt("id"), rs.getString("name"), rs.getString("email"));
            } else {
                return new Member(rs.getInt("id"), rs.getString("name"), rs.getString("email"));
            }
        }
        return null;
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            if (rs.getString("role").equals("Librarian")) {
                users.add(new Librarian(rs.getInt("id"), rs.getString("name"), rs.getString("email")));
            } else {
                users.add(new Member(rs.getInt("id"), rs.getString("name"), rs.getString("email")));
            }
        }
        return users;
    }

    public static User getUserByNamePassword(String username, String password) throws SQLException {
        String query = "SELECT * FROM users WHERE name = ? AND password = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, username);
        stmt.setString(2, password);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
                return new ConcreteUser(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("password"), rs.getString("role"));
        }
        return null;
    }
}

