package library.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import library.dao.UserDAO;
import library.model.Book;
import library.model.BorrowRecord;
import library.model.ConcreteBook;
import library.model.User;
   
public class AdminController extends DashController {

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
    private TableColumn<Book, String> availableColumn;
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
    private Pane detailBook;       
    @FXML
    private TextField searchAuthor;
    @FXML
    private TableView<Book> searchResult;
    @FXML
    private TableColumn<Book, String> Title;
    @FXML
    private TableColumn<Book, String> Author;
    @FXML
    private TableColumn<Book, String> ISBN;
    @FXML
    private TableColumn<?, ?> Categories;

    @FXML
    private TableView<BorrowRecord> borrowRecord;
    @FXML
    protected TableColumn<BorrowRecord, Integer> Id_User;
    @FXML
    protected TableColumn<BorrowRecord, String> Username;

    @FXML
    private Label greetingLabel;
    @FXML
    private Label dateTimeLabel;
    @FXML
    private Pane searchBookPane;
    @FXML
    private Pane manageBooksPane;
    @FXML
    private Pane manageUsersPane, ManagerBorrowBook, infoBook, infoBorrow;
    @FXML
    private Pane home;
    @FXML
    private StackPane contentArea;
    @FXML
    private ImageView logoytb, logofb;
    // private User user; 
    // private HostServices hostServices;
    private final BookController bookController = new BookController();
    private final ObservableList<Book> bookList = FXCollections.observableArrayList();
    private ObservableList<User> userList = FXCollections.observableArrayList();
    
