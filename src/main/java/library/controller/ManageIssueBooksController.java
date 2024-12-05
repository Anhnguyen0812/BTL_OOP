package library.controller;

import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import library.dao.BookDAO;
import library.dao.BorrowRecordDAO;
import library.dao.UserDAO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import library.model.Book;
import library.model.BorrowRecord;
import library.model.ISBNScannerWithUI;
import library.model.ImageHandler;
import library.model.QRScannerWithUI;
import library.model.User;

import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import org.springframework.cglib.core.Local;

import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Controller class for managing the issue and return of books.
 */
public class ManageIssueBooksController {
    @FXML
    private TextField userSearch, bookSearch;
    @FXML
    private Button userSearchButton, bookSearchButton, applyButton, cancelScanBookButton, cancelScanUserButton;
    @FXML
    private ChoiceBox<String> userChoice, bookChoice;
    @FXML
    private MenuButton menuButton;
    @FXML
    private GridPane bookGridPane;
    @FXML
    private Pane userPaneView, bookPaneView;
    @FXML
    private ImageView imageUser;
    @FXML
    private Label nameUser, emailUser, idUser;

    private User user;
    private List<Book> books = new ArrayList<>();
    UserDAO userDAO = UserDAO.getUserDAO();
    BookDAO bookDAO = BookDAO.getBookDAO();

    File audioFile = new File("src/main/resources/sound/success.wav");

    /**
     * Initializes the controller class. This method is automatically called after
     * the
     * FXML file has been loaded.
     */
    public void initialize() {
        userChoice.getItems().addAll("Name", "ID", "Email");
        userChoice.setValue("ID");
        bookChoice.getItems().addAll("Title", "ISBN");
        bookChoice.setValue("ISBN");
        cancelScanBookButton.setVisible(false);
        cancelScanUserButton.setVisible(false);
        menuButton.setText("Borrow");
    }

    QRScannerWithUI scannerUser = new QRScannerWithUI();
    ISBNScannerWithUI scannerBook = new ISBNScannerWithUI();

    /**
     * Shows an alert with the specified title and message.
     * 
     * @param title   The title of the alert.
     * @param message The message of the alert.
     */
    protected void showAlert(String title, String message) {
        // Implementation for showing an alert
        // For example, using JavaFX Alert:
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Searches for a user based on the search criteria and displays the user
     * details.
     */
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

    /**
     * Sets the user details in the UI.
     */
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

    /**
     * Searches for a book based on the search criteria and displays the book
     * details.
     */
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

    /**
     * Initiates the scanning process for a user QR code.
     */
    @FXML
    public void gotoScanUser() {
        cancelScanUserButton.setVisible(true);
        // Code here
        scannerUser.setOnScanCompleteListener(qrCode -> {
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
        scannerUser.show(userPaneView);

    }

    /**
     * Checks if the specified book is already in the list of books.
     * 
     * @param book The book to check.
     * @return True if the book is already in the list, false otherwise.
     */
    private boolean check(Book book) {
        for (Book b : books) {
            if (b.getId() == book.getId())
                return true;
        }
        return false;
    }

    /**
     * Initiates the scanning process for a book ISBN.
     */
    @FXML
    public void gotoScanBook() {
        cancelScanBookButton.setVisible(true);
        // Code here
        scannerBook.setOnScanCompleteListener(isbn -> {
            Platform.runLater(() -> {
                bookSearch.setText(isbn);
                bookChoice.setValue("ISBN");
                try {
                    String s = menuButton.getText();
                    Book book = bookDAO.getBookByISBN(isbn);
                    if (book != null) {
                        BorrowRecordDAO borrowRecordDAO = BorrowRecordDAO.getBorrowRecordDAO();
                        // System.out.println(s);
                        // if (s.equals("Borrow"))
                        // System.out.println(book.getId() + " " + user.getId());
                        if (s.equals("Borrow") && borrowRecordDAO.isBookBorrowed(user.getId(), book.getId())) {
                            System.out.println("error, book is borrowed");
                        } else if (s.equals("Return")
                                && !borrowRecordDAO.isBookDontReturn(book.getId(), user.getId())) {
                            System.out.println("error, Not Book found Record");
                        } else if (check(book))
                            showAlert("Error", "Book already added");
                        else {
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
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
        });
        scannerBook.show(bookPaneView);
    }

    /**
     * Applies the borrow or return operation based on the selected menu option.
     */
    @FXML
    public void gotoApply() throws SQLException {
        // Code here
        if (menuButton.getText().equals("Borrow")) {
            BorrowRecordDAO borrowRecordDAO = BorrowRecordDAO.getBorrowRecordDAO();
            for (Book book : books) {
                BookDAO bookDAO = BookDAO.getBookDAO();
                if (bookDAO.checkAvailble(book)) {
                    BorrowRecord borrowRecord = new BorrowRecord(1, user, book, LocalDate.now(),
                            LocalDate.now().plusDays(20), 1);
                    borrowRecordDAO.addBorrowRecord(borrowRecord);
                    book.setAvailable(book.getAvailable() - 1);
                    bookDAO.updateBook(book);

                } else {
                    showAlert("error", "Book is not available");
                }
            }
        } else {
            BorrowRecordDAO borrowRecordDAO = BorrowRecordDAO.getBorrowRecordDAO();
            for (Book book : books) {
                BorrowRecord a = borrowRecordDAO.getBorrowRecordByUserBook(user, book);
                a.setReturnDate(LocalDate.now());
                a.setStatus(3);
                borrowRecordDAO.updateBorrowRecord(a);
                book.setAvailable(book.getAvailable() + 1);
                bookDAO.updateBook(book);
                showAlert("succesfull", "Book is returned");
            }
        }
    }

    /**
     * Clears the user and book search fields and resets the UI.
     */
    private void clear() {
        userSearch.clear();
        bookSearch.clear();
        nameUser.setText("Name");
        emailUser.setText("Email");
        idUser.setText("Id");
        imageUser.setImage(new Image("/imgs/account.png"));
        books.clear();
        bookGridPane.getChildren().clear();
    }

    /**
     * Sets the UI for borrowing books.
     */
    @FXML
    public void gotoBorrow() {
        // Code here
        bookGridPane.getChildren().clear();
        menuButton.setText("Borrow");
        clear();

    }

    /**
     * Sets the UI for returning books.
     */
    @FXML
    public void gotoReturn() {
        // Code here
        bookGridPane.getChildren().clear();
        menuButton.setText("Return");
        clear();
    }

    /**
     * Cancels the user QR code scanning process.
     * 
     * @throws Exception If an error occurs while stopping the scanner.
     */
    @FXML
    public void gotoCancelScanUser() throws Exception {
        if (scannerUser != null)
            scannerUser.stopCamera();
        userPaneView.getChildren().clear();
        cancelScanUserButton.setVisible(false);
    }

    /**
     * Cancels the book ISBN scanning process.
     * 
     * @throws Exception If an error occurs while stopping the scanner.
     */
    @FXML
    public void gotoCancelScanBook() throws Exception {
        if (scannerBook != null)
            scannerBook.stopCamera();
        bookPaneView.getChildren().clear();
        cancelScanBookButton.setVisible(false);
    }

}
