package library.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import javafx.util.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javafx.scene.control.Label;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.geometry.Insets;
import javafx.animation.FadeTransition;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import library.dao.BookDAO;
import library.dao.BookReviewDAO;
import library.dao.BorrowRecordDAO;
import library.dao.NotiDAO;
import library.dao.UserDAO;
import library.model.Admin;
import library.model.Book;
import library.model.BorrowRecord;
import library.model.ISBNScannerWithUI;
import library.model.ImageHandler;
import library.model.User;
import library.model.UserQRCode;
import library.dao.AllDao;
import javafx.scene.chart.XYChart;

/**
 * Controller class for the Admin Dashboard.
 */
public class Dash_AdminController {

  @FXML
  private TextField author, publisher, isbn, bookId, search, issueField;
  // @FXML
  // private Insets title;

  @FXML
  private TextField title, userManageTextField, imgUrlField, titleField, authorField, isbnField, categoryField,
      descriptionField, rate_avgField, availbleField, QRCodeUrlField, usernameField, mailField, roleField,
      passwordField, confirmpassField, nameUserAddField, mailUserAddField, roleUserAddField, passwordAddField,
      confirmpassAddField;

  @FXML
  private Pane home, books, issueBooks, manageUsers, settings, noti, subUser, pane, editBookPane, editUserPane,
      addUserPane;
  @FXML
  private Button acceptAddUserButton, cancelAddUserButton, changePassButton;
  @FXML
  private TableColumn<BorrowRecord, Integer> idIssueBorrow, availbleIssueBorrow, statusIssueBorrow;
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
      applyEditUserButton, cancelEditUser, cancelAddUser, banUserButton;

  @FXML
  private MenuButton menuButtonIssue;
  @FXML
  private MenuItem buttonBorrow, buttonReturn;

  @FXML
  private Label borrowedLabel, passwordLabel, confirmpasswordLabel, idAdminLabel, mailAdminLabel;
  @FXML
  private Text nameAdminText;

  @FXML
  private Label totalUsersLabel, totalBooksLabel, remainingLabel, borrowLabel, returnLabel, borrowPendingLabel,
      returnPendingLabel, lateLabel;
  @FXML
  private TableView<Book> ListBooks;
  @FXML
  private TableView<BorrowRecord> borrowRequestTable, returnRequestTable;

  @FXML
  private Pane return_Book, add_Book;

  @FXML
  protected ProgressIndicator loading;
  @FXML
  private ListView<String> notiList, bookDueDateList;

  @FXML
  private Label welcome, date, notiNewLabel, titleManage, authorManage, isbnManage, categoryManage, availbleManage,
      borrowedManage, nameUser, emailUser, borrowBookUser, notReturnedUser;
  @FXML
  private GridPane searchView, searchReturnBooks, warringGridPane, searchUser;
  @FXML
  private ChoiceBox<String> searchChoice, searchUserBox, choiceIssue;

  @FXML
  private ImageView avatar, avatar1, imageManageBook, imageManageUser, qrUser, githubImg, fbImg;

  protected BorrowRecordDAO borrowRecordDAO = BorrowRecordDAO.getBorrowRecordDAO();
  protected Admin user;
  protected HostServices hostServices;

  @FXML
  private Button searchUserButton;

  @SuppressWarnings("rawtypes")

  @FXML
  BarChart barChart;

  AllDao allDao = AllDao.getAllDao();

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

  /**
   * Constructor for Dash_AdminController.
   */
  public Dash_AdminController() {
  }

  /**
   * Constructor for Dash_AdminController with user and host services.
   * 
   * @param user         the user
   * @param hostServices the host services
   */
  public Dash_AdminController(User user, HostServices hostServices) {
    this.user = (Admin) user;
    this.hostServices = hostServices;
  }

  /**
   * Shows an alert with the given title and message.
   * 
   * @param title   the title of the alert
   * @param message the message of the alert
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
   * Sets the home label with various statistics.
   */
  public void setHomeLabel() {
    totalBooksLabel.setText(String.valueOf(allDao.getTotalBooks()));
    totalUsersLabel.setText(String.valueOf(allDao.getTotalUsers()));
    remainingLabel.setText(String.valueOf(allDao.getTotalBooks() - allDao.getTotalBorrowRecords()));
    borrowLabel.setText(String.valueOf(allDao.getTotalBorrowRecords()));
    returnLabel.setText(String.valueOf(allDao.getTotalReturnedRecords()));
    borrowPendingLabel.setText(String.valueOf(allDao.getTotalBorrowPendingRecords()));
    returnPendingLabel.setText(String.valueOf(allDao.getTotalReturnedRecords()));
    lateLabel.setText(String.valueOf(allDao.getTotalOverdueBooks()));
  }

