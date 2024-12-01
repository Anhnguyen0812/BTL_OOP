package library.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import library.model.Book;
import library.model.ConcreteBook;
import library.util.DBConnection;

public class BookDAO {
  private final Connection connection;
  private static BookDAO instance;

  private BookDAO() {
    this.connection = DBConnection.getInstance().getConnection();
  }

  public static BookDAO getBookDAO() {
    if (instance == null) {
      instance = new BookDAO();
    }
    return instance;
  }

  public void updateBook(Book book) {
    // Implementation for updating the book in the database
    // This is a placeholder implementation
    String sql = "UPDATE books SET title = ?, author = ?, isbn = ?, available = ?, description = ?, imageUrl = ?, QRcode = ?, categories = ?, rate_avg = ? WHERE id = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, book.getTitle());
      statement.setString(2, book.getAuthor());
      statement.setString(3, book.getIsbn());
      statement.setBoolean(4, book.isAvailable());
      statement.setString(5, book.getDescription());
      statement.setString(6, book.getImageUrl());
      statement.setString(7, book.getQRcode());
      statement.setString(8, book.getCategories());
      statement.setDouble(9, book.getRateAvg());
      statement.setInt(10, book.getId());
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    System.out.println("Updating book: " + book.getTitle());
  }

  public boolean isbnExists(String isbn) {
    String sql = "SELECT COUNT(*) FROM books WHERE isbn = ?";
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
      pstmt.setString(1, isbn);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        return rs.getInt(1) > 0;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public boolean titleExists(String title) {
    String sql = "SELECT COUNT(*) FROM books WHERE title = ?";
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
      pstmt.setString(1, title);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        return rs.getInt(1) > 0;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public void addBook(Book book) throws SQLException {

    String query = "INSERT INTO books (title, author, isbn, available, description, imageUrl, QRcode, categories) VALUES ("
        + " ?, ?, ?, ?, ?, ?, ?, ?)";
    PreparedStatement stmt = connection.prepareStatement(query);
    if (book.getTitle() != null) {
      stmt.setString(1, book.getTitle());
    } else {
      stmt.setString(1, "N/A");
    }
    if (book.getAuthor() != null) {
      stmt.setString(2, book.getAuthor());
    } else {
      stmt.setString(2, "N/A");
    }
    if (book.getIsbn() != null) {
      stmt.setString(3, book.getIsbn());
    } else {
      stmt.setString(3, "N/A");
    }
    stmt.setBoolean(4, false); // Assuming available is always set to false when adding a new book
    if (book.getDescription() != null) {
      stmt.setString(5, book.getDescription());
    } else {
      stmt.setString(5, "N/A");
    }
    if (book.getImageUrl() != null) {
      stmt.setString(6, book.getImageUrl());
    }
    if (book.getQRcode() != null) {
      stmt.setString(7, book.getQRcode());
    }
    if (book.getCategories() != null) {
      stmt.setString(8, book.getCategories());
    } else {
      stmt.setString(8, "N/A");
    }
    stmt.executeUpdate();
  }

  // thêm sách để không bị trùng isbn
  public void addBookNoDuplicateIsbn(Book book) throws SQLException {

    if (book.getIsbn() != "N/A") {
      if (!isbnExists(book.getIsbn())) {
        addBook(book);
        System.out.println("no duplicate isbn");
        return;
      }
    } else {
      if (!titleExists(book.getTitle())) {
        addBook(book);
        System.out.println("no duplicate title");
        return;
      }
    }

    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
    alert.setTitle("Book duplicate");
    alert.setHeaderText("Book duplicate");
    alert.showAndWait();

  }

  public Book getBookById(int id) throws SQLException {
    String query = "SELECT * FROM books WHERE id = ?";
    PreparedStatement stmt = connection.prepareStatement(query);
    stmt.setInt(1, id);
    ResultSet rs = stmt.executeQuery();
    if (rs.next()) {
      String category = rs.getString("categories");
      String titlee = rs.getString("title");
      String authorName = rs.getString("author");
      String isbn = rs.getString("isbn");
      int available = rs.getInt("available");
      String description = rs.getString("description");
      String imageUrl = rs.getString("imageUrl");
      String QRcode = rs.getString("QRcode");
      Double rate_avg = rs.getObject("rate_avg") != null ? rs.getDouble("rate_avg") : null;
      Book temp2 = new ConcreteBook(id, titlee, authorName, isbn, available, description, imageUrl, QRcode);
      temp2.setCategories(category);
      temp2.setRateAvg(rate_avg);
      return temp2;
    }
    return null;
  }

  public Book getBookByISBN(String isbn) throws SQLException {
    String query = "SELECT * FROM books WHERE isbn = ?";
    PreparedStatement stmt = connection.prepareStatement(query);
    stmt.setString(1, isbn);
    ResultSet rs = stmt.executeQuery();
    if (rs.next()) {
      String category = rs.getString("categories");
      int id = rs.getInt("id");
      String title = rs.getString("title");
      String authorName = rs.getString("author");
      int available = rs.getInt("available");
      String description = rs.getString("description");
      String imageUrl = rs.getString("imageUrl");
      String QRcode = rs.getString("QRcode");
      Double rate_avg = rs.getObject("rate_avg") != null ? rs.getDouble("rate_avg") : null;
      Book temp2 = new ConcreteBook(id, title, authorName, isbn, available, description, imageUrl, QRcode);
      temp2.setCategories(category);
      temp2.setRateAvg(rate_avg);
      return temp2;
    }
    return null;
  }

  public ObservableList<Book> getListBookByISBN(String isbn) throws SQLException, InterruptedException {
    ObservableList<Book> books = FXCollections.observableArrayList();
    String query = "SELECT * FROM books WHERE isbn = ?";
    PreparedStatement stmt = connection.prepareStatement(query);
    stmt.setString(1, isbn);
    ResultSet rs = stmt.executeQuery();
    if (rs.next()) {
      String category = rs.getString("categories");
      int id = rs.getInt("id");
      String title = rs.getString("title");
      String authorName = rs.getString("author");
      int avaible = rs.getInt("available");
      String description = rs.getString("description");
      String imageUrl = rs.getString("imageUrl");
      String QRcode = rs.getString("QRcode");
      Double rate_avg = rs.getObject("rate_avg") != null ? rs.getDouble("rate_avg") : null;
      Book temp2 = new ConcreteBook(id, title, authorName, isbn, avaible, description, imageUrl, QRcode);
      temp2.setCategories(category);
      temp2.setRateAvg(rate_avg);
      books.add(temp2);
    }
    return books;
  }

  public boolean haveBook(String isbn) throws SQLException {
    String query = "SELECT * FROM books WHERE isbn = ?";
    PreparedStatement stmt = connection.prepareStatement(query);
    stmt.setString(1, isbn);
    ResultSet rs = stmt.executeQuery();
    return rs.next();
  }

  public ObservableList<Book> getBookByTitle(String title) throws SQLException {
    title = title.trim();
    ObservableList<Book> books = FXCollections.observableArrayList();
    String query = "SELECT * FROM books WHERE title LIKE ?";
    PreparedStatement stmt = connection.prepareStatement(query);
    stmt.setString(1, "%" + title + "%");

    ResultSet rs = stmt.executeQuery();
    while (rs.next()) {
      String category = rs.getString("categories");
      int id = rs.getInt("id");
      String titlee = rs.getString("title");
      String authorName = rs.getString("author");
      String isbn = rs.getString("isbn");
      int available = rs.getInt("available");
      String description = rs.getString("description");
      String imageUrl = rs.getString("imageUrl");
      String QRcode = rs.getString("QRcode");
      Double rate_avg = rs.getObject("rate_avg") != null ? rs.getDouble("rate_avg") : null;
      Book temp2 = new ConcreteBook(id, titlee, authorName, isbn, available, description, imageUrl, QRcode);
      temp2.setCategories(category);
      temp2.setRateAvg(rate_avg);
      books.add(temp2);
    }
    return books;
  }

  public Book getOneBookByTitle(String title) throws SQLException {
    title = title.trim();
    String query = "SELECT * FROM books WHERE title LIKE ?";
    PreparedStatement stmt = connection.prepareStatement(query);
    stmt.setString(1, "%" + title + "%");

    ResultSet rs = stmt.executeQuery();
    if (rs.next()) {
      String category = rs.getString("categories");
      int id = rs.getInt("id");
      String titlee = rs.getString("title");
      String authorName = rs.getString("author");
      String isbn = rs.getString("isbn");
      int available = rs.getInt("available");
      String description = rs.getString("description");
      String imageUrl = rs.getString("imageUrl");
      String QRcode = rs.getString("QRcode");
      Double rate_avg = rs.getObject("rate_avg") != null ? rs.getDouble("rate_avg") : null;
      Book temp2 = new ConcreteBook(id, titlee, authorName, isbn, available, description, imageUrl, QRcode);
      temp2.setCategories(category);
      temp2.setRateAvg(rate_avg);
      return temp2;

    }
    return null;
  }

  public ObservableList<Book> getBookByAuthor(String author) throws SQLException {
    author = author.trim();
    ObservableList<Book> books = FXCollections.observableArrayList();
    String query = "SELECT * FROM books WHERE author LIKE ?";
    PreparedStatement stmt = connection.prepareStatement(query);
    stmt.setString(1, "%" + author + "%");

    ResultSet rs = stmt.executeQuery();
    while (rs.next()) {
      String category = rs.getString("categories");
      int id = rs.getInt("id");
      String title = rs.getString("title");
      String authorName = rs.getString("author");
      String isbn = rs.getString("isbn");
      int available = rs.getInt("available");
      String description = rs.getString("description");
      String imageUrl = rs.getString("imageUrl");
      String QRcode = rs.getString("QRcode");

      Book temp2 = new ConcreteBook(id, title, authorName, isbn, available, description, imageUrl, QRcode);
      temp2.setCategories(category);
      books.add(temp2);
    }
    return books;
  }

  public List<Book> getAllBooks() throws SQLException {
    List<Book> books = new ArrayList<>();
    String query = "SELECT * FROM books";
    Statement stmt = connection.createStatement();
    ResultSet rs = stmt.executeQuery(query);
    while (rs.next()) {
      String category = rs.getString("categories");
      int id = rs.getInt("id");
      String title = rs.getString("title");
      String authorName = rs.getString("author");
      String isbn = rs.getString("isbn");
      int available = rs.getInt("available");
      String description = rs.getString("description");
      String imageUrl = rs.getString("imageUrl");
      String QRcode = rs.getString("QRcode");
      Double rate_avg = rs.getObject("rate_avg") != null ? rs.getDouble("rate_avg") : null;
      Book temp2 = new ConcreteBook(id, title, authorName, isbn, available, description, imageUrl, QRcode, rate_avg);
      temp2.setCategories(category);
      books.add(temp2);
    }
    return books;
  }

  public List<Book> getAllBooks1() throws SQLException {
    List<Book> books = new ArrayList<>();
    String query = "SELECT * FROM books";
    Statement stmt = connection.createStatement();
    ResultSet rs = stmt.executeQuery(query);
    while (rs.next()) {
      String category = rs.getString("categories");
      int id = rs.getInt("id");
      String titlee = rs.getString("title");
      String authorName = rs.getString("author");
      String isbn = rs.getString("isbn");
      int available = rs.getInt("available");
      String description = rs.getString("description");
      String imageUrl = rs.getString("imageUrl");
      String QRcode = rs.getString("QRcode");
      Double rate_avg = rs.getObject("rate_avg") != null ? rs.getDouble("rate_avg") : null;
      Book temp2 = new ConcreteBook(id, titlee, authorName, isbn, available, description, imageUrl, QRcode, rate_avg);
      temp2.setCategories(category);
      temp2.setRateAvg(rate_avg);

      books.add(temp2);
    }
    return books;
  }

  // lấy sách có thứ tự đánh giá cao nhất
  public List<Book> getTopBooks() throws SQLException {
    List<Book> books = new ArrayList<>();
    String query = "SELECT * FROM books ORDER BY rate_avg DESC";
    Statement stmt = connection.createStatement();
    ResultSet rs = stmt.executeQuery(query);
    while (rs.next()) {
      String categories = rs.getString("categories");
      int id = rs.getInt("id");
      String title = rs.getString("title");
      String authorName = rs.getString("author");
      String isbn = rs.getString("isbn");
      int available = rs.getInt("available");
      String description = rs.getString("description");
      String imageUrl = rs.getString("imageUrl");
      String bookUrl = rs.getString("QRcode");
      Double rate_avg = rs.getObject("rate_avg") != null ? rs.getDouble("rate_avg") : null;
      Book temp2 = new ConcreteBook(id, title, authorName, isbn, available, description, imageUrl, bookUrl, rate_avg);
      temp2.setCategories(categories);
      books.add(temp2);
      if (books.size() == 10) {
        break;
      }
    }
    return books;
  }

  // lấy danh sách sách mới thêm vào
  public List<Book> getNewBooks() throws SQLException {
    List<Book> books = new ArrayList<>();
    String query = "SELECT * FROM books ORDER BY id DESC";
    Statement stmt = connection.createStatement();
    ResultSet rs = stmt.executeQuery(query);
    while (rs.next()) {
      String categories = rs.getString("categories");
      int id = rs.getInt("id");
      String title = rs.getString("title");
      String authorName = rs.getString("author");
      String isbn = rs.getString("isbn");
      int available = rs.getInt("available");
      String description = rs.getString("description");
      String imageUrl = rs.getString("imageUrl");
      String bookUrl = rs.getString("QRcode");
      Double rate_avg = rs.getObject("rate_avg") != null ? rs.getDouble("rate_avg") : null;
      Book temp2 = new ConcreteBook(id, title, authorName, isbn, available, description, imageUrl, bookUrl, rate_avg);
      temp2.setCategories(categories);
      books.add(temp2);
      if (books.size() == 10) {
        break;
      }
    }
    return books;
  }

  public boolean borrowBook(Book book) throws SQLException {
    // Implement the logic to borrow a book
    // For example, update the book's availability in the database
    System.out.println("Borrowing book: " + book);
    if (book.isAvailable()) {
      String sql = "UPDATE books SET available = ? WHERE id = ?";
      try (PreparedStatement statement = connection.prepareStatement(sql)) {
        statement.setBoolean(1, false);
        statement.setInt(2, book.getId());
        statement.executeUpdate();
        return true;
      }
    }
    return false;
  }

  public void deleteBook(int id) throws SQLException {
    String query = "DELETE FROM books WHERE id = ?";
    PreparedStatement stmt = connection.prepareStatement(query);
    stmt.setInt(1, id);
    stmt.executeUpdate();

    // String resetAutoIncrementQuery = "ALTER TABLE books AUTO_INCREMENT = 1";
    // Statement resetStmt = connection.createStatement();
    // resetStmt.execute(resetAutoIncrementQuery);
  }

  public boolean returnBook(Book book) throws SQLException {
    // Implement the logic to return a book
    // For example, update the book's availability in the database
    System.out.println("Returning book: " + book);
    String sql = "UPDATE books SET available = ? WHERE id = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setBoolean(1, true);
      statement.setInt(2, book.getId());
      int rowsUpdated = statement.executeUpdate();
      return rowsUpdated > 0;
    }
  }
}
