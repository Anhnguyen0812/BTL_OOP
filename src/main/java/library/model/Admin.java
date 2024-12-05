package library.model;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import library.dao.BookDAO;
import library.dao.BorrowRecordDAO;
import library.dao.DAO;
import library.dao.DAOFactory;
import library.dao.UserDAO;

public class Admin extends User {

  private final BookDAO bookDAO = (BookDAO) DAOFactory.getDAO(DAOFactory.DAOType.BOOK_DAO);
  private final UserDAO userDAO = (UserDAO) DAOFactory.getDAO(DAOFactory.DAOType.USER_DAO);
  private final BorrowRecordDAO borrowRecordDAO = (BorrowRecordDAO) DAOFactory
      .getDAO(DAOFactory.DAOType.BORROW_RECORD_DAO);

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
    borrowRecordDAO.acceptRequestBorrow(borrowRecord);

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

  public void updateUser(User user) throws SQLException, NoSuchAlgorithmException {
    userDAO.updateUser(user);
  }

  public void banUser(int id) {
    userDAO.banUser(id);
  }
}
