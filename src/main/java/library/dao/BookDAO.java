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
import library.model.ArtBook;
import library.model.Book;
import library.model.ComputerBook;
import library.model.ConcreteBook;
import library.model.HistoryBook;
import library.model.ScienceBook;
import library.model.TechnologyBook;
import library.model.ThesisBook;
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
    String sql = "UPDATE books SET title = ?, author = ?, isbn = ?, description = ?, imageUrl = ?, QRcode = ?, categories = ?" 
      + ", available = ? WHERE id = ?";

    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setString(1, book.getTitle());
        pstmt.setString(2, book.getAuthor());
        pstmt.setString(3, book.getIsbn());
        pstmt.setString(4, book.getDescription());
        pstmt.setString(5, book.getImageUrl());
        pstmt.setString(6, book.getQRcode());
        pstmt.setString(7, book.getCategories());
        pstmt.setBoolean(8, book.isAvailable());
        pstmt.setInt(9, book.getId());
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

  public void addBook(Book book) throws SQLException {

    String query =
        "INSERT INTO books (title, author, isbn, available, description, imageUrl, QRcode, categories) VALUES ("
            + " ?, ?, ?, ?, ?, ?, ?, ?)";
    PreparedStatement stmt = connection.prepareStatement(query);
    stmt.setString(1, book.getTitle());
    stmt.setString(2, book.getAuthor());
    stmt.setString(3, book.getIsbn());
    stmt.setBoolean(4, book.isAvailable());
    stmt.setString(5, book.getDescription());
    stmt.setString(6, book.getImageUrl());
    stmt.setString(7, book.getQRcode());
    stmt.setString(8, book.getCategories());
    stmt.executeUpdate();
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
      boolean available = rs.getBoolean("available");
      String description = rs.getString("description");
      String imageUrl = rs.getString("imageUrl");
      String QRcode = rs.getString("QRcode");

      Book temp2;
        temp2 = switch (category) {
          case "Art" -> new ArtBook(id, titlee, authorName, isbn, available, description, imageUrl, QRcode);
          case "TechnologyBook" -> new TechnologyBook(id, titlee, authorName, isbn, available, description, imageUrl, QRcode);
          case "Science" -> new ScienceBook(id, titlee, authorName, isbn, available, description, imageUrl, QRcode);
          case "Computer" -> new ComputerBook(id, titlee, authorName, isbn, available, description, imageUrl, QRcode);
          case "HistoryBook" -> new HistoryBook(id, titlee, authorName, isbn, available, description, imageUrl, QRcode);
          case "EBook" -> new ConcreteBook(id, titlee, authorName, isbn, available, description, imageUrl, QRcode);
          case "Thesis" -> new ThesisBook(id, titlee, authorName, isbn, available, description, imageUrl, QRcode);
          default -> new ConcreteBook(id, titlee, authorName, isbn, available, description, imageUrl, QRcode);
        };
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
      boolean available = rs.getBoolean("available");
      String description = rs.getString("description");
      String imageUrl = rs.getString("imageUrl");
      String QRcode = rs.getString("QRcode");

      Book temp2;
        temp2 = switch (category) {
          case "Art" -> new ArtBook(id, title, authorName, isbn, available, description, imageUrl, QRcode);
          case "TechnologyBook" -> new TechnologyBook(id, title, authorName, isbn, available, description, imageUrl, QRcode);
          case "Science" -> new ScienceBook(id, title, authorName, isbn, available, description, imageUrl, QRcode);
          case "Computer" -> new ComputerBook(id, title, authorName, isbn, available, description, imageUrl, QRcode);
          case "HistoryBook" -> new HistoryBook(id, title, authorName, isbn, available, description, imageUrl, QRcode);
          case "EBook" -> new ConcreteBook(id, title, authorName, isbn, available, description, imageUrl, QRcode);
          case "Thesis" -> new ThesisBook(id, title, authorName, isbn, available, description, imageUrl, QRcode);
          default -> new ConcreteBook(id, title, authorName, isbn, available, description, imageUrl, QRcode);
        };
      return temp2;
    }
    return null;
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
      boolean available = rs.getBoolean("available");
      String description = rs.getString("description");
      String imageUrl = rs.getString("imageUrl");
      String QRcode = rs.getString("QRcode");

      Book temp2;
        temp2 = switch (category) {
          case "Art" -> new ArtBook(id, titlee, authorName, isbn, available, description, imageUrl, QRcode);
          case "TechnologyBook" -> new TechnologyBook(id, titlee, authorName, isbn, available, description, imageUrl, QRcode);
          case "Science" -> new ScienceBook(id, titlee, authorName, isbn, available, description, imageUrl, QRcode);
          case "Computer" -> new ComputerBook(id, titlee, authorName, isbn, available, description, imageUrl, QRcode);
          case "HistoryBook" -> new HistoryBook(id, titlee, authorName, isbn, available, description, imageUrl, QRcode);
          case "EBook" -> new ConcreteBook(id, titlee, authorName, isbn, available, description, imageUrl, QRcode);
          case "Thesis" -> new ThesisBook(id, titlee, authorName, isbn, available, description, imageUrl, QRcode);
          default -> new ConcreteBook(id, titlee, authorName, isbn, available, description, imageUrl, QRcode);
        };
      books.add(temp2);
    }
    return books;
  }

  public ObservableList<Book> getBookByAuthor(String author) throws SQLException {
    ObservableList<Book> books = FXCollections.observableArrayList();
    String query = "SELECT * FROM books WHERE author = ?";
    PreparedStatement stmt = connection.prepareStatement(query);
    stmt.setString(1, author);

    ResultSet rs = stmt.executeQuery();
    while (rs.next()) {
      String category = rs.getString("categories");
      int id = rs.getInt("id");
      String titleBook = rs.getString("title");
      String authorName = rs.getString("author");
      String isbn = rs.getString("isbn");
      boolean available = rs.getBoolean("available");
      String description = rs.getString("description");
      String imageUrl = rs.getString("imageUrl");
      String QRcode = rs.getString("QRcode");

      Book temp2;
        temp2 = switch (category) {
          case "Art" -> new ArtBook(id, titleBook, authorName, isbn, available, description, imageUrl, QRcode);
          case "TechnologyBook" -> new TechnologyBook(id, titleBook, authorName, isbn, available, description, imageUrl, QRcode);
          case "Science" -> new ScienceBook(id, titleBook, authorName, isbn, available, description, imageUrl, QRcode);
          case "Computer" -> new ComputerBook(id, titleBook, authorName, isbn, available, description, imageUrl, QRcode);
          case "HistoryBook" -> new HistoryBook(id, titleBook, authorName, isbn, available, description, imageUrl, QRcode);
          case "EBook" -> new ConcreteBook(id, titleBook, authorName, isbn, available, description, imageUrl, QRcode);
          case "Thesis" -> new ThesisBook(id, titleBook, authorName, isbn, available, description, imageUrl, QRcode);
          default -> new ConcreteBook(id, titleBook, authorName, isbn, available, description, imageUrl, QRcode);
        };
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
      String titlee = rs.getString("title");
      String authorName = rs.getString("author");
      String isbn = rs.getString("isbn");
      boolean available = rs.getBoolean("available");
      String description = rs.getString("description");
      String imageUrl = rs.getString("imageUrl");
      String QRcode = rs.getString("QRcode");

      Book temp2;
        temp2 = switch (category) {
          case "Art" -> new ArtBook(id, titlee, authorName, isbn, available, description, imageUrl, QRcode);
          case "TechnologyBook" -> new TechnologyBook(id, titlee, authorName, isbn, available, description, imageUrl, QRcode);
          case "Science" -> new ScienceBook(id, titlee, authorName, isbn, available, description, imageUrl, QRcode);
          case "Computer" -> new ComputerBook(id, titlee, authorName, isbn, available, description, imageUrl, QRcode);
          case "HistoryBook" -> new HistoryBook(id, titlee, authorName, isbn, available, description, imageUrl, QRcode);
          case "EBook" -> new ConcreteBook(id, titlee, authorName, isbn, available, description, imageUrl, QRcode);
          case "Thesis" -> new ThesisBook(id, titlee, authorName, isbn, available, description, imageUrl, QRcode);
          default -> new ConcreteBook(id, titlee, authorName, isbn, available, description, imageUrl, QRcode);
        };
        books.add(temp2);
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
