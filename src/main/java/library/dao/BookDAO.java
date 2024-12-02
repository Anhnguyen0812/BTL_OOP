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
import javafx.util.Pair;
import library.model.Book;
import library.model.ConcreteBook;
import library.util.DBConnection;

public class BookDAO implements DAO{
  private final Connection connection;
  private static BookDAO instance;
  private final BookReviewDAO bookReviewDAO = BookReviewDAO.getBookReviewDao();

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

    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
      pstmt.setString(1, book.getTitle());
      pstmt.setString(2, book.getAuthor());
      pstmt.setString(3, book.getIsbn());
      pstmt.setInt(4, book.isAvailable());
      pstmt.setString(5, book.getDescription());
      pstmt.setString(6, book.getImageUrl());
      pstmt.setString(7, book.getQRcode());
      pstmt.setString(8, book.getCategories());
      pstmt.setDouble(9, book.isAvailable());
      pstmt.setInt(10, book.getId());
      int cnt = pstmt.executeUpdate();
      System.out.println(cnt + " records affected");
    } catch (SQLException e) {
        e.printStackTrace();
    }
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

    String query = "INSERT INTO books (title, author, isbn, available, description, imageUrl, QRcode, categories, rate_avg) VALUES ("
        + " ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    PreparedStatement stmt = connection.prepareStatement(query);
    stmt.setString(1, book.getTitle());
    stmt.setString(2, book.getAuthor());
    stmt.setString(3, book.getIsbn());
    stmt.setInt(4, book.isAvailable());
    stmt.setString(5, book.getDescription());
    stmt.setString(6, book.getImageUrl());
    stmt.setString(7, book.getQRcode());
    stmt.setString(8, book.getCategories());
    stmt.setDouble(9, book.getRateAvg() != null ? book.getRateAvg() : 0.0);
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
      Book temp2;
      temp2 = new ConcreteBook(id, titlee, authorName, isbn, available, description, imageUrl, QRcode);
      temp2.setCategories(category);
      Pair<String, Double>  getCommentBook = bookReviewDAO.getReviewBook(id);
      if (getCommentBook == null || getCommentBook.getKey() == null || getCommentBook.getValue() == null) {
        temp2.setComment("Not yet comment");
      } else {
        temp2.setComment(getCommentBook.getKey());
        temp2.setRateAvg(getCommentBook.getValue());
      }
      return temp2;
    }
    return null;
  }

  public Book getBookByISBN(String isbn) throws SQLException, InterruptedException {
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
      Book temp2;
      temp2 = new ConcreteBook(id, title, authorName, isbn, available, description, imageUrl, QRcode);
      temp2.setCategories(category);
      Pair<String, Double>  getCommentBook = bookReviewDAO.getReviewBook(id);
      if (getCommentBook == null || getCommentBook.getKey() == null || getCommentBook.getValue() == null) {
        temp2.setComment("Not yet comment");
      } else {
        temp2.setComment(getCommentBook.getKey());
        temp2.setRateAvg(getCommentBook.getValue());
      }
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
      int available = rs.getInt("available");
      String description = rs.getString("description");
      String imageUrl = rs.getString("imageUrl");
      String QRcode = rs.getString("QRcode");
      Book temp2;
      temp2 = new ConcreteBook(id, title, authorName, isbn, available, description, imageUrl, QRcode);
      temp2.setCategories(category);
      Pair<String, Double>  getCommentBook = bookReviewDAO.getReviewBook(id);
      if (getCommentBook == null || getCommentBook.getKey() == null || getCommentBook.getValue() == null) {
        temp2.setComment("Not yet comment");
      } else {
        temp2.setComment(getCommentBook.getKey());
        temp2.setRateAvg(getCommentBook.getValue());
      }
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
      Book temp2;
      temp2 =  new ConcreteBook(id, titlee, authorName, isbn, available, description, imageUrl, QRcode);
      temp2.setCategories(category);
      Pair<String, Double>  getCommentBook = bookReviewDAO.getReviewBook(id);
      if (getCommentBook == null || getCommentBook.getKey() == null || getCommentBook.getValue() == null) {
        temp2.setComment("Not yet comment");
      } else {
        temp2.setComment(getCommentBook.getKey());
        temp2.setRateAvg(getCommentBook.getValue());
      }
      books.add(temp2);
    }
    return books;
  }

  public ObservableList<Book> getBookByAuthor(String author) throws SQLException {
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
      Pair<String, Double>  getCommentBook = bookReviewDAO.getReviewBook(id);
      if (getCommentBook == null || getCommentBook.getKey() == null || getCommentBook.getValue() == null) {
        temp2.setComment("Not yet comment");
      } else {
        temp2.setComment(getCommentBook.getKey());
        temp2.setRateAvg(getCommentBook.getValue());
      }
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
      Pair<String, Double>  getCommentBook = bookReviewDAO.getReviewBook(id);
      if (getCommentBook == null || getCommentBook.getKey() == null || getCommentBook.getValue() == null) {
        temp2.setComment("Not yet comment");
      } else {
        temp2.setComment(getCommentBook.getKey());
        temp2.setRateAvg(getCommentBook.getValue());
      }
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
      Book temp2 = new ConcreteBook(id, titlee, authorName, isbn, available, description, imageUrl, QRcode);
      temp2.setCategories(category);
      Pair<String, Double>  getCommentBook = bookReviewDAO.getReviewBook(id);
      if (getCommentBook == null || getCommentBook.getKey() == null || getCommentBook.getValue() == null) {
        temp2.setComment("Not yet comment");
      } else {
        temp2.setComment(getCommentBook.getKey());
        temp2.setRateAvg(getCommentBook.getValue());
      }
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
      Pair<String, Double>  getCommentBook = bookReviewDAO.getReviewBook(id);
      if (getCommentBook == null || getCommentBook.getKey() == null || getCommentBook.getValue() == null) {
        temp2.setComment("Not yet comment");
      } else {
        temp2.setComment(getCommentBook.getKey());
        temp2.setRateAvg(getCommentBook.getValue());
      }
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
      Pair<String, Double>  getCommentBook = bookReviewDAO.getReviewBook(id);
      if (getCommentBook == null || getCommentBook.getKey() == null || getCommentBook.getValue() == null) {
        temp2.setComment("Not yet comment");
      } else {
        temp2.setComment(getCommentBook.getKey());
        temp2.setRateAvg(getCommentBook.getValue());
      }
      books.add(temp2);
      if (books.size() == 10) {
        break;
      }
    }
    return books;
  }

  public void borrowBook(Book book) throws SQLException {
    // Implement the logic to borrow a book
    // For example, update the book's availability in the database
    System.out.println("Borrowing book: " + book);
    if (book.isAvailable() > 0) {
      String sql = "UPDATE books SET available = ? WHERE id = ?";
      try (PreparedStatement statement = connection.prepareStatement(sql)) {
        statement.setInt(1, book.isAvailable() - 1);
        statement.setInt(2, book.getId());
        statement.executeUpdate();
      }
    }
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
      statement.setInt(1, book.isAvailable() + 1);
      statement.setInt(2, book.getId());
      int rowsUpdated = statement.executeUpdate();
      return rowsUpdated > 0;
    }
  }
}
