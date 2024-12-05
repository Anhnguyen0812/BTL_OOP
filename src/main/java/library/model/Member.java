package library.model;

import java.sql.SQLException;

import library.dao.BookReviewDAO;
import library.dao.BorrowRecordDAO;
import library.dao.DAOFactory;

/**
 * Member class represents a library member with functionalities to request book
 * borrow and return, and assess books.
 */
public class Member extends User {

  private final BookReviewDAO bookReviewDAO = (BookReviewDAO) DAOFactory.getDAO(DAOFactory.DAOType.BOOK_REVIEW_DAO);
  private final BorrowRecordDAO borrowRecordDAO = (BorrowRecordDAO) DAOFactory
      .getDAO(DAOFactory.DAOType.BORROW_RECORD_DAO);

  public Member(int id, String name, String email) {
    super(id, name, email);
  }

  public Member(String name, String email, String password, String role, String salt) {
    super(name, email, password, role, salt);
  }

  public Member(int id, String name, String email, String role) {
    super(id, name, email, role);
  }

  public Member(int id, String name, String email, String password, String role, String salt) {
    super(id, name, email, password, role, salt);
  }

  public Member(String name, String email, String password, String salt) {
    super(name, email, password, "member", salt);
  }

  /**
   * Requests to borrow a book.
   * 
   * @param borrowRecord the borrow record containing book and user details
   */
  public void requestBorrowBook(BorrowRecord borrowRecord) {
    borrowRecordDAO.addRequestBorrowRecord(borrowRecord);
  }

  /**
   * Requests to return a borrowed book.
   * 
   * @param borrowRecord the borrow record containing book and user details
   */
  public void requestReturnBook(BorrowRecord borrowRecord) {
    borrowRecordDAO.requestReturnBook(borrowRecord);
  }

  /**
   * Assesses a book by adding or updating a review.
   * 
   * @param user   the user assessing the book
   * @param book   the book being assessed
   * @param assess the assessment text
   * @param count  the rating count
   */
  public void assessBook(User user, Book book, String assess, int count) {
    try {
      boolean check = bookReviewDAO.getReviewBookByUser(user.getId(), book.getId());
      if (check == false) {
        bookReviewDAO.addReview(book.getId(), user.getId(), assess, count);
      } else {
        bookReviewDAO.updateReview(user.getId(), book.getId(), assess, count);
      }
    } catch (SQLException e) {
      System.out.println("Error, Failed to handle book review.");
    }
  }
}
