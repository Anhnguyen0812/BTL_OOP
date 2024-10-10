package library.service;

import library.dao.*;
import library.model.*;

import java.sql.SQLException;
import java.util.List;
import java.sql.Connection;

import org.sqlite.SQLiteConnection;

public class UserService{
    private UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    //     public boolean checkLogin(String email, String password) {
    //     String query = "SELECT * FROM users WHERE email = ? AND password = ?";

    //     try (Connection conn = userDAO.connection;
    //          PreparedStatement pstmt = conn.prepareStatement(query)) {

    //         // Gán giá trị email và password vào các dấu ? trong câu lệnh SQL
    //         pstmt.setString(1, email);
    //         pstmt.setString(2, password);

    //         // Thực thi câu lệnh SQL
    //         ResultSet rs = pstmt.executeQuery();

    //         // Kiểm tra xem có bản ghi nào được trả về hay không
    //         if (rs.next()) {
    //             // Nếu có, thông tin đăng nhập hợp lệ
    //             return true;
    //         } else {
    //             // Nếu không có bản ghi nào, thông tin đăng nhập không hợp lệ
    //             return false;
    //         }

    //     } catch (SQLException e) {
    //         System.out.println(e.getMessage());
    //         return false;
    //     }
    // }

    public void addUser(String name, String email) {
        try {
            User user = new ConcreteUser(0, name, email); // ID tự động tăng
            userDAO.addUser(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User getUserById(int id) {
        try {
            return userDAO.getUserById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<User> getAllUsers() {
        try {
            return userDAO.getAllUsers();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

