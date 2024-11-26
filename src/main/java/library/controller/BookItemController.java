/**
 * hiển thị danh sách sách trong thư viện thông qua GridPane.
 */
package library.controller;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import library.dao.BookDAO;
import library.dao.BorrowRecordDAO;
import library.dao.NotiDAO;
import library.model.Book;
import library.model.BorrowRecord;
import library.model.User;

public class BookItemController {
    @FXML
    private Label title, author, isbn, ratingLabel;

    @FXML
    private ImageView bookImage;

    @FXML
    private Button borrowButton;

    @FXML
    private Canvas star1, star2, star3, star4, star5;

    private static User user;
    private static Book book;

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
        book = books;
    }

    public void setBorrowButtonVisible() {
        BookDAO bookDao = BookDAO.getBookDAO();
        try {
            if (!bookDao.haveBook(book.getIsbn())) {
                borrowButton.setVisible(false);
            } else if (book.isAvailable() == 0) {
                borrowButton.setVisible(false);
            } else {
                checkBorrowed();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void checkBorrowed() {
        BorrowRecordDAO borrowRecordDAO = BorrowRecordDAO.getBorrowRecordDAO();
        if (borrowRecordDAO.isBorrowed(user, book)) {
            borrowButton.setVisible(false);
        } else {
            borrowButton.setVisible(true);
        }
    }

    public void hideButton() {
        borrowButton.setVisible(false);
    }

    public void setReturnButton(BorrowRecord record) {
        borrowButton.setText("Return");
        borrowButton.setOnAction(event -> returnAction(record));
    }

    public void returnAction(BorrowRecord record) {
        BorrowRecordDAO borrowRecordDAO = BorrowRecordDAO.getBorrowRecordDAO();
        borrowRecordDAO.addResquestReturnRecord(record);
        NotiDAO notiDAO = NotiDAO.geNotiDAO();
        try {
            notiDAO.addNotificationFromUserToAdmin(1,
                    "User " + user.getId() + ", Name: " + user.getName() + " return book " + book.getTitle());
        } catch (Exception e) {
            System.out.println(e);
        }
        borrowButton.setVisible(true);
    }

    public void displayBookRate(double rateAvg, boolean isHaveRate) {
        if (!isHaveRate) {
            ratingLabel.setText("No rate");
            rateAvg = 0.0;
        } else {
            ratingLabel.setText("rate  " + String.format("%.1f", rateAvg));
        }
        ratingLabel.setUnderline(true);

        Canvas[] stars = { star1, star2, star3, star4, star5 };
        for (int i = 0; i < stars.length; i++) {
            GraphicsContext gc = stars[i].getGraphicsContext2D();
            gc.clearRect(0, 0, stars[i].getWidth(), stars[i].getHeight());
            if (rateAvg >= i + 0.8) {
                drawStar(gc, Color.YELLOW, 1.0);
            } else if (rateAvg >= i + 0.2) {
                drawStar(gc, Color.YELLOW, 0.5);
            } else {
                drawStar(gc, Color.GRAY, 1.0);
            }
        }
    }

    private void drawStar(GraphicsContext gc, Color color, double fillRatio) {
        double width = 15;
        double height = 15;
        gc.setFill(color);
        gc.beginPath();
        gc.moveTo(width * 0.5, 0);
        gc.lineTo(width * 0.61, height * 0.35);
        gc.lineTo(width, height * 0.35);
        gc.lineTo(width * 0.68, height * 0.57);
        gc.lineTo(width * 0.79, height);
        gc.lineTo(width * 0.5, height * 0.75);
        gc.lineTo(width * 0.21, height);
        gc.lineTo(width * 0.32, height * 0.57);
        gc.lineTo(0, height * 0.35);
        gc.lineTo(width * 0.39, height * 0.35);
        gc.closePath();
        gc.fill();

        if (fillRatio < 1.0) {
            gc.setFill(Color.GRAY);
            gc.beginPath();
            gc.moveTo(width * 0.5, 0);
            gc.lineTo(width * 0.61, height * 0.35);
            gc.lineTo(width, height * 0.35);
            gc.lineTo(width * 0.68, height * 0.57);
            gc.lineTo(width * 0.79, height);
            gc.lineTo(width * 0.5, height * 0.79);
            // gc.lineTo(width * 0.5, height);
            // gc.lineTo(width * 0.32, height * 0.57);
            // gc.lineTo(0, height * 0.35);
            // gc.lineTo(width * 0.39, height * 0.35);
            gc.closePath();
            gc.fill();
        }
    }

    @FXML
    public void borrowAction() {
        BorrowRecordDAO borrowRecordDAO = BorrowRecordDAO.getBorrowRecordDAO();
        BorrowRecord borrowRecord = new BorrowRecord(1, user, book, LocalDate.now(), null);
        borrowRecordDAO.addResquestBorrowRecord(borrowRecord);
        borrowButton.setVisible(false);
        NotiDAO notiDAO = NotiDAO.geNotiDAO();
        try {
            notiDAO.addNotificationFromUserToAdmin(1,
                    "User " + user.getId() + ", Name: " + user.getName() + " request to borrow book "
                            + book.getTitle());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
