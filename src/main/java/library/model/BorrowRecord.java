package library.model;

import java.time.LocalDate;

/**
 * BorrowRecord class represents a record of a book borrowed by a user.
 */
public class BorrowRecord {

  private int id;
  private User user;
  private Book book;
  private LocalDate borrowDate;
  private LocalDate returnDate;
  private int status;

  public BorrowRecord(int id, User user, Book book, LocalDate borrowDate, LocalDate returnDate) {
    this.id = id;
    this.user = user;
    this.book = book;
    this.borrowDate = borrowDate;
    this.returnDate = returnDate;
  }

  public BorrowRecord(int id, User user, Book book, LocalDate borrowDate, LocalDate returnDate, int status) {
    this.id = id;
    this.user = user;
    this.book = book;
    this.borrowDate = borrowDate;
    this.returnDate = returnDate;
    this.status = status;
  }

  // Getters và Setters
  public int getId() {
    return id;
  }

  public User getUser() {
    return user;
  }

  public Book getBook() {
    return book;
  }

  public LocalDate getBorrowDate() {
    return borrowDate;
  }

  public LocalDate getReturnDate() {
    return returnDate;
  }

  public void setReturnDate(LocalDate returnDate) {
    this.returnDate = returnDate;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  /**
   * Gets the due date for returning the book.
   * 
   * @return the due date as a String
   */
  public String getDueDate() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getDueDate'");
  }
}
