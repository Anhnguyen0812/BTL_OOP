package library.dao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Base64;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import library.model.Admin;
import library.model.Member;
import library.model.User;
import library.util.DBConnection;

public class UserDAO implements DAO {
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
    String query = "INSERT INTO users (name, email, password, role, salt, created_at) VALUES (?, ?, ?, ?, ?, ?)";
    PreparedStatement stmt = connection.prepareStatement(query);
    stmt.setString(1, user.getName());
    stmt.setString(2, user.getEmail());
    stmt.setString(3, hashPassword(user.getPassword(), user.getSalt()));
    stmt.setString(4, user.getRole());
    stmt.setString(5, user.getSalt());
    stmt.setDate(6, java.sql.Date.valueOf(LocalDate.now()));
    stmt.executeUpdate();
  }

  public void updateUser(User user) throws SQLException, NoSuchAlgorithmException {
    String query = "UPDATE users SET name = ?, email = ?, password = ?, role = ?, salt = ? WHERE id = ?";
    PreparedStatement stmt = connection.prepareStatement(query);
    stmt.setString(1, user.getName());
    stmt.setString(2, user.getEmail());
    stmt.setString(3, user.getPassword());
    stmt.setString(4, user.getRole());
    stmt.setString(5, user.getSalt());
    stmt.setInt(6, user.getId());
    stmt.executeUpdate();
  }

  public void updateUserWithoutPassword(User user) throws SQLException {
    String query = "UPDATE users SET name = ?, email = ?, role = ? WHERE id = ?";
    PreparedStatement stmt = connection.prepareStatement(query);
    stmt.setString(1, user.getName());
    stmt.setString(2, user.getEmail());
    stmt.setString(3, user.getRole());
    stmt.setInt(4, user.getId());
    stmt.executeUpdate();
  }

  public User getUserById(int id) throws SQLException {
    String query = "SELECT * FROM users WHERE id = ?";
    PreparedStatement stmt = connection.prepareStatement(query);
    stmt.setInt(1, id);
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

  public static ObservableList<User> getAllUsers() throws SQLException {
    ObservableList<User> users = FXCollections.observableArrayList();
    String query = "SELECT * FROM users";
    Statement stmt = connection.createStatement();
    ResultSet rs = stmt.executeQuery(query);
    while (rs.next()) {
      users.add(
          new User(
              rs.getInt("id"),
              rs.getString("name"),
              rs.getString("email"),
              rs.getString("password"),
              rs.getString("role"),
              rs.getString("salt")));
    }
    return users;
  }

  public static User getUserByName(String username) throws SQLException {
    String query = "SELECT * FROM users WHERE name = ?";
    PreparedStatement stmt = connection.prepareStatement(query);
    stmt.setString(1, username);
    ResultSet rs = stmt.executeQuery();
    if (rs.next()) {
      if (rs.getString("role").equals("admin")) {
        return new Admin(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getString("role"),
            rs.getString("salt"));
      } else {
        return new Member(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getString("role"),
            rs.getString("salt"));
      }
    }
    return null;
  }

  public ObservableList<User> getListUserByName(String username) throws SQLException {
    ObservableList<User> users = FXCollections.observableArrayList();
    String query = "SELECT * FROM users WHERE name LIKE ?";
    PreparedStatement stmt = connection.prepareStatement(query);
    stmt.setString(1, "%" + username + "%");
    ResultSet rs = stmt.executeQuery();
    while (rs.next()) {
      users.add(
          new User(
              rs.getInt("id"),
              rs.getString("name"),
              rs.getString("email"),
              rs.getString("password"),
              rs.getString("role"),
              rs.getString("salt")));
    }
    return users;
  }

  public ObservableList<User> getListUserByEmail(String email) throws SQLException {
    ObservableList<User> users = FXCollections.observableArrayList();
    String query = "SELECT * FROM users WHERE email LIKE ?";
    PreparedStatement stmt = connection.prepareStatement(query);
    stmt.setString(1, "%" + email + "%");
    ResultSet rs = stmt.executeQuery();
    while (rs.next()) {
      users.add(
          new User(
              rs.getInt("id"),
              rs.getString("name"),
              rs.getString("email"),
              rs.getString("password"),
              rs.getString("role"),
              rs.getString("salt")));
    }
    return users;
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

  public void deleteUser(User user) throws SQLException {
    String query = "DELETE FROM users WHERE id = ?";
    PreparedStatement stmt = connection.prepareStatement(query);
    stmt.setInt(1, user.getId());
    stmt.executeUpdate();
  }

  public static String getNameById(int id) throws SQLException {
    String query = "SELECT name FROM users WHERE id = ?";
    PreparedStatement stmt = connection.prepareStatement(query);
    stmt.setInt(1, id);
    ResultSet rs = stmt.executeQuery();
    if (rs.next()) {
      return rs.getString("name");
    }
    return null;
  }

  public boolean checkBan(String username) {
    String query = "SELECT ban FROM users WHERE name = ?";
    try {
      PreparedStatement stmt = connection.prepareStatement(query);
      stmt.setString(1, username);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        return rs.getBoolean("ban");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public void banUser(int id) {
    String query = "UPDATE users SET ban = true WHERE id = ?";
    try {
      PreparedStatement stmt = connection.prepareStatement(query);
      stmt.setInt(1, id);
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public boolean checkIsBan(int id) {
    String query = "SELECT ban FROM users WHERE id = ?";
    try {
      PreparedStatement stmt = connection.prepareStatement(query);
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        return rs.getBoolean("ban");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public void deBanUser(int id) {
    String query = "UPDATE users SET ban = false WHERE id = ?";
    try {
      PreparedStatement stmt = connection.prepareStatement(query);
      stmt.setInt(1, id);
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
