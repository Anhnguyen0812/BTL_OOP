package library.controller;

import java.io.IOException; // Adjust the package path as necessary
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import library.api.GoogleBooksAPI;
import library.dao.BookDAO;
import library.dao.BorrowRecordDAO;
import library.model.Book;
import library.model.User;
import library.service.BookService;
import library.service.BorrowService;
import library.service.UserService;
import library.util.DBConnection;

public class LibraryController {
  @FXML
  private TextField userNameField;
  @FXML
  private TextField userEmailField;
  @FXML
  private TextField bookTitleField;
  @FXML
  private TextField bookAuthorField;
  @FXML
  private TextField bookISBNField;
  @FXML
  private TextArea searchResultsArea;
  @FXML
  private ListView<String> userListView;
  @FXML
  private ListView<String> bookListView;

  private UserService userService;
  private BookService bookService;
  private BorrowService borrowService;
  private GoogleBooksAPI googleBooksAPI;

  DBConnection connection = DBConnection.getInstance();

  public LibraryController() {
    this.userService = new UserService();
    this.bookService = new BookService(BookDAO.getBookDAO());
    this.borrowService = new BorrowService(BorrowRecordDAO.getBorrowRecordDAO(), BookDAO.getBookDAO());
    this.googleBooksAPI = new GoogleBooksAPI();
  }

  @FXML
  public void addUser() throws NoSuchAlgorithmException {
    String name = userNameField.getText();
    String email = userEmailField.getText();
    User user = new User(0, name, email);
    userService.addUser(user);
    updateUserList();
  }

  @FXML
  public void searchBook() throws IOException {
    String query = bookTitleField.getText();
    String response = googleBooksAPI.searchBook(query);
    searchResultsArea.setText(response != null ? response : "No results found.");
  }

  @FXML
  public void borrowBook() {
    String selectedUser = userListView.getSelectionModel().getSelectedItem();
    String selectedBook = bookListView.getSelectionModel().getSelectedItem();

    if (selectedUser != null && selectedBook != null) {
      User user = userService.getUserById(
          Integer.parseInt(selectedUser.split(" ")[0])); // Giả sử ID là số đầu tiên trong chuỗi
      Book book = bookService.getBookById(
          Integer.parseInt(selectedBook.split(" ")[0])); // Giả sử ID là số đầu tiên trong chuỗi

      borrowService.borrowBook(user, book);
      updateBookList();
    } else {
      showAlert("Please select a user and a book to borrow.");
    }
  }

  @FXML
  public void returnBook() {
    // Phương thức để trả sách
    // Cần logic để lấy thông tin bản ghi mượn từ người dùng
    // Bạn có thể lưu trữ các bản ghi mượn trong ListView để trả sách
  }

  private void updateUserList() {
    List<User> users = userService.getAllUsers();
    userListView.getItems().clear();
    for (User user : users) {
      userListView.getItems().add(user.getId() + " " + user.getName());
    }
  }

  private void updateBookList() {
    List<Book> books = bookService.getAllBooks();
    bookListView.getItems().clear();
    for (Book book : books) {
      bookListView.getItems().add(book.getId() + " " + book.getTitle());
    }
  }

  private void showAlert(String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Information");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
}
