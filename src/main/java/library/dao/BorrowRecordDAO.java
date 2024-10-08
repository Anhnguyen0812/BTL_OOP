package library.dao;

// import library.model.*;

// import java.sql.*;
// import java.util.ArrayList;
// import java.util.List;
import java.time.LocalDate;
import library.model.BorrowRecord;
    import library.model.Book;
    import library.model.User;
    import library.service.*; // Add this import
    import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.util.ArrayList;
    import java.util.List;
public class BorrowRecordDAO {

        private Connection connection;
    
        public BorrowRecordDAO(Connection connection) {
            this.connection = connection;
        }
    
        // Thêm bản ghi mượn sách
        public void addBorrowRecord(BorrowRecord record) {
            String query = "INSERT INTO borrow_records (user_id, book_id, borrow_date, return_date) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, record.getUser().getId());
                stmt.setInt(2, record.getBook().getId());
                stmt.setDate(3, java.sql.Date.valueOf(record.getBorrowDate()));
                stmt.setDate(4, record.getReturnDate() != null ? java.sql.Date.valueOf(record.getReturnDate()) : null);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    
        // Lấy danh sách bản ghi mượn sách
        public List<BorrowRecord> getAllBorrowRecords() {
            List<BorrowRecord> records = new ArrayList<>();
            String query = "SELECT * FROM borrow_records";
            try (PreparedStatement stmt = connection.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int userId = rs.getInt("user_id");
                    int bookId = rs.getInt("book_id");
                    LocalDate borrowDate = rs.getDate("borrow_date").toLocalDate();
                    LocalDate returnDate = rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null;
    
                    // Giả sử bạn có lớp UserService và BookService để lấy thông tin người dùng và sách
                    UserService userService = new UserService(new UserDAO(connection));
                    BookService bookService = new BookService(new BookDAO(connection));
                    User user = userService.getUserById(userId); // Phương thức giả định
                    Book book = bookService.getBookById(bookId); // Phương thức giả định
                    
                    int recordId = rs.getInt("id");
                    BorrowRecord record = new BorrowRecord(recordId, user, book, borrowDate, returnDate);
                    records.add(record);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return records;
        }
    
        // Xóa bản ghi mượn sách
        public void deleteBorrowRecord(int recordId) {
            String query = "DELETE FROM borrow_records WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, recordId);
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
    
                    User user = new UserService(new UserDAO(connection)).getUserById(userId);
                    Book book = new BookService(new BookDAO(connection)).getBookById(bookId);
                    
                    return new BorrowRecord(recordId, user, book, borrowDate, returnDate);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    
//     private Connection connection;

//     public BorrowRecordDAO(Connection connection) {
//         this.connection = 
// import library.service.BookService;
// import library.service.UserService;

//         PreparedStatement stmt = connection.prepareStatement(query);
//         stmt.setInt(1, record.getUser().getId());
//         stmt.setInt(2, record.getBook().getId());
//         stmt.setDate(3, Date.valueOf(record.getBorrowDate()));
//         stmt.setDate(4, Date.valueOf(record.getReturnDate()));
//         stmt.executeUpdate();
//     }

//     public List<BorrowRecord> getBorrowRecordsByUserId(int userId) throws SQLException {
//         List<BorrowRecord> records = new ArrayList<>();
//         String query = "SELECT * FROM borrow_records WHERE user_id = ?";
//         PreparedStatement stmt = connection.prepareStatement(query);
//         stmt.setInt(1, userId);
//         ResultSet rs = stmt.executeQuery();
//         while (rs.next()) {
//             records.add(new BorrowRecord(
//                 rs.getInt("id"),
//                 new User(userId, " ", " "), // Placeholder for User
//                 new Book(rs.getInt("book_id"), "", "", "", true), // Placeholder for Book
//                 rs.getDate("borrow_date").toLocalDate(),
//                 rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null // Return date can be null if not returned yet
//             ));
//         }
//         return records;
//     }
// }

