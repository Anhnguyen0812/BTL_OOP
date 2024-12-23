package library.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.gluonhq.charm.glisten.control.Avatar;
import com.gluonhq.impl.charm.a.a.a;
import com.gluonhq.impl.charm.a.b.b.s;
import com.gluonhq.impl.charm.a.b.b.u;

import javafx.scene.control.Label;
import javafx.scene.Node;
import javafx.util.Duration;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.geometry.Insets;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
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
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import library.dao.BookDAO;
import library.dao.BorrowRecordDAO;
import library.dao.NotiDAO;
import library.model.*;
import library.service.UserService;
import library.dao.AllDao;
import library.dao.BookReviewDAO;
import library.dao.BorrowRecordDAO;

/**
 * Controller class for the dashboard of the library management system.
 */
public class DashController {

  // @FXML
  // private Insets title;

  @FXML
  private TextField title;

  @FXML
  private Pane home, books, issueBooks, returnBooks, settings, noti, subUser, pane;

  @FXML
  protected TableColumn<BorrowRecord, Integer> Id_Book;
  @FXML
  protected TableColumn<BorrowRecord, String> Title_Book;
  @FXML
  protected TableColumn<BorrowRecord, Date> ngaymuon;
  @FXML
  protected TableColumn<BorrowRecord, Date> ngaytra;

  @FXML
  private Button searchLib, searchGG, home_Button, books_Button, returnBooks_Button, issueBooks_Button, settings_Button;
  @FXML
  private TableView<Book> ListBooks;
  @FXML
  private TableView<BorrowRecord> List_Borrow;
  @FXML
  private Pane return_Book, add_Book;

  @FXML
  protected ProgressIndicator loading;

  @FXML
  private ListView<String> notiList, bookDueDateList;

  @FXML
  private Label welcome, date, notiNewLabel;
  @FXML
  private GridPane searchView, searchReturnBooks, featuredBookGridPane;
  @FXML
  private ChoiceBox<String> searchChoice, returnChoice;

  @FXML
  private ImageView avatar, avatar1, githubImg, fbImg;

  @FXML
  private MenuButton featuredBookButton;

  @FXML
  private Label neww, old, verify;
  @FXML
  private TextField newpass, oldpass, verifypass;
  @FXML
  private Button changepass, assessment, returnBook, borrowBook;
  @FXML
  private Pane boxchange;

  @FXML
  private Label error, iduser, username, email;

  @FXML
  private Button reloadReturnBooksButton;

  @FXML
  StackedAreaChart chart1;

  @FXML
  PieChart chart2;

  AllDao allDao = AllDao.getAllDao();

  @FXML
  private Button Books, logOut, user_Button;
  @FXML
  private ProgressIndicator p1, p2, p3;
  @FXML
  private ScrollPane featuredScrollPane;

  @FXML
  private Canvas star1, star2, star3, star4, star5;
  @FXML
  private TextField comment;
  @FXML
  private Button addComment, closeCommentPane;
  @FXML
  private Pane commentPane;

  public static final int MAX_COLUMN_RESULTS = 150;
  public static final int MAX_RESULTS_EACH_TIME = 20;
  private boolean isScrolling = false;
  List<Book> booksTop;
  List<Book> booksNew;
  List<Book> booksRecent;
  protected BorrowRecordDAO borrowRecordDAO = BorrowRecordDAO.getBorrowRecordDAO();
  protected Member user;
  protected HostServices hostServices;
  protected BookDAO bookDAO = BookDAO.getBookDAO();
  private NotiDAO notiDAO = NotiDAO.geNotiDAO();
  protected final BookController bookController = new BookController();
  private BookReviewDAO bookReviewDAO = BookReviewDAO.getBookReviewDao();

  double scrollSpeed = 1; // Tốc độ cuộn (pixels/giây)

  boolean isHomeTop = true;

  /**
   * Default constructor for DashController.
   */
  public DashController() {
  }