    public AdminController(User user, HostServices hostServices) {
        super(user, hostServices); // Call the UserController constructor with user and hostServices
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
    public void showManageBooksPane() throws SQLException {
        hideAllPanes();  // Hide all panes
        bookTable.setItems(bookController.getAllBooks());
        manageBooksPane.setVisible(true);  // Show Manage Books Pane
    }

    // Method to show the Manage Users pane
    @FXML
    public void showManageUsersPane() throws SQLException {
        hideAllPanes();  // Hide all panes
        userTable.setItems(getUserList());
        manageUsersPane.setVisible(true);  // Show Manage Users Pane
    }

    @FXML
    public void showManagerBorrowBook() {
        hideAllPanes();  // Hide all panes
        borrowRecord.setItems(borrowRecordDAO.getAllBorrowRecords());
        ManagerBorrowBook.setVisible(true);  // Show Manage Users Pane
    }

    // Hide all panes
    private void hideAllPanes() {
        home.setVisible(false);
        searchBookPane.setVisible(false);
        manageBooksPane.setVisible(false);
        manageUsersPane.setVisible(false);
        logoytb.setVisible(true);
        logofb.setVisible(true);
        ManagerBorrowBook.setVisible(false);
    }

    // Phương thức khởi tạo, thiết lập các cột cho bảng sách và người dùng     
    @FXML
    @Override
    public void initialize() {
        Image ytb = new Image("/imgs/logoytb.png");
        Image fb = new Image("/imgs/logofb.png");
        logoytb.setImage(ytb);
        logoytb.setOnMouseClicked(this::handleYouTubeClick);

        logofb.setImage(fb);
        logofb.setOnMouseClicked(this::handleFaceBookClick);

        logoutButton.setOnAction(event -> logOut());
        searchBookButton.setOnAction(event -> {
            try {
                handleSearchBook(searchBook.getText(),searchAuthor.getText(), searchResult);
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
        
        searchResult.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Kiểm tra nhấp đúp
                Book selectedBook = searchResult.getSelectionModel().getSelectedItem();
                if (selectedBook != null) {
                    detailBook.getChildren().clear();
                    
                    // Sử dụng CompletableFuture để tải dữ liệu trong một luồng nền
                    CompletableFuture.runAsync(() -> {
                        BookDetailController detailController = new BookDetailController();
                        try {
                            Parent bookDetailParent = detailController.asParent(selectedBook);

                            // Cập nhật giao diện trong luồng JavaFX
                            Platform.runLater(() -> {
                                detailBook.getChildren().add(bookDetailParent);
                            });
                        } catch (IOException e) {
                            Platform.runLater(() -> showAlert("Error", "Could not load book details."));
                        }
                    });
                }
            }
        });

        borrowRecord.setOnMouseClicked(event -> {
            // if (event.getClickCount() == 2) { // Kiểm tra nhấp đúp
            //     BorrowRecord selectedBook = borrowRecord.getSelectionModel().getSelectedItem();
            //     if (selectedBook != null) {
            //         infoBook.getChildren().clear();
                    
            //         // Sử dụng CompletableFuture để tải dữ liệu trong một luồng nền
            //         CompletableFuture.runAsync(() -> {
            //             BookDetailController detailController = new BookDetailController();
            //             try {
            //                 // Parent bookDetailParent = detailController.asParent(selectedBook);

            //                 // Cập nhật giao diện trong luồng JavaFX
            //                 Platform.runLater(() -> {
            //                     // infoBook.getChildren().add(bookDetailParent);
            //                 });
            //             } catch (IOException e) {
            //                 Platform.runLater(() -> showAlert("Error", "Could not load book details."));
            //             }
            //         });
            //     }
            // }
        });

        bookTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Kiểm tra nhấp đúp
                Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
                if (selectedBook != null) {
                    infoBook.getChildren().clear();
                    
                    // Sử dụng CompletableFuture để tải dữ liệu trong một luồng nền
                    CompletableFuture.runAsync(() -> {
                        BookDetailController detailController = new BookDetailController();
                        try {
                            Parent bookDetailParent = detailController.infoBook(selectedBook, user);

                            // Cập nhật giao diện trong luồng JavaFX
                            Platform.runLater(() -> {
                                infoBook.getChildren().add(bookDetailParent);
                            });
                        } catch (IOException e) {
                            Platform.runLater(() -> showAlert("Error", "Could not load book details."));
                        }
                    });
                }
            }
        });

        // Thiết lập cột cho bảng sách
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        availableColumn.setCellValueFactory(new PropertyValueFactory<>("available")); 
        bookTable.setItems(bookList);

        // bookTable.setOnMouseClicked(event -> {
        //     if (event.getClickCount() == 2) { // Kiểm tra nhấp đúp
        //         Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        //         if (selectedBook != null) {
        //             BookDetailController detailController = new BookDetailController();
        //             detailController.showBookDetails(selectedBook);
        //         }
        //     }
        // });
        // Thiết lập cột cho bảng người dùng
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        userTable.setItems(userList);

        Title.setCellValueFactory(new PropertyValueFactory<>("title"));
        Author.setCellValueFactory(new PropertyValueFactory<>("author"));
        ISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        Categories.setCellValueFactory(new PropertyValueFactory<>("categories"));

        // Bang thong tin sach muon
        Id_Book.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getBook().getId()));
        Title_Book.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getBook().getTitle())));
        Id_User.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getUser().getId()));
        Username.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getUser().getName())));
        ngaymuon.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        ngaytra.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        borrowRecord.setItems(borrowRecordDAO.getAllBorrowRecords());
    }
    // Xử lý sự kiện đăng xuất
    @FXML
    private Button logoutButton; // Add a button for logout

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
        UserDAO userDAO = UserDAO.getUserDAO();
        userDAO.addUser(newUser);

        userList.add(newUser);
        // Xóa các trường nhập liệu sau khi thêm người dùng
        usernameField.clear();
        emailField.clear();
        passwordField.clear();
        roleField.clear();
        }
       
    // Hiển thị thông báo lỗi hoặc thành công

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

    private void handleFaceBookClick(MouseEvent event) {
        try {
            String url = "https://www.instagram.com/cristiano/";     
            // Mở link bằng trình duyệt mặc định
            hostServices.showDocument(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
