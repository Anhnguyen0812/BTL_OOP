
package library.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
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
import library.service.UserService;
import library.ui.UIFactory;
import library.ui.UIInterface;

public class LoginControllerTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private UserService userService;

    @Mock
    private UIFactory uiFactory;

    @InjectMocks
    private LoginController loginController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testMoveToSignup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/Signup.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            loginController.MoveToSignup();
            assertTrue(stage.isShowing());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    // public void testMoveToAccount() throws SQLException,
    // NoSuchAlgorithmException, IOException {
    // when(userService.checkLogin(anyString(), anyString(),
    // anyString())).thenReturn(1);
    // User user = new User("testUser", "test@example.com", "password", "admin",
    // "salt");
    // when(userDAO.getUserByName(anyString())).thenReturn(user);
    // UIInterface uiInterface = mock(UIInterface.class);
    // when(uiFactory.getInterface(anyString(), any(User.class),
    // any())).thenReturn(uiInterface);
    // when(uiInterface.getDashboard()).thenReturn(new Parent() {});

    // loginController.Username.setText("testUser");
    // loginController.Pass.setText("password");
    // loginController.MoveToAccount();

    // verify(uiFactory, times(1)).getInterface(anyString(), any(User.class),
    // any());
    // }
}