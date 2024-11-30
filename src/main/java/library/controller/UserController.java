package library.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javafx.application.HostServices;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import library.dao.BookDAO;
import library.dao.BorrowRecordDAO;
import library.model.Book;
import library.model.BorrowRecord;
import library.model.User;
import library.service.BookService;
import library.util.DBConnection;

public class UserController {

  protected User user;

  protected HostServices hostServices;

  private Stage stage;
  @FXML private TableView<Book> booksTable;

  @FXML private TextField searchField;

  private final DBConnection connection = DBConnection.getInstance();

  private final BookService bookService =
      new BookService(BookDAO.getBookDAO()); // Giả định đã có service xử lý logic mượn sách
  private BorrowRecordDAO borrowRecordDAO = BorrowRecordDAO.getBorrowRecordDAO();
  private BookDAO bookDAO = BookDAO.getBookDAO();
  private BookController bookController = new BookController();
  private LocalDate today = LocalDate.now();

  public UserController(User user, HostServices hostServices) {
    this.user = user;
    this.hostServices = hostServices;
  }

  @FXML
  public void initialize() {
    // Khởi tạo cột cho bảng sách
    TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
    titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

    TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
    authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

    TableColumn<Book, String> genreColumn = new TableColumn<>("Genre");
    genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
    booksTable.getColumns().addAll(titleColumn, authorColumn, genreColumn);
    // Tải sách ban đầu
    loadBooks();
  }

  private void loadBooks() {
    booksTable.getItems().clear(); // Xóa danh sách sách cũ
    List<Book> books = bookService.getAllBooks();
    booksTable.getItems().setAll(books); // Hiển thị sách lên TableView
  }

  @FXML
  private void handleSearchBook() throws IOException, SQLException {
    String query = searchField.getText();
    ObservableList<Book> filteredBooks = bookController.searchBookByTitleMaxResult(query, 20);
    booksTable.getItems().setAll(filteredBooks); // Cập nhật kết quả tìm kiếm
  }

  private void loadBorrowedBooks() {
    // TODO
  }

  @FXML
  private void handleBorrowBook() throws SQLException {
    Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
    if (selectedBook != null) {
      BorrowRecord borrowRecord =
          new BorrowRecord(0, user, selectedBook, today, today.plusMonths(2));
      borrowRecordDAO.addBorrowRecord(borrowRecord);
      bookDAO.borrowBook(selectedBook);
    } else {
      showAlert("Error", "Please select a book to borrow.");
    }
    loadBorrowedBooks();
  }

  @FXML
  private void handleReturnBook() throws SQLException {
    Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
    if (selectedBook != null) {
      borrowRecordDAO.deleteBorrowRecord(selectedBook);
      bookDAO.returnBook(selectedBook);
    } else {
      showAlert("Error", "Please select a book to return.");
    }
  }

  private void showAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setContentText(message);
    alert.showAndWait();
  }

  @FXML
  private void handleLogout() {
    try {
      Parent root = FXMLLoader.load(getClass().getResource("/library/login.fxml"));
      stage = (Stage) booksTable.getScene().getWindow();
      stage.setScene(new Scene(root, 500, 400));
      stage.show();
    } catch (IOException e) {
      showAlert("Error", "Could not load login interface.");
    }
  }
}
