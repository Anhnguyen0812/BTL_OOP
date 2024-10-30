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
import javafx.stage.Stage;
import library.AppLaunch;
import library.dao.UserDAO;
import library.model.User;
import library.service.UserService;
    
public class LoginController {

    // Method to get user by username and password
    
 
        @FXML
        private Button LoginButton;
        @FXML
        private Button SigninButton;
        @FXML
        private TextField Username;
        @FXML
        private PasswordField Passhide;
        @FXML
        private TextField Pass;
        @FXML
        private Button hide;

        private User user;

        private HostServices hostServices;
    
        @FXML
        public void initialize() {
            Passhide.textProperty().bindBidirectional(Pass.textProperty());
            Pass.setVisible(false);
            LoginButton.setDefaultButton(true);
        }
    
        @FXML
        public void MoveToSignin() {
    
            try {
                FXMLLoader loader = new FXMLLoader(AppLaunch.class.getResource("/library/Signin.fxml"));
                Parent root = loader.load();
                SigninController controller = loader.getController();
                Stage stage = (Stage) LoginButton.getScene().getWindow();
                stage.setScene(new Scene(root, 500, 350));
                stage.setTitle("Library Management System");
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
                hide.setText("o");
            } else {
                Pass.setVisible(true);
                Passhide.setVisible(false);
                hide.setText("-");
            }
        }
        
    private User getUserbyname(String username) throws SQLException {
        UserDAO userdao = UserDAO.getUserDAO();
        return userdao.getUserByName(username);
    }
        @FXML
        public void MoveToAccount() throws SQLException, NoSuchAlgorithmException {
            System.out.println("Username: " + Username.getText() + ", Password: " + Pass.getText());
            int check = UserService.checkLogin(Username.getText(), Pass.getText(), "admin");
            if (check == 1){
                try {
                    user = getUserbyname(Username.getText());
                    AdminController adminController = new AdminController(user, hostServices);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/Admin.fxml"));
                    loader.setController(adminController);
                    Parent root = loader.load();
                    Stage stage = (Stage) LoginButton.getScene().getWindow();
                    stage.setScene(new Scene(root, 960, 720));
                    stage.setTitle("Library Management System");
                    stage.centerOnScreen();
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } 
            else if (check == 2) {
                    try {
                        // user = getUserbyname(Username.getText());
                        // // DashController dashController = new DashController(user, hostServices);
                        // UserController userController = new UserController(user, hostServices);
                        // FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/dash.fxml"));
                        // loader.setController(userController);
                        // Parent root = loader.load();
                        // Stage stage = (Stage) LoginButton.getScene().getWindow();
                        // stage.setScene(new Scene(root, 960, 720));
                        // stage.setTitle("Library Management System");
                        // stage.centerOnScreen();
                        // stage.show();
                        user = getUserbyname(Username.getText());
                        DashController dashController = new DashController(user, hostServices);
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/dash.fxml"));
                        loader.setController(dashController);
                        Parent root = loader.load();
                        Stage stage = (Stage) LoginButton.getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.setTitle("Library Management System");
                        stage.centerOnScreen();
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            } else {
                System.out.println("Login failed");
            }
        }

        public void setHostServices(HostServices hostServices) {
            this.hostServices = hostServices;
        }

        public User getUser() {
            return user;
        }
     
}
