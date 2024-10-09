package library.controller;

import library.api.GoogleBooksAPI;
import library.model.Book;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import library.model.ConcreteBook;

public class AdminController {

    // Các trường nhập liệu cho quản lý sách
    @FXML
    private TextField bookTitleField;

    @FXML
    private TextField bookAuthorField;

    @FXML
    private TextField bookISBNField;

    // Bảng hiển thị danh sách sách
    @FXML
    private TableView<Book> bookTable;

    @FXML
    private TableColumn<Book, String> titleColumn;

    @FXML
    private TableColumn<Book, String> authorColumn;

    @FXML
    private TableColumn<Book, String> isbnColumn;

    // Các trường nhập liệu cho quản lý người dùng
    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    // Bảng hiển thị danh sách người dùng
    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private TableColumn<User, String> emailColumn;

    @FXML
    private TextField searchBook;

    @FXML
    private TextField searchAuthor;

    @FXML
    private ListView<String> listView;

    @FXML
    private ListView<Book> searchResult;

    // Danh sách Observable để lưu trữ sách và người dùng
    private ObservableList<Book> bookList = FXCollections.observableArrayList();
    private ObservableList<User> userList = FXCollections.observableArrayList();

    // Phương thức khởi tạo, thiết lập các cột cho bảng sách và người dùng
    @FXML
    public void initialize() {

        // Thiết lập cột cho bảng sách
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        bookTable.setItems(bookList);

        // Thiết lập cột cho bảng người dùng
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        userTable.setItems(userList);
    }

