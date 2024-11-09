package library.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import library.dao.BookDAO;
import library.dao.BorrowRecordDAO;
import library.model.Book;
import library.model.BorrowRecord;
import library.model.User; // Add this import statement

// import library.controller.BorrowRecordController; // Removed redundant import

public class BookDetailController {

  @FXML private Label titleLabel;

  @FXML private Label authorLabel;

  @FXML private Label isbnLabel;

  @FXML private Label descriptionLabel, category;

  @FXML private ImageView bookImageView;

  @FXML private ImageView qrCodeImageView; // ImageView để hiển thị mã QR

  @FXML private Button returnbook, addbook, delete;

  private static User user;
  private static Book bookk;
  private BorrowRecord record;
  private BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAO();
  private LocalDate today = LocalDate.now();
  private BookDAO bookDAO = BookDAO.getBookDAO();

  public Image generateQRCode(String text, int width, int height)
      throws WriterException, IOException {
    QRCodeWriter qrCodeWriter = new QRCodeWriter();
    BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

    // Chuyển BitMatrix thành hình ảnh
    ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
    MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
    ByteArrayInputStream inputStream = new ByteArrayInputStream(pngOutputStream.toByteArray());

    return new Image(inputStream); // Trả về hình ảnh QR dưới dạng Image
  }

  public void setBookDetails(Book book) {
    titleLabel.setText(book.getTitle());
    authorLabel.setText(book.getAuthor());
    // isbnLabel.setText(book.getIsbn());

    // Hiển thị mô tả nếu có
    if (book.getDescription() != null) {
      descriptionLabel.setText(book.getDescription());
    } else {
      descriptionLabel.setText("No description available.");
    }

    // Hiển thị hình ảnh nếu có
    if (book.getImageUrl() != null) {
      Image image = new Image(book.getImageUrl());
      bookImageView.setImage(image);
    } else {
      bookImageView.setImage(null);
    }

    if (book.getQRcode() != null) {
      // Tạo mã QR từ URL sách
      try {
        Image qrCodeImage = generateQRCode(book.getQRcode(), 150, 150); // Kích thước 150x150 pixel
        qrCodeImageView.setImage(qrCodeImage); // Gửi mã QR sang BookDetailController
      } catch (WriterException | IOException e) {
        e.printStackTrace();
        // Handle the exception, e.g., show an error message to the user
      }
    }

    category.setText(book.getCategories());
  }

  public void showBookDetails(Book book) {
    if (book != null) {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/BookDetail.fxml"));
        Parent root = loader.load();
        ((BookDetailController) loader.getController()).setBookDetails(book);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Book Details");
        stage.show();
      } catch (Exception e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Failed to load book details.");
        alert.showAndWait();
      }
    }
  }

  public Parent asParent(Book book) throws IOException {
    if (book == null) {
      return null;
    }
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/BookDetail.fxml"));
    Parent bookDetail = loader.load();
    BookDetailController controller = loader.getController();
    controller.setBookDetails(book);
    return bookDetail;
  }

  public Parent addBookDetail(Book book, User user) throws IOException {
    BookDetailController.user = user;
    BookDetailController.bookk = book;
    if (book == null) {
      return null;
    }
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/Addbookdetail.fxml"));
    Parent bookDetail = loader.load();
    BookDetailController controller = loader.getController();
    controller.setBookDetails(book);
    return bookDetail;
  }

  @FXML
  public void initialize() {
    if (addbook != null) {
      addbook.setOnAction(
          e -> {
            try {
              if (bookk.isAvailable()) {
                record = new BorrowRecord(0, user, bookk, today, today.plusMonths(2));
                borrowRecordDAO.addBorrowRecord(record);
              }
              else {
                // TODO
              }
            } catch (Exception ex) {
              Logger.getLogger(BookDetailController.class.getName())
                  .log(Level.SEVERE, ex.getMessage(), ex);
            }
          });
    }
    if (returnbook != null) {
      returnbook.setOnAction(
          e -> {
            try {
              borrowRecordDAO.deleteBorrowRecord(bookk);
            } catch (Exception ex) {
              Logger.getLogger(BookDetailController.class.getName())
                  .log(Level.SEVERE, ex.getMessage(), ex);
            }
          });
    }
    if (delete != null) {
      delete.setOnAction(
          e -> {
            try {
              bookDAO.deleteBook(bookk.getId());
            } catch (Exception ex) {
              Logger.getLogger(BookDetailController.class.getName())
                  .log(Level.SEVERE, ex.getMessage(), ex);
            }
          });
    }
  }

  public Parent returnBookDetail(Book book, User user) throws IOException {
    BookDetailController.user = user;
    BookDetailController.bookk = book;
    if (book == null) {
      return null;
    }
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/Returnbookdetail.fxml"));
    Parent bookDetail = loader.load();
    BookDetailController controller = loader.getController();
    controller.setBookDetails(book);
    return bookDetail;
  }

  public Parent infoBook(Book book, User user) throws IOException {
    BookDetailController.user = user;
    BookDetailController.bookk = book;
    if (book == null) {
      return null;
    }
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/infoBook.fxml"));
    Parent bookDetail = loader.load();
    BookDetailController controller = loader.getController();
    controller.setBookDetails(book);
    return bookDetail;
  }
}
