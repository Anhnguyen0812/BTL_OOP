
package library.controller;

import java.io.IOException;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import javafx.application.HostServices;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import library.dao.UserDAO;
import library.model.Book;
import library.model.ConcreteBook;
import library.model.User;
   
public class AdminController extends UserController {

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
    @FXML
    private TableColumn<?, ?> availableColumn;
    // Các trường nhập liệu cho quản lý người dùng
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField roleField;
    // Bảng hiển thị danh sách người dùng       
    @FXML
    private Button searchBookButton; // Add a button for search
    @FXML
    private TableColumn<?, ?> idColumn;
    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, String> emailColumn;   
    @FXML
    private TableColumn<User, String> roleColumn;       
    @FXML
    private TextField searchBook;  
    @FXML
    private VBox detailBook;       
    @FXML
    private TextField searchAuthor;
    @FXML
    private ListView<Book> searchResult;
    @FXML
    private Label greetingLabel;
    @FXML
    private Label dateTimeLabel;
    @FXML
    private Pane searchBookPane;
    @FXML
    private Pane manageBooksPane;
    @FXML
    private Pane manageUsersPane;
    @FXML
    private Pane home;
    @FXML
    private StackPane contentArea;
    @FXML
    private ImageView logoytb;
    private User user; 
    private final BookController bookController = new BookController();
    private HostServices hostServices;
        // @Override
        // public void showDocument(String url) {
        //     try {
        //         URI uri = new URI(url);
        //         java.awt.Desktop.getDesktop().browse(uri);
        //     } catch (Exception e) {
        //         e.printStackTrace();
        //     }
        // }
       // Danh sách Observable để lưu trữ sách và người dùng
    private final ObservableList<Book> bookList = FXCollections.observableArrayList();
    private ObservableList<User> userList = FXCollections.observableArrayList();

