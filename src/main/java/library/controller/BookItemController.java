/**
 * hiển thị danh sách sách trong thư viện thông qua GridPane.
 */
package library.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import library.model.Book;
import library.model.User;
import java.time.LocalDate;
import library.dao.BorrowRecordDAO;
import library.model.BorrowRecord;

public class BookItemController {
    @FXML
    private Label title, author, isbn;

    @FXML
    private ImageView bookImage;

    @FXML
    private Button borrowButton;

    User user;
    Book book;

    public void initialize() {
    }

    public void setBookData(Book books, int i, User user) {
        title.setText(i + ". " + books.getTitle());
        author.setText("by   " + books.getAuthor());
        isbn.setText("isbn  " + books.getIsbn());
        if (books.getImageUrl() != null) {
            Image image = new Image(books.getImageUrl(), true);
            bookImage.setImage(image);
        }
        this.user = user;
        this.book = books;

    }

    @FXML
    public void borrowAction() {
        BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAO();
        BorrowRecord borrowRecord = new BorrowRecord(0, user, book, LocalDate.now(), null);
        borrowRecordDAO.addBorrowRecord(borrowRecord);
    }

}
