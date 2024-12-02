package library.model;

import java.sql.SQLException;

import library.dao.BookReviewDAO;
import library.dao.BorrowRecordDAO;

public class Member extends User {

  private final BookReviewDAO bookReviewDAO = BookReviewDAO.getBookReviewDao();
    private final BorrowRecordDAO borrowRecordDAO = BorrowRecordDAO.getBorrowRecordDAO();

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

  public void requestBorrowBook(BorrowRecord borrowRecord) {
    borrowRecordDAO.addRequestBorrowRecord(borrowRecord);
  }

  public void requestReturnBook(BorrowRecord borrowRecord) {
    borrowRecordDAO.requestReturnBook(borrowRecord);
  }
    
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
