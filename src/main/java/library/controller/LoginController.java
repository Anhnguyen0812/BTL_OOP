package library.controller;

import javafx.fxml.FXML;
    import javafx.scene.control.Button;
    import javafx.scene.control.TextField;
    import javafx.scene.control.PasswordField;
    import javafx.fxml.FXMLLoader;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.stage.Stage;

    import java.io.IOException;
import java.sql.SQLException;
    import library.AppLaunch;
import library.dao.UserDAO;
import library.model.User;
import library.service.UserService;
    
public class LoginController {

    // Method to get user by username and password
    private User getUserbynamepassword(String username, String password) throws SQLException {
        UserDAO userdao = new UserDAO();
        return userdao.getUserByNamePassword(username, password);
    }
 
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
    
        @FXML
        public void MoveToAccount() throws SQLException {
            System.out.println("Username: " + Username.getText() + ", Password: " + Pass.getText());
            int check = UserService.checkLogin(Username.getText(), Pass.getText(), "admin");
            if (check == 1){
                try {
                    user = getUserbynamepassword(Username.getText(), Pass.getText());
                    AdminController adminController = new AdminController(user);
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
                        user = getUserbynamepassword(Username.getText(), Pass.getText());
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/User.fxml"));
                        Parent root = loader.load();
                        Stage stage = (Stage) LoginButton.getScene().getWindow();
                        stage.setScene(new Scene(root, 960, 720));
                        stage.setTitle("Library Management System");
                        stage.centerOnScreen();
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            } else {
                    // Display error message for 5 seconds
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/Admin.fxml"));
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
        }

        public User getUser() {
            return user;
        }
     
}
