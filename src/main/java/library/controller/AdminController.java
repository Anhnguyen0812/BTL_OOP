package library.controller;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import javafx.concurrent.Task;
import library.dao.BookDAO;
import library.dao.UserDAO;
import library.model.Book;
import library.model.BorrowRecord;
import library.model.ConcreteBook;
import library.model.User;
import library.service.UserService;

public class AdminController extends DashController implements Initializable {
  private final BoxBlur blur = new BoxBlur(10, 10, 3);

  // Các trường nhập liệu cho quản lý sách
  @FXML
  private TextField bookTitleField, descriptionField, linkImg, quantity;
  @FXML
  private TextField bookAuthorField;
  @FXML
  private TextField bookISBNField;
  @FXML
  private TextField bookCategoryField;
  // Bảng hiển thị danh sách sách
  @FXML
  private TableView<Book> bookTable;
  @FXML
  private TableColumn<Book, Integer> idC;
  @FXML
  private TableColumn<Book, String> titleColumn;
  @FXML
  private TableColumn<Book, String> authorColumn;
  @FXML
  private TableColumn<Book, String> isbnColumn;
  @FXML
  private TableColumn<Book, String> availableColumn;
  // Các trường nhập liệu cho quản lý người dùng
  @FXML
  private TextField usernameField;
  @FXML
  private TextField emailField;
  @FXML
  private TextField passwordField;
  @FXML
  private TextField roleField;
  // Bảng hiển thị danh sách người dùng
  @FXML
  private Button searchBookButton; // Add a button for search

  @FXML
  private TableColumn<?, ?> idColumn;
  @FXML
  private TableView<User> userTable;
  @FXML
  private TableColumn<User, String> usernameColumn;
  @FXML
  private TableColumn<User, String> emailColumn;
  @FXML
  private TableColumn<User, String> roleColumn;
  @FXML
  private TextField searchBook;
  @FXML
  private Button logOut;
  @FXML
  private Pane detailBook;
  @FXML
  private TextField searchAuthor;
  @FXML
  private TableView<Book> searchResult;
  @FXML
  private TableColumn<Book, String> Title;
  @FXML
  private TableColumn<Book, String> Author;
  @FXML
  private TableColumn<Book, String> ISBN;
  @FXML
  private TableColumn<?, ?> Categories;

  @FXML
  private TableView<BorrowRecord> borrowRecord;
  @FXML
  protected TableColumn<BorrowRecord, Integer> Id_User;
  @FXML
  protected TableColumn<BorrowRecord, String> Username;

  @FXML
  private Pane paneAU, paneAB;
  @FXML
  private Button AU;

  @FXML
  private Label iduser, username, email, newpassword, totalbook;
  @FXML
  private Button create;
  @FXML
  private Pane detailUser;

  @FXML
  private Label totalBook, totalUser, totalBorrow;
  @FXML
  private TextField updateTitle, updateAuthor, updateIsbn, updateDescription, updateImageUrl, updateQRcode,
      updateCategory;
  @FXML
  private Label greetingLabel;
  @FXML
  private Label dateTimeLabel;
  @FXML
  private Pane searchBookPane;
  @FXML
  private Pane manageBooksPane;
  @FXML
  private Pane manageUsersPane, ManagerBorrowBook, infoBorrow, updateB;
  @FXML
  private Pane infoBook;
  @FXML
  private Pane home;
  @FXML
  private AnchorPane origin;
  private static final Logger LOGGER = Logger.getLogger(AdminController.class.getName());
  @FXML
  private ImageView logofb, logoytb;
  @FXML
  private Button delete, updateBook, update;
  @FXML
  private PieChart pieChart;
  private final BookController bookController = new BookController();
  private final ObservableList<Book> bookList = FXCollections.observableArrayList();
  private ObservableList<User> userList = FXCollections.observableArrayList();
  private Book bookk;

  public AdminController(User user, HostServices hostServices) {
    super(user, hostServices); // Call the UserController constructor with user and hostServices
  }

  // Method to show the Search Book pane
  @FXML
  public void showSearchBookPane() {
    hideAllPanes(); // Hide all panes
    searchBookPane.setVisible(true); // Show Search Book Pane
  }

