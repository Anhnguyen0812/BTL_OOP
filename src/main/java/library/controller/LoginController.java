package library.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import javafx.application.HostServices;
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
import library.model.User;
import library.service.UserService;
import library.ui.UIFactory;
import library.ui.UIInterface;

public class LoginController {

  // Method to get user by username and password

  @FXML
  private Button loginButton;
  @FXML
  private Button signupButton;
  @FXML
  private TextField Username;
  @FXML
  private PasswordField Passhide;
  @FXML
  private TextField Pass;
  @FXML
  private Button hide;
  @FXML
  private ImageView imgHide;
  @FXML
  private Pane pane;

  private User user;

  private HostServices hostServices;

  @SuppressWarnings("unused")
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
  private User getUserbyname(String username) throws SQLException {
    UserDAO userdao = UserDAO.getUserDAO();
    return userdao.getUserByName(username);
  }

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
    } else {
        System.out.println("Login failed. Invalid credentials.");
    }
  }

  public void setHostServices(HostServices hostServices) {
    this.hostServices = hostServices;
  }

  public User getUser() {
    return user;
  }

}