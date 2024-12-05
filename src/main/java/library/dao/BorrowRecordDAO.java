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

public class BorrowRecordDAO implements DAO {

  /*
   * status
   * null: chua muon
   * 0: yeu cau muon
   * 1: da muon
   * 2: yeu cau tra
   * 3: da tra
   */

  private Connection connection;
  private BookDAO bookDAO = BookDAO.getBookDAO();
  private static BorrowRecordDAO instance;

  private BorrowRecordDAO () {
    connection = DBConnection.getInstance().getConnection();
  }

  public static BorrowRecordDAO getBorrowRecordDAO() {
    if (instance == null) {
      instance = new BorrowRecordDAO();
    }
    return instance;
  }

  // Thêm bản ghi mượn sách
  public void addBorrowRecord(BorrowRecord record) {
    String query = "INSERT INTO borrow_records (user_id, book_id, borrow_date, return_date) VALUES (?, ?, ?, ?)";
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

  // them request muon sach
  public void addRequestBorrowRecord(BorrowRecord record) {
    String query = "INSERT INTO borrow_records (user_id, book_id, borrow_date, return_date, status) VALUES (?, ?, ?, ?, ?)";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setInt(1, record.getUser().getId());
      stmt.setInt(2, record.getBook().getId());
      stmt.setDate(3, java.sql.Date.valueOf(record.getBorrowDate()));
      stmt.setDate(
          4, record.getReturnDate() != null ? java.sql.Date.valueOf(record.getReturnDate()) : null);
      stmt.setInt(5, 0);
      stmt.executeUpdate();
    } catch (SQLException e) {
      Logger.getLogger(BorrowRecordDAO.class.getName()).log(Level.SEVERE, null, e);
      e.printStackTrace();
    }
  }

