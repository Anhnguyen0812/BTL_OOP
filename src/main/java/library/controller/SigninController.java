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
import javafx.stage.Stage;
import library.dao.UserDAO;
import library.model.User;

public class SigninController {

  @FXML private Button SigninButton;
  @FXML private TextField Username;
  @FXML private TextField Email;
  @FXML private PasswordField Passhide;
  @FXML private PasswordField CPasshide;
  @FXML private TextField Pass;
  @FXML private TextField CPass;
  @FXML private Button hideButton;
  @FXML private Button hideButton1;

  @FXML private Button LoginButton;

  public static String getSalt() throws NoSuchAlgorithmException {
    // Tạo ra salt ngẫu nhiên với SecureRandom
    SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
    byte[] salt = new byte[16];
    sr.nextBytes(salt);
    // Mã hóa salt thành chuỗi base64 để dễ lưu trữ
    return Base64.getEncoder().encodeToString(salt);
  }

  public void initialize() {
    Passhide.textProperty().bindBidirectional(Pass.textProperty());
    CPasshide.textProperty().bindBidirectional(CPass.textProperty());
    Pass.setVisible(false);
    CPass.setVisible(false);
  }

  public void MoveToLogin() throws SQLException, NoSuchAlgorithmException {
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
    LoginButton.setDefaultButton(true);
    if (Passhide.getText().equals(CPasshide.getText())
        && !Username.getText().isEmpty()
        && !Email.getText().isEmpty()
        && UserDAO.getUserByName(Username.getText()) == null) {
      try {
        User user =
            new User(Username.getText(), Email.getText(), Passhide.getText(), "User", getSalt());
        userDAO.addUser(user);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/Login.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) SigninButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else if (LoginButton.isDefaultButton()) {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/Login.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) SigninButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void togglePass() {
    if (Pass.isVisible()) {
      Pass.setVisible(false);
      CPass.setVisible(false);
      Passhide.setVisible(true);
      CPasshide.setVisible(true);
      hideButton.setText("o");
      hideButton1.setText("o");

    } else {
      Pass.setVisible(true);
      CPass.setVisible(true);
      Passhide.setVisible(false);
      CPasshide.setVisible(false);
      hideButton.setText("-");
      hideButton1.setText("-");
    }
  }
}
