package library.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate; // Add this import
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import library.model.Book;
import library.model.BorrowRecord;
import library.model.User;
import library.service.BookService;
import library.service.UserService;
import library.util.DBConnection;

public class BorrowRecordDAO {

  private Connection connection;
  private BookDAO bookDAO = BookDAO.getBookDAO();

  public BorrowRecordDAO() {
    this.connection = DBConnection.getInstance().getConnection();
  }

  public BorrowRecordDAO(Connection connection) {
    this.connection = connection;
  }

  // Thêm bản ghi mượn sách
  public void addBorrowRecord(BorrowRecord record) {
    String query = "INSERT INTO borrow_records (user_id, book_id, borrow_date, return_date) VALUES (?, ?, ?,"
        + " ?)";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setInt(1, record.getUser().getId());
      stmt.setInt(2, record.getBook().getId());
      stmt.setDate(3, java.sql.Date.valueOf(record.getBorrowDate()));
      stmt.setDate(
          4, record.getReturnDate() != null ? java.sql.Date.valueOf(record.getReturnDate()) : null);
      stmt.executeUpdate();
      bookDAO.borrowBook(record.getBook());
    } catch (SQLException e) {
      Logger.getLogger(BorrowRecordDAO.class.getName()).log(Level.SEVERE, null, e);
      e.printStackTrace();
    }
  }

  // Lấy danh sách bản ghi mượn sách
  public ObservableList<BorrowRecord> getAllBorrowRecords() {
    ObservableList<BorrowRecord> records = FXCollections.observableArrayList();
    String query = "SELECT * FROM borrow_records";
    try (PreparedStatement stmt = connection.prepareStatement(query);
        ResultSet rs = stmt.executeQuery()) {
      while (rs.next()) {
        int userId = rs.getInt("user_id");
        int bookId = rs.getInt("book_id");
        LocalDate borrowDate = rs.getDate("borrow_date").toLocalDate();
        LocalDate returnDate = rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null;

        // Giả sử bạn có lớp UserService và BookService để lấy thông tin người dùng và
        // sách
        UserService userService = new UserService(UserDAO.getUserDAO());
        User user = userService.getUserById(userId); // Phương thức giả định
        Book book = bookDAO.getBookById(bookId); // Phương thức giả định

        int recordId = rs.getInt("id");
        BorrowRecord record = new BorrowRecord(recordId, user, book, borrowDate, returnDate);
        records.add(record);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return records;
  }

  // Lấy danh sách bản ghi mượn sách khi cột returnDate Null
  public ObservableList<BorrowRecord> getAllBorrowRecordsWithNullReturnDate() {
    ObservableList<BorrowRecord> records = FXCollections.observableArrayList();
    String query = "SELECT * FROM borrow_records WHERE return_date IS NULL";
    try (PreparedStatement stmt = connection.prepareStatement(query);
        ResultSet rs = stmt.executeQuery()) {
      while (rs.next()) {
        int userId = rs.getInt("user_id");
        int bookId = rs.getInt("book_id");
        LocalDate borrowDate = rs.getDate("borrow_date").toLocalDate();

        UserService userService = new UserService(UserDAO.getUserDAO());
        User user = userService.getUserById(userId);
        Book book = bookDAO.getBookById(bookId);

        int recordId = rs.getInt("id");
        BorrowRecord record = new BorrowRecord(recordId, user, book, borrowDate, null);
        records.add(record);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return records;
  }

  // tra sach
  public void returnBook(BorrowRecord record) {
    String query = "UPDATE borrow_records SET return_date = ? WHERE id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
      stmt.setInt(2, record.getId());
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // Xóa bản ghi mượn sách
  public void deleteBorrowRecord(Book book) {
    String query = "DELETE FROM borrow_records WHERE book_id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setInt(1, book.getId());
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // Tìm bản ghi mượn theo ID
  public BorrowRecord getBorrowRecordById(int recordId) {
    String query = "SELECT * FROM borrow_records WHERE id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setInt(1, recordId);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        int userId = rs.getInt("user_id");
        int bookId = rs.getInt("book_id");
        LocalDate borrowDate = rs.getDate("borrow_date").toLocalDate();
        LocalDate returnDate = rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null;
        User user = new UserService(UserDAO.getUserDAO()).getUserById(userId);
        Book book = new BookService(bookDAO).getBookById(bookId);
        return new BorrowRecord(recordId, user, book, borrowDate, returnDate);
      }
    } catch (SQLException e) {
    }
    return null;
  }

  public ObservableList<BorrowRecord> getBorrowRecordsByUserId(User user) throws SQLException {

    ObservableList<BorrowRecord> records = FXCollections.observableArrayList();
    String query = "SELECT * FROM borrow_records WHERE user_id = ?";
    PreparedStatement stmt = connection.prepareStatement(query);
    stmt.setInt(1, user.getId());
    ResultSet rs = stmt.executeQuery();
    while (rs.next()) {
      records.add(
          new BorrowRecord(
              rs.getInt("id"),
              user,
              bookDAO.getBookById(rs.getInt("book_id")),
              rs.getDate("borrow_date").toLocalDate(),
              rs.getDate("return_date") != null
                  ? rs.getDate("return_date").toLocalDate()
                  : null // Return date can be null if not returned yet
          ));
    }
    return records;
  }

  public ObservableList<BorrowRecord> getReturnRecordsByUserId(User user) throws SQLException {
    ObservableList<BorrowRecord> records = FXCollections.observableArrayList();
    String query = "SELECT * FROM borrow_records WHERE user_id = ? AND return_date IS NOT NULL";
    PreparedStatement stmt = connection.prepareStatement(query);
    stmt.setInt(1, user.getId());
    ResultSet rs = stmt.executeQuery();
    while (rs.next()) {
      records.add(
          new BorrowRecord(
              rs.getInt("id"),
              user,
              bookDAO.getBookById(rs.getInt("book_id")),
              rs.getDate("borrow_date").toLocalDate(),
              rs.getDate("return_date").toLocalDate()));
    }
    return records;
  }

  public ObservableList<BorrowRecord> getBorrowRecordsByUserIdWithoutReturnDate(User user) throws SQLException {
    ObservableList<BorrowRecord> records = FXCollections.observableArrayList();
    String query = "SELECT * FROM borrow_records WHERE user_id = ? AND return_date IS NULL";
    PreparedStatement stmt = connection.prepareStatement(query);
    stmt.setInt(1, user.getId());
    ResultSet rs = stmt.executeQuery();
    while (rs.next()) {
      records.add(
          new BorrowRecord(
              rs.getInt("id"),
              user,
              bookDAO.getBookById(rs.getInt("book_id")),
              rs.getDate("borrow_date").toLocalDate(),
              null // Return date can be null if not returned yet
          ));
    }
    return records;
  }

  public boolean isBorrowed(User user, Book book) {
    String query = "SELECT * FROM borrow_records WHERE user_id = ? AND book_id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setInt(1, user.getId());
      stmt.setInt(2, book.getId());
      ResultSet rs = stmt.executeQuery();
      return rs.next();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public int countBookBorrow(int user_id) {
    String sql = "SELECT COUNT(*) FROM books WHERE user_id = ?";
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
      pstmt.setInt(1, user_id);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        return rs.getInt(1);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return 0;
  }
}