  /**
   * Constructor for DashController with user and host services.
   * 
   * @param user         the user
   * @param hostServices the host services
   */
  public DashController(User user, HostServices hostServices) {
    this.user = (Member) user;
    this.hostServices = hostServices;
  }

  /**
   * Shows an alert with the specified title and message.
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
   * Initializes the dashboard controller.
   * 
   * @throws SQLException if a database access error occurs
   */
  public void initialize() throws SQLException {
    // set up loading
    loading.setVisible(false);

    // Set up title for table
    welcome.setText("Welcome " + user.getName() + "!");
    user_Button.setText(user.getName());
    // link github and fb
    githubImg.setOnMouseClicked(event -> {
      hostServices.showDocument("https://github.com/Anhnguyen0812/BTL_OOP");
    });
    fbImg.setOnMouseClicked(event -> {
      hostServices.showDocument("https://www.facebook.com/profile.php?id=100018071889476");
    });

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
    returnBooks.setVisible(false);
    issueBooks.setVisible(false);
    settings.setVisible(false);
    home_Button.styleProperty().set("-fx-background-color: #777777");

    javafx.animation.TranslateTransition transition = new javafx.animation.TranslateTransition(
        javafx.util.Duration.millis(300), noti);

    // Set up ChoiceBox for search and return
    searchChoice.getItems().addAll("Title", "Author", "ISBN");
    returnChoice.getItems().addAll("Borrow Request", "Borrow", "Return Request", "Returned");

    // Set up values for ChoiceBox
    searchChoice.setValue("Title");
    returnChoice.setValue("Borrow Request");

    // get Lib Info
    int totalBooks = bookDAO.getAllBooks().size();
    int totalUsers = UserService.getAllUsers().size();
    int totalBorrowRecords = borrowRecordDAO.getAllBorrowRecords().size();

    // Set up PieChart
    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
        new PieChart.Data("Books", totalBooks),
        new PieChart.Data("Users", totalUsers),
        new PieChart.Data("Borrow Records", totalBorrowRecords));
    pieChartData.forEach(data -> {
      data.setName(data.getName() + " (" + (int) data.getPieValue() + ")");
    });
    chart2.setData(pieChartData);

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

    iduser.setText(String.valueOf("Id : " + user.getId()));
    username.setText("Username : " + user.getName());
    email.setText("Email : " + user.getEmail());

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

    // feature book
    booksTop = bookDAO.getTopBooks();
    booksNew = bookDAO.getNewBooks();
    List<BorrowRecord> borrowRecordRecent = borrowRecordDAO.getRecentBorrowRecordsByUserId(user);
    booksRecent = new ArrayList<>();
    for (BorrowRecord record : borrowRecordRecent) {
      booksRecent.add(record.getBook());
    }

    // load booksTop vào featuredBookGridPane
    setFeaturedBooks(booksTop);

    // làm featuregridpane lặp lại vô hạn
    // Duplicate the featured books to create an infinite loop effect

    // Sử dụng AnimationTimer để cuộn liên tục
    AnimationTimer timer = new AnimationTimer() {
      private long lastUpdate = 0;

      @Override
      public void handle(long now) {
        if (home.isVisible() && isHomeTop && isScrolling && now - lastUpdate >= 16_666_667) { // ~60 FPS
          double currentVvalue = featuredScrollPane.getVvalue();
          double newVvalue = currentVvalue + (scrollSpeed / 60); // Tính toán Vvalue mới
          if (newVvalue >= 1) {
            newVvalue = 0; // Quay trở lại đầu khi cuộn hết
          }
          featuredScrollPane.setVvalue(newVvalue);
          lastUpdate = now;
        }
      }
    };
    timer.start();

