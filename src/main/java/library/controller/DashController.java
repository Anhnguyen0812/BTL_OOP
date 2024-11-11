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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import library.dao.BookDAO;
import library.model.Book;

import java.io.IOException;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class DashController {

    @FXML
    private Pane home, books, returnBooks, issueBooks, settings, subUser, noti;

    @FXML
    private Button home_Button, books_Button, logOut, returnBooks_Button, issueBooks_Button, settings_Button;

    @FXML
    private GridPane grid;

    public void initialize() {
        home.setVisible(true);
        books.setVisible(false);
        returnBooks.setVisible(false);
        issueBooks.setVisible(false);
        settings.setVisible(false);
    }

    public void resetStyle() {
        home.setVisible(false);
        books.setVisible(false);
        returnBooks.setVisible(false);
        issueBooks.setVisible(false);
        home_Button.styleProperty().set("-fx-background-color: #A6AEBF");
        books_Button.styleProperty().set("-fx-background-color: #A6AEBF");
        returnBooks_Button.styleProperty().set("-fx-background-color: #A6AEBF");
        issueBooks_Button.styleProperty().set("-fx-background-color: #A6AEBF");
        settings_Button.styleProperty().set("-fx-background-color: #A6AEBF");
    }

    public void gotoDash() {
        resetStyle();
        home.setVisible(true);
        home_Button.styleProperty().set("-fx-background-color: #777777");
    }

    public void gotoBooks() {
        resetStyle();
        books.setVisible(true);
        books_Button.styleProperty().set("-fx-background-color: #777777");
    }

    public void gotoReturnBooks() {
        resetStyle();
        returnBooks.setVisible(true);
        returnBooks_Button.styleProperty().set("-fx-background-color: #777777");
    }

    public void gotoIssueBooks() {
        resetStyle();
        issueBooks.setVisible(true);
        issueBooks_Button.styleProperty().set("-fx-background-color: #777777");
    }

    public void gotoSettings() {
        resetStyle();
        settings.setVisible(true);
        settings_Button.styleProperty().set("-fx-background-color: #777777");
    }

    public void goToSubUser() {
        if (subUser.isVisible()) {
            subUser.setVisible(false);
        } else {
            subUser.setVisible(true);
        }
    }

    public void goToNoti() {
        if (noti.isVisible()) {
            noti.setVisible(false);
        } else {
            noti.setVisible(true);
        }
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