  // Phương thức khởi tạo, thiết lập các cột cho bảng sách và người dùng
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // origin.getStyleClass().add("pane-background");
    Image ytb = new Image("/imgs/logoytb.png", true);
    Image fb = new Image("/imgs/logofb.png", true);
    logoytb.setImage(ytb);
    logoytb.setOnMouseClicked(this::handleYouTubeClick);

    logofb.setImage(fb);
    logofb.setOnMouseClicked(this::handleFaceBookClick);

    logOut.setOnAction(event -> logOut());

    greetingLabel.setText("Hello, admin " + user.getName() + "!");
    // Thiết lập ngày và giờ hiện tại
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy | EEEE, hh:mm a");
    Timeline timeline = new Timeline(
        new KeyFrame(
            Duration.seconds(1),
            event -> {
              // Lấy thời gian hiện tại và định dạng
              String formattedDateTime = LocalDateTime.now().format(formatter);
              // Cập nhật vào label
              dateTimeLabel.setText(formattedDateTime);
            }));

    // Thiết lập lặp vô hạn
    timeline.setCycleCount(Timeline.INDEFINITE);
    // Bắt đầu timeline
    timeline.play();
    try {
      bookList.setAll(bookDAO.getAllBooks());
      userList.setAll(getUserList());
    } catch (SQLException e) {
      showAlert("Database Error", "Could not load books from the database.");
    }

    totalBook.setText(String.valueOf(bookList.size()));
    totalUser.setText(String.valueOf(userList.size()));
    totalBorrow.setText(String.valueOf(borrowRecordDAO.getAllBorrowRecords().size()));

    // Xử lý sự kiện tìm kiếm sách
    searchBookButton.setOnAction(
        event -> {
          try {
            detailBook.getChildren().clear();
            loading.setVisible(true);
            detailBook.getChildren().add(loading);
            searchResult.getItems().clear();
            handleSearchBookGG(searchBook.getText(), searchResult);
          } catch (Exception e) {
            showAlert("Error", "An error occurred while searching for books.");
          }
        });

    searchResult.setOnMouseClicked(
        event -> {
          if (event.getClickCount() == 2) { // Kiểm tra nhấp đúp
            Book selectedBook = searchResult.getSelectionModel().getSelectedItem();
            if (selectedBook != null) {
              detailBook.getChildren().clear();
              // Sử dụng CompletableFuture để tải dữ liệu trong một luồng nền
              CompletableFuture.runAsync(
                  () -> {
                    BookDetailController detailController = new BookDetailController();
                    try {
                      Parent bookDetailParent = detailController.asParent(selectedBook);
                      // Cập nhật giao diện trong luồng JavaFX
                      Platform.runLater(
                          () -> {
                            detailBook.getChildren().add(bookDetailParent);
                          });
                    } catch (IOException e) {
                      Platform.runLater(() -> showAlert("Error", "Could not load book details."));
                    }
                  });
            }
          }
        });

    borrowRecord.setOnMouseClicked(
        event -> {
            if (event.getClickCount() == 2) { // Kiểm tra nhấp đúp
              BorrowRecord record = borrowRecord.getSelectionModel().getSelectedItem();
              if (record != null) {
                  infoBorrow.getChildren().clear();
                  System.out.println(record.getBook().getId());
                  // Sử dụng CompletableFuture để tải dữ liệu trong một luồng nền
                  CompletableFuture.runAsync(() -> {
                      BookDetailController detailController = new BookDetailController();
                      try {
                          Parent bookDetailParent = detailController.infoBorrow(record.getBook(), record.getUser(), record);
                          // Cập nhật giao diện trong luồng JavaFX
                          Platform.runLater(() -> {
                            infoBorrow.getChildren().add(bookDetailParent);
                          });
                      } catch (IOException e) {
                          Platform.runLater(() -> showAlert("Error", "Could not load book details."));
                      }
                  });
              }
          }
        });

