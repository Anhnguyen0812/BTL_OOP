package library.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import library.dao.BookDAO;
import library.model.Book;
import library.service.BookService;
import library.util.DBConnection;
import library.api.GoogleBooksAPI;

public class BookController {
    @FXML
    private TextField bookTitleField;
    @FXML
    private TextField bookAuthorField;
    @FXML
    private TextField bookISBNField;
    @FXML
    private ListView<String> bookListView;
    @FXML
    private TextArea searchResultsArea;

    private BookService bookService;
    private GoogleBooksAPI googleBooksAPI;

    public BookController() {
        this.bookService = new BookService(new BookDAO(DBConnection.getConnection()));
        this.googleBooksAPI = new GoogleBooksAPI();
    }

    @FXML
    public void addBook() {
        String title = bookTitleField.getText();
        String author = bookAuthorField.getText();
        String isbn = bookISBNField.getText();
        bookService.addBook(title, author, isbn);
        updateBookList();
    }

    @FXML
    public void updateBook() {
        String selectedBook = bookListView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            int bookId = Integer.parseInt(selectedBook.split(" ")[0]); // Giả sử ID là số đầu tiên trong chuỗi
            String title = bookTitleField.getText();
            String author = bookAuthorField.getText();
            String isbn = bookISBNField.getText();
            bookService.updateBook(bookId, title, author, isbn);
            updateBookList();
        } else {
            showAlert("Please select a book to update.");
        }
    }

    @FXML
    public void deleteBook() throws SQLException {
        String selectedBook = bookListView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            int bookId = Integer.parseInt(selectedBook.split(" ")[0]); // Giả sử ID là số đầu tiên trong chuỗi
            bookService.deleteBook(bookId, DBConnection.getConnection());
            updateBookList();
        } else {
            showAlert("Please select a book to delete.");
        }
    }

    @FXML
    public void searchBook() throws IOException {
        String query = bookTitleField.getText();
        String response = googleBooksAPI.searchBook(query);
        searchResultsArea.setText(response != null ? response : "No results found.");
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
