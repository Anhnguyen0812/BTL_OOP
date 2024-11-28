package library.controller;

import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import library.dao.BookDAO;
import library.dao.UserDAO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import library.model.Book;
import library.model.ISBNScannerWithUI;
import library.model.ImageHandler;
import library.model.QRScannerWithUI;
import library.model.User;

import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class ManageIssueBooksController {
    @FXML
    TextField userSearch, bookSearch;
    @FXML
    Button userSearchButton, bookSearchButton, applyButton;
    @FXML
    ChoiceBox<String> userChoice, bookChoice, choiceAction;
    @FXML
    GridPane bookGridPane;
    @FXML
    Pane userPaneView, bookPaneView;
    @FXML
    ImageView imageUser;
    @FXML
    Label nameUser, emailUser, idUser;

    private User user;
    private List<Book> books = new ArrayList<>();
    UserDAO userDAO = UserDAO.getUserDAO();
    BookDAO bookDAO = BookDAO.getBookDAO();

    File audioFile = new File("src/main/resources/sound/success.wav");

    public void initialize() {
        userChoice.getItems().addAll("Name", "ID", "Email");
        userChoice.setValue("ID");
        bookChoice.getItems().addAll("Title", "ISBN");
        bookChoice.setValue("ISBN");
        choiceAction.getItems().addAll("Borrow", "Return");
        choiceAction.setValue("Borrow");
    }

    @FXML
    public void gotoFindUser() {
        // Code here
        String searchText = userSearch.getText();
        String searchCriteria = userChoice.getValue();

        if (searchCriteria.equals("Name")) {
            try {
                user = UserDAO.getUserByName(searchText);
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle the exception appropriately
            }
            // Implement search by user name
            System.out.println("Searching user by name: " + searchText);
        } else if (searchCriteria.equals("Email")) {
            try {
                user = UserDAO.getUserByEmail(searchText);
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle the exception appropriately
            }
            // Implement search by user ID
            System.out.println("Searching user by ID: " + searchText);
        } else {
            try {
                user = userDAO.getUserById(Integer.parseInt(searchText));
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle the exception appropriately
            }
            // Implement search by user email
            System.out.println("Searching user by ID: " + searchText);
        }
        setUserItem();
    }

    private void setUserItem() {
        nameUser.setText("Name  " + user.getName());
        emailUser.setText("Email  " + user.getEmail());
        idUser.setText(String.valueOf("Id   " + user.getId()));
        ImageHandler imageHandler = new ImageHandler();
        if (imageHandler.loadImage(user.getId()) != null) {
            imageUser.setImage(imageHandler.loadImage(user.getId()));
        } else {
            imageUser.setImage(new Image("/imgs/account.png"));
        }
    }

    @FXML
    public void gotoFindBook() {
        // Code here
        String searchText = bookSearch.getText();
        String searchCriteria = bookChoice.getValue();
        if (searchCriteria.equals("Title")) {
            try {
                Book book = bookDAO.getOneBookByTitle(searchText);
                books.add(book);
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle the exception appropriately
            }
            // Implement search by book title
            System.out.println("Searching book by title: " + searchText);
        } else {
            try {
                Book book = bookDAO.getBookByISBN(searchText);
                if (book != null)
                    books.add(book);
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle the exception appropriately
            }
            // Implement search by book ISBN
            System.out.println("Searching book by ISBN: " + searchText);
        }
        int size = books.size();

        if (size > 0)
            try {
                Book book = books.get(size - 1);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/BookItem.fxml"));
                Parent bookDetail = loader.load();
                BookItemController controller = loader.getController();
                controller.setItemData(book, size, user);
                controller.hideButton();
                bookGridPane.add(bookDetail, 1, books.indexOf(book) + 1);
                bookGridPane.setMargin(bookDetail, new Insets(5, 0, 5, 0));
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception appropriately
            }

    }

    @FXML
    public void gotoScanUser() {
        // Code here
        QRScannerWithUI scanner = new QRScannerWithUI();
        scanner.setOnScanCompleteListener(qrCode -> {
            Platform.runLater(() -> {
                String userId = qrCode.split(",")[0];
                userSearch.setText(userId);
                userChoice.setValue("ID");
                try {
                    user = userDAO.getUserById(Integer.parseInt(userId));
                    if (user != null) {
                        setUserItem();
                        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                        Clip clip = AudioSystem.getClip();
                        clip.open(audioStream);
                        clip.start();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
        scanner.show(userPaneView);

    }

    @FXML
    public void gotoScanBook() {
        // Code here
        ISBNScannerWithUI scanner = new ISBNScannerWithUI();
        scanner.setOnScanCompleteListener(isbn -> {
            Platform.runLater(() -> {
                bookSearch.setText(isbn);
                bookChoice.setValue("ISBN");
                try {
                    Book book = bookDAO.getBookByISBN(isbn);
                    if (book != null) {
                        books.add(book);
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/BookItem.fxml"));
                        Parent bookDetail = loader.load();
                        BookItemController controller = loader.getController();
                        controller.setItemData(book, books.size(), user);
                        controller.hideButton();
                        bookGridPane.add(bookDetail, 1, books.indexOf(book) + 1);
                        bookGridPane.setMargin(bookDetail, new Insets(5, 0, 5, 0));
                        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                        Clip clip = AudioSystem.getClip();
                        clip.open(audioStream);
                        clip.start();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
        });
        scanner.show(bookPaneView);
    }

    @FXML
    public void gotoApply() {
        // Code here
    }

}
