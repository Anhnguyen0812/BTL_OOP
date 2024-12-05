package library.controller;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javafx.fxml.FXML;
import library.dao.BookDAO;
import library.model.Book;
import library.model.ISBNScannerWithUI;
import library.model.ConcreteBook;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

/**
 * Controller class for adding books to the library.
 */
public class AddBookController {
    private Book book1, book2;

    @FXML
    private Button AddButton, cancelScanButton;

    @FXML
    private TextField imgUrl, title, author, isbn, category, description, rateAvg, availble, availbleScan, linkQrCode;
    BookDAO bookDAO = BookDAO.getBookDAO();

    @FXML
    ImageView imageBook;

    @FXML
    private Label title2, author2, isbn2, description2, category2, isbnFind;
    @FXML
    private Pane root;
    ISBNScannerWithUI scanner = new ISBNScannerWithUI();

    /**
     * Shows an alert with the specified message and title.
     * 
     * @param message The message of the alert.
     * @param title   The title of the alert.
     */
    private void showAlert(String message, String title) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Initializes the controller class. This method is automatically called after
     * the
     * FXML file has been loaded.
     */
    public void initialize() {
        cancelScanButton.setVisible(false);
    }

    /**
     * Searches for a book by its ISBN and displays the book details.
     * 
     * @throws Exception If an error occurs during the search.
     */
    @FXML
    public void gotoFindBook() throws Exception {
        BookController bookController = new BookController();
        book2 = bookController.searchBookByISBN(isbnFind.getText());
        if (book2 != null) {
            imageBook.setImage(new Image(book2.getImageUrl(), true));
            title2.setText(book2.getTitle());
            author2.setText(book2.getAuthor());
            isbn2.setText(book2.getIsbn());
            description2.setText(book2.getDescription());
            category2.setText(book2.getCategories());
        } else {
            System.out.println("Book not found");
        }
    }

    /**
     * Initiates the scanning process for a book ISBN.
     * 
     * @throws Exception If an error occurs during the scanning process.
     */
    @FXML
    public void gotoScanBook() throws Exception {
        cancelScanButton.setVisible(true);
        scanner.show(root);
        scanner.setOnScanCompleteListener((isbntext) -> {
            isbnFind.setText("Isbn: " + isbntext);
            try {
                BookController bookController = new BookController();
                book2 = bookController.searchBookByISBN(isbntext);
                book2.setRateAvg(null);
                // Play success sound
                File audioFile = new File("src/main/resources/sound/success.wav");
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

                // Tạo và mở clip
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);

                // Phát âm thanh
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (book2 != null) {
                if (book2.getImageUrl() != null)
                    imageBook.setImage(new Image(book2.getImageUrl(), true));
                else
                    imageBook.setImage(new Image("/imgs/noBook.png", true));
                title2.setText(book2.getTitle());
                author2.setText(book2.getAuthor());
                isbn2.setText(book2.getIsbn());
                description2.setText(book2.getDescription());
                category2.setText(book2.getCategories());
            } else {
                System.out.println("Book not found");
            }
        });

    }

    /**
     * Adds a new book to the library with the details provided in the form.
     * 
     * @throws Exception If an error occurs during the book addition.
     */
    @FXML
    public void gotoAddBook1() throws Exception {

        if (title.getText().equals("") || author.getText().equals("") || isbn.getText().equals("")
                || availble.getText().equals("")) {
            showAlert("error", "Please fill all the important fields");
            return;
        } else
            book1 = new ConcreteBook(0,
                    title.getText(),
                    author.getText(),
                    isbn.getText(),
                    Integer.parseInt(availble.getText()),
                    description.getText().isEmpty() ? null : description.getText(),
                    imgUrl.getText().isEmpty() ? null : imgUrl.getText(),
                    linkQrCode.getText().isEmpty() ? null : linkQrCode.getText(),
                    rateAvg.getText().isEmpty() ? null : Double.parseDouble(rateAvg.getText()));

        if (book1 != null)
            bookDAO.addBookNoDuplicateIsbn(book1);
        else
            System.out.println("Book is null");

    }

    /**
     * Adds the scanned book to the library with the specified availability.
     * 
     * @throws Exception If an error occurs during the book addition.
     */
    @FXML
    public void gotoAddBook2() throws Exception {
        book2.setAvailable(Integer.parseInt(availbleScan.getText()));
        if (book2 != null)
            bookDAO.addBookNoDuplicateIsbn(book2);
        else
            System.out.println("Book is null");
        // scanner.stopCamera();

        showAlert("Book added successfully", "Success");
    }

    /**
     * Cancels the book ISBN scanning process.
     * 
     * @throws Exception If an error occurs while stopping the scanner.
     */
    @FXML
    public void gotoCancelScan() throws Exception {
        if (scanner != null)
            scanner.stopCamera();
        root.getChildren().clear();
        cancelScanButton.setVisible(false);
    }

}