    // Sử dụng Timeline để kiểm soát thời gian cuộn và dừng
    Timeline timeline = new Timeline(
        new KeyFrame(Duration.seconds(0.2), event -> isScrolling = false), // Dừng cuộn sau 0.5 giây
        new KeyFrame(Duration.seconds(3.2), event -> isScrolling = true) // Bắt đầu cuộn sau 3.5 giây (0.5s cuộn + 3s
                                                                         // dừng)
    );
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();

    // Dừng cuộn khi trỏ chuột vào
    featuredScrollPane.setOnMouseEntered(event -> {
      timeline.pause(); // Dừng Timeline khi trỏ chuột vào
    });
    featuredBookButton.setOnMouseEntered(event -> {
      timeline.pause(); // Dừng Timeline khi trỏ chuột vào
    });

    // Không tự động cuộn khi trỏ chuột ra ngoài
    featuredScrollPane.setOnMouseExited(event -> {
      timeline.play(); // Tiếp tục Timeline khi trỏ chu���t ra ngoài
    });
    featuredBookButton.setOnMouseExited(event -> {
      timeline.play(); // Tiếp tục Timeline khi trỏ chuột ra ngoài
    });

    // Set up notiList

    javafx.scene.effect.BoxBlur blur = new javafx.scene.effect.BoxBlur(10, 10, 3);
    changepass.setOnAction(event -> {
      boxchange.setVisible(true);
      if (home.isVisible()) {
        home.setEffect(blur);
      } else if (books.isVisible()) {
        books.setEffect(blur);
      } else if (issueBooks.isVisible()) {
        issueBooks.setEffect(blur);
      } else if (returnBooks.isVisible()) {
        returnBooks.setEffect(blur);
      } else if (settings.isVisible()) {
        settings.setEffect(blur);
      }
    });

    closeCommentPane.setOnAction(event -> {
      commentPane.setVisible(false);
    });

