package library.controller;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import com.gluonhq.impl.charm.a.b.b.s;
import com.gluonhq.impl.charm.a.b.b.u;
import javafx.scene.control.ChoiceBox;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import library.dao.BookDAO;
import library.dao.BorrowRecordDAO;
import library.model.Book;
import library.model.BorrowRecord;
import library.model.User;
import library.service.UserService;

public class DashController implements Initializable {

  @FXML
  private ChoiceBox<String> searchChoice, returnChoice;

  @FXML private TextField author, publisher, isbn, bookId, search;
  @FXML
  private Button reloadReturnBooksButton;
  // @FXML
  // private Insets  title;

  @FXML private TextField title;
  @FXML private Pane home, books, issueBooks, returnBooks, settings, noti, subUser;
  @FXML protected Button home_Button, books_Button, returnBook, Books, logOut, user_Button;
  @FXML protected TableColumn<BorrowRecord, Integer> Id_Book;
  @FXML protected TableColumn<BorrowRecord, String> Title_Book;
  @FXML protected TableColumn<BorrowRecord, Date> ngaymuon;
  @FXML protected TableColumn<BorrowRecord, Date> ngaytra;
  @FXML private Label welcome, date;
  @FXML private Button searchLib, searchGG, X, returnBooks_Button, issueBooks_Button, settings_Button;
  @FXML private TableView<BorrowRecord> List_Borrow;
  @FXML private Pane return_Book, add_Book;
  @FXML private Button changepass;  
  @FXML protected ProgressIndicator loading;

  @FXML private Label neww, old, verify;
  @FXML private TextField newpass, oldpass, verifypass;

  @FXML
  public ImageView imageBook;
  @FXML
  public Label titleBook;
  @FXML
  public Label authorBook;
  @FXML
  public Button seeDetailBook;  // Adjust based on your Book class
  @FXML
  private ScrollPane stackPane;
  @FXML
  public Pane book, boxchange;
  @FXML
  public Pane moredetail;
  @FXML
  private Button borrowBook;
  @FXML
  private Label error, iduser, username, email;
  @FXML
  private HBox Recently;
      private int currentIndex = 0;
      private List<Book> bookLists = new ArrayList<>();
      private List<Pane> paneList = new ArrayList<>(); // List to store all panes
      protected List<Book> recentBook = new ArrayList<>();
      protected List<Pane> paneRecentBook = new ArrayList<>();
      private List<BorrowRecord> borrowRecords = new ArrayList<>();
  protected BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAO();
  private BorrowRecord record;
  protected LocalDate today = LocalDate.now();
  protected User user;
  protected HostServices hostServices;
  protected BookDAO bookDAO = BookDAO.getBookDAO();
  protected final BookController bookController = new BookController();
  protected final BookDetailController bookDetailController = new BookDetailController();
  protected GaussianBlur blur = new GaussianBlur();
  
  public DashController() {}

  public DashController(User user, HostServices hostServices) {
    this.user = user;
    this.hostServices = hostServices;
  }

  protected void showAlert(String title, String message) {
    javafx.scene.control.Alert alert =
        new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  @Override
  public void initialize(URL url, ResourceBundle res) {

    // Set up ChoiceBox for search and return
    // searchChoice.getItems().addAll("Title", "Author", "ISBN");
    // returnChoice.getItems().addAll("Borrowed", "Returned", "unReturned");

    // // Set up values for ChoiceBox
    // searchChoice.setValue("Title");
    // returnChoice.setValue("Borrowed");


    welcome.setText("Welcome " + user.getName() + "!");
    user_Button.setText(user.getName());

    iduser.setText(String.valueOf("Id : " + user.getId()));
    username.setText("Username : " + user.getName());
    email.setText("Email : " + user.getEmail());
    
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy | EEEE, hh:mm a");
    Timeline timeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                event -> {
                  // Lấy thời gian hiện tại và định dạng
                  String formattedDateTime = LocalDateTime.now().format(formatter);
                  // Cập nhật vào label
                  date.setText(formattedDateTime);
                }));

