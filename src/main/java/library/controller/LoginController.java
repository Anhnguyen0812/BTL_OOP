package library.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import javafx.application.HostServices;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import library.dao.UserDAO;
import library.model.*;
import library.service.UserService;
import library.ui.UIFactory;
import library.ui.UIInterface;

/**
 * Controller class for handling user login operations.
 */
public class LoginController {

  // Method to get user by username and password

  @FXML
  private Button loginButton;
  @FXML
  private Button signupButton, getCode, confirm;
  @FXML
  private TextField Username;
  @FXML
  private PasswordField Passhide;
  @FXML
  private TextField Pass, email, code, newpass, veritypass;
  @FXML
  private Button hide;
  @FXML
  private ImageView imgHide;
  @FXML
  private Pane forgotPane, loginPane;

  private User user;

  private HostServices hostServices;

  @SuppressWarnings("unused")
  /**
   * Initializes the controller class. This method is automatically called after
   * the
   * FXML file has been loaded.
   */
  @FXML
  public void initialize() {
    Passhide.textProperty().bindBidirectional(Pass.textProperty());
    Pass.setVisible(false);
    // LoginButton.setDefaultButton(true);
    loginButton.setDefaultButton(true);
    loginButton.setOnAction(event -> {
      try {
        MoveToAccount();
      } catch (SQLException | NoSuchAlgorithmException | IOException e) {
        e.printStackTrace();
      }
    });

  }

  /**
   * Shows an alert with the given title and message.
   * 
   * @param title   the title of the alert
   * @param message the message of the alert
   */
  protected void showAlert(String title, String message) {
    // Implementation for showing an alert
    // For example, using JavaFX Alert:
    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  /**
   * Moves to the signup screen.
   */
  @FXML
  public void MoveToSignup() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/Signup.fxml"));
      Parent root = loader.load();
      Stage stage = (Stage) signupButton.getScene().getWindow();
      stage.setScene(new Scene(root));
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Toggles the visibility of the password fields.
   */
  @FXML
  public void togglePass() {
    if (Pass.isVisible()) {
      Pass.setVisible(false);
      Passhide.setVisible(true);
      imgHide.setImage(new Image("imgs/hidden.png"));
    } else {
      Pass.setVisible(true);
      Passhide.setVisible(false);
      imgHide.setImage(new Image("imgs/show.png"));
    }
  }

  @SuppressWarnings("static-access")
  /**
   * Retrieves a user by their username.
   * 
   * @param username The username of the user.
   * @return The user object.
   * @throws SQLException If a database access error occurs.
   */
  private User getUserbyname(String username) throws SQLException {
    UserDAO userdao = UserDAO.getUserDAO();
    return userdao.getUserByName(username);
  }

  private boolean checkBan(String username) throws SQLException {
    UserDAO userdao = UserDAO.getUserDAO();
    return userdao.checkBan(username);
  }

  /**
   * Handles the login process and moves to the appropriate dashboard based on the
   * user role.
   * 
   * @throws SQLException             If a database access error occurs.
   * @throws NoSuchAlgorithmException If the specified algorithm is not available.
   * @throws IOException              If an I/O error occurs.
   */
  @FXML
  public void MoveToAccount() throws SQLException, NoSuchAlgorithmException, IOException {
    System.out.println("Username: " + Username.getText() + ", Password: " + Pass.getText());
    int check = UserService.checkLogin(Username.getText(), Pass.getText(), "admin");

    if (check == 1) {
      user = getUserbyname(Username.getText());
      UIInterface admin = UIFactory.getInterface("ADMIN", user, hostServices);

      if (admin != null) {
        Parent root = admin.getDashboard();
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Library Management System");
        stage.centerOnScreen();
        stage.show();
      } else {
        System.out.println("Failed to load Admin interface.");
      }
    } else if (check == 2) {
      if (checkBan(Username.getText())) {
        showAlert("Thông báo", "Tài khoản của bạn đã bị khóa");
      } else {
        user = getUserbyname(Username.getText());
        UIInterface userInterface = UIFactory.getInterface("USER", user, hostServices);

        if (userInterface != null) {
          Parent root = userInterface.getDashboard();
          Stage stage = (Stage) loginButton.getScene().getWindow();
          stage.setScene(new Scene(root));
          stage.setTitle("Library Management System");
          stage.centerOnScreen();
          stage.show();
        } else {
          System.out.println("Failed to load User interface.");
        }
      }
    } else {
      System.out.println("Login failed. Invalid credentials.");
    }

  }

  /**
   * Sets the HostServices instance.
   * 
   * @param hostServices The HostServices instance.
   */
  public void setHostServices(HostServices hostServices) {
    this.hostServices = hostServices;
  }

  /**
   * Returns the current user.
   * 
   * @return The current user.
   */
  public User getUser() {
    return user;
  }

  /**
   * Moves to the forgot password screen and handles the password recovery
   * process.
   */
  @FXML
  public void MoveToForgotPassword() {
    forgotPane.setVisible(true);
    loginPane.setVisible(false);
    String token = PasswordRecoveryService.randomCode();
    System.out.println(token);
    getCode.setOnAction(event -> {
      if (!email.getText().isEmpty()) {
        Task<Void> task = new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            UserDAO userDAO = UserDAO.getUserDAO();
            User user1 = UserDAO.getUserByEmail(email.getText());
            if (user1 != null) {
              PasswordRecoveryService.sendEmail(email.getText(), "Mã xác nhận là: ", token);
            } else {
              System.out.println("Email not found.");
            }
            return null;
          }
        };
        new Thread(task).start();
        // PasswordRecoveryService.sendEmail("hoangbao28112005@gmail.com", "Mã xác nhận
        // là: ", token);
      }
    });

    confirm.setOnAction(event -> {
      String confirmCode = code.getText();
      String newpassword = newpass.getText();
      String verifypassword = veritypass.getText();
      if (confirmCode.equals(token) && newpassword.equals(verifypassword)) {
        try {
          User user1 = UserDAO.getUserByEmail(email.getText());
          if (user1 != null) {
            String salt = SignupController.getSalt();
            String hashPassword = UserDAO.hashPassword(newpassword, salt);
            user1.setPassword(hashPassword);
            user1.setSalt(salt);
            UserDAO userDAO = UserDAO.getUserDAO();
            userDAO.updateUser(user1);
            System.out.println("Password updated successfully.");
          } else {
            System.out.println("Email not found.");
          }
        } catch (SQLException | NoSuchAlgorithmException e) {
          e.printStackTrace();
        }
      } else {
        System.out.println("Invalid code or password.");
      }
    });
  }

  /**
   * Moves back to the login screen.
   */
  @FXML
  public void MoveToLogin() {
    forgotPane.setVisible(false);
    loginPane.setVisible(true);
  }
}