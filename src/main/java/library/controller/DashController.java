package library.controller;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gluonhq.impl.charm.a.b.b.s;
import com.gluonhq.impl.charm.a.b.b.u;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.geometry.Insets;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import library.dao.BookDAO;
import library.dao.BorrowRecordDAO;
import library.model.Book;
import library.model.BorrowRecord;
import library.model.ConcreteBook;
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
  private Pane home, books, issueBooks, returnBooks, settings, noti, subUser;
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
  private Label welcome;
  @FXML
  private GridPane searchView, searchReturnBooks;
  @FXML
  private ChoiceBox<String> searchChoice, returnChoice;

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
  private BookDAO bookDAO = BookDAO.getBookDAO();
  protected final BookController bookController = new BookController();

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

    welcome.setText("Welcome " + user.getName() + "!");
    user_Button.setText(user.getName());

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

    int totalBooks = allDao.getTotalBooks();
    int totalUsers = allDao.getTotalUsers();
    int totalBorrowRecords = allDao.getTotalBorrowRecords();

    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
        new PieChart.Data("Books", totalBooks),
        new PieChart.Data("Users", totalUsers),
        new PieChart.Data("Borrow Records", totalBorrowRecords));
    pieChartData.forEach(data -> {
      data.setName(data.getName() + " (" + (int) data.getPieValue() + ")");
    });
    chart2.setData(pieChartData);

  }

  private void setBorrowedBookItems(List<BorrowRecord> borrowRecords) {
    searchReturnBooks.getChildren().clear();
    int column = 1;
    int row = 1;
    int i = 0;
    for (BorrowRecord record : borrowRecords) {
      try {
        i++;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/BookItem.fxml"));
        AnchorPane bookItem = loader.load();
        BookItemController controller = loader.getController();
        controller.setBookData(record.getBook(), i, user);
        controller.setReturnButton(record);
        if (column == 3) {
          column = 1;
          row++;
          if (row >= 30)
            break;
        }
        searchReturnBooks.add(bookItem, column++, row);
        GridPane.setMargin(bookItem, new Insets(5)); // Add margin of 5px between items
        bookItem.setOnMouseClicked(event -> {
          if (event.getClickCount() == 2) {
            showBookDetails(record.getBook());
          }
        });
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void saveSearchResults(List<Book> books) {
    for (Book book : books) {
      try {
        bookDAO.addBook(book);
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
    List<BorrowRecord> borrowRecords = borrowRecordDAO.getBorrowRecordsByUserIdWithoutReturnDate(user);
    setBorrowedBookItems(borrowRecords);
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
  public void reloadReturnBooksAction() throws SQLException {
    if (returnChoice.getValue().equals("Borrowed")) {
      List<BorrowRecord> borrowRecords = borrowRecordDAO.getBorrowRecordsByUserId(user);
      setBorrowedBookItems(borrowRecords);
    } else if (returnChoice.getValue().equals("Returned")) {
      List<BorrowRecord> borrowRecords = borrowRecordDAO.getReturnRecordsByUserId(user);
      setBorrowedBookItems(borrowRecords);
    } else if (returnChoice.getValue().equals("unReturned")) {
      List<BorrowRecord> borrowRecords = borrowRecordDAO.getBorrowRecordsByUserIdWithoutReturnDate(user);
      setBorrowedBookItems(borrowRecords);
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
          saveSearchResults(foundBooks);

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
          setBookItem(books, false);
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
        List<Book> books = bookController.searchBookByTitle(bookTitle);
        Platform.runLater(() -> {
          setBookItem(books, true);
        });
        return null;
      }
    };
    new Thread(task).start();

  }

  private void setBookItem(List<Book> books, boolean needCheck) {
    searchView.getChildren().clear();
    int column = 1;
    int row = 1;
    int i = 0;
    if (books.size() == 0) {
      showAlert("No result", "No book found");
      Label noBooksLabel = new Label("No Books found!");
      searchView.add(noBooksLabel, 1, 1);
    } else
      for (Book book : books) {
        try {
          i++;
          FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/BookItem.fxml"));
          AnchorPane bookItem = loader.load();
          BookItemController controller = loader.getController();
          controller.setBookData(book, i, user);
          if (needCheck) {
            controller.setBorrowButtonVisible();
          } else {
            controller.checkBorrowed();
          }

          if (column == 3) {
            column = 1;
            row++;
            if (row >= 30)
              break;
          }
          searchView.add(bookItem, column++, row);
          GridPane.setMargin(bookItem, new Insets(5)); // Add margin of 20px between items
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

  public void gotoNoti() {
    if (noti.isVisible()) {
      noti.setVisible(false);
    } else {
      noti.setVisible(true);
    }
  }

  public void gotoSubUser() {
    if (subUser.isVisible()) {
      subUser.setVisible(false);
    } else {
      subUser.setVisible(true);
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
      Parent root = loader.load();

      // Get the controller of Detail.fxml and set the book data
      DetailController detailController = loader.getController();
      detailController.setBookData(book);

      Stage stage = new Stage();
      stage.setScene(new Scene(root));
      stage.setTitle("Book Details");
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}