    // set up book
    SearchLibrary();
    // setup bookduedatelist
    List<BorrowRecord> dueSoonBooks = borrowRecordDAO.getDueSoonBooks(user.getId());
    List<BorrowRecord> overdueBooks = borrowRecordDAO.getOverdueBooks(user.getId());
    bookDueDateList.getItems().clear();
    if (dueSoonBooks.size() == 0 && overdueBooks.size() == 0) {
      bookDueDateList.getItems().add("No books due soon or overdue.");
    } else {
      bookDueDateList.getItems().clear();
      if (dueSoonBooks.size() > 0)
        for (BorrowRecord record : dueSoonBooks) {
          bookDueDateList.getItems()
              .add("Due Soon: " + record.getBook().getTitle() + " - Due Date: " + record.getReturnDate());
        }

      if (overdueBooks.size() > 0)
        for (BorrowRecord record : overdueBooks) {
          Label overdueLabel = new Label(
              "Overdue: " + record.getBook().getTitle() + " - Due Date: " + record.getReturnDate());
          overdueLabel.setTextFill(Color.RED);
          bookDueDateList.getItems().add(overdueLabel.getText());
        }
    }
  }

  /**
   * Sets the featured books in the grid pane.
   * 
   * @param booksTop the list of top books
   */
  public void setFeaturedBooks(List<Book> booksTop) {
    int column = 1;
    int row = 1;
    int i = 0;
    for (Book book : booksTop) {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/BookItem.fxml"));
        AnchorPane bookItem = loader.load();
        BookItemController controller = loader.getController();
        i++;
        controller.setItemData(book, i, user);
        controller.hideButton();

        if (book.getRateAvg() == null) {
          controller.displayBookRate(0, false);
        } else {
          controller.displayBookRate(book.getRateAvg(), true);
        }
        controller.setReturnButton(null);

        if (column == 3) {
          column = 1;
          row++;
          if (row >= 30)
            break;
        }
        featuredBookGridPane.add(bookItem, column++, row);
        GridPane.setMargin(bookItem, new Insets(5)); // Add margin of 5px between items
        bookItem.setOnMouseClicked(event -> {
          if (event.getClickCount() == 2) {
            try {
              showBookDetails(book, false, false, null, 0);
            } catch (SQLException e) {
              e.printStackTrace();
              showAlert("Error", "Failed to show book details.");
            }
          }
        });
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

  }

  /**
   * Applies a circular clip to the specified image view.
   * 
   * @param imageView the image view to apply the clip to
   */
  private void applyCircularClip(ImageView imageView) {
    Circle clip = new Circle(25, 25, 25);
    imageView.setClip(clip);
  }

  int j = 0;

  /**
   * Sets the borrowed book items in the grid pane.
   * 
   * @param borrowRecords the list of borrow records
   * @param k             the starting index
   * @param needClear     whether to clear the existing items
   * @param status        the status of the borrow records
   */
  private void setBorrowedBookItems(List<BorrowRecord> borrowRecords, int k, boolean needClear, int status,
      int showassess) {
    if (needClear)
      searchReturnBooks.getChildren().clear();

    j = k;
    int column = j % 2;
    int row = j / 2 + 1;
    for (; j < borrowRecords.size() + 1; j++) {
      try {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/BookItem.fxml"));
        AnchorPane bookItem = loader.load();
        BookItemController controller = loader.getController();

        BorrowRecord record = borrowRecords.get(j - 1);
        controller.setItemData(record.getBook(), j, user);

        if (record.getBook().getRateAvg() == null) {
          controller.displayBookRate(0, false);
        } else {
          controller.displayBookRate(record.getBook().getRateAvg(), true);
        }
        controller.hideButton();

        if (column == 3) {
          column = 1;
          row++;
          if (row >= MAX_COLUMN_RESULTS)
            break;
        }
        searchReturnBooks.add(bookItem, column++, row);

        if (status == 1) {
          Button returnRequestButton = new Button("Return Request");
          returnRequestButton.setOnAction(event -> {
            user.requestReturnBook(record);

            try {
              notiDAO.addNotificationFromUserToAdmin(1,
                  "User " + user.getId() + ", Name: " + user.getName() + " requested to return book "
                      + record.getBook().getTitle());
            } catch (SQLException e) {
              e.printStackTrace();
            }

            returnRequestButton.setDisable(true);
          });

          bookItem.getChildren().add(returnRequestButton);
          returnRequestButton.setLayoutX(10); // Set X coordinate
          returnRequestButton.setLayoutY(10); // Set Y coordinate
        }

        GridPane.setMargin(bookItem, new Insets(5)); // Add margin of 5px between items
        bookItem.setOnMouseClicked(event -> {
          if (event.getClickCount() == 2) {
            try {
              showBookDetails(record.getBook(), true, false, record, showassess);
            } catch (SQLException e) {
              e.printStackTrace();
              showAlert("Error", "Failed to show book details.");
            }
          }
        });

        if (j % 20 == 0) {
          Button loadMoreButton = new Button("Load More");
          loadMoreButton.setOnAction(event -> {
            searchReturnBooks.getChildren().remove(loadMoreButton);
            setBorrowedBookItems(borrowRecords, j + 1, false, status, showassess);
          });
          searchReturnBooks.add(loadMoreButton, 2, row + 1, 3, 1);
          GridPane.setMargin(loadMoreButton, new Insets(5));
          break;
        }

      } catch (IOException e) {
        e.printStackTrace();
      }
    }

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
   * Resets the style of the dashboard.
   */
  public void resetStyle() {
    home.setVisible(false);
    books.setVisible(false);
    returnBooks.setVisible(false);
    issueBooks.setVisible(false);
    settings.setVisible(false);
    home_Button.styleProperty().set("-fx-background-color: #A6AEBF");
    books_Button.styleProperty().set("-fx-background-color: #A6AEBF");
    returnBooks_Button.styleProperty().set("-fx-background-color: #A6AEBF");
    issueBooks_Button.styleProperty().set("-fx-background-color: #A6AEBF");
    settings_Button.styleProperty().set("-fx-background-color: #A6AEBF");
  }

  /**
   * Navigates to the home pane.
   */
  public void gotoHome() {
    resetStyle();
    home.setVisible(true);
    home_Button.styleProperty().set("-fx-background-color: #777777");
    FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), home);
    fadeTransition.setFromValue(0.0);
    fadeTransition.setToValue(1.0);
    fadeTransition.play();
  }

  /**
   * Navigates to the books pane.
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
   * Navigates to the return books pane.
   * 
   * @throws SQLException if a database access error occurs
   */

  public void gotoReturnBooks() throws SQLException {
    resetStyle();
    returnBooks.setVisible(true);
    returnBooks_Button.styleProperty().set("-fx-background-color: #777777");
    List<BorrowRecord> borrowRecords = borrowRecordDAO.getBorrowRequestByUserId(user.getId());

    if (!borrowRecords.isEmpty())
      setBorrowedBookItems(borrowRecords, 1, true, 0, 0);

    FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), returnBooks);
    fadeTransition.setFromValue(0.0);
    fadeTransition.setToValue(1.0);
    fadeTransition.play();
  }

  /**
   * Navigates to the issue books pane.
   */
  public void gotoIssueBooks() {
    resetStyle();
    issueBooks.setVisible(true);
    issueBooks_Button.styleProperty().set("-fx-background-color: #777777");

    FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), issueBooks);
    fadeTransition.setFromValue(0.0);
    fadeTransition.setToValue(1.0);
    fadeTransition.play();
  }

  /**
   * Navigates to the settings pane.
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
   * Navigates to the rate book pane.
   * 
   * @throws SQLException if a database access error occurs
   */
  @FXML
  public void gotoRateBook() throws SQLException {
    booksTop = bookDAO.getTopBooks();
    setFeaturedBooks(booksTop);
  }

  /**
   * Navigates to the recent books pane.
   * 
   * @throws SQLException if a database access error occurs
   */
  @FXML
  public void gotoRecentBooks() throws SQLException {
    setFeaturedBooks(booksRecent);
  }

  /**
   * Navigates to the new books pane.
   * 
   * @throws SQLException if a database access error occurs
   */
  @FXML
  public void gotoNewBooks() throws SQLException {
    booksNew = bookDAO.getNewBooks();
    setFeaturedBooks(booksNew);
  }

  /**
   * Handles the save image action.
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
   * Loads the image for the specified image view.
   * 
   * @param imageView_ the image view to load the image into
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
   * Reloads the return books based on the selected choice.
   * 
   * @throws SQLException if a database access error occurs
   */
  @FXML
  public void reloadReturnBooksAction() throws SQLException {
    if (returnChoice.getValue().equals("Borrow Request")) {
      List<BorrowRecord> borrowRecords = borrowRecordDAO.getBorrowRequestByUserId(user);
      setBorrowedBookItems(borrowRecords, 1, true, 0, 0);
    } else if (returnChoice.getValue().equals("Borrow")) {
      List<BorrowRecord> borrowRecords = borrowRecordDAO.getBorrowRecordsByUserId(user);
      setBorrowedBookItems(borrowRecords, 1, true, 1, 1);
    } else if (returnChoice.getValue().equals("Return Request")) {
      List<BorrowRecord> borrowRecords = borrowRecordDAO.getReturnRequestByUserId(user);
      setBorrowedBookItems(borrowRecords, 1, true, 0, 1);
    } else {
      List<BorrowRecord> borrowRecords = borrowRecordDAO.getReturnedByUserId(user);
      setBorrowedBookItems(borrowRecords, 1, true, 0, 1);
    }
  }

  /**
   * Searches the library for books based on the title.
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
  }

  /**
   * Searches for books on Google based on the title.
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
   * @param needCheck whether to check if the books are borrowed
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

          if (book.getRateAvg() == null) {
            controller.displayBookRate(0, false);
          } else {
            controller.displayBookRate(book.getRateAvg(), true);
          }
          if (needCheck) {
            controller.setBorrowButtonVisible();
          } else {
            controller.checkBorrowed();
          }
          if (isGoogle) {
            controller.hideButton();
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
            if (event.getClickCount() == 2) {
              try {
                showBookDetails(book, false, isGoogle, null, 0);
              } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to show book details.");
              }
            }
          });

          if (i % MAX_RESULTS_EACH_TIME == 0) {
            Button loadMoreButton = new Button("Load More");
            loadMoreButton.setOnAction(event -> {
              searchView.getChildren().remove(loadMoreButton);
              Task<Void> loadMoreTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                  if (isGoogle) {
                    try {
                      List<Book> book1 = bookController.searchBookByTitleWithStartIndex(title.getText(), i,
                          MAX_RESULTS_EACH_TIME);
                      books.addAll(book1);
                    } catch (Exception e) {
                      e.printStackTrace();
                    }
                  }
                  return null;
                }
              };
              loading.progressProperty().bind(loadMoreTask.progressProperty());
              loadMoreTask.setOnSucceeded(e -> {
                setBookItem(books, i + 1, needCheck, false, isGoogle);
                loading.setVisible(false);
                loading.progressProperty().unbind();
              });
              loadMoreTask.setOnFailed(e -> {
                loading.setVisible(false);
                loading.progressProperty().unbind();
                showAlert("Error", "Failed to load more books.");
              });
              new Thread(loadMoreTask).start();
              loading.setVisible(true);

            });
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
   * Navigates to the notifications pane.
   */
  public void gotoNoti() {
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
   * Navigates to the sub-user pane.
   */
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
   * Navigates to the change password pane.
   */
  public void gotoChangePass() {
    if (boxchange.isVisible()) {
      boxchange.setVisible(false);
      if (home.isVisible()) {
        home.setEffect(null);
      } else if (books.isVisible()) {
        books.setEffect(null);
      } else if (issueBooks.isVisible()) {
        issueBooks.setEffect(null);
      } else if (returnBooks.isVisible()) {
        returnBooks.setEffect(null);
      } else if (settings.isVisible()) {
        settings.setEffect(null);
      }
    } else {
      boxchange.setVisible(true);
      boxchange.toFront();
    }
  }

  /**
   * Logs out the current user and navigates to the login pane.
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
   * Shows the details of the specified book.
   * 
   * @param book          the book to show details for
   * @param checkBorrowed whether to check if the book is borrowed
   * @param isGoogle      whether the book is from Google
   * @param record        the borrow record of the book
   * @throws SQLException if a database access error occurs
   */
  public void showBookDetails(Book book, boolean checkBorrowed, boolean isGoogle, BorrowRecord record, int showAssess)
      throws SQLException, SQLException {
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
      } else if (returnBooks.isVisible()) {
        returnBooks.setEffect(blur);
      } else if (settings.isVisible()) {
        settings.setEffect(blur);
      }
      bookDetailPane.setLayoutX(170); // Set X coordinate
      bookDetailPane.setLayoutY(80); // Set Y coordinate

      pane.getChildren().add(bookDetailPane);
      // dang duoc muon
      if (checkBorrowed && showAssess == 1) {
        bookDetailPane.getChildren().add(assessment);
        bookDetailPane.getChildren().add(returnBook);
        assessment.setLayoutX(720);
        assessment.setLayoutY(322);
        assessment.setVisible(true);
        returnBook.setVisible(true);
        returnBook.setOnAction(event -> {
          if (borrowRecordDAO.isBorrowed(user, book)) {
            user.requestReturnBook(record);
            notiDAO = NotiDAO.geNotiDAO();
            try {
              notiDAO.addNotificationFromUserToAdmin(1,
                  "User " + user.getId() + ", Name: " + user.getName() + " return book " + book.getTitle());
            } catch (Exception e) {
              System.out.println(e);
            }
            returnBook.setVisible(true);
          }
        });
        addComment.setOnAction(event -> {
          String commentText = comment.getText();
          Canvas[] stars = { star1, star2, star3, star4, star5 };
          int count = 0;
          for (Canvas star : stars) {
            GraphicsContext gc = star.getGraphicsContext2D();
            if (isStarYellow(gc)) {
              count++;
            }
          }
          try {
            boolean check = bookReviewDAO.getReviewBookByUser(user.getId(), book.getId());
            if (check == false) {
              bookReviewDAO.addReview(book.getId(), user.getId(), commentText, count);
              bookReviewDAO.updateRateBook(book.getId(), bookReviewDAO.getRateBook(book.getId()));
            } else {
              bookReviewDAO.updateReview(user.getId(), book.getId(), commentText, count);
              bookReviewDAO.updateRateBook(book.getId(), bookReviewDAO.getRateBook(book.getId()));
            }
            BookDAO bookDAO = BookDAO.getBookDAO();
            book.setRateAvg(bookDAO.getBookById(book.getId()).getRateAvg());
          } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to handle book review.");
          }
        });
      } else if (!isGoogle) {
        bookDetailPane.getChildren().add(borrowBook);
        borrowBook.setVisible(true);
        borrowBook.setOnAction(borrowEvent -> {
          try {
            if (!borrowRecordDAO.isBorrowed(user, book)) {
              BorrowRecord borrowRecord = new BorrowRecord(1, user, book, LocalDate.now(),
                  LocalDate.now().plusDays(20));
              user.requestBorrowBook(borrowRecord);
              borrowBook.setVisible(false);
              NotiDAO notiDAO = NotiDAO.geNotiDAO();
              notiDAO.addNotificationFromUserToAdmin(1,
                  "User " + user.getId() + ", Name: " + user.getName() + " request to borrow book "
                      + book.getTitle());
            } else {
              Label notiactive = new Label();
              bookDetailPane.getChildren().add(notiactive);
              notiactive.setText("You Borrowed This Book");
              notiactive.setVisible(true);
              notiactive.setLayoutX(700);
              notiactive.setLayoutY(322);
              notiactive.setTextFill(Color.RED);
              Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), e -> notiactive.setVisible(false)));
              timeline.setCycleCount(1); // Chạy một lần
              timeline.play();
            }
          } catch (Exception e) {
            System.out.println(e);
          }
        });
      }

      Button closeButton = new Button("X");
      closeButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-weight: bold;");
      closeButton.setOnAction(event -> {
        pane.getChildren().remove(bookDetailPane);
        home.setEffect(null);
        books.setEffect(null);
        issueBooks.setEffect(null);
        returnBooks.setEffect(null);
        settings.setEffect(null);
        isHomeTop = true;
        assessment.setVisible(false);
        returnBook.setVisible(false);
        borrowBook.setVisible(false);
      });
      bookDetailPane.getChildren().add(closeButton);
      closeButton.setLayoutX(1050);
      closeButton.setLayoutY(10);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Changes the password of the current user.
   * 
   * @throws SQLException             if a database access error occurs
   * @throws NoSuchAlgorithmException if the hashing algorithm is not available
   */
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
      String salt = Dash_AdminController.getSalt();
      String hashedPassword = UserService.hashPassword(newPassword, salt);
      user.setPassword(hashedPassword);
      user.setSalt(salt);
      UserService userService = new UserService();
      userService.editUser(user);
      old.setVisible(false);
      neww.setVisible(false);
      verify.setVisible(false);
    }
  }

  /**
   * Enables dark mode for the dashboard.
   */
  @FXML
  public void gotoDarkMode() {
    // Set up dark mode
    home.setStyle("-fx-background-color: #333333");
    books.setStyle("-fx-background-color: #333333");
    issueBooks.setStyle("-fx-background-color: #333333");
    returnBooks.setStyle("-fx-background-color: #333333");
    settings.setStyle("-fx-background-color: #333333");
    noti.setStyle("-fx-background-color: #333333");
    subUser.setStyle("-fx-background-color: #333333");
    pane.setStyle("-fx-background-color: #333333");
    searchView.setStyle("-fx-background-color: #333333");
    searchReturnBooks.setStyle("-fx-background-color: #333333");
    featuredBookGridPane.setStyle("-fx-background-color: #333333");
    featuredScrollPane.setStyle("-fx-background-color: #333333");
    featuredBookButton.setStyle("-fx-background-color: #333333");
    featuredBookButton.setTextFill(javafx.scene.paint.Color.WHITE);
    featuredBookButton.setEffect(new javafx.scene.effect.DropShadow());
    featuredBookButton.setOpacity(0.8);
    featuredBookButton.setPadding(new Insets(10, 20, 10, 20));
    featuredBookButton.setPrefSize(200, 50);
    featuredBookButton.setLayoutX(50);
    featuredBookButton.setLayoutY(50);
    featuredBookButton.setWrapText(true);
    featuredBookButton.setGraphicTextGap(10);
    featuredBookButton.setGraphic(new ImageView(new Image("/imgs/featured.png")));
    featuredBookButton.setOnAction(event -> {
      gotoHome();
    });
  }

  /**
   * Navigates to the assessment pane.
   */
  @FXML
  public void gotoAssessment() {
    if (commentPane.isVisible()) {
      commentPane.setVisible(false);
    } else {
      commentPane.setVisible(true);
      commentPane.toFront();
      Canvas[] stars = { star1, star2, star3, star4, star5 };
      for (Canvas star : stars) {
        GraphicsContext gc = star.getGraphicsContext2D();
        gc.clearRect(0, 0, star.getWidth(), star.getHeight());
        gc.setFill(Color.GRAY);
        drawStar(gc, 10, 10, 10, Color.GRAY);
        star.setOnMouseClicked(event -> {
          if (event.getClickCount() == 1) {
            int clickedIndex = java.util.Arrays.asList(stars).indexOf(star);
            for (int i = 0; i <= clickedIndex; i++) {
              drawStar(stars[i].getGraphicsContext2D(), 10, 10, 10, Color.YELLOW); // Tô sáng ngôi sao
            }
            for (int i = clickedIndex + 1; i < stars.length; i++) {
              drawStar(stars[i].getGraphicsContext2D(), 10, 10, 10, Color.GRAY); // Chuyển thành màu xám
            }
          }
        });
      }
    }
  }

  /**
   * Checks if the star is yellow.
   * 
   * @param gc the graphics context of the star
   * @return true if the star is yellow, false otherwise
   */
  private boolean isStarYellow(GraphicsContext gc) {
    Color color = (Color) gc.getFill();
    return color.equals(Color.YELLOW);
  }

  /**
   * Draws a star with the specified parameters.
   * 
   * @param gc     the graphics context to draw the star
   * @param x      the x-coordinate of the star
   * @param y      the y-coordinate of the star
   * @param radius the radius of the star
   * @param color  the color of the star
   */
  private void drawStar(GraphicsContext gc, double x, double y, double radius, Color color) {
    double innerRadius = radius / 2.5; // Bán kính bên trong ngôi sao
    double[] xPoints = new double[10];
    double[] yPoints = new double[10];
    // Tính tọa độ các đỉnh của ngôi sao
    for (int k = 0; k < 10; k++) {
      double angle = Math.toRadians(-90 + k * 36); // Góc mỗi đỉnh (36 độ)
      double r = (k % 2 == 0) ? radius : innerRadius; // Đỉnh ngoài hoặc trong
      xPoints[k] = x + r * Math.cos(angle);
      yPoints[k] = y + r * Math.sin(angle);
    }
    // Vẽ ngôi sao
    gc.setFill(color);
    gc.fillPolygon(xPoints, yPoints, 10);
  }

}