  // chap nhan yeu cau muon sach
  public void acceptRequestBorrow(BorrowRecord record) {
    String query = "UPDATE borrow_records SET borrow_date = ?, return_date = ?, status = 1 WHERE id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
      stmt.setDate(2, java.sql.Date.valueOf(LocalDate.now().plusDays(20)));
      stmt.setInt(3, record.getId());
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // yeu cau tra sach
  public void requestReturnBook(BorrowRecord record) {
    String query = "UPDATE borrow_records SET return_date = ?, status = 2 WHERE id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
      stmt.setInt(2, record.getId());
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // chap nhan yeu cau tra sach
  public void acceptRequestReturn(BorrowRecord record) {
    String query = "UPDATE borrow_records SET return_date = ?, status = 3 WHERE id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
      stmt.setInt(2, record.getId());
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // tu choi yeu cau muon sach
  public void rejectRequestBorrow(BorrowRecord record) {
    String query = "DELETE FROM borrow_records WHERE id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setInt(1, record.getId());
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // tu choi yeu cau tra sach
  public void rejectRequestReturn(BorrowRecord record) {
    String query = "UPDATE borrow_records SET status = 1 WHERE id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setInt(1, record.getId());
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // get list book with status = 0
  public ObservableList<BorrowRecord> getBorrowRequest() {
    ObservableList<BorrowRecord> records = FXCollections.observableArrayList();
    String query = "SELECT * FROM borrow_records WHERE status = 0";
    try (PreparedStatement stmt = connection.prepareStatement(query);
        ResultSet rs = stmt.executeQuery()) {
      while (rs.next()) {
        int userId = rs.getInt("user_id");
        int bookId = rs.getInt("book_id");
        LocalDate borrowDate = rs.getDate("borrow_date").toLocalDate();
        LocalDate returnDate = rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null;
        int recordId = rs.getInt("id");
        // Giả sử bạn có lớp UserService và BookService để lấy thông tin người dùng và
        // sách
        UserService userService = new UserService();
        User user = userService.getUserById(userId); // Phương thức giả định
        Book book = bookDAO.getBookById(bookId); // Phương thức giả định
        if (user == null || book == null) {
          deleteBorrowRecordbyId(recordId);
          continue;
        }
        BorrowRecord record = new BorrowRecord(recordId, user, book, borrowDate, returnDate);
        records.add(record);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return records;
  }

  // get list book with status = 1 and user_id
  public ObservableList<BorrowRecord> getBorrowRequestByUserId(int user_id) {
    ObservableList<BorrowRecord> records = FXCollections.observableArrayList();
    String query = "SELECT * FROM borrow_records WHERE status = 1 AND user_id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setInt(1, user_id);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        int userId = rs.getInt("user_id");
        int bookId = rs.getInt("book_id");
        LocalDate borrowDate = rs.getDate("borrow_date").toLocalDate();
        LocalDate returnDate = rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null;

        UserService userService = new UserService();
        User user = userService.getUserById(userId);
        Book book = bookDAO.getBookById(bookId);

        int recordId = rs.getInt("id");
        BorrowRecord record = new BorrowRecord(recordId, user, book, borrowDate, returnDate);
        records.add(record);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return records;
  }

  // get list book with status = 1
  public ObservableList<BorrowRecord> getBorrowed() {
    ObservableList<BorrowRecord> records = FXCollections.observableArrayList();
    String query = "SELECT * FROM borrow_records WHERE status = 1";
    try (PreparedStatement stmt = connection.prepareStatement(query);
        ResultSet rs = stmt.executeQuery()) {
      while (rs.next()) {
        int userId = rs.getInt("user_id");
        int bookId = rs.getInt("book_id");
        LocalDate borrowDate = rs.getDate("borrow_date").toLocalDate();
        LocalDate returnDate = rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null;

        UserService userService = new UserService();
        User user = userService.getUserById(userId);
        Book book = bookDAO.getBookById(bookId);

        int recordId = rs.getInt("id");
        BorrowRecord record = new BorrowRecord(recordId, user, book, borrowDate, returnDate);
        records.add(record);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return records;
  }

  // get list book with status = 2
  public ObservableList<BorrowRecord> getReturnRequest() {
    ObservableList<BorrowRecord> records = FXCollections.observableArrayList();
    String query = "SELECT * FROM borrow_records WHERE status = 2";
    try (PreparedStatement stmt = connection.prepareStatement(query);
        ResultSet rs = stmt.executeQuery()) {
      while (rs.next()) {
        int userId = rs.getInt("user_id");
        int bookId = rs.getInt("book_id");
        int status = rs.getInt("status");
        LocalDate borrowDate = rs.getDate("borrow_date").toLocalDate();
        LocalDate returnDate = rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null;
        UserService userService = new UserService();
        User user = userService.getUserById(userId);
        Book book = bookDAO.getBookById(bookId);
        int recordId = rs.getInt("id");
        if (user == null || book == null) {
          deleteBorrowRecordbyId(recordId);
          continue;
        }
        BorrowRecord record = new BorrowRecord(recordId, user, book, borrowDate, returnDate, status);
        records.add(record);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return records;
  }

  // get list book with status = 3
  public ObservableList<BorrowRecord> getReturned() {
    ObservableList<BorrowRecord> records = FXCollections.observableArrayList();
    String query = "SELECT * FROM borrow_records WHERE status = 3";
    try (PreparedStatement stmt = connection.prepareStatement(query);
        ResultSet rs = stmt.executeQuery()) {
      while (rs.next()) {
        int userId = rs.getInt("user_id");
        int bookId = rs.getInt("book_id");
        LocalDate borrowDate = rs.getDate("borrow_date").toLocalDate();
        LocalDate returnDate = rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null;

        UserService userService = new UserService();
        User user = userService.getUserById(userId);
        Book book = bookDAO.getBookById(bookId);

        int recordId = rs.getInt("id");
        BorrowRecord record = new BorrowRecord(recordId, user, book, borrowDate, returnDate);
        records.add(record);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return records;
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
        UserService userService = new UserService();
        User user = userService.getUserById(userId); // Phương thức giả định
        Book book = bookDAO.getBookById(bookId); // Phương thức giả định
        if (user == null || book == null) {
          continue;
        }
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

        UserService userService = new UserService();
        User user = userService.getUserById(userId);
        Book book = bookDAO.getBookById(bookId);
        int status = rs.getInt("status");
        int recordId = rs.getInt("id");
        BorrowRecord record = new BorrowRecord(recordId, user, book, borrowDate, null, status);
        records.add(record);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return records;
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
        User user = new UserService().getUserById(userId);
        Book book = new BookService(bookDAO).getBookById(bookId);
        int status = rs.getInt("status");
        return new BorrowRecord(recordId, user, book, borrowDate, returnDate, status);
      }
    } catch (SQLException e) {
    }
    return null;
  }

  // Lấy danh sách bản ghi mượn sách theo user gần đây, tối đa 10 sách
  public ObservableList<BorrowRecord> getRecentBorrowRecordsByUserId(User user) {
    ObservableList<BorrowRecord> records = FXCollections.observableArrayList();
    String query = "SELECT * FROM borrow_records WHERE user_id = ? ORDER BY borrow_date DESC LIMIT 10";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
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
                    : null, // Return date can be null if not returned yet
                rs.getInt("status")
            ));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return records;
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
                  : null,
              rs.getInt("status") // Return date can be null if not returned yet
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
              rs.getDate("return_date").toLocalDate(),
              rs.getInt("status")
            ));
    }
    return records;
  }

  // Lấy danh sách bản ghi mượn sách khi cột returnDate Null
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

  // kiem tra da muon sach chua
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

  public boolean isBorrowedByBookId(int id) {
    String query = "SELECT * FROM borrow_records WHERE book_id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();
      return rs.next();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public boolean isUserBorrowed(int id) {
    String query = "SELECT * FROM borrow_records WHERE user_id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();
      return rs.next();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  // Lấy sách có status = 0 theo user_id
  public ObservableList<BorrowRecord> getBorrowRequestByUserId(User user) {
    ObservableList<BorrowRecord> records = FXCollections.observableArrayList();
    String query = "SELECT * FROM borrow_records WHERE user_id = ? AND status = 0";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
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
                    : null, // Return date can be null if not returned yet
                
                rs.getInt("status")
            ));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return records;
  }

  // Lấy sách có status = 2 theo user_id
  public ObservableList<BorrowRecord> getReturnRequestByUserId(User user) {
    ObservableList<BorrowRecord> records = FXCollections.observableArrayList();
    String query = "SELECT * FROM borrow_records WHERE user_id = ? AND status = 2";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
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
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return records;
  }

  // Lấy sách có status = 3 theo user_id
  public ObservableList<BorrowRecord> getReturnedByUserId(User user) {
    ObservableList<BorrowRecord> records = FXCollections.observableArrayList();
    String query = "SELECT * FROM borrow_records WHERE user_id = ? AND status = 3";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
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
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return records;
  }

  // đếm số lượng sách status = 1 theo user_id
  public int countBookBorrowed(int user_id) {
    String sql = "SELECT COUNT(*) FROM borrow_records WHERE user_id = ? AND status = 1";
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

  // đếm số lượng sách status = 0 theo user_id
  public int countBookRequest(int user_id) {
    String sql = "SELECT COUNT(*) FROM borrow_records WHERE user_id = ? AND status = 0";
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

  // đếm số lượng sách status = 2 theo user_id
  public int countBookReturnRequest(int user_id) {
    String sql = "SELECT COUNT(*) FROM borrow_records WHERE user_id = ? AND status = 2";
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

  // đếm số lượng sách status = 3 theo user_id
  public int countBookReturned(int user_id) {
    String sql = "SELECT COUNT(*) FROM borrow_records WHERE user_id = ? AND status = 3";
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

  public int countBookBorrow(int user_id) {
    String sql = "SELECT COUNT(*) FROM borrow_records WHERE user_id = ?";
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

  public void deleteBorrowRecordbyId(int id) {
    String query = "DELETE FROM borrow_records WHERE id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setInt(1, id);
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public ObservableList<BorrowRecord> getBookAlmostDueByUser(int id) {
    // Placeholder implementation
    String query = "SELECT * FROM borrow_records WHERE status = 1 AND user_id = ? AND return_date < DATE_ADD(CURDATE(), INTERVAL 3 DAY)";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();
      ObservableList<BorrowRecord> records = FXCollections.observableArrayList();
      while (rs.next()) {
        records.add(
            new BorrowRecord(
                rs.getInt("id"),
                new UserService().getUserById(rs.getInt("user_id")),
                bookDAO.getBookById(rs.getInt("book_id")),
                rs.getDate("borrow_date").toLocalDate(),
                rs.getDate("return_date").toLocalDate(),
                rs.getInt("status")));
      }
      return records;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public ObservableList<BorrowRecord> getAllBookAlmostDue() {
    // Placeholder implementation
    String query = "SELECT * FROM borrow_records WHERE status = 1 AND return_date < DATE_ADD(CURDATE(), INTERVAL 3 DAY)";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      ResultSet rs = stmt.executeQuery();
      ObservableList<BorrowRecord> records = FXCollections.observableArrayList();
      while (rs.next()) {
        records.add(
            new BorrowRecord(
                rs.getInt("id"),
                new UserService().getUserById(rs.getInt("user_id")),
                bookDAO.getBookById(rs.getInt("book_id")),
                rs.getDate("borrow_date").toLocalDate(),
                rs.getDate("return_date").toLocalDate(),
                rs.getInt("status")));
      }
      return records;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

}
