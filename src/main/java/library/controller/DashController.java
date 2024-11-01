package library.controller;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.Pane;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.stage.Stage;
import library.dao.BookDAO;
import library.dao.BorrowRecordDAO;
import library.model.Book;
import library.model.BorrowRecord;
import library.model.ConcreteBook;
import library.model.User;

public class DashController {

    @FXML
    private TextField author, publisher, isbn, bookId, search;
    // @FXML
    // private Insets  title;

    @FXML
    private TextField title;
    
    @FXML
    private Pane Dashboard, Searchbooks, Issue, BorrowBooks;
    @FXML
    private TableColumn<Book, Integer> Id;
    @FXML
    private TableColumn<Book, String> Title;
    @FXML
    private TableColumn<Book, String> Author;
    @FXML
    private TableColumn<Book, String> ISBN;
    @FXML
    private TableColumn<?, ?> Available;

   @FXML
    protected TableColumn<BorrowRecord, Integer> Id_Book;
    @FXML
    protected TableColumn<BorrowRecord, String> Title_Book;
    @FXML
    protected TableColumn<BorrowRecord, Date> ngaymuon;
    @FXML
    protected TableColumn<BorrowRecord, Date> ngaytra;

    @FXML
    private Button searchLib, searchGG;
    @FXML
    private TableView<Book> ListBooks;
    @FXML
    private TableView<BorrowRecord> List_Borrow;
    @FXML
    private Pane return_Book, add_Book;

    protected BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAO();
    protected User user;
    protected HostServices hostServices;

    @FXML
    private Button Books, logOut;
    private BookDAO bookDAO = BookDAO.getBookDAO();
    protected final BookController bookController = new BookController();

    public DashController() {
    }

    public DashController(User user, HostServices hostServices) {
        this.user = user;
        this.hostServices = hostServices;
    }