  /**
   * Sets up the bar chart with data.
   */
  public void setUpBarChart() {
    // Set up barchart
    XYChart.Series<String, Number> borrowSeries = new XYChart.Series<>();
    borrowSeries.setName("Books Borrowed");

    XYChart.Series<String, Number> userSeries = new XYChart.Series<>();
    userSeries.setName("New Users");

    LocalDate today = LocalDate.now();
    for (int i = 4; i >= 0; i--) {
      LocalDate date = today.minusDays(i);
      String dateString = date.toString();

      int booksBorrowed = allDao.getBooksBorrowedOnDate(date);
      int newUsers = allDao.getNewUsersOnDate(date);

      borrowSeries.getData().add(new XYChart.Data<>(dateString, booksBorrowed));
      userSeries.getData().add(new XYChart.Data<>(dateString, newUsers));
    }

    barChart.getData().clear();
    barChart.getData().addAll(borrowSeries, userSeries);
  }

  /**
   * Initializes the controller.
   * 
   * @throws SQLException if a database access error occurs
   */
  public void initialize() throws SQLException {

    // setup sub
    subUser.setVisible(false);
    idAdminLabel.setText("ID:    " + user.getId());
    mailAdminLabel.setText("Mail:  " + user.getEmail());
    nameAdminText.setText("Name:  " + user.getName());

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
    choiceIssue.getItems().addAll("Name", "Mail");

    // Set up values for ChoiceBox
    searchChoice.setValue("Title");
    searchUserBox.setValue("Name");

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
      notiList.setOnMouseClicked(event -> {
        if (event.getClickCount() == 1) {
          String selectedItem = notiList.getSelectionModel().getSelectedItem();
          notiList.getItems().remove(selectedItem);
          selectedItem = selectedItem.substring(selectedItem.indexOf(" ") + 1);
          try {
            notiDAO.deleteNoti(selectedItem);
          } catch (SQLException e) {
            e.printStackTrace();
          }
          int currentNotiCount = Integer.parseInt(notiNewLabel.getText());
          notiNewLabel.setText(String.valueOf(currentNotiCount - 1));
        }
      });
    }
    notiList.getItems().add("Borrow Requests: " + bookStatus0);
    notiList.getItems().add("Books Borrowed: " + bookStatus1);
    notiList.getItems().add("Return Requests: " + bookStatus2);
    notiList.getItems().add("Books Returned: " + borrowRecordDAO.countBookReturned(user.getId()));