    // Thiết lập lặp vô hạn
    timeline.setCycleCount(Timeline.INDEFINITE);
    // Bắt đầu timeline
    timeline.play();
    home.setVisible(true);
    books.setVisible(false);
    returnBooks.setVisible(false);
    issueBooks.setVisible(false);
    settings.setVisible(false);
    home_Button.styleProperty().set("-fx-background-color: #777777");

    try {
        borrowRecords.addAll(borrowRecordDAO.getBorrowRecordsByUser(user));
    } catch (SQLException e) {
        showAlert("Error", "Could not load borrow records.");
    }

    searchGG.setOnAction(
        e -> {
          loading.setVisible(true);
          String bookTitle = title.getText();
          String bookAuthor = author.getText();
          handleSearchBookGG(bookTitle, bookAuthor, new TableView<>());
          stackPane.setVisible(true);
          String defaultImagePath = getClass().getResource("/imgs/unnamed.jpg").toExternalForm();
          imageBook.setImage(new Image(defaultImagePath, true));
          book.setVisible(true);
        });

    searchLib.setOnAction(
        e -> {
          loading.setVisible(true);
          String bookTitle = title.getText();
          String bookAuthor = author.getText();
          stackPane.setVisible(true);
          handleSearchBookLib(bookTitle, bookAuthor);
          String defaultImagePath = getClass().getResource("/imgs/unnamed.jpg").toExternalForm();
          imageBook.setImage(new Image(defaultImagePath, true));
          book.setVisible(true);
        });
    // <TableColumn fx:id="Id" prefWidth="84.0" text="STT" />
    // Id.setCellValueFactory(new PropertyValueFactory<>("id"));
    Id_Book.setCellValueFactory(
        cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getBook().getId()));
    Title_Book.setCellValueFactory(
        cellData ->
            new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getBook().getTitle())));
    ngaymuon.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
    ngaytra.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

    try {
        List_Borrow.setItems(borrowRecordDAO.getBorrowRecordsByUser(user));
    } catch (SQLException e) {
        showAlert("Error", "Could not load borrow records.");
    }

    List_Borrow.setOnMouseClicked(
        event -> {
          if (event.getClickCount() == 2) { // Kiểm tra nhấp đúp
            BorrowRecord selectedBook = List_Borrow.getSelectionModel().getSelectedItem();
            return_Book.getChildren().clear();
            try {
              return_Book
                  .getChildren()
                  .add(bookDetailController.returnBookDetail(selectedBook.getBook(), user));
            } catch (IOException e) {
              showAlert("Error", "Could not load book details.");
            }
          }
        });

    stackPane.setOnMouseClicked(
        event -> {
          if (event.getTarget() instanceof Pane) {
            Pane selectedPane = (Pane) event.getTarget();
            int index = paneList.indexOf(selectedPane);
            if (index >= 0) {
              processSelected(selectedPane, bookLists.get(index));
            }
          }
        });

    seeDetailBook.setOnAction(event -> {
        Book selectedBook = getCurrentBook();
        moredetail.getChildren().clear();
        lammo();
        try {
            moredetail.getChildren().add(bookDetailController.asParent(selectedBook));
        } catch (IOException e) {
            showAlert("Error", "Could not load book details.");
        }
        moredetail.getChildren().add(X);
        moredetail.getChildren().add(borrowBook);
        moredetail.getChildren().add(error);
        moredetail.setStyle("-fx-background-color-:rgb(187, 189, 180);");
    });

    try {
        borrowRecordDAO.getBookRecent(recentBook);
    } catch (SQLException e) {
        showAlert("Error", "Could not load recent books.");
    }
    for (Book recent : recentBook) {
      Pane model = new Pane();
      try {
          model.getChildren().add(bookDetailController.recentBook(recent));
      } catch (IOException e) {
          showAlert("Error", "Could not load recent book details.");
      }
      Recently.getChildren().add(model);
      paneRecentBook.add(model);

      model.setOnMouseClicked(event -> {
        if (event.getClickCount() == 2) {
          model.setStyle("-fx-background-color: #D0E8FF;");
          moredetail.getChildren().clear();
          lammo();
          try {
              moredetail.getChildren().add(bookDetailController.asParent(recent));
          } catch (IOException e) {
              showAlert("Error", "Could not load book details.");
          }
          moredetail.getChildren().add(X);
          moredetail.getChildren().add(borrowBook);
          moredetail.getChildren().add(error);
          moredetail.setStyle("-fx-background-color-:rgb(187, 189, 180);");
          }
        });
      }

      changepass.setOnAction(event -> {
        boxchange.setVisible(true);
        lammo();
        moredetail.setVisible(false);
        borrowBook.setVisible(false);
        X.setVisible(false);
      });
      
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
    xoalammo();
  }

  public void gotoHome() {
    resetStyle();
    home.setVisible(true);
    home_Button.styleProperty().set("-fx-background-color: #777777");
  }

  public void gotoBooks() {
    resetStyle();
    books.setVisible(true);
    books_Button.styleProperty().set("-fx-background-color: #777777");
  }

  public void gotoReturnBooks() throws SQLException {
    resetStyle();
    returnBooks.setVisible(true);
    List_Borrow.setItems(borrowRecordDAO.getBorrowRecordsByUser(user));
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

  public void gotoSubUser() {
    if (subUser.isVisible()) {
      subUser.setVisible(false);
    } else {
      subUser.setVisible(true);
    }
  }

  public void gotoNoti() {
    if (noti.isVisible()) {
      noti.setVisible(false);
    } else {
      noti.setVisible(true);
    }
  }
  
  protected void handleSearchBookLib(String bookTitle, String bookAuthor) {
    Task<ObservableList<Book>> task =
        new Task<ObservableList<Book>>() {
          @Override
          protected ObservableList<Book> call() throws Exception {
            ObservableList<Book> foundBooks = FXCollections.observableArrayList();
            // Tìm kiếm theo tiêu đề
            if (!bookTitle.isEmpty()) {
              foundBooks.addAll(bookDAO.getBookByTitle(bookTitle));
            }
            // Tìm kiếm theo tác giả
            if (!bookAuthor.isEmpty()) {
              foundBooks.addAll(bookDAO.getBookByAuthor(bookAuthor));
            }
            Thread.sleep(500); // Giả lập việc tìm kiếm mất thời gianThrea
            return foundBooks;
          }

          @Override
          protected void succeeded() {
            loading.setVisible(false);
            super.succeeded();
            Platform.runLater(
                () -> {
                  bookLists.clear(); // Xóa kết quả cũ
                  bookLists.addAll(getValue()); // Thêm kết quả mới
                  try {
                      show(getValue());
                  } catch (IOException ioException) {
                      showAlert("Error", "Could not load book details.");
                  }
                });
          }

          @Override
          protected void failed() {
            loading.setVisible(false);
            super.failed();
            // Xử lý ngoại lệ nếu có
            bookLists.clear(); // Hoặc cập nhật một thông báo lỗi
          }
        };
    // Chạy task trong một luồng riêng
    new Thread(task).start();
  }

  protected void handleSearchBookGG(String bookTitle, String bookAuthor, TableView<Book> tableView) {
    Task<ObservableList<Book>> task =
        new Task<ObservableList<Book>>() {
          @Override
          protected ObservableList<Book> call() throws Exception {
            ObservableList<Book> foundBooks = FXCollections.observableArrayList();
            // Tìm kiếm theo tiêu đề
            if (!bookTitle.isEmpty()) {
              foundBooks.addAll(bookController.searchBook(bookTitle));
            }
            // Tìm kiếm theo tác giả
            if (!bookAuthor.isEmpty()) {
              foundBooks.addAll(bookController.searchBook(bookAuthor));
            }
            Thread.sleep(500); // Giả lập việc tìm kiếm mất thời gianThrea
            return foundBooks;
          }

          @Override
          protected void succeeded() {
            super.succeeded();
            Platform.runLater(
                () -> {
                  bookLists.clear(); // Xóa kết quả cũ
                  bookLists.addAll(getValue()); // Thêm kết quả mới
                  tableView.setItems(getValue());
                  try {
                    show(getValue());
                  } catch (IOException ioException) {
                    showAlert("Error", "Could not load book details.");
                  }
                });
          }

          @Override
          protected void failed() {
            loading.setVisible(false);
            super.failed();
            // Xử lý ngoại lệ nếu có
            bookLists.clear(); // Hoặc cập nhật một thông báo lỗi
          }
        };
    // Chạy task trong một luồng riêng
    new Thread(task).start();
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

    private void show(List<Book> books) throws IOException {
        VBox vbox = new VBox(20); // Create a VBox for the books with 10px spacing
        vbox.getStyleClass().add("sidebar");
        vbox.setStyle("-fx-background-radius: 15px;");
        paneList.clear();
        for (Book b : books) {
            Pane bookPane = new Pane();
            try {
                bookPane.getChildren().add(bookDetailController.createPane(b)); // Create a Pane for each book
            } catch (IOException e) {
                e.printStackTrace();
            }
            vbox.getChildren().add(bookPane); // Add the Pane to the VBox
            paneList.add(bookPane);

            bookPane.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    processSelected(bookPane, b);
                }
            });
        }
        stackPane.setContent(vbox);
        loading.setVisible(false); // Set the VBox as the content of the ScrollPane
        bookLists = books;
    }

    private void processSelected(Pane pane, Book book) {
        currentIndex = paneList.indexOf(pane);
        if (selectedPane != null) {
            // Reset the background color of the previously selected pane
            selectedPane.setStyle("-fx-background-color: #FFFFFF;");
            selectedPane.setStyle("-fx-cursor: hand;");
        }

        // Set the background color of the currently selected pane
        pane.setStyle("-fx-background-color: #D0E8FF;");  // Soft blue for selected

        selectedPane = pane;  // Update selectedPane to the new selection
        updateMainDisplay(book);  // Load selected book details

        scrollToPane(currentIndex);
    }

    private void scrollToPane(int index) {
        if (paneList.isEmpty()) return;
        // Get the total height of the VBox (container for all panes)
        double totalHeight = paneList.size() * (200 + 20); // Pane height + VBox spacing
        // Calculate the target scroll position based on the pane's position
        double targetY = index * (200 + 20);
        // Calculate scroll position within bounds by considering the height of the ScrollPane's viewport
        double viewportHeight = stackPane.getViewportBounds().getHeight();
        double vboxHeight = totalHeight + 20;  // Adding extra spacing
        // Adjust scroll value to center the selected pane, so it appears in the viewport if possible
        double scrollValue = (targetY - (viewportHeight / 2)) / (vboxHeight - viewportHeight);
        // Ensure scrollValue is within the bounds of 0 to 1
        stackPane.setVvalue(Math.max(0, Math.min(scrollValue, 1)));
    }

    private Pane selectedPane = null;  // Track the currently selected pane

    private void updateMainDisplay(Book book) {
        // Update the main display with the selected book's details
        if (book.getImageUrl() != null && !book.getImageUrl().isEmpty()) {
            imageBook.setImage(new Image(book.getImageUrl(), true));
        } else {
            String defaultImagePath = getClass().getResource("/imgs/unnamed.jpg").toExternalForm();
            imageBook.setImage(new Image(defaultImagePath, true));
        }
        titleBook.setText(book.getTitle());
        authorBook.setText(book.getAuthor());
    }

    public void previousBook(ActionEvent actionEvent) {
        if(bookLists != null) {
            currentIndex--;
            if(currentIndex < 0) {
                currentIndex = bookLists.size() - 1;
            }
            processSelected(paneList.get(currentIndex), bookLists.get(currentIndex));
        }
    }

    public void nextBook(ActionEvent actionEvent) {
        if(bookLists != null) {
            currentIndex++;
            if(currentIndex >= bookLists.size()) {
                currentIndex = 0;
            }
            processSelected(paneList.get(currentIndex), bookLists.get(currentIndex));
        }
    }

    public Button getSeeDetailBook() {
        return seeDetailBook;
    }

    public Book getCurrentBook() {
        return bookLists.get(currentIndex);
    }

    private void lammo() {
      moredetail.setStyle("-fx-background-color-:rgb(187, 189, 180);");
      moredetail.setVisible(true);
      borrowBook.setVisible(true);
      X.setVisible(true);
      // manageBooksPane.setStyle("-fx-background-color-: rgba(157, 146, 146, 0.5);");
      books.setEffect(blur);
      home.setEffect(blur);
      returnBooks.setEffect(blur);
      issueBooks.setEffect(blur);
      settings.setEffect(blur);
    }

    @FXML
    private void xoalammo() {
      moredetail.setVisible(false);
      books.setEffect(null);
      home.setEffect(null);
      returnBooks.setEffect(null);
      issueBooks.setEffect(null);
      settings.setEffect(null); 
      X.setVisible(false);
      boxchange.setVisible(false);
    }

    @FXML
    private void borrowBook() {
      Book selectedBook = getCurrentBook();
      try {
        if (selectedBook.isAvailable()) {
          record = new BorrowRecord(0, user, selectedBook, today, today.plusMonths(2));
          borrowRecordDAO.addBorrowRecord(record);
          borrowRecords.add(record);
          error.setText("Borrowed successfully!");
          error.setVisible(true);
                // Tạo Timeline để ẩn Label sau 5 giây
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
                    error.setVisible(false); // Ẩn Label sau 5 giây
                }));
                timeline.setCycleCount(1); // Chỉ chạy một lần
                timeline.play();
                xoalammo();
              }
              else {
                error.setText("Can not borrow this book!");
                error.setVisible(true);
                // Tạo Timeline để ẩn Label sau 5 giây
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
                    error.setVisible(false); // Ẩn Label sau 5 giây
                }));

                // Chạy Timeline
                timeline.setCycleCount(1); // Chỉ chạy một lần
                timeline.play();
              }
            } catch (Exception ex) {
              java.util.logging.Logger.getLogger(DashController.class.getName()).log(Level.SEVERE, null, ex);
            }
      
    }

    @FXML
    private void changePassword() throws SQLException, NoSuchAlgorithmException {
      String oldPassword = oldpass.getText();
      oldPassword = UserService.hashPassword(oldPassword, user.getSalt());
      String newPassword = newpass.getText();
      String checkPassword = UserService.hashPassword(newPassword, user.getSalt());
      String verifyPassword = verifypass.getText();
      if (!oldPassword.equals(user.getPassword())) {
        old.setVisible(true);
      } else if (checkPassword.equals(oldPassword)) {
        neww.setVisible(true);
        old.setVisible(false);
      } else if (!newPassword.equals(verifyPassword)) {
        verify.setVisible(true);
        neww.setVisible(false);
      } else {
          String salt = AdminController.getSalt();
          String hashedPassword = UserService.hashPassword(newPassword, salt);
          user.setPassword(hashedPassword);
          user.setSalt(salt);
          UserService userService =  new UserService();
          userService.editUser(user);
          old.setVisible(false);
          neww.setVisible(false);
          verify.setVisible(false);
      }
    }
}
