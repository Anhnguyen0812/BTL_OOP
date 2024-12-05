package library.model;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import library.dao.BookDAO;
import library.dao.BorrowRecordDAO;
import library.dao.DAO;
import library.dao.DAOFactory;
import library.dao.UserDAO;

/**
 * Admin class represents an administrator with functionalities to manage books
 * and users.
 */
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

  /**
   * Adds a new book to the library.
   * 
   * @param book the book to add
   * @throws SQLException if a database access error occurs
   */
  public void addBook(Book book) throws SQLException {
    bookDAO.addBook(book);
  }

  /**
   * Deletes a book from the library.
   * 
   * @param book the book to delete
   * @throws SQLException if a database access error occurs
   */
  public void deleteBook(Book book) throws SQLException {
    bookDAO.deleteBook(book.getId());
  }

  /**
   * Updates the details of a book in the library.
   * 
   * @param book the book to update
   */
  public void updateBook(Book book) {
    bookDAO.updateBook(book);
  }

  /**
   * Accepts a request to borrow a book.
   * 
   * @param borrowRecord the borrow record containing book and user details
   */
  public void acceptRequestBorrow(BorrowRecord borrowRecord) {
    borrowRecordDAO.acceptRequestBorrow(borrowRecord);
    try {
      bookDAO.borrowBook(borrowRecord.getBook());
    } catch (SQLException e) {
      System.out.println("error borrow book");
    }
  }

  /**
   * Rejects a request to borrow a book.
   * 
   * @param borrowRecord the borrow record containing book and user details
   */
  public void rejectRequestBorrow(BorrowRecord borrowRecord) {
    borrowRecordDAO.acceptRequestBorrow(borrowRecord);

  }

  /**
   * Accepts a request to return a borrowed book.
   * 
   * @param borrowRecord the borrow record containing book and user details
   */
  public void acceptRequestReturn(BorrowRecord borrowRecord) {
    borrowRecordDAO.acceptRequestReturn(borrowRecord);
    try {
      bookDAO.returnBook(borrowRecord.getBook());
    } catch (SQLException e) {
      System.out.println("error return book");
    }
  }

  /**
   * Rejects a request to return a borrowed book.
   * 
   * @param borrowRecord the borrow record containing book and user details
   */
  public void rejectRequestReturn(BorrowRecord borrowRecord) {
    borrowRecordDAO.rejectRequestReturn(borrowRecord);
  }

  /**
   * Adds a new user to the library system.
   * 
   * @param user the user to add
   * @throws SQLException             if a database access error occurs
   * @throws NoSuchAlgorithmException if the specified algorithm is not available
   */
  public void addUser(User user) throws SQLException, NoSuchAlgorithmException {
    userDAO.addUser(user);
  }

  /**
   * Deletes a user from the library system.
   * 
   * @param user the user to delete
   * @throws SQLException if a database access error occurs
   */
  public void deleteUser(User user) throws SQLException {
    userDAO.deleteUser(user);
  }

  /**
   * Updates the details of a user in the library system.
   * 
   * @param user the user to update
   * @throws SQLException             if a database access error occurs
   * @throws NoSuchAlgorithmException if the specified algorithm is not available
   */
  public void updateUser(User user) throws SQLException, NoSuchAlgorithmException {
    userDAO.updateUser(user);
  }

  /**
   * Bans a user from the library system.
   * 
   * @param id the ID of the user to ban
   */
  public void banUser(int id) {
    userDAO.banUser(id);
  }
}
