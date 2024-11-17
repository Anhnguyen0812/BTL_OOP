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
import library.dao.BookDAO;
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

    public void setBorrowButtonVisible() {
        BookDAO bookDao = BookDAO.getBookDAO();
        try {
            if (!bookDao.haveBook(book.getIsbn())) {
                borrowButton.setVisible(false);
            } else {
                checkBorrowed();
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void checkBorrowed() {
        BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAO();
        if (borrowRecordDAO.isBorrowed(user, book)) {
            borrowButton.setVisible(false);
        } else {
            borrowButton.setVisible(true);
        }
    }

    public void setReturnButton(BorrowRecord record) {
        borrowButton.setText("Return");
        borrowButton.setOnAction(event -> returnAction(record));
    }

    public void returnAction(BorrowRecord record) {
        BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAO();
        borrowRecordDAO.returnBook(record);
        borrowButton.setVisible(true);
    }

    @FXML
    public void borrowAction() {
        BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAO();
        BorrowRecord borrowRecord = new BorrowRecord(0, user, book, LocalDate.now(), null);
        borrowRecordDAO.addBorrowRecord(borrowRecord);
        borrowButton.setVisible(false);

    }

}
