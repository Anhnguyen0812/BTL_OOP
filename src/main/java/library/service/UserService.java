package library.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;

import library.dao.UserDAO;
import library.model.User;
import library.util.DBConnection;

public class UserService {
  private static final DBConnection connection = DBConnection.getInstance();
  private static final UserDAO userDAO = UserDAO.getUserDAO();

  public UserService() {
  }

  public static String hashPassword(String password, String salt) throws NoSuchAlgorithmException {
    // Kết hợp mật khẩu với salt
    String saltedPassword = password + salt;
    // Sử dụng thuật toán SHA-256 để băm
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    byte[] hashedBytes = md.digest(saltedPassword.getBytes());
    // Mã hóa thành chuỗi base64 để dễ lưu trữ
    return Base64.getEncoder().encodeToString(hashedBytes);
  }

  public static int checkLogin(String username, String password, String role)
      throws SQLException, NoSuchAlgorithmException {
    String query = "SELECT * FROM users WHERE name = ?";
    Connection conn = connection.getConnection();
    PreparedStatement pstmt = conn.prepareStatement(query);
    // Gán giá trị email và password vào các dấu ? trong câu lệnh SQL
    pstmt.setString(1, username);
    // Thực thi câu lệnh SQL
    ResultSet rs = pstmt.executeQuery();
    // Kiểm tra xem có bản ghi nào được trả về hay không
    if (rs.getString("password") != null
        && rs.getString("password").equals(hashPassword(password, rs.getString("salt")))) {
      if (rs.getString("role").equals(role))
        return 1;
      else
        return 2;
    } else
      return 3;
    // Nếu không có bản ghi nào, thông tin đăng nhập không hợp lệ
  }

  public void addUser(User user) throws NoSuchAlgorithmException {
    try {
      userDAO.addUser(user);
    } catch (SQLException e) {
    }
  }

  public User getUserById(int id) {
    try {
      return userDAO.getUserById(id);
    } catch (SQLException e) {
      return null;
    }
  }

  public static List<User> getAllUsers() {
    try {
      return UserDAO.getAllUsers();
    } catch (SQLException e) {
      return null;
    }
  }

  public void editUser(User user) {
    String query = "UPDATE users SET password = ?, salt = ? WHERE id = ?";
    try (PreparedStatement stmt = connection.getConnection().prepareStatement(query)) {
      stmt.setString(1, user.getPassword());
      stmt.setString(2, user.getSalt());
      stmt.setInt(3, user.getId());
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}