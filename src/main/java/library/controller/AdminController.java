package library.controller;

import library.model.*;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

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
            BookController bookController = new BookController();
            searchResult.getItems().clear();
            if (title != null) {
                searchResult.getItems().addAll(bookController.searchBook(title));
            }

            if (author != null) {
                searchResult.getItems().addAll(bookController.searchBook(author));
            }
            
            searchResult.setOnMouseClicked(event -> {
                Book selectedBook = searchResult.getSelectionModel().getSelectedItem();
                BookDetailController detailController = new BookDetailController();
                detailController.showBookDetails(selectedBook);
            });
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
               Book newBook = new ConcreteBook(0, title, author, isbn, "", "", 0, description, false);
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
               User newUser = new ConcreteUser(0, username, email);
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
       
}
