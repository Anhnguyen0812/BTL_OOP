package library.controller;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import com.gluonhq.charm.glisten.control.Avatar;
import com.gluonhq.impl.charm.a.a.a;
import com.gluonhq.impl.charm.a.b.a.b;
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
import library.dao.AllDao;

public class DashController {

  @FXML
  private TextField author, publisher, isbn, bookId, search;
  // @FXML
  // private Insets title;

  @FXML
  private TextField title;

  @FXML
  private Pane home, books, issueBooks, returnBooks, settings, noti, subUser, pane;
  @FXML
  private TableColumn<Book, Integer> Id;
  @FXML
  private TableColumn<Book, String> Title;
  @FXML
  private TableColumn<Book, String> Author;
  @FXML
  private TableColumn<Book, String> ISBN;
  @FXML
  private TableColumn<?, ?> Available;

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
  private Label welcome, date, notiNewLabel;
  @FXML
  private GridPane searchView, searchReturnBooks, featuredBookGridPane;
  @FXML
  private ChoiceBox<String> searchChoice, returnChoice;

  @FXML
  private ImageView avatar, avatar1;

  @FXML
  private MenuButton featuredBookButton;

  protected BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAO();
  protected User user;
  protected HostServices hostServices;

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

  public static final int MAX_COLUMN_RESULTS = 150;
  public static final int MAX_RESULTS_EACH_TIME = 20;
  private boolean isScrolling = false;
  List<Book> booksTop;
  List<Book> booksNew;
  List<Book> booksRecent;

  protected BookDAO bookDAO = BookDAO.getBookDAO();
  private NotiDAO notiDAO = NotiDAO.geNotiDAO();
  protected final BookController bookController = new BookController();

  double scrollSpeed = 2; // Tốc độ cuộn (pixels/giây)

  boolean isHomeTop = true;

  public DashController() {
  }

  public DashController(User user, HostServices hostServices) {
    this.user = user;
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
    int totalBooks = allDao.getTotalBooks();
    int totalUsers = allDao.getTotalUsers();
    int totalBorrowRecords = allDao.getTotalBorrowRecords();

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
        controller.setBookData(book, i, user);
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
            showBookDetails(book);
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
        controller.setBookData(record.getBook(), j, user);

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
            showBookDetails(record.getBook());
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
      setBorrowedBookItems(borrowRecords, 1, true);
    } else if (returnChoice.getValue().equals("Returned")) {
      List<BorrowRecord> borrowRecords = borrowRecordDAO.getReturnRecordsByUserId(user);
      setBorrowedBookItems(borrowRecords, 1, true);
    } else if (returnChoice.getValue().equals("unReturned")) {
      List<BorrowRecord> borrowRecords = borrowRecordDAO.getBorrowRecordsByUserIdWithoutReturnDate(user);
      setBorrowedBookItems(borrowRecords, 1, true);
    }
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
          foundBooks.addAll(bookController.searchBook(bookTitle));
        }
        // Tìm kiếm theo tác giả
        if (!bookAuthor.isEmpty()) {
          foundBooks.addAll(bookController.searchBook(bookAuthor));
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
          controller.setBookData(book, i, user);
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
      } else if (returnBooks.isVisible()) {
        returnBooks.setEffect(blur);
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
        returnBooks.setEffect(null);
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
}