package library.model;

import java.time.LocalDate;

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

  public void setId(int id) {
    this.id = id;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public void setBook(Book book) {
    this.book = book;
  }

  public void setBorrowDate(LocalDate borrowDate) {
    this.borrowDate = borrowDate;
  }

}