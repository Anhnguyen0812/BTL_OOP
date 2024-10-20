package library.service;

import library.dao.*;
import library.model.*;
import library.util.DBConnection;

import java.sql.SQLException;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.google.zxing.Result;

public class UserService{
    private final UserDAO userDAO;
    private final static DBConnection connection = DBConnection.getInstance();

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

        public static int checkLogin(String username, String password, String role) throws SQLException {
        String query = "SELECT * FROM users WHERE name = ? AND password = ?";
        String query2 = "SELECT * FROM users WHERE role = ?";

        Connection conn = connection.getConnection();

        PreparedStatement pstmt = conn.prepareStatement(query);
        PreparedStatement pstmt2 = conn.prepareStatement(query2);

            // Gán giá trị email và password vào các dấu ? trong câu lệnh SQL
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        pstmt2.setString(1, role);

            // Thực thi câu lệnh SQL
        ResultSet rs = pstmt.executeQuery();
        ResultSet rs2 = pstmt2.executeQuery();

            // Kiểm tra xem có bản ghi nào được trả về hay không
        if (rs.next()) {
            if (rs2.next()) return 1;
            else return 2;
        }
        else return 3;
            // Nếu không có bản ghi nào, thông tin đăng nhập không hợp lệ
    }

    public void addUser(String name, String email) {
        try {
            User user = new ConcreteUser(0, name, email); // ID tự động tăng
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


    public List<User> getAllUsers() {
        try {
            return userDAO.getAllUsers();
        } catch (SQLException e) {
            return null;
        }
    }
}

