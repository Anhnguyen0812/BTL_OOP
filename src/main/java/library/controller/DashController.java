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
import library.model.Book;
import library.model.BorrowRecord;
import library.model.ConcreteBook;
import library.model.ImageHandler;
import library.model.User;
import library.service.UserService;
import library.dao.AllDao;
import library.dao.BookReviewDAO;
import library.dao.BorrowRecordDAO;
import library.model.Member;

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
  private ListView<String> notiList;

  @FXML
  private Label welcome, date, notiNewLabel, notiactive;
  @FXML
  private GridPane searchView, searchReturnBooks, featuredBookGridPane;
  @FXML
  private ChoiceBox<String> searchChoice, returnChoice;

  @FXML
  private ImageView avatar, avatar1;

  @FXML
  private MenuButton featuredBookButton;

  @FXML private Label neww, old, verify;
  @FXML private TextField newpass, oldpass, verifypass;
  @FXML private Button changepass, assessment, returnBook, borrowBook;
  @FXML private Pane boxchange;

  @FXML
  private Label error, iduser, username, email;

  @FXML
  private Button reloadReturnBooksButton;

  @FXML
  StackedAreaChart chart1;

  @FXML
  PieChart chart2;

  AllDao allDao = new AllDao();

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
  private final BookReviewDAO bookReviewDAO = BookReviewDAO.getBookReviewDao();

  private final double scrollSpeed = 1; // Tốc độ cuộn (pixels/giây)

  boolean isHomeTop = true;

  public DashController() {
  }

  public DashController(User user, HostServices hostServices) {
    this.user = (Member) user;
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

    borrowRecordDAO.deleteBorrowRecordbyId(257);

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
    returnBooks.setVisible(false);
    issueBooks.setVisible(false);
    settings.setVisible(false);
    home_Button.styleProperty().set("-fx-background-color: #777777");

    // Set up ChoiceBox for search and return
    searchChoice.getItems().addAll("Title", "Author", "ISBN");
    returnChoice.getItems().addAll("Borrowed", "Returned", "unReturned");

    // Set up values for ChoiceBox
    searchChoice.setValue("Title");
    returnChoice.setValue("Borrowed");

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
      timeline.play(); // Tiếp tục Timeline khi trỏ chuột ra ngoài
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

  }

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
                showBookDetails(book, false, false, null);
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

  private void applyCircularClip(ImageView imageView) {
    Circle clip = new Circle(25, 25, 25);
    imageView.setClip(clip);
  }

  int j = 0;

  private void setBorrowedBookItems(List<BorrowRecord> borrowRecords, int k, boolean needClear) {
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
        controller.setReturnButton(record);

        if (column == 3) {
          column = 1;
          row++;
          if (row >= MAX_COLUMN_RESULTS)
            break;
        }
        searchReturnBooks.add(bookItem, column++, row);
        GridPane.setMargin(bookItem, new Insets(5)); // Add margin of 5px between items
        bookItem.setOnMouseClicked(event -> {
          if (event.getClickCount() == 2) {
            try {
                showBookDetails(record.getBook(), true, false, record);
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
            setBorrowedBookItems(borrowRecords, j + 1, false);
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
    returnBooks.setVisible(false);
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
    returnBooks.setVisible(true);
    returnBooks_Button.styleProperty().set("-fx-background-color: #777777");
    List<BorrowRecord> borrowRecords = borrowRecordDAO.getBorrowRequestByUserId(user.getId());
    System.out.println(user.getId());
    if (!borrowRecords.isEmpty())
    setBorrowedBookItems(borrowRecords, 1, true);
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

  @FXML
  public void gotoRateBook() {
    setFeaturedBooks(booksTop);
  }

  @FXML
  public void gotoRecentBooks() {
    setFeaturedBooks(booksRecent);
  }

  @FXML
  public void gotoNewBooks() {
    setFeaturedBooks(booksNew);
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
  public void reloadReturnBooksAction() throws SQLException {
    if (returnChoice.getValue().equals("Borrowed")) {
      List<BorrowRecord> borrowRecords = borrowRecordDAO.getBorrowRecordsByUserId(user);
      if (!borrowRecords.isEmpty())
      setBorrowedBookItems(borrowRecords, 1, true);
    } else if (returnChoice.getValue().equals("Returned")) {
      List<BorrowRecord> borrowRecords = borrowRecordDAO.getReturnRecordsByUserId(user);
      if (!borrowRecords.isEmpty())
      setBorrowedBookItems(borrowRecords, 1, true);
    } else if (returnChoice.getValue().equals("unReturned")) {
      List<BorrowRecord> borrowRecords = borrowRecordDAO.getBorrowRecordsByUserIdWithoutReturnDate(user);
      if (!borrowRecords.isEmpty())
      setBorrowedBookItems(borrowRecords, 1, true);
    }
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
                showBookDetails(book, false, isGoogle, null);
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
              if (isGoogle) {
                try {
                  List<Book> book1 = bookController.searchBookByTitleWithStartIndex(title.getText(), i,
                      MAX_RESULTS_EACH_TIME);
                  books.addAll(book1);
                } catch (Exception e) {
                  e.printStackTrace();
                }

              }
              setBookItem(books, i + 1, needCheck, false, isGoogle);

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
      e.printStackTrace();
    }
  }

  public void showBookDetails(Book book, boolean checkBorrowed, boolean isGoogle, BorrowRecord record) throws SQLException , SQLException {
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
      } else if (returnBooks.isVisible()) {
        returnBooks.setEffect(blur);
      } else if (settings.isVisible()) {
        settings.setEffect(blur);
      }
      bookDetailPane.setLayoutX(170); // Set X coordinate
      bookDetailPane.setLayoutY(80); // Set Y coordinate
      
      pane.getChildren().add(bookDetailPane);
      // dang duoc muon
      if (checkBorrowed) {
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
            } else {
              bookReviewDAO.updateReview(user.getId(), book.getId(), commentText, count);
            }
          } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to handle book review.");
          }
        });
      }
      else if (!isGoogle) {
        bookDetailPane.getChildren().add(borrowBook);
        borrowBook.setVisible(true);
        borrowBook.setOnAction(borrowEvent -> {
          try {
            if (!borrowRecordDAO.isBorrowed(user, book)) {
            BorrowRecord borrowRecord = new BorrowRecord(1, user, book, LocalDate.now(), LocalDate.now().plusDays(20));
            user.requestBorrowBook(borrowRecord);
            borrowBook.setVisible(false);
            NotiDAO notiDAO = NotiDAO.geNotiDAO();
              notiDAO.addNotificationFromUserToAdmin(1,
                      "User " + user.getId() + ", Name: " + user.getName() + " request to borrow book "
                              + book.getTitle());
            } else {
              bookDetailPane.getChildren().add(notiactive);
              notiactive.setText("You Borrowed This Book");
              notiactive.setVisible(true);
              notiactive.setLayoutX(700);
              notiactive.setLayoutY(322);
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
              drawStar(gc, 15, 15, 15, Color.GRAY);
              star.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                      if (isStarYellow(gc)) {
                          drawStar(gc, 15, 15, 15, Color.GRAY); // Chuyển thành màu xám
                      } else {
                          drawStar(gc, 15, 15, 15, Color.YELLOW); // Chuyển thành màu vàng
                      }
                }
              });
          }
    }
  }

  private boolean isStarYellow(GraphicsContext gc) {
    Color color = (Color) gc.getFill();
    return color.equals(Color.YELLOW);
  }

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