    userTable.setOnMouseClicked(event -> {
      if (event.getClickCount() == 2) { // Kiểm tra nhấp đúp
        detailUser.setVisible(true);

        manageUsersPane.setEffect(blur);
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        iduser.setText(String.valueOf("ID : " + selectedUser.getId()));
        username.setText("Username : " + selectedUser.getName());
        email.setText("Email : " + selectedUser.getEmail());
        ObservableList<BorrowRecord> borrowRecords = FXCollections.observableArrayList();
        try {
          borrowRecords = borrowRecordDAO.getBorrowRecordsByUserId(selectedUser);
        } catch (SQLException e) {
          showAlert("Database Error", "Could not load borrow records from the database.");
        }
        totalbook.setText("Total Book : " + borrowRecords.size());

        create.setOnAction(
            e -> {
              newpassword.setText("123456");
              newpassword.setVisible(true);
              String salt = null;
              try {
                salt = getSalt();
              } catch (NoSuchAlgorithmException ex) {
                showAlert("Error", "Could not generate salt for password hashing.");
                return;
              }
              String pass = null;
              try {
                pass = UserService.hashPassword("123456", salt);
              } catch (NoSuchAlgorithmException ex) {
                showAlert("Error", "Could not hash password.");
              }
              selectedUser.setPassword(pass);
              selectedUser.setSalt(salt);
              UserService userService = new UserService();
              userService.editUser(selectedUser);
            });
      }
    });

    bookTable.setOnMouseClicked(
        event -> {
          if (event.getClickCount() == 2) { // Kiểm tra nhấp đúp
            Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
            if (selectedBook != null) {
              bookk = selectedBook;
              infoBook.getChildren().clear();

              // Sử dụng CompletableFuture để tải dữ liệu trong một luồng nền
              CompletableFuture.runAsync(
                  () -> {
                    BookDetailController detailController = new BookDetailController();
                    try {
                      Parent bookDetailParent = detailController.infoBook(selectedBook, user, "/library/infoBook.fxml");
                      // Cập nhật giao diện trong luồng JavaFX
                      Platform.runLater(
                          () -> {
                            infoBook.getChildren().add(bookDetailParent);
                            infoBook.getChildren().add(update);
                          });
                    } catch (IOException e) {
                      Platform.runLater(() -> showAlert("Error", "Could not load book details."));
                    }
                  });
            }
          }
        });