    // Xử lý sự kiện đăng xuất
    @FXML
    private void handleLogout() {
        // Thực hiện logic đăng xuất (ví dụ quay lại giao diện đăng nhập)
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/library/login.fxml"));
            Stage stage = (Stage) bookTable.getScene().getWindow();
            stage.setScene(new Scene(root, 500, 400));
            stage.show();
        } catch (Exception e) {
            showAlert("Error", "Could not load login interface.");
        }
    }

    // Xử lý sự kiện tìm kiếm sách
    @FXML
    private void handleSearchBook() throws IOException {
        // Thực hiện tìm kiếm sách (ví dụ sử dụng Google Books API)
        // Sau khi tìm kiếm xong, hiển thị kết quả lên bảng sách
        String title = searchBook.getText();
        String author = searchAuthor.getText();
        GoogleBooksAPI apiService = new GoogleBooksAPI();
        String jsonData = "";
        if (title != null) {
            jsonData = apiService.searchBook(title);
        }

        String jsonData1 = "";
        if (author != null) {
            jsonData1 = apiService.searchBook(author);
        }
        // listView.getItems().clear();
        // listView.getItems().addAll(parseBooks(jsonData));
        // listView.getItems().addAll(parseBooks(jsonData1));
        searchResult.getItems().clear();
        searchResult.getItems().addAll(parseBooks(jsonData));
        searchResult.getItems().addAll(parseBooks(jsonData1));
        searchResult.setOnMouseClicked(event -> {
            // String selectedItem = listView.getSelectionModel().getSelectedItem();
            // if (selectedItem != null) {
            // Book selectedBook =
            // bookList.get(listView.getSelectionModel().getSelectedIndex());
            // try {
            // FXMLLoader loader = new
            // FXMLLoader(getClass().getResource("/library/BookDetail.fxml"));
            // Parent root = loader.load();
            // BookDetailController detailController = loader.getController();
            // detailController.setBookDetails(selectedBook);

            // Stage stage = new Stage();
            // stage.setScene(new Scene(root));
            // stage.setTitle("Book Details");
            // stage.show();
            // } catch (Exception e) {
            // e.printStackTrace();
            // Alert alert = new Alert(Alert.AlertType.ERROR);
            // alert.setTitle("Error");
            // alert.setHeaderText(null);
            // alert.setContentText("Failed to load book details.");
            // alert.showAndWait();
            // }
            // String[] parts = selectedItem.split(" by ");
            // if (parts.length == 2) {
            // bookTitleField.setText(parts[0]);
            // String authorAndIsbn = parts[1];
            // if (authorAndIsbn.contains("ISBN: ")) {
            // int isbnIndex = authorAndIsbn.indexOf("ISBN: ");
            // bookAuthorField.setText(authorAndIsbn.substring(0, isbnIndex).trim());
            // bookISBNField.setText(authorAndIsbn.substring(isbnIndex + 6).trim());
            // } else {
            // bookAuthorField.setText(authorAndIsbn.trim());
            // }
            // }
            // }

            Book selectedBook = searchResult.getSelectionModel().getSelectedItem();
            if (selectedBook != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/BookDetail.fxml"));
                    Parent root = loader.load();
                    BookDetailController detailController = loader.getController();
                    detailController.setBookDetails(selectedBook);

                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Book Details");
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to load book details.");
                    alert.showAndWait();
                }
            }
        });

    }

    // private ObservableList<String> parseBooks(String jsonData) {
    // ObservableList<String> books = FXCollections.observableArrayList();
    // JSONObject jsonObject = new JSONObject(jsonData);
    // System.out.println(jsonObject.toString()); // In ra JSON để kiểm tra nội dung
    // JSONArray booksArray = jsonObject.getJSONArray("items");
    // for (int i = 0; i < booksArray.length(); i++) {
    // JSONObject book = booksArray.getJSONObject(i).getJSONObject("volumeInfo");
    // String title = book.getString("title");
    // String authors = book.getJSONArray("authors").join(", ");
    // String isbn =
    // book.getJSONArray("industryIdentifiers").getJSONObject(0).getString("identifier");
    // books.add(title + " by " + authors + " ISBN: " + isbn);
    // }
    // return books;
    // }

    private ObservableList<Book> parseBooks(String jsonData) {
        // ObservableList<Book> books = FXCollections.observableArrayList();
        ObservableList<Book> books = FXCollections.observableArrayList();
        JSONObject jsonObject = new JSONObject(jsonData);

        // Kiểm tra xem có trường "items" trong JSON không
        if (jsonObject.has("items")) {
            JSONArray booksArray = jsonObject.getJSONArray("items");
            for (int i = 0; i < booksArray.length(); i++) {
                JSONObject volumeInfo = booksArray.getJSONObject(i).getJSONObject("volumeInfo");
                String title = volumeInfo.getString("title");
                String isbn = volumeInfo.has("industryIdentifiers")
                        ? volumeInfo.getJSONArray("industryIdentifiers").getJSONObject(0).getString("identifier")
                        : "N/A";
                String authorName = volumeInfo.has("authors") ? volumeInfo.getJSONArray("authors").getString(0) : "N/A";
                String description = volumeInfo.has("description") ? volumeInfo.getString("description") : null;
                String imageUrl = volumeInfo.has("imageLinks")
                        ? volumeInfo.getJSONObject("imageLinks").getString("thumbnail")
                        : null;
                Book temp = new ConcreteBook(title, authorName, isbn, description, imageUrl);
                // books.add(temp.getTitle() + " by " + temp.getAuthor() + " ISBN: " +
                // temp.getIsbn());
                books.add(temp);
            }
        } else {
            System.out.println("No books found in JSON data.");
        }

        return books;
    }

    // Xử lý thêm sách
    @FXML
    private void handleAddBook() {
        String title = bookTitleField.getText();
        String author = bookAuthorField.getText();
        String isbn = bookISBNField.getText();
        String description = "";

        // Kiểm tra đầu vào
        if (title.isEmpty() || author.isEmpty() || isbn.isEmpty()) {
            showAlert("Input Error", "All fields must be filled.");
            return;
        }

        // Thêm sách vào danh sách
        Book newBook = new ConcreteBook(title, author, isbn, description, null);
        bookList.add(newBook);

        // Xóa các trường nhập liệu sau khi thêm sách
        bookTitleField.clear();
        bookAuthorField.clear();
        bookISBNField.clear();
    }

    // Xử lý thêm người dùng
    @FXML
    private void handleAddUser() {
        String username = usernameField.getText();
        String email = emailField.getText();

        // Kiểm tra đầu vào
        if (username.isEmpty() || email.isEmpty()) {
            showAlert("Input Error", "All fields must be filled.");
            return;
        }

        // Thêm người dùng vào danh sách
        User newUser = new User(username, email);
        userList.add(newUser);

        // Xóa các trường nhập liệu sau khi thêm người dùng
        usernameField.clear();
        emailField.clear();
    }

    // Hiển thị thông báo lỗi hoặc thành công
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Lớp User đại diện cho người dùng trong bảng
    public static class User {
        private String username;
        private String email;

        public User(String username, String email) {
            this.username = username;
            this.email = email;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }
    }

}
