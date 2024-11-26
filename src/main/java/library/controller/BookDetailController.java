package library.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import library.dao.BookDAO;
import library.dao.BorrowRecordDAO;
import library.model.Book;
import library.model.BorrowRecord;
import library.model.User;
import library.service.UserService;

// import library.controller.BorrowRecordController; // Removed redundant import

public class BookDetailController {

  @FXML
  private Label titleLabel;
  @FXML
  private Label authorLabel;
  @FXML
  private Label isbnLabel, idLabel, assertLabel;
  @FXML
  private Label descriptionLabel, category;
  @FXML
  private ImageView bookImageView, imageRecentBook;
  @FXML
  private Label titleRecentBook;
  @FXML
  private ImageView qrCodeImageView; // ImageView để hiển thị mã QR
  @FXML
  private Button returnbook, addbook, delete, update;
  @FXML
  private Label notBorrowBook;
  @FXML
  private Pane updateBook;
  @FXML
  private Label modelTitle, modelAuthor, modelIsbn;

  @FXML private Label iduser, title, username, email, borrowdate, returndate;
  @FXML private ImageView imageBook;
  
  @FXML
  private Label password;
  @FXML
  private ImageView modelImage;
  private static User user;
  private static User user1;
  private static Book bookk;
  private BorrowRecord record;
  private BorrowRecordDAO borrowRecordDAO = BorrowRecordDAO.getBorrowRecordDAO();
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

  public void test(Book book) {
    modelTitle.setText(book.getTitle());
    modelAuthor.setText(book.getAuthor());
    modelIsbn.setText(book.getIsbn());
    // isbnLabel.setText(book.getIsbn());
    // Hiển thị hình ảnh nếu có
    if (book.getImageUrl() != null) {
      Image image = new Image(book.getImageUrl(), true);
      modelImage.setImage(image);
    } else {
      String defaultImagePath = getClass().getResource("/imgs/unnamed.jpg").toExternalForm();
      modelImage.setImage(new Image(defaultImagePath, true));
    }
  }

  public void setBookDetails(Book book) {
    titleLabel.setText("Title : " + book.getTitle());
    authorLabel.setText("Author : " + book.getAuthor());
    if (isbnLabel != null) {
      isbnLabel.setText("Isbn : " + book.getIsbn());
    }
    if (idLabel != null) {
      idLabel.setText(book.getId() > -1 ? "Id : " + book.getId() : "book not exist in database");
    }

    // Hiển thị mô tả nếu có
    if (book.getDescription() != null) {
      descriptionLabel.setText("Description : " + book.getDescription());
    } else {
      descriptionLabel.setText("No description available.");
    }

    // Hiển thị hình ảnh nếu có
    if (book.getImageUrl() != null) {
      Image image = new Image(book.getImageUrl(), true);
      bookImageView.setImage(image);
    } else {
      String defaultImagePath = getClass().getResource("/imgs/unnamed.jpg").toExternalForm();
      bookImageView.setImage(new Image(defaultImagePath, true));
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

    category.setText("Category : " + book.getCategories());
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
    if (update != null) {
      update.setOnAction(
          e -> {
            try {
              updateBook.setVisible(true);
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

  public Parent infoBook(Book book, User user, String link) throws IOException {
    BookDetailController.user = user;
    BookDetailController.bookk = book;
    if (book == null) {
      return null;
    }
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/Infobook.fxml"));
    Parent bookDetail = loader.load();
    BookDetailController controller = loader.getController();
    controller.setBookDetails(book);
    return bookDetail;
  }

  public Parent createPane(Book book) throws IOException {
    if (book == null) {
      return null;
    }
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/modelbook.fxml"));
    Parent bookDetail = loader.load();
    BookDetailController controller = loader.getController();
    controller.test(book);
    return bookDetail;
  }

  public ImageView getImageRecentBook() {
    return imageRecentBook;
  }

  public Label getTitleRecentBook() {
    return titleRecentBook;
  }

  public Parent recentBook(Book book) throws IOException {
    if (book == null) {
      return null; // Nếu book null, trả về null ngay lập tức.
    }
    // Load giao diện FXML của chi tiết sách
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/RecentBook.fxml"));
    Parent bookDetail = loader.load();
    // Lấy controller từ FXML
    BookDetailController controller = loader.getController();
    // Kiểm tra và thiết lập ảnh
    Image image;
    if (book.getImageUrl() != null && !book.getImageUrl().isEmpty()) {
      image = new Image(book.getImageUrl(), true); // true để tải ảnh không đồng bộ.
    } else {
      String defaultImagePath = getClass().getResource("/imgs/unnamed.jpg").toExternalForm();
      image = new Image(defaultImagePath, true);
    }
    controller.getImageRecentBook().setImage(image);
    controller.getTitleRecentBook().setText(book.getTitle());
    return bookDetail;
  }

  public Parent infoBorrow(Book book, User user, BorrowRecord record) throws IOException {
    if (book == null) {
      return null;
    }
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/infoBorrow.fxml"));
    Parent bookDetail = loader.load();
    BookDetailController controller = loader.getController();
    controller.setContentInfoBorrow(book, user, record);
    return bookDetail;
  }

  public void setContentInfoBorrow(Book book, User user, BorrowRecord record) throws IOException {
    if (book == null) {
      return;
    }
    title.setText(book.getTitle());
    // Hiển thị hình ảnh nếu có
    if (book.getImageUrl() != null) {
      Image image = new Image(book.getImageUrl(), true);
      imageBook.setImage(image);
    } else {
      String defaultImagePath = getClass().getResource("/imgs/unnamed.jpg").toExternalForm();
      imageBook.setImage(new Image(defaultImagePath, true));
    }
    // Hiển thị thông tin người mượn
    iduser.setText("Id : " + user.getId());
    username.setText("Username : " + user.getName());
    email.setText("Email : " + user.getEmail());
    borrowdate.setText("Borrow date : " + record.getBorrowDate());
    returndate.setText("Return date : " + record.getReturnDate());
  }

  @FXML
  public void createPassword() throws NoSuchAlgorithmException {
    password.setText("Password : 123456");
    password.setVisible(true);
    String salt = AdminController.getSalt();
    String pass = UserService.hashPassword("123456", salt);
    user1.setSalt(salt);
    user1.setPassword(pass);
    UserService userService = new UserService();
    userService.editUser(user1);
  }

}
