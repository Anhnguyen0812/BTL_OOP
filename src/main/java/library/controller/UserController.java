package library.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import library.dao.BookDAO;
import library.model.Book;
import library.service.BookService;

import java.util.List;

public class UserController {

    @FXML
    private TableView<Book> booksTable;

    @FXML
    private TextField searchField;

    private final BookService bookService = new BookService(new BookDAO(getDatabaseConnection())); // Giả định đã có service xử lý logic mượn sách

    private java.sql.Connection getDatabaseConnection() {
        // Implement your logic to get a database connection here
        // For example:
        // return DriverManager.getConnection("jdbc:yourdatabaseurl", "username", "password");
        return null; // Replace this with actual connection code
    }

    @FXML
    private void initialize() {
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
        List<Book> books = bookService.getAllBooks();
        booksTable.getItems().setAll(books); // Hiển thị sách lên TableView
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText();
        List<Book> filteredBooks = bookService.searchBooks(query);
        booksTable.getItems().setAll(filteredBooks); // Cập nhật kết quả tìm kiếm
    }

    @FXML
    private void handleBorrowBook() {
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            boolean success = bookService.borrowBook(selectedBook);
            if (success) {
                showAlert("Success", "Book borrowed successfully!");
                loadBooks(); // Cập nhật danh sách sách sau khi mượn
            } else {
                showAlert("Error", "This book is already borrowed.");
            }
        } else {
            showAlert("Error", "Please select a book to borrow.");
        }
    }

    @FXML
    private void handleReturnBook() {
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            boolean success = bookService.returnBook(selectedBook);
            if (success) {
                showAlert("Success", "Book returned successfully!");
                loadBooks(); // Cập nhật danh sách sách sau khi trả
            } else {
                showAlert("Error", "This book is not borrowed.");
            }
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
}
