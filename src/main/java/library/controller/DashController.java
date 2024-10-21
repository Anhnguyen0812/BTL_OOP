package library.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class DashController {

    @FXML
    private Pane Dashboard, Searchbooks, ManageBooks, Issue_and_return;

    @FXML
    private Button Books, logOut;

    public void initialize() {

        Dashboard.setVisible(true);
        Searchbooks.setVisible(false);
        ManageBooks.setVisible(false);
        Issue_and_return.setVisible(false);
    }

    public void gotoDash() {
        Dashboard.setVisible(true);
        Searchbooks.setVisible(false);
        ManageBooks.setVisible(false);
        Issue_and_return.setVisible(false);

    }

    public void gotoBooks() {
        Dashboard.setVisible(false);
        Searchbooks.setVisible(true);
        ManageBooks.setVisible(false);
        Issue_and_return.setVisible(false);
    }

    public void logOut() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) logOut.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Library Management System");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
