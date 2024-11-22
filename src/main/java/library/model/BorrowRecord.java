package library.model;

import java.time.LocalDate;

public class BorrowRecord {

  private int id;
  private User user;
  private Book book;
  private LocalDate borrowDate;
  private LocalDate returnDate;

  public BorrowRecord(int id, User user, Book book, LocalDate borrowDate, LocalDate returnDate) {
    this.id = id;
    this.user = user;
    this.book = book;
    this.borrowDate = borrowDate;
    this.returnDate = returnDate;
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
}