    public AdminController() {
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    public AdminController(User user, HostServices hostServices) {
        this.user = user;
        this.hostServices = hostServices;
    }
    public AdminController(User user) {
        this.user = user;
    }

    // Method to show the Search Book pane
    @FXML
    public void showSearchBookPane() {
        hideAllPanes();  // Hide all panes
        searchBookPane.setVisible(true);  // Show Search Book Pane
    }

    @FXML
    public void Home() {
        hideAllPanes();  // Hide all panes
        home.setVisible(true);  // Show Manage Books Pane
    }

    // Method to show the Manage Books pane
    @FXML
    public void showManageBooksPane() {
        hideAllPanes();  // Hide all panes
        manageBooksPane.setVisible(true);  // Show Manage Books Pane
    }

    // Method to show the Manage Users pane
    @FXML
    public void showManageUsersPane() {
        hideAllPanes();  // Hide all panes
        manageUsersPane.setVisible(true);  // Show Manage Users Pane
    }

    // Hide all panes
    private void hideAllPanes() {
        home.setVisible(false);
        searchBookPane.setVisible(false);
        manageBooksPane.setVisible(false);
        manageUsersPane.setVisible(false);
        logoytb.setVisible(true);
    }

    // Phương thức khởi tạo, thiết lập các cột cho bảng sách và người dùng     
    @FXML
    @Override
    public void initialize() {
        Image ytb = new Image("/image/logoytb.png");
        logoytb.setImage(ytb);
        logoytb.setOnMouseClicked(this::handleYouTubeClick);

        logoutButton.setOnAction(event -> handleLogout());
        searchBookButton.setOnAction(event -> {
            try {
                handleSearchBook();
            } catch (Exception e) {
                showAlert("Error", "An error occurred while searching for books.");
            }
        });
        greetingLabel.setText("Hello, admin " + user.getName() + "!");
        // Thiết lập ngày và giờ hiện tại
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy | EEEE, hh:mm a");
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            // Lấy thời gian hiện tại và định dạng
            String formattedDateTime = LocalDateTime.now().format(formatter);
            // Cập nhật vào label
            dateTimeLabel.setText(formattedDateTime);
        }));

        // Thiết lập lặp vô hạn
        timeline.setCycleCount(Timeline.INDEFINITE);
        // Bắt đầu timeline
        timeline.play();
        try {
            bookList.setAll(bookController.getAllBooks());
            userList.setAll(getUserList());
        } catch (SQLException e) {
            showAlert("Database Error", "Could not load books from the database.");
        }

        // Thiết lập cột cho bảng sách
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        availableColumn.setCellValueFactory(new PropertyValueFactory<>("available")); 
        bookTable.setItems(bookList);

        bookTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Kiểm tra nhấp đúp
                Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
                if (selectedBook != null) {
                    BookDetailController detailController = new BookDetailController();
                    detailController.showBookDetails(selectedBook);
                }
            }
        });
        // Thiết lập cột cho bảng người dùng
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        userTable.setItems(userList);
    }
    // Xử lý sự kiện đăng xuất
    @FXML
    private Button logoutButton; // Add a button for logout
    
    @FXML
    private void handleLogout() {
        // Thực hiện logic đăng xuất (ví dụ quay lại giao diện đăng nhập)
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/library/login.fxml"));
            Stage stage = (Stage) bookTable.getScene().getWindow();
            stage.setScene(new Scene(root, 500, 400));
            stage.show();
        } catch (IOException | RuntimeException e) {
        showAlert("Error", "Could not load login interface.");
        }
    }
    // Xử lý sự kiện tìm kiếm sách
    /**
     * @throws IOException
     * @throws SQLException
     * @throws InterruptedException
     */
    @FXML
    private void handleSearchBook() {
        String title = searchBook.getText();
        String author = searchAuthor.getText();            
        // Xóa các mục cũ trong ListView
        searchResult.getItems().clear();
        Label loadingLabel = new Label("Loading...");
        loadingLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #007bff;"); // Tùy chỉnh kiểu dáng nếu cần
        searchResult.getItems().add(new ConcreteBook(0, "Loading...", "", "", "", "", 0, "", false)); // Thêm thông báo loading vào danh sách
        // Tạo một task để tìm kiếm sách
        Task<ObservableList<Book>> task = new Task<ObservableList<Book>>() {
            @Override
            protected ObservableList<Book> call() throws Exception {
                ObservableList<Book> foundBooks = FXCollections.observableArrayList();
                    // Tìm kiếm theo tiêu đề
                    if (!title.isEmpty()) {
                        foundBooks.addAll(bookController.searchBook(title));
                    }           
                    // Tìm kiếm theo tác giả
                    if (!author.isEmpty()) {
                        foundBooks.addAll(bookController.searchBook(author));
                    }
                    Thread.sleep(500); // Giả lập việc tìm kiếm mất thời gianThrea
                return foundBooks;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                Platform.runLater(() -> {
                    searchResult.getItems().clear(); // Xóa kết quả cũ
                    searchResult.getItems().addAll(getValue()); // Thêm kết quả mới
                });
            }
            
            @Override
            protected void failed() {
                super.failed();
                // Xử lý ngoại lệ nếu có
                searchResult.setItems(FXCollections.observableArrayList()); // Hoặc cập nhật một thông báo lỗi
                }
            };
            
        // Chạy task trong một luồng riêng
            new Thread(task).start();
            
            // Xử lý sự kiện khi người dùng nhấp chuột vào kết quả tìm kiếm
            searchResult.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) { // Kiểm tra nhấp đúp
                    Book selectedBook = searchResult.getSelectionModel().getSelectedItem();
                    if (selectedBook != null) {
                        BookDetailController detailController = new BookDetailController();
                        // detailController.showBookDetails(selectedBook);
                        
                        detailBook.getChildren().clear();
                        try {
                            detailBook.getChildren().add(detailController.asParent(selectedBook));
                        } catch (IOException e) {
                            showAlert("Error", "Could not load book details.");
                        }
                    }
                }
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
       
    public static String getSalt() throws NoSuchAlgorithmException {
        // Tạo ra salt ngẫu nhiên với SecureRandom
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        // Mã hóa salt thành chuỗi base64 để dễ lưu trữ
        return Base64.getEncoder().encodeToString(salt);
    }
    // Xử lý thêm người dùng
    @FXML
    private void handleAddUser() throws NoSuchAlgorithmException, SQLException {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String role = roleField.getText();      
        // Kiểm tra đầu vào
        if (username.isEmpty() || email.isEmpty()) {
            showAlert("Input Error", "All fields must be filled.");
                return;
        }
        
        // Thêm người dùng vào danh sách
        User newUser = new User(0, username, email, password, role, getSalt());
        UserDAO userDAO = new UserDAO();
        userDAO.addUser(newUser);

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

    public ObservableList<User> getUserList() throws SQLException {
        return UserDAO.getAllUsers();
    }

    private void handleYouTubeClick(MouseEvent event) {
        try {
            String url = "https://www.youtube.com/@cristiano";     
            // Mở link bằng trình duyệt mặc định
            hostServices.showDocument(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