    protected void showAlert(String title, String message) {
        // Implementation for showing an alert
        // For example, using JavaFX Alert:
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void initialize() throws SQLException {

        Dashboard.setVisible(true);
        Searchbooks.setVisible(false);
        BorrowBooks.setVisible(false);
        Issue.setVisible(false);

        // searchGG.setOnAction(e -> {
        //     handleSearchBook();
        // });

        Id.setCellValueFactory(new PropertyValueFactory<>("id"));
        Title.setCellValueFactory(new PropertyValueFactory<>("title"));
        Author.setCellValueFactory(new PropertyValueFactory<>("author"));
        ISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        Available.setCellValueFactory(new PropertyValueFactory<>("available")); 

        searchLib.setOnAction(e -> {
            String bookTitle = title.getText();
            // String bookAuthor = author.getText();
            int bookId = isbn.getText().isEmpty() ? 0 : Integer.parseInt(isbn.getText());

            // Xóa các mục cũ trong ListView
            ListBooks.getItems().clear();
            Task<ObservableList<Book>> task = new Task<ObservableList<Book>>() {
                @Override
                protected ObservableList<Book> call() throws Exception {
                    ObservableList<Book> foundBooks = FXCollections.observableArrayList();
                        // Tìm kiếm theo tiêu đề
                        if (!bookTitle.isEmpty()) {     
                            foundBooks.addAll(bookDAO.getBookByTitle(bookTitle));
                        }           
                        // Tìm kiếm theo tác giả
                        if (bookId > 0) {
                            foundBooks.addAll(bookDAO.getBookById(bookId));
                        }
                        Thread.sleep(500); // Giả lập việc tìm kiếm mất thời gianThrea
                    return foundBooks;
                }
    
                @Override
                protected void succeeded() {
                    super.succeeded();
                    Platform.runLater(() -> {
                        ListBooks.getItems().clear(); // Xóa kết quả cũ
                        ListBooks.getItems().addAll(getValue()); // Thêm kết quả mới
                    });
                }
                
                @Override
                protected void failed() {
                    super.failed();
                    // Xử lý ngoại lệ nếu có
                    ListBooks.setItems(FXCollections.observableArrayList()); // Hoặc cập nhật một thông báo lỗi
                    }
                };
                
            // Chạy task trong một luồng riêng
                new Thread(task).start();
        });
        // <TableColumn fx:id="Id" prefWidth="84.0" text="STT" />
        // Id.setCellValueFactory(new PropertyValueFactory<>("id"));
        Id_Book.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getBook().getId()));
        Title_Book.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(String.valueOf(cellData.getValue().getBook().getTitle())));
        ngaymuon.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        ngaytra.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
       
        ListBooks.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) { // Kiểm tra nhấp đúp
                    Book selectedBook = ListBooks.getSelectionModel().getSelectedItem();
                    System.out.println(selectedBook.getQRcode());
                    add_Book.getChildren().clear();
                    if (selectedBook != null) {
                        BookDetailController detailController = new BookDetailController();                        
                        try {
                            add_Book.getChildren().add(detailController.addBookDetail(selectedBook, user));
                        } catch (IOException e) {
                            showAlert("Error", "Could not load book details.");
                        }
                    }
                }
            });    
            
        List_Borrow.setItems(borrowRecordDAO.getBorrowRecordsByUserId(user));

        List_Borrow.setOnMouseClicked(event-> {
            if (event.getClickCount() == 2) { // Kiểm tra nhấp đúp
                BorrowRecord selectedBook = List_Borrow.getSelectionModel().getSelectedItem();
                BookDetailController detailController = new BookDetailController();
                return_Book.getChildren().clear();
                try {
                    return_Book.getChildren().add(detailController.returnBookDetail(selectedBook.getBook(), user));
                } catch (IOException e) {
                    showAlert("Error", "Could not load book details.");
                }
            }
        });
    }

    @FXML
    public void gotoDash() {
        Dashboard.setVisible(true);
        Searchbooks.setVisible(false);
        BorrowBooks.setVisible(false);
        Issue.setVisible(false);

    }
    @FXML
    public void gotosearchBooks() {
        Dashboard.setVisible(false);
        Searchbooks.setVisible(true);
        BorrowBooks.setVisible(false);
        Issue.setVisible(false);
    }
    @FXML
    public void gotoborrowBooks() throws SQLException {
        Dashboard.setVisible(false);
        Searchbooks.setVisible(false);
        BorrowBooks.setVisible(true);
        Issue.setVisible(false);
        List_Borrow.setItems(borrowRecordDAO.getBorrowRecordsByUserId(user));
    }
    @FXML
    public void gotoIssue() {
        Dashboard.setVisible(false);
        Searchbooks.setVisible(false);
        BorrowBooks.setVisible(false);
        Issue.setVisible(true);
    }

    protected void handleSearchBook(String bookTitle, String bookAuthor, TableView<Book> ListBook) {
        // String bookTitle = title.getText();
        // String bookAuthor = author.getText();            
        // Xóa các mục cũ trong ListView
        ListBook.getItems().clear();
        // Label loadingLabel = new Label("Loading...");
        // loadingLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #007bff;"); // Tùy chỉnh kiểu dáng nếu cần
        ListBook.getItems().add(new ConcreteBook(0, "Loading...", "", "", "", "", 0, "", false)); // Thêm thông báo loading vào danh sách
        // Tạo một task để tìm kiếm sách
        Task<ObservableList<Book>> task = new Task<ObservableList<Book>>() {
            @Override
            protected ObservableList<Book> call() throws Exception {
                ObservableList<Book> foundBooks = FXCollections.observableArrayList();
                    // Tìm kiếm theo tiêu đề
                    if (!bookTitle.isEmpty()) {
                        foundBooks.addAll(bookController.searchBook(bookTitle));
                    }           
                    // Tìm kiếm theo tác giả
                    if (!bookAuthor.isEmpty()) {
                        foundBooks.addAll(bookController.searchBook(bookAuthor));
                    }
                    Thread.sleep(500); // Giả lập việc tìm kiếm mất thời gianThrea
                return foundBooks;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                Platform.runLater(() -> {
                    ListBook.getItems().clear(); // Xóa kết quả cũ
                    ListBook.getItems().addAll(getValue()); // Thêm kết quả mới
                });
            }
            
            @Override
            protected void failed() {
                super.failed();
                // Xử lý ngoại lệ nếu có
                ListBooks.setItems(FXCollections.observableArrayList()); // Hoặc cập nhật một thông báo lỗi
                }
            };
        // Chạy task trong một luồng riêng
            new Thread(task).start();
        }

    public void logOut() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) logOut.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Library Management System");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}