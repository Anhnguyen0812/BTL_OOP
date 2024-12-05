
package library.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import library.dao.BookDAO;
import library.dao.UserDAO;
import library.model.User;

public class ManageIssueBooksControllerTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private BookDAO bookDAO;

    @InjectMocks
    private ManageIssueBooksController manageIssueBooksController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // public void testGotoFindUser() throws SQLException {
    // User user = new User("testUser", "test@example.com", "password", "User",
    // "salt");
    // when(userDAO.getUserByName(anyString())).thenReturn(user);

    // manageIssueBooksController.userSearch.setText("testUser");
    // manageIssueBooksController.userChoice.setValue("Name");
    // manageIssueBooksController.gotoFindUser();

    // assertEquals("testUser", manageIssueBooksController.nameUser.getText());
    // }

    @Test
    public void testGotoFindBook() throws SQLException {
        // Implement similar to testGotoFindUser
    }

    @Test
    public void testGotoScanUser() {
        // Implement test for scanning user
    }

    @Test
    public void testGotoScanBook() {
        // Implement test for scanning book
    }

    @Test
    public void testGotoApply() {
        // Implement test for applying borrow/return
    }
}