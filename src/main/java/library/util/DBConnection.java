package library.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
  // private static final String URL = "jdbc:mysql://localhost:3306/data"; // Thay đổi tên cơ sở dữ
  // liệu nếu cần
  // private static final String USER = "hoangyen"; // Tên người dùng
  // private static final String PASSWORD = "bao12345"; // Mật khẩu của người dùng
  private static final String URL = "jdbc:sqlite:src/main/java/com/databases/document.db";
  private static Connection connection;
  private static DBConnection instance;

  private DBConnection() {
    try {
      // Tạo kết nối
      connection = DriverManager.getConnection(URL);
    } catch (SQLException e) {
      System.out.println("Can not connect to SQLite");
    }
  }

  // Phương thức để lấy kết nối

  public static DBConnection getInstance() {
    if (instance == null) {
      instance = new DBConnection();
    }
    return instance;
  }

  public Connection getConnection() {
    return connection;
  }

  // Phương thức để đóng kết nối
  public static void closeConnection() {
    if (connection != null) {
      try {
        connection.close();
      } catch (SQLException e) {
        System.out.println("Can not disconnect SQlite");
      }
    }
  }
}
