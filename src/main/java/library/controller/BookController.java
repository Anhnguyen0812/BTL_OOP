package library.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Statement;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONArray;

import library.dao.BookDAO;
import library.model.Book;
import library.model.ConcreteBook;
import library.model.ReferenceBook;
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

    // private ObservableList<Book> books = FXCollections.observableArrayList();

    private DBConnection connection = DBConnection.getInstance();

    public BookController() {
        
        this.bookService = new BookService(new BookDAO(connection.getConnection()));
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
            bookService.deleteBook(bookId, connection.getConnection());
            updateBookList();
        } else {
            showAlert("Please select a book to delete.");
        }
    }

    public ObservableList<Book> searchBook(String query) throws IOException, SQLException {
        // Task<ObservableList<Book>> task = new Task<ObservableList<Book>>() {
        //     @Override
        //     protected ObservableList<Book> call() throws Exception {
        //         // Gọi hàm lấy dữ liệu từ Google Books API
        //         String response = googleBooksAPI.searchBook(query);
        //         // Phân tích JSON trong luồng riêng
        //         return parseBooks(response);
        //     }

        //     @Override
        //     protected void succeeded() {
        //         super.succeeded();
        //         // Cập nhật ListView với danh sách sách tìm được
        //         searchResult.setItems(getValue());
        //     }

        //     @Override
        //     protected void failed() {
        //         super.failed();
        //         // Xử lý ngoại lệ nếu có
        //         searchResult.setItems(FXCollections.observableArrayList()); // Hoặc cập nhật một thông báo lỗi
        //     }
        // };

        // new Thread(task).start(); 
        
        // return null;
        String response = googleBooksAPI.searchBook(query);
        return parseBooks(response);
    }

    private ObservableList<Book> parseBooks(String jsonData) throws SQLException {
        // ObservableList<Book> books = FXCollections.observableArrayList();
        ObservableList<Book> books = FXCollections.observableArrayList();
        JSONObject jsonObject = new JSONObject(jsonData);
    
            // Kiểm tra xem có trường "items" trong JSON không
        if (jsonObject.has("items")) {
            JSONArray booksArray = jsonObject.getJSONArray("items");
            for (int i = 0; i < booksArray.length(); i++) {
                JSONObject volumeInfo = booksArray.getJSONObject(i).getJSONObject("volumeInfo");
                String title = volumeInfo.getString("title");
                String isbn = volumeInfo.has("industryIdentifiers") ? volumeInfo.getJSONArray("industryIdentifiers").getJSONObject(0).getString("identifier") : "N/A";
                String authorName = volumeInfo.has("authors") ? volumeInfo.getJSONArray("authors").getString(0) : "N/A";
                String description = volumeInfo.has("description") ? volumeInfo.getString("description") : null;
                String imageUrl = volumeInfo.has("imageLinks") ? volumeInfo.getJSONObject("imageLinks").getString("thumbnail") : null;
                String bookUrl = volumeInfo.has("infoLink") ? (String) volumeInfo.get("infoLink") : null;
                Book temp = new ConcreteBook(title, authorName, isbn, description, imageUrl, bookUrl);
                // books.add(temp.getTitle() + " by " + temp.getAuthor() + " ISBN: " + temp.getIsbn());
                books.add(temp);
                Book temp2 = new ConcreteBook(0, title, authorName, isbn, true, description, imageUrl, bookUrl);
                BookDAO bookDAO = new BookDAO(connection.getConnection());
                bookDAO.addBook(temp2);
                
            }
        } else {
            System.out.println("No books found in JSON data.");
        }
        
        return books;
    }

    public ObservableList<Book> getAllBooks() throws SQLException {
        ObservableList<Book> books = FXCollections.observableArrayList();
        String query = "SELECT * FROM books";
        Statement stmt = connection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            books.add(new ReferenceBook(rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getString("isbn"), rs.getBoolean("available")));
        }
        return books;
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