    if (update != null) {
      update.setOnAction(
          e -> {
            try {
              lammo();
            } catch (Exception ex) {
              Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
          });
    }

    if (updateBook != null) {
      updateBook.setOnAction(
          e -> {
            try {
              int id = bookk.getId();
              String title = updateTitle.getText() != null ? updateTitle.getText() : bookk.getTitle();
              String author = updateAuthor.getText() != null ? updateAuthor.getText() : bookk.getAuthor();
              String isbn = updateIsbn.getText() != null ? updateIsbn.getText() : bookk.getIsbn();
              int available = bookk.isAvailable();
              String description = updateDescription.getText() != null ? updateDescription.getText()
                  : bookk.getDescription();
              String imageUrl = updateImageUrl.getText() != null ? updateImageUrl.getText() : bookk.getImageUrl();
              String qrCode = updateQRcode.getText() != null ? updateQRcode.getText() : bookk.getQRcode();
              String updatedCategory = updateCategory.getText() != null ? updateCategory.getText()
                  : bookk.getCategories();
              Book temp2 = new ConcreteBook(id, title, author, isbn, available, description, imageUrl, qrCode);
              temp2.setCategories(updatedCategory);
              bookDAO.updateBook(temp2);
            } catch (Exception ex) {
              Logger.getLogger(BookDetailController.class.getName())
                  .log(Level.SEVERE, ex.getMessage(), ex);
            }
          });
      xoalammo();
    }

    // Thiết lập cột cho bảng sách
    idC.setCellValueFactory(new PropertyValueFactory<>("id"));
    titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
    authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
    isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
    availableColumn.setCellValueFactory(new PropertyValueFactory<>("available"));
    bookTable.setItems(bookList);

    usernameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
    idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
    userTable.setItems(userList);

    Title.setCellValueFactory(new PropertyValueFactory<>("title"));
    Author.setCellValueFactory(new PropertyValueFactory<>("author"));
    ISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
    Categories.setCellValueFactory(new PropertyValueFactory<>("categories"));

    // Bang thong tin sach muon
    Id_Book.setCellValueFactory(
        cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getBook().getId()));
    Title_Book.setCellValueFactory(
        cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getBook().getTitle())));
    Id_User.setCellValueFactory(
        cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getUser().getId()));
    Username.setCellValueFactory(
        cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getUser().getName())));
    ngaymuon.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
    ngaytra.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
    borrowRecord.setItems(borrowRecordDAO.getAllBorrowRecords());

    AU.setOnAction(
        e -> {
          try {
            paneAU.setVisible(true);
            manageUsersPane.setEffect(blur);
          } catch (Exception ex) {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
          }
        });
  }

  @FXML
  public void Home() {
    hideAllPanes(); // Hide all panes
    home.setVisible(true); // Show Manage Books Pane
  }

  // Method to show the Manage Books pane
  @FXML
  public void showManageBooksPane() throws SQLException {
    hideAllPanes(); // Hide all panes
    bookList.clear();
    bookList.setAll(bookDAO.getAllBooks());
    bookTable.setItems(bookList);
    totalBook.setText(String.valueOf(bookList.size()));
    infoBook.getChildren().clear();
    xoalammo();
    manageBooksPane.setVisible(true); // Show Manage Books Pane
  }

  // Method to show the Manage Users pane
  @FXML
  public void showManageUsersPane() throws SQLException {
    hideAllPanes(); // Hide all panes
    userList.clear();
    userList.setAll(getUserList());
    totalUser.setText(String.valueOf(userList.size()));
    userTable.setItems(userList);
    manageUsersPane.setVisible(true); // Show Manage Users Pane
  }

  @FXML
  public void showManagerBorrowBook() {
    hideAllPanes(); // Hide all panes

    borrowRecord.setItems(borrowRecordDAO.getAllBorrowRecords());
    ManagerBorrowBook.setVisible(true); // Show Manage Users Pane
  }

  // Hide all panes
  private void hideAllPanes() {
    home.setVisible(false);
    searchBookPane.setVisible(false);
    manageBooksPane.setVisible(false);
    manageUsersPane.setVisible(false);
    logoytb.setVisible(true);
    logofb.setVisible(true);
    ManagerBorrowBook.setVisible(false);
    updateB.setVisible(false);
  }
  // Xử lý sự kiện đăng xuất
  // Add a button for logout
  @FXML
  private void gotoAddBook() {
    if (paneAB.visibleProperty().get()) {
      paneAB.setVisible(false);
      manageBooksPane.setEffect(null);
    } else {
      paneAB.setVisible(true);
      manageBooksPane.setEffect(blur);
    }
  }

  @FXML
  private void handleAddBook() throws SQLException, InterruptedException {
    String title = bookTitleField.getText();
    String authorName = bookAuthorField.getText();
    String isbn = bookISBNField.getText();
    int available = quantity.getText().isEmpty() ? 1 : Integer.parseInt(quantity.getText());
    String category = bookCategoryField.getText();
    String description = descriptionField.getText();
    String imageUrl = !linkImg.getText().isEmpty() ? linkImg.getText() : null;
    // Kiểm tra đầu vào
    if (title.isEmpty() || authorName.isEmpty()) {
      showAlert("Input Error", "All fields must be filled.");
      return;
    }

    Book checkBook = BookDAO.getBookDAO().getBookByISBN(isbn);
    // Thêm sách vào danh sách
    if (checkBook != null) {
      available += checkBook.isAvailable();
    }
    Book temp2 = new ConcreteBook(0, title, authorName, isbn, available, description, imageUrl, null);
    temp2.setCategories(category);
    bookList.add(temp2);
    BookDAO bookDAO = BookDAO.getBookDAO();
    bookDAO.addBook(temp2);
    // Xóa các trường nhập liệu sau khi thêm sách
    bookTitleField.clear();
    bookAuthorField.clear();
    bookISBNField.clear();
    bookCategoryField.clear();
    descriptionField.clear();
    linkImg.clear();
    quantity.clear();
  }

  public static String getSalt() throws NoSuchAlgorithmException {
    // Tạo ra salt ngẫu nhiên với SecureRandom
    SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
    byte[] salt = new byte[16];
    sr.nextBytes(salt);
    // Mã hóa salt thành chuỗi base64 để dễ lưu trữ
    return Base64.getEncoder().encodeToString(salt);
  }

  // Xử lý thêm người dùng
  @FXML
  private void handleAddUser() throws NoSuchAlgorithmException, SQLException {
    String username = usernameField.getText();
    String email = emailField.getText();
    String password = passwordField.getText();
    String role = roleField.getText();
    // Kiểm tra đầu vào
    if (username.isEmpty() || email.isEmpty()) {
      showAlert("Input Error", "All fields must be filled.");
      return;
    }
    String hashPassword = UserService.hashPassword(password, getSalt());
    // Thêm người dùng vào danh sách
    User newUser = new User(0, username, email, hashPassword, role, getSalt());
    UserDAO userDAO = UserDAO.getUserDAO();
    userDAO.addUser(newUser);

    userList.add(newUser);
    // Xóa các trường nhập liệu sau khi thêm người dùng
    usernameField.clear();
    emailField.clear();
    passwordField.clear();
    roleField.clear();
  }

  // Hiển thị thông báo lỗi hoặc thành công

  public ObservableList<User> getUserList() throws SQLException {
    return UserDAO.getAllUsers();
  }

  private void handleYouTubeClick(MouseEvent event) {
    try {
      String url = "https://www.youtube.com/@cristiano";
      // Mở link bằng trình duyệt mặc định
      hostServices.showDocument(url);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void handleFaceBookClick(MouseEvent event) {
    try {
      String url = "https://www.facebook.com/th1enq";
      // Mở link bằng trình duyệt mặc định
      hostServices.showDocument(url);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void lammo() {
    updateB.setStyle("-fx-background-color-:rgb(165, 190, 67);");
    updateB.setVisible(true);
    updateB.getStyleClass().add("pane-background");
    // manageBooksPane.setStyle("-fx-background-color-: rgba(157, 146, 146, 0.5);");
    manageBooksPane.setEffect(blur);
  }

  @FXML
  private void xoalammo() {
    updateB.setVisible(false);
    manageBooksPane.setEffect(null);
    manageUsersPane.setEffect(null);
    newpassword.setVisible(false);
    detailUser.setVisible(false);
    paneAU.setVisible(false);
  }

  @FXML
  private TextField infoUser;
  @FXML
  private MenuButton searchField;

  @FXML
  private void searchUser() {
    String info = infoUser.getText();
    String search = searchField.getText();
    try {
      userList.clear();
      if (search.equals("Username")) {
        userList.setAll(UserDAO.getUserByName(info));
      } else if (search.equals("Email")) {
        userList.setAll(UserDAO.getUserByEmail(info));
      } else if (search.equals("Iduser")) {
        UserDAO userDAO = UserDAO.getUserDAO();
        userList.setAll(userDAO.getUserById(Integer.parseInt(info)));
      } else {
        userList.setAll(UserDAO.getAllUsers());
      }
      userTable.setItems(userList);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  protected void handleSearchBookGG(String bookTitle, TableView<Book> tableView) {
    Task<ObservableList<Book>> task =
        new Task<ObservableList<Book>>() {
          @Override
          protected ObservableList<Book> call() throws Exception {
            ObservableList<Book> foundBooks = FXCollections.observableArrayList();
            // Tìm kiếm theo tiêu đề
            if (!bookTitle.isEmpty()) {
              foundBooks.addAll(bookController.searchBookByTitleMaxResult(bookTitle, 20));
            }
            Thread.sleep(500);
            return foundBooks;
          }
          @Override
          protected void succeeded() {
            super.succeeded();
            Platform.runLater(
                () -> {
                  tableView.setItems(getValue());
                  loading.setVisible(false);
                });
          }
          @Override
          protected void failed() {
            loading.setVisible(false);
            super.failed();
          }
        };
    // Chạy task trong một luồng riêng
    new Thread(task).start();
  }
}
