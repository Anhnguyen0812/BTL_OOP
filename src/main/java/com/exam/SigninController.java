package com.exam;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.io.IOException;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.PasswordField;

public class SigninController {
    @FXML
    private Button SigninButton;
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

    public void initialize() {
        Passhide.textProperty().bindBidirectional(Pass.textProperty());
        CPasshide.textProperty().bindBidirectional(CPass.textProperty());
        Pass.setVisible(false);
        CPass.setVisible(false);

    }

    public void MoveToLogin() {
        System.out.println("Username: " + Username.getText() + " Email: " + Email.getText() + " Password: "
                + Passhide.getText() + " Confirm Password: " + CPasshide.getText());
        if (Passhide.getText().equals(CPasshide.getText()) && !Username.getText().isEmpty()
                && !Email.getText().isEmpty()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
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
