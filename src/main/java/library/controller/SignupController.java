package library.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Base64;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import library.dao.UserDAO;
import library.model.User;

/**
 * Controller class for handling user signup operations.
 */
public class SignupController {

  @FXML
  private TextField Username;
  @FXML
  private TextField Email;
  @FXML
  private PasswordField Passhide;
  @FXML
  private PasswordField CPasshide;
  @FXML
  private TextField Pass;
  @FXML
  private TextField CPass;
  @FXML
  private Button hideButton;
  @FXML
  private Button hideButton1;
  @FXML
  private ImageView imgHide1, imgHide2;

  @FXML
  private Button signupButton, loginButton;

  /**
   * Generates a random salt for password hashing.
   * 
   * @return A base64 encoded salt string.
   * @throws NoSuchAlgorithmException If the specified algorithm is not available.
   */
  public static String getSalt() throws NoSuchAlgorithmException {
    // Tạo ra salt ngẫu nhiên với SecureRandom
    SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
    byte[] salt = new byte[16];
    sr.nextBytes(salt);
    // Mã hóa salt thành chuỗi base64 để dễ lưu trữ
    return Base64.getEncoder().encodeToString(salt);
  }

  /**
   * Initializes the controller class. This method is automatically called after
   * the
   * FXML file has been loaded.
   */
  public void initialize() {
    Passhide.textProperty().bindBidirectional(Pass.textProperty());
    CPasshide.textProperty().bindBidirectional(CPass.textProperty());
    Pass.setVisible(false);
    CPass.setVisible(false);
  }

  /**
   * Handles the signup process and moves to the login screen if successful.
   * 
   * @throws SQLException             If a database access error occurs.
   * @throws NoSuchAlgorithmException If the specified algorithm is not available.
   */
  public void MoveToSignup() throws SQLException, NoSuchAlgorithmException {
    System.out.println(
        "Username: "
            + Username.getText()
            + " Email: "
            + Email.getText()
            + " Password: "
            + Passhide.getText()
            + " Confirm Password: "
            + CPasshide.getText());
    UserDAO userDAO = UserDAO.getUserDAO();
    signupButton.setDefaultButton(true);
    if (Passhide.getText().equals(CPasshide.getText())
        && !Username.getText().isEmpty()
        && !Email.getText().isEmpty()
        && UserDAO.getUserByName(Username.getText()) == null
        && UserDAO.getUserByEmail(Email.getText()) == null) {
      try {
        User user = new User(Username.getText(), Email.getText(), Passhide.getText(), "User", getSalt());
        userDAO.addUser(user);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/Login.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) signupButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else if (signupButton.isDefaultButton()) {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/Login.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) signupButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Moves to the login screen.
   */
  public void MoveToLogin() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/Login.fxml"));
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
  public void togglePass() {
    if (Pass.isVisible()) {
      Pass.setVisible(false);
      CPass.setVisible(false);
      Passhide.setVisible(true);
      CPasshide.setVisible(true);
      imgHide1.setImage(new Image("/imgs/hidden.png"));
      imgHide2.setImage(new Image("/imgs/hidden.png"));

    } else {
      Pass.setVisible(true);
      CPass.setVisible(true);
      Passhide.setVisible(false);
      CPasshide.setVisible(false);
      imgHide1.setImage(new Image("/imgs/show.png"));
      imgHide2.setImage(new Image("/imgs/show.png"));
    }
  }
}
