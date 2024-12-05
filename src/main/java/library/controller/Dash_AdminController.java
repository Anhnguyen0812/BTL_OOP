package library.controller;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.mapping.Table;
import org.springframework.cglib.core.Local;

import com.gluonhq.charm.glisten.control.Avatar;
import com.gluonhq.impl.charm.a.a.a;
import com.gluonhq.impl.charm.a.b.b.s;
import com.gluonhq.impl.charm.a.b.b.u;
import com.gluonhq.impl.charm.a.b.b.m;
import com.gluonhq.impl.charm.a.b.b.t;
import javafx.scene.control.Label;
import javafx.scene.Node;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.geometry.Insets;
import javafx.animation.AnimationTimer;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import library.dao.BookDAO;
import library.dao.BorrowRecordDAO;
import library.dao.NotiDAO;
import library.dao.UserDAO;
import library.model.Book;
import library.model.BorrowRecord;
import library.model.ImageHandler;
import library.model.User;
import library.model.UserQRCode;
import library.dao.AllDao;
import library.model.Admin;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class Dash_AdminController {

  @FXML
  private TextField author, publisher, isbn, bookId, search;
  // @FXML
  // private Insets title;

  @FXML
  private TextField title, userManageTextField, imgUrlField, titleField, authorField, isbnField, categoryField;

  @FXML
  private Pane home, books, issueBooks, manageUsers, settings, noti, subUser, pane, editBookPane, editUserPane, addUserPane;;
  @FXML
  private TableColumn<BorrowRecord, Integer> idIssueBorrow, availbleIssueBorrow, statusIssueBorrow;;
  @FXML
  private TableColumn<BorrowRecord, String> titleIssueBorrow, isbnIssueBorrow, userIssueBorrow;
  @FXML
  private TableColumn<BorrowRecord, LocalDate> borrowDateIssueBorrow, returnDateIssueBorrow;
  @FXML
  private TableColumn<BorrowRecord, Void> actionBorrow;

  @FXML
  private Button searchLib, searchGG, home_Button, books_Button, returnBooks_Button, issueBooks_Button, settings_Button;
  @FXML
  private Button addBookButton, editBookButton, deleteBookButton, qrCodeButton, applyEditBook, cancelEditBook,
  applyEditUserButton, cancelEditUser, cancelAddUser;
  @FXML
  private TextField descriptionField, rate_avgField, availbleField, QRCodeUrlField, usernameField, mailField, roleField,
  passwordField, confirmField, nameUserAddField, mailUserAddField, roleUserAddField, passwordAddField,
  confirmpassAddField;
  @FXML
  private Button acceptAddUserButton, cancelAddUserButton;
  @FXML
  private Label totalBooksLabel, borrowedLabel;
  @FXML
  private TableView<Book> ListBooks;
  @FXML
  private TableView<BorrowRecord> borrowRequestTable, returnRequestTable;

  @FXML
  private Pane return_Book, add_Book;

  @FXML
  protected ProgressIndicator loading;
  @FXML
  private ListView<String> notiList;

  @FXML
  private Label welcome, date, notiNewLabel, titleManage, authorManage, isbnManage, categoryManage, availbleManage,
      borrowedManage, nameUser, emailUser, borrowBookUser, notReturnedUser;
  @FXML
  private GridPane searchView, searchReturnBooks, warringGridPane, searchUser;
  @FXML
  private ChoiceBox<String> searchChoice, searchUserBox;
  @FXML
  private ChoiceBox<Button> requestBox;

  @FXML
  private ImageView avatar, avatar1, imageManageBook, imageManageUser, qrUser;

  protected BorrowRecordDAO borrowRecordDAO = BorrowRecordDAO.getBorrowRecordDAO();
  protected Admin user;
  protected HostServices hostServices;

  @FXML
  private Button searchUserButton;

  @SuppressWarnings("rawtypes")
  @FXML
  BarChart barChart;

  AllDao allDao = new AllDao();

  @FXML
  private Button Books, logOut, user_Button;
  @FXML
  private ProgressIndicator p1, p2, p3;
  @FXML
  private Tab directlyTab;

  public static final int MAX_COLUMN_RESULTS = 150;
  public static final int MAX_RESULTS_EACH_TIME = 40;

  List<Book> booksTop;
  List<Book> booksNew;
  List<Book> booksRecent;

  protected BookDAO bookDAO = BookDAO.getBookDAO();
  private NotiDAO notiDAO = NotiDAO.geNotiDAO();
  protected final BookController bookController = new BookController();

  double scrollSpeed = 2; // Tốc độ cuộn (pixels/giây)

  boolean isHomeTop = true;

  private User manageUserEdit;
  private Book manageBookEdit;

  public Dash_AdminController() {
  }

  public Dash_AdminController(User user, HostServices hostServices) {
    this.user = (Admin) user;
    this.hostServices = hostServices;
  }

  protected void showAlert(String title, String message) {
    // Implementation for showing an alert
    // For example, using JavaFX Alert:
    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  public void initialize() throws SQLException {
    
    // Set up title for table
    welcome.setText("Welcome " + user.getName() + "!");
    user_Button.setText(user.getName());

    // Set up date
    // Set up date
    // Update date label with current date and time
    java.util.Date now = new java.util.Date();
    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEEE, dd MMMM yyyy -  HH:mm",
        new java.util.Locale("vi", "VN"));
    date.setText(sdf.format(now));

    // Schedule a task to update the date label every second
    javafx.animation.Timeline dateUpdateTimeline = new javafx.animation.Timeline(
        new javafx.animation.KeyFrame(
            javafx.util.Duration.seconds(15),
            event -> date.setText(sdf.format(new java.util.Date()))));
    dateUpdateTimeline.setCycleCount(javafx.animation.Animation.INDEFINITE);
    dateUpdateTimeline.play();

    // Set Pane
    home.setVisible(true);
    books.setVisible(false);
    manageUsers.setVisible(false);
    issueBooks.setVisible(false);
    settings.setVisible(false);
    home_Button.styleProperty().set("-fx-background-color: #777777");

    // Set up ChoiceBox for search and return
    searchChoice.getItems().addAll("Title", "Author", "ISBN");
    searchUserBox.getItems().addAll("Name", "Mail");
    Button buttonBorrow = new Button("Borrow");
    Button buttonReturn = new Button("Return");
    requestBox.getItems().addAll(buttonBorrow, buttonReturn);
    requestBox.setConverter(new StringConverter<Button>() {
      @Override
      public String toString(Button button) {
          return button.getText(); // Chỉ lấy text từ Button
      }

      @Override
      public Button fromString(String string) {
          return null; // Không cần sử dụng ở đây
      }
  });

  requestBox.setOnAction(e -> {
    Button selectedButton = requestBox.getValue(); // Lấy Button được chọn
    if (selectedButton != null) {
        selectedButton.fire(); // Kích hoạt hành động của Button
    }
});

  buttonBorrow.setOnAction(event -> {
    gotoIssueBooks();
  });

  buttonReturn.setOnAction(event -> {
    ObservableList<BorrowRecord> data = borrowRecordDAO.getReturnRequest();
    borrowRequestTable.setItems(data);
  });

    // Set up values for ChoiceBox
    searchChoice.setValue("Title");
    searchUserBox.setValue("Name");
    requestBox.setValue(buttonBorrow);

    // Set up barchart

    // Set up avatar
    handleLoadImage(avatar);
    avatar1.setImage(avatar.getImage());
    avatar1.setPreserveRatio(true);

    applyCircularClip(avatar);
    applyCircularClip(avatar1);

    // Borrowed Books
    int bookStatus0 = borrowRecordDAO.countBookRequest(user.getId());
    int bookStatus1 = borrowRecordDAO.countBookBorrowed(user.getId());
    int bookStatus2 = borrowRecordDAO.countBookReturnRequest(user.getId());

    // Display borrow information in notiList
    notiList.getItems().clear();
    List<String> ListNoti = notiDAO.getNotificationsFromAdminToUser(user.getId());
    notiNewLabel.setText(String.valueOf(ListNoti.size()));
    for (int i = 0; i < ListNoti.size(); i++) {
      notiList.getItems().add(i + 1 + ". " + ListNoti.get(i));
    }
    notiList.getItems().add("Borrow Requests: " + bookStatus0);
    notiList.getItems().add("Books Borrowed: " + bookStatus1);
    notiList.getItems().add("Return Requests: " + bookStatus2);
    notiList.getItems().add("Books Returned: " + borrowRecordDAO.countBookReturned(user.getId()));

    double scrollSpeed = 2; // Tốc độ cuộn (pixels/giây)

    // Set issue book table
    borrowRequestTable.getColumns().clear();
    idIssueBorrow.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getId()));
    titleIssueBorrow.setCellValueFactory(param -> {
      if (param.getValue().getBook() != null)
      return new ReadOnlyObjectWrapper<>(
        "(id:" + param.getValue().getBook().getId() + ")-" + param.getValue().getBook().getTitle());
      else
        return new ReadOnlyObjectWrapper<>(param.getValue().getBook().getTitle());
    });

    isbnIssueBorrow.setCellValueFactory(param -> {
      if (param.getValue().getBook() != null)
        return new ReadOnlyObjectWrapper<>(param.getValue().getBook().getIsbn());
      else
        return new ReadOnlyObjectWrapper<>("");
    });
    // availbleIssueBorrow = new TableColumn<>("Available");
    availbleIssueBorrow.setCellValueFactory(param -> {
      if (param.getValue().getBook() != null)
        return new ReadOnlyObjectWrapper<>(param.getValue().getBook().isAvailable());
      else
        return new ReadOnlyObjectWrapper<>(0);
    });
    // userIssueBorrow = new TableColumn<>("User");
    userIssueBorrow.setCellValueFactory(param -> {
      if (param.getValue().getUser() != null)
        return new ReadOnlyObjectWrapper<>(
            "(id:" + param.getValue().getUser().getId() + ")-" + param.getValue().getUser().getName());
      else
        return new ReadOnlyObjectWrapper<>("");
    });
    // borrowDateIssueBorrow = new TableColumn<>("Borrow Date");
    borrowDateIssueBorrow.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
    ///returnDateIssueBorrow = new TableColumn<>("return_date");
    returnDateIssueBorrow.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
    statusIssueBorrow.setCellValueFactory(new PropertyValueFactory<>("status"));
    // Cột chứa nút xác nhận
    // actionBorrow = new TableColumn<>("Actions");
    actionBorrow.setCellFactory(param -> new TableCell<>() {
      private final Button acceptButton = new Button("Accept");
      private final Button rejectButton = new Button("Reject");

      {
        acceptButton.setOnAction(event -> {
          if (requestBox.getValue().getText().equals("Borrow")) {
            BorrowRecord request = getTableView().getItems().get(getIndex());
            System.out.println("Accepted: " + request.getId());
            user.acceptRequestBorrow(request);
            getTableView().getItems().remove(request); // Xóa yêu cầu khỏi danh sách
          } else {
            BorrowRecord request = getTableView().getItems().get(getIndex());
            System.out.println("Accepted: " + request.getId());
            user.acceptRequestReturn(request);
            getTableView().getItems().remove(request); // Xóa yêu cầu khỏi danh sách
          }
        });

        rejectButton.setOnAction(event -> {
          if (requestBox.getValue().getText().equals("Borrow")) {
            BorrowRecord request = getTableView().getItems().get(getIndex());
            System.out.println("Rejected: " + request.getId());
            user.rejectRequestBorrow(request);
            getTableView().getItems().remove(request); // Xóa yêu cầu khỏi danh sách
          } else {
            BorrowRecord request = getTableView().getItems().get(getIndex());
            System.out.println("Rejected: " + request.getId());
            user.rejectRequestReturn(request);
            getTableView().getItems().remove(request); // Xóa yêu cầu khỏi danh sách
          }
        });

      }

      @Override
      protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
          setGraphic(null);
        } else {
          HBox actionButtons = new HBox(10, acceptButton, rejectButton);
          setGraphic(actionButtons);
        }
      }

    });
    borrowRequestTable.getColumns().addAll(idIssueBorrow, titleIssueBorrow, isbnIssueBorrow, userIssueBorrow,
        availbleIssueBorrow, borrowDateIssueBorrow,
        returnDateIssueBorrow, statusIssueBorrow, actionBorrow);

    // setup directlyTab
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/ManageIssueBooks.fxml"));
    Parent manageIssueBooksPane = null;
    try {
      manageIssueBooksPane = loader.load();
    } catch (IOException e) {
      showAlert("Error", "Failed to load Manage Issue Books pane.");
    }
    directlyTab.setContent(manageIssueBooksPane);
    // set up books
    SearchLibrary();
    // set up users
    searchUserManage();
  }

  private void applyCircularClip(ImageView imageView) {
    Circle clip = new Circle(25, 25, 25);
    imageView.setClip(clip);
  }

  int j = 1;

  private void setUserItem(List<User> users, int k, boolean needClear) {

    if (needClear) {
      searchUser.getChildren().clear();
    }
    j = k;

    int column = k % 2;
    int row = k / 2 + 1;

    for (int i = 0; i < users.size(); i++) {
      User user = users.get(i);
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/BookItem.fxml"));
        AnchorPane userItem = loader.load();
        BookItemController controller = loader.getController();
        controller.setItemData(user, i, user);
        manageUserEdit = user;

        if (column == 3) {
          column = 1;
          row++;
        }

        searchUser.add(userItem, column++, row);
        GridPane.setMargin(userItem, new Insets(5)); // Add margin of 5px between items

        userItem.setOnMouseClicked(event -> {
          if (event.getClickCount() == 1) {
            showUser(user);
            manageUserEdit = user;
          }
          if (event.getClickCount() == 2) {
            // showUserDetails(user);
          }
        });

      } catch (IOException e) {
        System.out.println("error upload fxml");
      }
    }
  }

  public void showUser(User user) {
    ImageHandler mageHandler = new ImageHandler();
    imageManageUser.setImage(mageHandler.loadImage(user.getId()));
    nameUser.setText("Name: " + user.getName());
    emailUser.setText("Mail: " + user.getEmail());
    int bookunpaid = borrowRecordDAO.countBookBorrowed(user.getId())
        + borrowRecordDAO.countBookReturnRequest(user.getId()) + borrowRecordDAO.countBookReturned(user.getId());
    borrowBookUser.setText("Borrowed: " + borrowRecordDAO.countBookBorrow(user.getId()));
    notReturnedUser.setText("Not Returned: " + bookunpaid);
    qrUser.setImage(UserQRCode.generateQRCode(user));

  }

  private void saveSearchResults(List<Book> books) {
    for (Book book : books) {
      try {
        bookDAO.addBookNoDuplicateIsbn(book);
      } catch (SQLException e) {
        showAlert("Error", "Failed to save book: " + book.getTitle());
      }
    }
  }

  public void resetStyle() {
    home.setVisible(false);
    books.setVisible(false);
    manageUsers.setVisible(false);
    issueBooks.setVisible(false);
    settings.setVisible(false);
    home_Button.styleProperty().set("-fx-background-color: #A6AEBF");
    books_Button.styleProperty().set("-fx-background-color: #A6AEBF");
    returnBooks_Button.styleProperty().set("-fx-background-color: #A6AEBF");
    issueBooks_Button.styleProperty().set("-fx-background-color: #A6AEBF");
    settings_Button.styleProperty().set("-fx-background-color: #A6AEBF");
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
    searchGG.setOnAction(event -> searchGoogle());
    searchGG.setDefaultButton(true);
  }

  public void gotoReturnBooks() throws SQLException {
    resetStyle();
    manageUsers.setVisible(true);
    returnBooks_Button.styleProperty().set("-fx-background-color: #777777");
    List<BorrowRecord> borrowRecords = borrowRecordDAO.getBorrowRequestByUserId(user.getId());
  }

  public void gotoIssueBooks() {
    resetStyle();
    issueBooks.setVisible(true);
    issueBooks_Button.styleProperty().set("-fx-background-color: #777777");
    ObservableList<BorrowRecord> data = borrowRecordDAO.getBorrowRequest();
    borrowRequestTable.setItems(data);
  }

  public void gotoSettings() {
    resetStyle();
    settings.setVisible(true);
    settings_Button.styleProperty().set("-fx-background-color: #777777");
  }

  public void goToSubUser() {
    if (subUser.isVisible()) {
      subUser.setVisible(false);
    } else {
      subUser.setVisible(true);
    }
  }

  public void goToNoti() {
    if (noti.isVisible()) {
      noti.setVisible(false);
    } else {
      noti.setVisible(true);
    }
  }

  @FXML
  private void handleSaveImage(ActionEvent event) {
    ImageHandler imageHandler = new ImageHandler();
    imageHandler.saveImage((Stage) ((Node) event.getSource()).getScene().getWindow(), user.getId());
    handleLoadImage(avatar);
    avatar1.setImage(avatar.getImage());

    applyCircularClip(avatar);
    applyCircularClip(avatar1);
  }

  private void handleLoadImage(ImageView imageView_) {
    ImageHandler imageHandler = new ImageHandler();
    ImageView imageView = imageHandler.loadImage(user.getId() + ".png"); // replace with your image file name
    if (imageView == null) {
      imageView = new ImageView(new Image("/imgs/account.png"));
    }
    double minDimension = Math.min(imageView.getImage().getWidth(), imageView.getImage().getHeight());
    javafx.scene.image.Image croppedImage = new javafx.scene.image.WritableImage(
        imageView.getImage().getPixelReader(),
        (int) ((imageView.getImage().getWidth() - minDimension) / 2),
        (int) ((imageView.getImage().getHeight() - minDimension) / 2),
        (int) minDimension,
        (int) minDimension);
    imageView_.setImage(croppedImage);
  }

  @FXML
  public void searchUserManage() throws SQLException {
    UserDAO userDAO = UserDAO.getUserDAO();
    List<User> users = new ArrayList<>();
    if (userManageTextField.getText().isEmpty()) {
      users = userDAO.getAllUsers();
    }
    if (searchUserBox.getValue().equals("Name")) {
      users = userDAO.getListUserByName(userManageTextField.getText());

    } else {
      users = userDAO.getListUserByEmail(userManageTextField.getText());
    }
    setUserItem(users, 1, true);
  }

  protected void handleSearchBookLib(String bookTitle, String bookAuthor,
      TableView<Book> ListBook) {
    ListBook.getItems().clear();
    Task<ObservableList<Book>> task = new Task<ObservableList<Book>>() {
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
              ListBook.getItems().clear(); // Xóa kết quả cũ
              ListBook.getItems().addAll(getValue()); // Thêm kết quả mới
            });
      }

      @Override
      protected void failed() {
        loading.setVisible(false);
        super.failed();
        // Xử lý ngoại lệ nếu có
        ListBooks.setItems(
            FXCollections.observableArrayList()); // Hoặc cập nhật một thông báo lỗi
      }
    };
    // Chạy task trong một luồng riêng
    new Thread(task).start();
  }

  protected void handleSearchBookGG(String bookTitle, String bookAuthor,
      TableView<Book> ListBook) {
    ListBook.getItems().clear();
    Task<ObservableList<Book>> task = new Task<ObservableList<Book>>() {
      @Override
      protected ObservableList<Book> call() throws Exception {
        ObservableList<Book> foundBooks = FXCollections.observableArrayList();
        // Tìm kiếm theo tiêu đề
        if (!bookTitle.isEmpty()) {
              foundBooks.addAll(bookController.searchBookByTitleMaxResult(bookTitle, 20));
            }
        Thread.sleep(0); // Giả lập việc tìm kiếm mất thời gianThrea
        return foundBooks;
      }

      @Override
      protected void succeeded() {
        loading.setVisible(false);
        super.succeeded();
        Platform.runLater(
            () -> {
              ListBook.getItems().clear(); // Xóa kết quả cũ
              ListBook.getItems().addAll(getValue()); // Thêm kết quả mới
            });
      }

      @Override
      protected void failed() {
        loading.setVisible(false);
        super.failed();
        // Xử lý ngoại lệ nếu có
        ListBooks.setItems(
            FXCollections.observableArrayList()); // Hoặc cập nhật một thông báo lỗi
      }
    };
    // Chạy task trong một luồng riêng
    new Thread(task).start();

  }

  @FXML
  public void SearchLibrary() {
    String bookTitle = title.getText();

    Task<Void> task = new Task<Void>() {
      @Override
      protected Void call() throws Exception {
        List<Book> books;

        if (bookTitle.isEmpty()) {
          books = bookDAO.getAllBooks();

        } else if (searchChoice.getValue().equals("Title")) {
          books = bookDAO.getBookByTitle(bookTitle);
        } else if (searchChoice.getValue().equals("Author")) {
          books = bookDAO.getBookByAuthor(bookTitle);
        } else {
          books = bookDAO.getListBookByISBN(bookTitle);
        }
        Platform.runLater(() -> {
          setBookItem(books, 1, false, true, false);
        });
        return null;
      }
    };
    new Thread(task).start();
  }

  @FXML
  public void searchGoogle() {
    Task<Void> task = new Task<Void>() {
      @Override
      protected Void call() throws Exception {
        String bookTitle = title.getText();
        List<Book> books = bookController.searchBookByTitleWithStartIndex(bookTitle, 0, MAX_RESULTS_EACH_TIME);

        System.out.println(books.size());

        Platform.runLater(() -> {
          setBookItem(books, 1, true, true, true);
        });
        return null;
      }
    };
    new Thread(task).start();

  }

  int i = 1;

  private void setBookItem(List<Book> books, int j, boolean needCheck, boolean needClear, boolean isGoogle) {
    // if (isGoogle)
    // saveSearchResults(books);

    i = j;
    if (needClear)
      searchView.getChildren().clear();
    int column = i % 2;
    int row = i / 2 + 1;

    if (books.size() == 0) {
      showAlert("No result", "No book found");
      Label noBooksLabel = new Label("No Books found!");
      searchView.add(noBooksLabel, 1, 1);
    } else
      for (; i <= books.size(); i++) {
        Book book = books.get(i - 1);
        try {
          FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/BookItem.fxml"));
          AnchorPane bookItem = loader.load();
          BookItemController controller = loader.getController();
          controller.setItemData(book, i, user);
          manageBookEdit = book;
          controller.hideButton();

          if (book.getRateAvg() == null) {
            controller.displayBookRate(0, false);
          } else {
            controller.displayBookRate(book.getRateAvg(), true);
          }

          if (column == 3) {
            column = 1;
            row++;
            if (row >= MAX_COLUMN_RESULTS + 1)
              break;
          }

          searchView.add(bookItem, column++, row);
          GridPane.setMargin(bookItem, new Insets(5)); // Add margin of 20px between items

          bookItem.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
              manageBookEdit = book;
              showBook(book);
            }
            if (event.getClickCount() == 2) {
              showBookDetails(book);
            }
          });

          if (i % MAX_RESULTS_EACH_TIME == 0) {
            Button loadMoreButton = new Button("Load More");
            loadMoreButton.setOnAction(event -> {
              searchView.getChildren().remove(loadMoreButton);
              if (isGoogle) {
                try {
                  List<Book> book1 = bookController.searchBookByTitleWithStartIndex(title.getText(), i,
                      MAX_RESULTS_EACH_TIME);
                  books.addAll(book1);
                } catch (IOException | SQLException e) {
                  System.out.println("error download list book search by title");
                }

              }
              setBookItem(books, i + 1, needCheck, false, isGoogle);

            });
            if (books.size() > i)
              searchView.add(loadMoreButton, 2, row + 1, 3, 1);

            GridPane.setMargin(loadMoreButton, new Insets(5));
            break;
          }
        } catch (IOException e) {
          System.out.println("error upload bookitem.fxml");
        }
      }
  }

  public void showBook(Book book) {
    if (book.getImageUrl() != null)
      imageManageBook.setImage(new Image(book.getImageUrl(), true));
    else
      imageManageBook.setImage(new Image("/imgs/noBook.png", true));
    imageManageBook.setImage(new Image(book.getImageUrl(), true));
    titleManage.setText("(id: " + book.getId() + ")" + book.getTitle());
    authorManage.setText("by  " + book.getAuthor());
    isbnManage.setText("isbn  " + book.getIsbn());
    categoryManage.setText("category   " + book.getCategories());
    availbleManage.setText(book.isAvailable() > 0 ? "Available" : "Not Available");
    borrowedManage.setText("Borrowed: " + borrowRecordDAO.countBookBorrowed(book.getId()));
  }

  public void gotoNoti() {
    if (noti.isVisible()) {
      noti.setVisible(false);
    } else {
      noti.setVisible(true);
      noti.toFront();
    }
  }

  public void gotoSubUser() {
    if (subUser.isVisible()) {
      subUser.setVisible(false);
    } else {
      subUser.setVisible(true);
      subUser.toFront();
    }
  }

  @FXML
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
      System.out.println("error can not logout");
    }
  }

  public void showBookDetails(Book book) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/Detail.fxml"));
      Pane bookDetailPane = loader.load();
      bookDetailPane.setStyle("-fx-background-color: rgba(92, 161, 171, 0.3);");
      DetailController controller = loader.getController();
      controller.setBookData(book);
      isHomeTop = false;
      if (book.getRateAvg() == null) {
        controller.displayBookRate(0, false);
      } else {
        controller.displayBookRate(book.getRateAvg(), true);
      }
      javafx.scene.effect.BoxBlur blur = new javafx.scene.effect.BoxBlur(10, 10, 3);
      if (home.isVisible()) {
        home.setEffect(blur);
      } else if (books.isVisible()) {
        books.setEffect(blur);
      } else if (issueBooks.isVisible()) {
        issueBooks.setEffect(blur);
      } else if (manageUsers.isVisible()) {
        manageUsers.setEffect(blur);
      } else if (settings.isVisible()) {
        settings.setEffect(blur);
      }
      bookDetailPane.setLayoutX(170); // Set X coordinate
      bookDetailPane.setLayoutY(80); // Set Y coordinate
      pane.getChildren().add(bookDetailPane);

      Button closeButton = new Button("X");
      closeButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-weight: bold;");
      closeButton.setOnAction(event -> {
        pane.getChildren().remove(bookDetailPane);
        home.setEffect(null);
        books.setEffect(null);
        issueBooks.setEffect(null);
        manageUsers.setEffect(null);
        settings.setEffect(null);
        isHomeTop = true;
      });
      bookDetailPane.getChildren().add(closeButton);
      closeButton.setLayoutX(1050);
      closeButton.setLayoutY(10);

    } catch (IOException e) {
      System.out.println("error show book detail");
    }
  }

  @FXML
  public void gotoAddBook() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/AddBook.fxml"));
      Pane addBookPane = loader.load();
      addBookPane.setStyle("-fx-background-color: rgba(92, 161, 171, 0.3);");

      javafx.scene.effect.BoxBlur blur = new javafx.scene.effect.BoxBlur(10, 10, 3);
      if (home.isVisible()) {
        home.setEffect(blur);
      } else if (books.isVisible()) {
        books.setEffect(blur);
      } else if (issueBooks.isVisible()) {
        issueBooks.setEffect(blur);
      } else if (manageUsers.isVisible()) {
        manageUsers.setEffect(blur);
      } else if (settings.isVisible()) {
        settings.setEffect(blur);
      }

      addBookPane.setLayoutX(170); // Set X coordinate
      addBookPane.setLayoutY(80); // Set Y coordinate
      pane.getChildren().add(addBookPane);

      Button closeButton = new Button("X");
      closeButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-weight: bold;");
      closeButton.setOnAction(event -> {
        pane.getChildren().remove(addBookPane);
        home.setEffect(null);
        books.setEffect(null);
        issueBooks.setEffect(null);
        manageUsers.setEffect(null);
        settings.setEffect(null);
      });
      addBookPane.getChildren().add(closeButton);
      closeButton.setLayoutX(1060);
      closeButton.setLayoutY(2);

    } catch (IOException e) {
      System.out.println("error cannot add book");
    }
  }

  @FXML
  public void gotoEditBook() {
    if (manageBookEdit != null) {
      editBookPane.setVisible(true);
      editBookPane.setStyle("-fx-background-color: rgba(92, 161, 171, 0.3);");

      imgUrlField.setText(manageBookEdit.getImageUrl());
      titleField.setText(manageBookEdit.getTitle());
      authorField.setText(manageBookEdit.getAuthor());
      isbnField.setText(manageBookEdit.getIsbn());
      categoryField.setText(manageBookEdit.getCategories());
      descriptionField.setText(manageBookEdit.getDescription());
      rate_avgField.setText(manageBookEdit.getRateAvg() != null ? String.valueOf(manageBookEdit.getRateAvg()) : "");
      availbleField.setText(String.valueOf(manageBookEdit.isAvailable()));
      QRCodeUrlField.setText(manageBookEdit.getQRcode());

      javafx.scene.effect.BoxBlur blur = new javafx.scene.effect.BoxBlur(10, 10,
          3);
      if (home.isVisible()) {
        home.setEffect(blur);
      } else if (books.isVisible()) {
        books.setEffect(blur);
      } else if (issueBooks.isVisible()) {
        issueBooks.setEffect(blur);
      } else if (manageUsers.isVisible()) {
        manageUsers.setEffect(blur);
      } else if (settings.isVisible()) {
        settings.setEffect(blur);
      }

      applyEditBook.setOnAction(event -> {

        manageBookEdit.setImageUrl(imgUrlField.getText() == null ? manageBookEdit.getImageUrl() : imgUrlField.getText());
        manageBookEdit.setTitle(titleField.getText() == null ? manageBookEdit.getTitle() : titleField.getText());
        manageBookEdit.setAuthor(authorField.getText() == null ? manageBookEdit.getAuthor() : authorField.getText());
        manageBookEdit.setIsbn(isbnField.getText() == null ? manageBookEdit.getIsbn() : isbnField.getText());
        manageBookEdit.setCategories(
            categoryField.getText() == null ? manageBookEdit.getCategories() : categoryField.getText());
        manageBookEdit.setDescription(
            descriptionField.getText() == null ? manageBookEdit.getDescription() : descriptionField.getText());
        manageBookEdit.setRateAvg(rate_avgField.getText() == null ? manageBookEdit.getRateAvg()
            : Double.parseDouble(rate_avgField.getText()));
        manageBookEdit.setAvailable(availbleField.getText() == null ? manageBookEdit.isAvailable()
            : Integer.parseInt(availbleField.getText()));
        manageBookEdit
            .setQRcode(QRCodeUrlField.getText() == null ? manageBookEdit.getQRcode() : QRCodeUrlField.getText());
        manageBookEdit.setQRcode(QRCodeUrlField.getText());

        bookDAO.updateBook(manageBookEdit);
        showAlert("Success", "Book updated successfully.");
        SearchLibrary(); // Refresh the book list
        editBookPane.setVisible(false);
        home.setEffect(null);
        books.setEffect(null);
        issueBooks.setEffect(null);
        manageUsers.setEffect(null);
        settings.setEffect(null);
      });

      cancelEditBook.setOnAction(event -> {
        editBookPane.setVisible(false);
        home.setEffect(null);
        books.setEffect(null);
        issueBooks.setEffect(null);
        manageUsers.setEffect(null);
        settings.setEffect(null);
      });
    } else {
      showAlert("Error", "No book selected to edit.");
    }
  }

  @FXML
  public void gotoDeleteBook() {
    if (manageBookEdit != null) {
      try {
        if (borrowRecordDAO.isBorrowedByBookId(manageBookEdit.getId())) {
          return;
        }
        user.deleteBook(manageBookEdit);
        SearchLibrary(); // Refresh the book list
      } catch (SQLException e) {
        showAlert("Error", "Failed to delete book: " + manageBookEdit.getTitle());
      }
    } else {
      showAlert("Error", "No book selected to delete.");
    }
  }

  @FXML
  public void gotoEditUser() {
    if (manageUserEdit != null) {
      editUserPane.setVisible(true);
      editUserPane.setStyle("-fx-background-color: rgba(92, 161, 171, 0.3);");

      usernameField.setText(manageUserEdit.getName());
      mailField.setText(manageUserEdit.getEmail());
      roleField.setText(manageUserEdit.getRole());

      javafx.scene.effect.BoxBlur blur = new javafx.scene.effect.BoxBlur(10, 10, 3);

      manageUsers.setEffect(blur);

      applyEditUserButton.setOnAction(event -> {
        manageUserEdit.setName(usernameField.getText());
        manageUserEdit.setEmail(mailField.getText());
        manageUserEdit.setRole(roleField.getText());
        manageUserEdit.setPassword(passwordField.getText());

        UserDAO userDAO = UserDAO.getUserDAO();
        try {
          user.editUser(manageUserEdit);
          showAlert("Success", "User updated successfully.");
          searchUserManage();
        } catch (SQLException e) {
          showAlert("Error", "Failed to update user: " + manageUserEdit.getName());
        }
      });

      cancelEditUser.setOnAction(event -> {
        editUserPane.setVisible(false);
        home.setEffect(null);
        books.setEffect(null);
        issueBooks.setEffect(null);
        manageUsers.setEffect(null);
        settings.setEffect(null);
      });
    } else {
      showAlert("Error", "No user selected to edit.");
    }
  }

  public static String getSalt() throws NoSuchAlgorithmException {
    // Tạo ra salt ngẫu nhiên với SecureRandom
    SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
    byte[] salt = new byte[16];
    sr.nextBytes(salt);
    // Mã hóa salt thành chuỗi base64 để dễ lưu trữ
    return Base64.getEncoder().encodeToString(salt);
  }

  @FXML
  public void gotoAddUser() throws SQLException {
    addUserPane.setVisible(true);
    addUserPane.setStyle("-fx-background-color: rgba(92, 161, 171, 0.3);");

    javafx.scene.effect.BoxBlur blur = new javafx.scene.effect.BoxBlur(10, 10, 3);
    manageUsers.setEffect(blur);

    acceptAddUserButton.setOnAction(event -> {
      try {
        if (confirmpassAddField.getText().equals(passwordAddField.getText())
            && !nameUserAddField.getText().isEmpty()
            && !mailUserAddField.getText().isEmpty()
            && UserDAO.getUserByName(nameUserAddField.getText()) == null) {
              User newUser = new User(nameUserAddField.getText(), mailUserAddField.getText(), passwordAddField.getText(),
              "User", getSalt());
              UserDAO userDAO = UserDAO.getUserDAO();
          user.addUser(newUser);
          showAlert("Success", "User added successfully.");
          searchUserManage();
          addUserPane.setVisible(false);
          manageUsers.setEffect(null);

        } else {
          showAlert("Error", "Passwords do not match or name is empty or user already exists.");
        }
      } catch (SQLException | NoSuchAlgorithmException e) {
        showAlert("Error", "Failed to add user: " + e.getMessage());
      }
    });

    cancelAddUserButton.setOnAction(event -> {
      addUserPane.setVisible(false);
      manageUsers.setEffect(null);
    });
  }

  @FXML
  public void gotoDeleteUser() {
    if (manageUserEdit != null) {
      if (borrowRecordDAO.isUserBorrowed(manageUserEdit.getId()) || manageUserEdit.getRole().equals("admin")) {
        showAlert("Error", "Cannot delete user as they have borrowed books.");
        return;
      }
      UserDAO userDAO = UserDAO.getUserDAO();
      try {
        user.deleteUser(manageUserEdit);
        showAlert("Success", "User deleted successfully.");
        searchUserManage();
      } catch (SQLException e) {
        showAlert("Error", "Failed to delete user: " + manageUserEdit.getName());
      }
    } else {
      showAlert("Error", "No user selected to delete.");
    }
  }

  @FXML
  public void gotoQRCode() {

  }

  @FXML
  public void gotoDarkMode() {

  }

}