package com.exam;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class LoginController {
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

    @FXML
    public void initialize() {
        Passhide.textProperty().bindBidirectional(Pass.textProperty());
        Pass.setVisible(false);
        LoginButton.setDefaultButton(true);
    }

    @FXML
    public void MoveToSignin() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Signin.fxml"));
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
    public void MoveToSearch() {
        System.out.println("Username: " + Username.getText() + ", Password: " + Pass.getText());
        if (Username.getText().equals("anhnguyen") && Pass.getText().equals("admin")) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("GoogleSearch.fxml"));
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
            Stage stage = (Stage) LoginButton.getScene().getWindow();
            Parent root = stage.getScene().getRoot();
            Label errorMessage = new Label("Invalid Username or Password!");
            errorMessage.setStyle("-fx-text-fill: brown;");
            ((Pane) root).getChildren().add(errorMessage);
            errorMessage.setLayoutX(120);
            errorMessage.setLayoutY(210);

            PauseTransition pause = new PauseTransition(Duration.seconds(5));
            pause.setOnFinished(event -> ((Pane) root).getChildren().remove(errorMessage));
            pause.play();
        }
    }
}
