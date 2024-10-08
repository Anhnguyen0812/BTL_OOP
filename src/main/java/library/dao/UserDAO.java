package library.dao;

import library.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public void addUser(User user) throws SQLException {
        String query = "INSERT INTO users (name, email, role) VALUES (?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, user.getName());
        stmt.setString(2, user.getEmail());
        stmt.setString(3, user.getRole());
        stmt.executeUpdate();
    }

    public User getUserById(int id) throws SQLException {
        String query = "SELECT * FROM users WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            if (rs.getString("role").equals("Librarian")) {
                return new Librarian(rs.getInt("id"), rs.getString("name"), rs.getString("email"));
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
}

