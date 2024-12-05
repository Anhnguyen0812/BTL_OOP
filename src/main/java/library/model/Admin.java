package library.model;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import library.dao.BookDAO;
import library.dao.BorrowRecordDAO;
import library.dao.UserDAO;

public class Admin extends User {

  private final BookDAO bookDAO = BookDAO.getBookDAO();
  private final UserDAO userDAO = UserDAO.getUserDAO();
  private final BorrowRecordDAO borrowRecordDAO = BorrowRecordDAO.getBorrowRecordDAO();

  public Admin(int id, String name, String email) {
    super(id, name, email);
  }

  public Admin(String name, String email, String password, String role, String salt) {
    super(name, email, password, role, salt);
  }

  public Admin(int id, String name, String email, String role) {
    super(id, name, email, role);
  }

  public Admin(int id, String name, String email, String password, String role, String salt) {
    super(id, name, email, password, role, salt);
  }

  public Admin(String name, String email, String password, String salt) {
    super(name, email, password, "admin", salt);
  }

  public void addBook(Book book) throws SQLException {
    bookDAO.addBook(book);
  }

  public void deleteBook(Book book) throws SQLException {
    bookDAO.deleteBook(book.getId());
  }

  public void updateBook(Book book) {
    bookDAO.updateBook(book);
  }

  public void acceptRequestBorrow(BorrowRecord borrowRecord) {
    borrowRecordDAO.acceptRequestBorrow(borrowRecord);
    try {
      bookDAO.borrowBook(borrowRecord.getBook());
    } catch (SQLException e) {
      System.out.println("error borrow book"); 
    }
  }

  public void rejectRequestBorrow(BorrowRecord borrowRecord) {
    borrowRecordDAO.rejectRequestBorrow(borrowRecord);
  
  }

  public void acceptRequestReturn(BorrowRecord borrowRecord) {
    borrowRecordDAO.acceptRequestReturn(borrowRecord);
    try {
      bookDAO.returnBook(borrowRecord.getBook());
    } catch (SQLException e) {
      System.out.println("error return book");
    }
  }

  public void rejectRequestReturn(BorrowRecord borrowRecord) {
    borrowRecordDAO.rejectRequestReturn(borrowRecord);
  }

  public void addUser(User user) throws SQLException, NoSuchAlgorithmException {
    userDAO.addUser(user);
  }

  public void deleteUser(User user) throws SQLException {
    userDAO.deleteUser(user);
  }

  public void editUser(User user) throws SQLException {
    userDAO.updateUser(user);
  }
}
