
package library.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import library.model.User;

public class UserDAOTest {
    private UserDAO userDAO;

    @BeforeEach
    public void setUp() {
        userDAO = UserDAO.getUserDAO();
    }

    @Test
    public void testAddUser() throws SQLException, NoSuchAlgorithmException {
        User user = new User(1, "Test User", "test@example.com", "password", "member", "salt");
        userDAO.addUser(user);
        User retrievedUser = userDAO.getUserById(1);
        assertEquals(user.getName(), retrievedUser.getName());
    }

    @Test
    public void testUpdateUser() throws SQLException, NoSuchAlgorithmException {
        User user = new User(1, "Updated User", "updated@example.com", "newpassword", "admin", "newsalt");
        userDAO.updateUser(user);
        User retrievedUser = userDAO.getUserById(1);
        assertEquals(user.getEmail(), retrievedUser.getEmail());
    }

    @Test
    public void testDeleteUser() throws SQLException {
        User user = new User(1, "Test User", "test@example.com", "password", "member", "salt");
        userDAO.deleteUser(user);
        User retrievedUser = userDAO.getUserById(1);
        assertNull(retrievedUser);
    }
}