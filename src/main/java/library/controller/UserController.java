package library.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import library.dao.BookDAO;
import library.model.Book;
import library.service.BookService;

import java.util.List;

import library.dao.BorrowRecordDAO;
import library.model.BorrowRecord;
import library.model.User;
import library.util.DBConnection;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.io.IOException;

import javafx.stage.Stage;

public class UserController extends LoginController{
    private Stage stage;
    
    @FXML
    private TableView<Book> booksTable;

    @FXML
    private TextField searchField;

    private final DBConnection connection = DBConnection.getInstance();

    private final BookService bookService = new BookService(new BookDAO(connection.getConnection())); // Giả định đã có service xử lý logic mượn sách

    LocalDate today = LocalDate.now();

    public UserController() {
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
        List<Book> books = bookService.getAllBooks();
        booksTable.getItems().setAll(books); // Hiển thị sách lên TableView
    }

    @FXML
    private void handleSearchBook() {
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
                BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAO(connection.getConnection());
                // borrowRecordDAO.addBorrowRecord(new BorrowRecord(0, user, selectedBook, today,today.plusMonths(2) ));
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

    private void returnBook(Book selectedBook) {
        // TODO
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