    // Set issue book table
    borrowRequestTable.getColumns().clear();
    // idIssueBorrow = new TableColumn<>("ID");
    idIssueBorrow.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getId()));
    // titleIssueBorrow = new TableColumn<>("Title");
    titleIssueBorrow.setCellValueFactory(param -> {
      if (param.getValue().getBook() != null)
        return new ReadOnlyObjectWrapper<>(
            "(id:" + param.getValue().getBook().getId() + ")-" + param.getValue().getBook().getTitle());
      else
        return new ReadOnlyObjectWrapper<>(param.getValue().getBook().getTitle());
    });

    // isbnIssueBorrow = new TableColumn<>("ISBN");
    isbnIssueBorrow.setCellValueFactory(param -> {
      if (param.getValue().getBook() != null)
        return new ReadOnlyObjectWrapper<>(param.getValue().getBook().getIsbn());
      else
        return new ReadOnlyObjectWrapper<>("");
    });
    // availbleIssueBorrow = new TableColumn<>("Available");
    availbleIssueBorrow.setCellValueFactory(param -> {
      if (param.getValue().getBook() != null)
        return new ReadOnlyObjectWrapper<>(param.getValue().getBook().getAvailable());
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

    // cot chua trang thai
    statusIssueBorrow.setCellValueFactory(new PropertyValueFactory<>("status"));
    // Cột chứa nút xác nhận
    // actionBorrow = new TableColumn<>("Actions");
    actionBorrow.setCellFactory(param -> new TableCell<>() {
      private final Button acceptButton = new Button("Accept");
      private final Button rejectButton = new Button("Reject");

      {
        // acceptButton.setOnAction(event -> {
        // BorrowRecord request = getTableView().getItems().get(getIndex());
        // System.out.println("Accepted: " + request.getId());
        // getTableView().getItems().remove(request); // Xóa yêu cầu khỏi danh sách
        // borrowRecordDAO.increaseStatus(request);

        // });

        // rejectButton.setOnAction(event -> {
        // BorrowRecord request = getTableView().getItems().get(getIndex());
        // System.out.println("Rejected: " + request.getId());
        // getTableView().getItems().remove(request); // Xóa yêu cầu khỏi danh sách

        // borrowRecordDAO.deleteBorrowRecord(request);

        // });

        acceptButton.setOnAction(event -> {
          BorrowRecord request = getTableView().getItems().get(getIndex());
          int status = request.getStatus();
          if (status == 0) {
            System.out.println("Accepted: " + request.getId());
            user.acceptRequestBorrow(request);
            Book book = request.getBook();
            book.setAvailable(book.getAvailable() - 1);
            bookDAO.updateBook(book);
            getTableView().getItems().remove(request); // Xóa yêu cầu khỏi danh sách

            try {
              notiDAO.addNotificationFromAdminToUser(request.getUser().getId(),
                  "Your request borrow book  '" + request.getBook().getTitle() + "' has been accepted");

            } catch (SQLException e) {
              e.printStackTrace();
            }

          } else {
            System.out.println("Accepted: " + request.getId());
            user.acceptRequestReturn(request);
            getTableView().getItems().remove(request); // Xóa yêu cầu khỏi danh sách
            Book book = request.getBook();
            book.setAvailable(book.getAvailable() + 1);
            bookDAO.updateBook(book);
            try {
              notiDAO.addNotificationFromAdminToUser(request.getUser().getId(),
                  "Your request return book  '" + request.getBook().getTitle() + "' has been accepted");

            } catch (SQLException e) {
              e.printStackTrace();

            }
          }
          // set the request to status 1
          // show alert
          showAlert(getAccessibleHelp(), "Request" + request.getId() + " Accepted");

        });

        rejectButton.setOnAction(event -> {
          BorrowRecord request = getTableView().getItems().get(getIndex());
          int status = request.getStatus();
          if (status == 0) {
            System.out.println("Rejected: " + request.getId());
            user.rejectRequestBorrow(request);
            getTableView().getItems().remove(request); // Xóa yêu cầu khỏi danh sách
            try {
              notiDAO.addNotificationFromAdminToUser(request.getUser().getId(),
                  "Your request borrow book  '" + request.getBook().getTitle() + "' has been rejected");

            } catch (SQLException e) {
              e.printStackTrace();
            }
          } else {
            System.out.println("Rejected: " + request.getId());
            user.rejectRequestReturn(request);
            getTableView().getItems().remove(request); // Xóa yêu cầu khỏi danh sách
            try {
              notiDAO.addNotificationFromAdminToUser(request.getUser().getId(),
                  "Your request return book  '" + request.getBook().getTitle() + "' has been rejected");

            } catch (SQLException e) {
              e.printStackTrace();
            }
          }

          // set the request to status null
          // show alert
          showAlert(getAccessibleHelp(), "Request" + request.getId() + " Rejected");
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
      e.printStackTrace();
      showAlert("Error", "Failed to load Manage Issue Books pane.");
    }
    directlyTab.setContent(manageIssueBooksPane);

    // set up books
    SearchLibrary();

    // set up users
    searchUserManage();

    // link github and fb
    githubImg.setOnMouseClicked(event -> {
      hostServices.showDocument("https://github.com/Anhnguyen0812/BTL_OOP");
    });
    fbImg.setOnMouseClicked(event -> {
      hostServices.showDocument("https://www.facebook.com/profile.php?id=100018071889476");
    });

    // set home Label
    setHomeLabel();

    // set up barchart
    setUpBarChart();

    // set up book due date
    bookDueDateList.getItems().clear();
    List<BorrowRecord> bookDueDate = borrowRecordDAO.getBookDueDate();
    if (bookDueDate.size() == 0) {
      bookDueDateList.getItems().add("No book is due");
    } else
      for (int i = 0; i < bookDueDate.size(); i++) {
        bookDueDateList.getItems().add(i + 1 + ". " + bookDueDate.get(i).getUser().getName() + " - "
            + bookDueDate.get(i).getBook().getTitle() + " - " + bookDueDate.get(i).getReturnDate());
      }

  }

  /**
   * Applies a circular clip to the given ImageView.
   * 
   * @param imageView the ImageView to apply the clip to
   */
  private void applyCircularClip(ImageView imageView) {
    Circle clip = new Circle(25, 25, 25);
    imageView.setClip(clip);
  }

  int j = 1;

  /**
   * Sets the user items in the grid pane.
   * 
   * @param users     the list of users
   * @param k         the starting index
   * @param needClear whether to clear the existing items
   */
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
            UserDAO userDao = UserDAO.getUserDAO();
            if (userDao.checkIsBan(manageUserEdit.getId())) {
              banUserButton.setText("Unban User");
            } else {
              banUserButton.setText("Ban User");
            }
          }
          if (event.getClickCount() == 2) {
            // showUserDetails(user);
          }
        });

      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Shows the details of the given user.
   * 
   * @param user the user to show details for
   */
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

  /**
   * Saves the search results to the database.
   * 
   * @param books the list of books to save
   */
  private void saveSearchResults(List<Book> books) {
    for (Book book : books) {
      try {
        bookDAO.addBookNoDuplicateIsbn(book);
      } catch (SQLException e) {
        showAlert("Error", "Failed to save book: " + book.getTitle());
      }
    }
  }

  /**
   * Resets the style of the UI components.
   */
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

  /**
   * Navigates to the home view.
   */
  public void gotoHome() {
    resetStyle();
    home.setVisible(true);
    home_Button.styleProperty().set("-fx-background-color: #777777");
    setHomeLabel();
    FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), home);
    fadeTransition.setFromValue(0.0);
    fadeTransition.setToValue(1.0);
    fadeTransition.play();
  }

  /**
   * Navigates to the books view.
   */
  public void gotoBooks() {
    resetStyle();
    books.setVisible(true);
    books_Button.styleProperty().set("-fx-background-color: #777777");
    searchGG.setOnAction(event -> searchGoogle());
    searchGG.setDefaultButton(true);

    FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), books);
    fadeTransition.setFromValue(0.0);
    fadeTransition.setToValue(1.0);
    fadeTransition.play();
  }

  /**
   * Navigates to the return books view.
   * 
   * @throws SQLException if a database access error occurs
   */
  public void gotoReturnBooks() throws SQLException {
    resetStyle();
    manageUsers.setVisible(true);
    returnBooks_Button.styleProperty().set("-fx-background-color: #777777");

    FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), manageUsers);
    fadeTransition.setFromValue(0.0);
    fadeTransition.setToValue(1.0);
    fadeTransition.play();
  }

  /**
   * Navigates to the issue books view.
   */
  public void gotoIssueBooks() {
    resetStyle();
    issueBooks.setVisible(true);
    issueBooks_Button.styleProperty().set("-fx-background-color: #777777");
    buttonBorrow.setOnAction(event -> {
      menuButtonIssue.setText("Borrow Requests");
      ObservableList<BorrowRecord> data = borrowRecordDAO.getBorrowRequest();
      borrowRequestTable.setItems(data);
      menuButtonIssue.setText("Borrow Requests");
    });

    buttonReturn.setOnAction(event -> {
      menuButtonIssue.setText("Return Requests");
      ObservableList<BorrowRecord> data = borrowRecordDAO.getReturnRequest();
      borrowRequestTable.setItems(data);
      menuButtonIssue.setText("Return Requests");
    });
    ObservableList<BorrowRecord> data;
    if (menuButtonIssue.getText().equals("Borrow Requests")) {
      data = borrowRecordDAO.getBorrowRequest();
    } else {
      data = borrowRecordDAO.getReturnRequest();
    }
    borrowRequestTable.setItems(data);

    FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), issueBooks);
    fadeTransition.setFromValue(0.0);
    fadeTransition.setToValue(1.0);
    fadeTransition.play();
  }

  /**
   * Searches for issue records based on the selected criteria.
   * 
   * @throws SQLException if a database access error occurs
   */
  @FXML
  public void gotoSearchIssue() throws SQLException {
    if (choiceIssue.getValue().equals("Name")) {
      if (menuButtonIssue.getText().equals("Borrow Requests")) {
        ObservableList<BorrowRecord> data = borrowRecordDAO.getBorrowRecordByName(issueField.getText());
        if (data == null) {
          showAlert("no record found!", "no borrow record found!");
          borrowRequestTable.setItems(null);
        } else
          borrowRequestTable.setItems(data);
      } else {
        ObservableList<BorrowRecord> data = borrowRecordDAO.getReturnRecordByName(issueField.getText());
        if (data == null) {
          showAlert("no record found!", "no borrow record found!");
          borrowRequestTable.setItems(null);
        } else
          borrowRequestTable.setItems(data);
      }

    } else {
      if (menuButtonIssue.getText().equals("Borrow Requests")) {
        ObservableList<BorrowRecord> data = borrowRecordDAO.getBorrowRecordByMail(issueField.getText());
        if (data == null) {
          showAlert("no record found!", "no return record found!");
          borrowRequestTable.setItems(null);
        } else
          borrowRequestTable.setItems(data);
      } else {
        ObservableList<BorrowRecord> data = borrowRecordDAO.getReturnRecordByMail(issueField.getText());
        if (data == null) {
          showAlert("no record found!", "no borrow record found!");
          borrowRequestTable.setItems(null);
        } else
          borrowRequestTable.setItems(data);
      }
    }
  }

  /**
   * Navigates to the settings view.
   */
  public void gotoSettings() {
    resetStyle();
    settings.setVisible(true);
    settings_Button.styleProperty().set("-fx-background-color: #777777");
    FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), settings);
    fadeTransition.setFromValue(0.0);
    fadeTransition.setToValue(1.0);
    fadeTransition.play();
  }

  /**
   * Handles saving the user's image.
   * 
   * @param event the action event
   */
  @FXML
  private void handleSaveImage(ActionEvent event) {
    ImageHandler imageHandler = new ImageHandler();
    imageHandler.saveImage((Stage) ((Node) event.getSource()).getScene().getWindow(), user.getId());
    handleLoadImage(avatar);
    avatar1.setImage(avatar.getImage());

    applyCircularClip(avatar);
    applyCircularClip(avatar1);
  }

  /**
   * Loads the user's image into the given ImageView.
   * 
   * @param imageView_ the ImageView to load the image into
   */
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

  /**
   * Searches for users to manage.
   * 
   * @throws SQLException if a database access error occurs
   */
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

  /**
   * Searches the library for books.
   */
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
    loading.progressProperty().bind(task.progressProperty());
    new Thread(task).start();
    loading.setVisible(true);
    task.setOnSucceeded(event -> {
      loading.setVisible(false);
      loading.progressProperty().unbind();
    });
    task.setOnFailed(event -> {
      loading.setVisible(false);
      loading.progressProperty().unbind();
      showAlert("Error", "Failed to search books on Google.");
    });
  }

  /**
   * Searches for books on Google.
   */
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
    loading.progressProperty().bind(task.progressProperty());
    new Thread(task).start();
    loading.setVisible(true);
    task.setOnSucceeded(event -> {
      loading.setVisible(false);
      loading.progressProperty().unbind();
    });
    task.setOnFailed(event -> {
      loading.setVisible(false);
      loading.progressProperty().unbind();
      showAlert("Error", "Failed to search books on Google.");
    });
  }

  int i = 1;

  /**
   * Sets the book items in the grid pane.
   * 
   * @param books     the list of books
   * @param j         the starting index
   * @param needCheck whether to check for duplicates
   * @param needClear whether to clear the existing items
   * @param isGoogle  whether the books are from Google
   */
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
              showBook(book);
              manageBookEdit = book;
            }
            if (event.getClickCount() == 2) {
              showBookDetails(book);
            }
          });

          if (i % MAX_RESULTS_EACH_TIME == 0) {
            Button loadMoreButton = new Button("Load More");
            loadMoreButton.setOnAction(event -> {

              Task<Void> loadMoreTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                  if (isGoogle) {
                    List<Book> moreBooks = bookController.searchBookByTitleWithStartIndex(title.getText(), i,
                        MAX_RESULTS_EACH_TIME);
                    Platform.runLater(() -> {
                      books.addAll(moreBooks);
                      setBookItem(books, i + 1, needCheck, false, isGoogle);
                    });
                  } else {
                    Platform.runLater(() -> setBookItem(books, i + 1, needCheck, false, isGoogle));
                  }
                  return null;
                }
              };
              new Thread(loadMoreTask).start();
              loading.progressProperty().bind(loadMoreTask.progressProperty());
              loading.setVisible(true);
              loadMoreTask.setOnSucceeded(e -> {
                loading.setVisible(false);
                loading.progressProperty().unbind();
              });
              loadMoreTask.setOnFailed(e -> {
                loading.setVisible(false);
                loading.progressProperty().unbind();
                showAlert("Error", "Failed to load more books.");
              });

            });
            if (isGoogle || books.size() > i)
              searchView.add(loadMoreButton, 2, row + 1, 3, 1);

            GridPane.setMargin(loadMoreButton, new Insets(5));
            break;
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
  }

  /**
   * Shows the details of the given book.
   * 
   * @param book the book to show details for
   */
  public void showBook(Book book) {
    if (book.getImageUrl() != null)
      imageManageBook.setImage(new Image(book.getImageUrl(), true));
    else
      imageManageBook.setImage(new Image("/imgs/noBook.png", true));
    titleManage.setText("(id: " + book.getId() + ")" + book.getTitle());
    authorManage.setText("by  " + book.getAuthor());
    isbnManage.setText("isbn  " + book.getIsbn());
    categoryManage.setText("category   " + book.getCategories());
    availbleManage.setText(String.valueOf(book.getAvailable()));
    borrowedManage.setText("Borrowed: " + borrowRecordDAO.countBookBorrowed(book.getId()));
  }

  /**
   * Navigates to the notifications view.
   */
  @FXML
  public void gotoNoti() {
    // if (noti.isVisible()) {
    // noti.setVisible(false);
    // } else {
    // noti.setVisible(true);
    // }
    javafx.animation.TranslateTransition transition = new javafx.animation.TranslateTransition(
        javafx.util.Duration.millis(300), noti);
    if (noti.isVisible()) {
      transition.setFromX(0);
      transition.setToX(noti.getWidth());
      transition.setOnFinished(event -> noti.setVisible(false));
    } else {
      noti.setVisible(true);
      transition.setFromX(noti.getWidth());
      transition.setToX(0);
      noti.toFront();
    }
    transition.play();

  }

  /**
   * Navigates to the sub-user view.
   */
  @FXML
  public void gotoSubUser() {

    javafx.animation.TranslateTransition transition = new javafx.animation.TranslateTransition(
        javafx.util.Duration.millis(200), subUser);
    if (subUser.isVisible()) {
      transition.setFromY(0);
      transition.setToY(subUser.getHeight());
      transition.setOnFinished(event -> subUser.setVisible(false));
    } else {
      subUser.setVisible(true);
      transition.setFromY(subUser.getHeight());
      transition.setToY(0);
      subUser.toFront();
    }
    transition.play();
  }

  /**
   * Logs out the current user.
   */
  @FXML
  public void logOut() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/Login.fxml"));
      Parent root = loader.load();
      Stage stage = (Stage) logOut.getScene().getWindow();
      stage.setScene(new Scene(root));
      stage.setTitle("Library Management System");
      LoginController loginController = loader.getController();
      loginController.setHostServices(hostServices);
      stage.centerOnScreen();
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Shows the detailed view of the given book.
   * 
   * @param book the book to show details for
   */
  public void showBookDetails(Book book) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/Detail.fxml"));
      Pane bookDetailPane = loader.load();
      bookDetailPane.setStyle("-fx-background-color: rgba(92, 161, 171, 0.3);");
      DetailController controller = loader.getController();
      controller.setBookData(book, hostServices);
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
      e.printStackTrace();
    }
  }

  /**
   * Navigates to the add book view.
   */
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
      e.printStackTrace();
    }
  }

  /**
   * Navigates to the edit book view.
   */
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
      availbleField.setText(String.valueOf(manageBookEdit.getAvailable()));
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

        manageBookEdit
            .setImageUrl(!imgUrlField.getText().isEmpty() ? manageBookEdit.getImageUrl() : imgUrlField.getText());
        manageBookEdit.setTitle(!titleField.getText().isEmpty() ? manageBookEdit.getTitle() : titleField.getText());
        manageBookEdit.setAuthor(!authorField.getText().isEmpty() ? manageBookEdit.getAuthor() : authorField.getText());
        manageBookEdit.setIsbn(isbnField.getText().isEmpty() ? manageBookEdit.getIsbn() : isbnField.getText());
        manageBookEdit.setCategories(
            categoryField.getText().isEmpty() ? manageBookEdit.getCategories() : categoryField.getText());
        manageBookEdit.setDescription(
            !descriptionField.getText().isEmpty() ? manageBookEdit.getDescription() : descriptionField.getText());
        if (!rate_avgField.getText().isEmpty())
          manageBookEdit.setRateAvg(Double.parseDouble(rate_avgField.getText()));
        manageBookEdit.setAvailable(availbleField.getText().isEmpty() ? 0
            : Integer.parseInt(availbleField.getText()));
        manageBookEdit
            .setQRcode(!QRCodeUrlField.getText().isEmpty() ? manageBookEdit.getQRcode() : QRCodeUrlField.getText());
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

  /**
   * Deletes the selected book.
   */
  @FXML
  public void gotoDeleteBook() {
    if (manageBookEdit != null) {
      try {
        if (borrowRecordDAO.isBookBorrowed(manageBookEdit.getId())) {
          showAlert("Error", "Cannot delete book as it is currently borrowed.");
          return;
        } else {
          bookDAO.deleteBook(manageBookEdit.getId());
          showAlert("Success", "Book deleted successfully.");
          SearchLibrary(); // Refresh the book list
        }
      } catch (SQLException e) {
        showAlert("Error", "Failed to delete book: " + manageBookEdit.getTitle());
      }
    } else {
      showAlert("Error", "No book selected to delete.");
    }
  }

  @FXML
  public void gotoBanUser() throws SQLException {
    UserDAO userDao = UserDAO.getUserDAO();
    if (!userDao.checkIsBan(manageUserEdit.getId())) {
      if (manageUserEdit != null) {
        try {
          user.banUser(manageUserEdit.getId());
          showAlert("Success", "User banned successfully.");
          searchUserManage();
        } catch (SQLException e) {
          showAlert("Error", "Failed to ban user: " + manageUserEdit.getName());
        }
      } else {
        showAlert("Error", "No user selected to ban.");
      }
    } else {
      try {
        user.deBanUser(manageUserEdit.getId());
        showAlert("Success", "User banned successfully.");
        searchUserManage();
      } catch (SQLException e) {
        showAlert("Error", "Failed to ban user: " + manageUserEdit.getName());
      }

    }

  }

  /**
   * Generates a QR code for the selected book.
   */
  @FXML
  public void gotoQRCode() {
    if (manageBookEdit != null) {
      try {
        Image qrCodeImage = ISBNScannerWithUI.generateISBN(manageBookEdit.getIsbn());
        Stage qrStage = new Stage();
        ImageView qrImageView = new ImageView(qrCodeImage);
        Pane qrPane = new Pane(qrImageView);
        Scene qrScene = new Scene(qrPane, 400, 200);
        qrStage.setScene(qrScene);
        qrStage.setTitle("QR Code for ISBN: " + manageBookEdit.getIsbn());
        qrStage.show();
      } catch (Exception e) {
        showAlert("Error", "Failed to generate QR code for ISBN: " + manageBookEdit.getIsbn());
      }
    } else {
      showAlert("Error", "No book selected to generate QR code.");
    }
  }

  /**
   * Navigates to the edit user view.
   */
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

      Button changeImageButton = new Button("Change Image");
      changeImageButton.setOnAction(event -> {
        ImageHandler imageHandler = new ImageHandler();
        imageHandler.saveImage((Stage) ((Node) event.getSource()).getScene().getWindow(), manageUserEdit.getId());
        ImageView newImageView = imageHandler.loadImage(manageUserEdit.getId() + ".png");
        if (newImageView != null) {
          imageManageUser.setImage(newImageView.getImage());
        }
      });

      editUserPane.getChildren().add(changeImageButton);
      changeImageButton.setLayoutX(284); // Set X coordinate
      changeImageButton.setLayoutY(80); // Set Y coordinate

      applyEditUserButton.setOnAction(event -> {

        manageUserEdit.setName(usernameField.getText());
        manageUserEdit.setEmail(mailField.getText());
        manageUserEdit.setRole(roleField.getText());

        if (!usernameField.getText().isEmpty() && !mailField.getText().isEmpty() && !roleField.getText().isEmpty()) {

          if (passwordField.isVisible()) {
            if (!passwordField.getText().equals(confirmpassField.getText())) {
              showAlert("Error", "Passwords do not match.");

            } else
              try {
                String salt = getSalt();
                manageUserEdit.setPassword(UserDAO.hashPassword(passwordField.getText(), salt));
                manageUserEdit.setSalt(salt);

                try {
                  try {
                    user.updateUser(manageUserEdit);
                    showAlert("Success", "User updated successfully.");

                  } catch (NoSuchAlgorithmException e) {
                    showAlert("Error", "Failed to update user: " + manageUserEdit.getName());
                    showAlert("Success", "User updated successfully.");
                    searchUserManage();
                  } catch (SQLException e) {
                    showAlert("Error", "Failed to update user: " + manageUserEdit.getName());
                  }
                } catch (SQLException e) {
                  showAlert("Error", "Failed to update user: " + manageUserEdit.getName());
                }

              } catch (NoSuchAlgorithmException e) {
                showAlert("Error", "Failed to generate salt for user: " + manageUserEdit.getName());
              }
            passwordAddField.clear();
            confirmpassAddField.clear();
            passwordAddField.setVisible(false);
            confirmpassAddField.setVisible(false);
            passwordLabel.setVisible(false);
            confirmpasswordLabel.setVisible(false);
          } else {

            try {
              try {
                user.updateUser(manageUserEdit);
                showAlert("Success", "User updated successfully.");

              } catch (NoSuchAlgorithmException e) {
                showAlert("Error", "Failed to update user: " + manageUserEdit.getName());
                showAlert("Success", "User updated successfully.");
                searchUserManage();
              } catch (SQLException e) {
                showAlert("Error", "Failed to update user: " + manageUserEdit.getName());
              }
            } catch (SQLException e) {
              showAlert("Error", "Failed to update user: " + manageUserEdit.getName());
            }
          }

        }
      });

      changePassButton.setOnAction(event -> {
        passwordField.setVisible(true);
        confirmpassField.setVisible(true);
        passwordLabel.setVisible(true);
        confirmpasswordLabel.setVisible(true);

      });

      cancelEditUser.setOnAction(event -> {
        editUserPane.setVisible(false);
        home.setEffect(null);
        books.setEffect(null);
        issueBooks.setEffect(null);
        manageUsers.setEffect(null);
        settings.setEffect(null);
        passwordField.setVisible(false);
        confirmpassField.setVisible(false);
        passwordField.clear();
        confirmpassField.clear();
        passwordLabel.setVisible(false);
        confirmpasswordLabel.setVisible(false);

      });
    } else {
      showAlert("Error", "No user selected to edit.");
    }
  }

  /**
   * Generates a random salt for password hashing.
   * 
   * @return the generated salt
   * @throws NoSuchAlgorithmException if the algorithm is not available
   */
  public static String getSalt() throws NoSuchAlgorithmException {
    // Tạo ra salt ngẫu nhiên với SecureRandom
    SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
    byte[] salt = new byte[16];
    sr.nextBytes(salt);
    // Mã hóa salt thành chuỗi base64 để dễ lưu trữ
    return Base64.getEncoder().encodeToString(salt);
  }

  /**
   * Navigates to the add user view.
   * 
   * @throws SQLException if a database access error occurs
   */
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

  /**
   * Deletes the selected user.
   */
  @FXML
  public void gotoDeleteUser() {
    if (manageUserEdit != null) {
      if (manageUserEdit.getRole().equals("admin")) {
        showAlert("Error", "Cannot delete admin user.");
        return;
      }
      BookReviewDAO bookReviewDAO = BookReviewDAO.getBookReviewDao();
      if (borrowRecordDAO.isUserBorrowed(manageUserEdit.getId())
          || notiDAO.isUserHasNotification(manageUserEdit.getId())
          || bookReviewDAO.isUserHasReview(manageUserEdit.getId())) {
        showAlert("Error", "Cannot delete user as they have borrowed books.");
        return;
      }
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

  /**
   * Toggles the dark mode.
   */
  @FXML
  public void gotoDarkMode() {

  }
}