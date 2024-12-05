
package library.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import library.dao.UserDAO;
import library.model.User;

public class SignupControllerTest {

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private SignupController signupController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetSalt() throws NoSuchAlgorithmException {
        String salt = SignupController.getSalt();
        assertNotNull(salt);
        assertEquals(24, salt.length());
    }

    // public void testMoveToSignup() throws SQLException, NoSuchAlgorithmException
    // {
    // when(userDAO.getUserByName(anyString())).thenReturn(null);
    // doNothing().when(userDAO).addUser(any(User.class));

    // signupController.Username.setText("testUser");
    // signupController.Email.setText("test@example.com");
    // signupController.Passhide.setText("password");
    // signupController.CPasshide.setText("password");

    // signupController.MoveToSignup();

    // verify(userDAO, times(1)).addUser(any(User.class));
    // }

    @Test
    public void testMoveToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/Login.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            signupController.MoveToLogin();
            assertTrue(stage.isShowing());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
}