package library.dao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import library.model.User;
import library.util.DBConnection;

public class UserDAO {
  private static Connection connection;
  private static UserDAO instance;

  private UserDAO() {
    UserDAO.connection = DBConnection.getInstance().getConnection();
  }

  public static UserDAO getUserDAO() {
    if (instance == null) {
      instance = new UserDAO();
    }
    return instance;
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
    String query = "INSERT INTO users (name, email, password, role, salt) VALUES (?, ?, ?, ?, ?)";
    PreparedStatement stmt = connection.prepareStatement(query);
    stmt.setString(1, user.getName());
    stmt.setString(2, user.getEmail());
    stmt.setString(3, hashPassword(user.getPassword(), user.getSalt()));
    stmt.setString(4, user.getRole());
    stmt.setString(5, user.getSalt());
    stmt.executeUpdate();
  }

  public User getUserById(int id) throws SQLException {
    String query = "SELECT * FROM users WHERE id = ?";
    PreparedStatement stmt = connection.prepareStatement(query);
    stmt.setInt(1, id);
    ResultSet rs = stmt.executeQuery();
    if (rs.next()) {
      return new User(rs.getInt("id"), rs.getString("name"), rs.getString("email"));
    }
    return null;
  }

  public static ObservableList<User> getAllUsers() throws SQLException {
    ObservableList<User> users = FXCollections.observableArrayList();
    String query = "SELECT * FROM users";
    Statement stmt = connection.createStatement();
    ResultSet rs = stmt.executeQuery(query);
    while (rs.next()) {
      users.add(
          new User(
              rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("role")));
    }
    return users;
  }

  public static User getUserByName(String username) throws SQLException {
    String query = "SELECT * FROM users WHERE name = ?";
    PreparedStatement stmt = connection.prepareStatement(query);
    stmt.setString(1, username);
    ResultSet rs = stmt.executeQuery();
    if (rs.next()) {
      return new User(
          rs.getInt("id"),
          rs.getString("name"),
          rs.getString("email"),
          rs.getString("password"),
          rs.getString("role"),
          rs.getString("salt"));
    }
    return null;
  }

  public static User getUserByEmail(String userEmail) throws SQLException {
    String query = "SELECT * FROM users WHERE email LIKE ?";
    PreparedStatement stmt = connection.prepareStatement(query);
    stmt.setString(1, userEmail);
    ResultSet rs = stmt.executeQuery();
    if (rs.next()) {
      return new User(
          rs.getInt("id"),
          rs.getString("name"),
          rs.getString("email"),
          rs.getString("password"),
          rs.getString("role"),
          rs.getString("salt"));
    }
    return null;
  }
}
