package library.service;

import java.time.LocalDate;

import library.dao.BookDAO;
import library.dao.BorrowRecordDAO;
import library.model.Book;
import library.model.BorrowRecord;
import library.model.User;

public class BorrowService {
  private BorrowRecordDAO borrowRecordDAO;
  private BookDAO bookDAO;

  public BorrowService(BorrowRecordDAO borrowRecordDAO, BookDAO bookDAO) {
    this.borrowRecordDAO = borrowRecordDAO;
    this.bookDAO = bookDAO;
  }

  public void borrowBook(User user, Book book) {
    if (book.isAvailable() > 0) {
      // Tạo bản ghi mượn sách
      BorrowRecord record =
          new BorrowRecord(0, user, book, LocalDate.now(), LocalDate.now().plusWeeks(2));
      borrowRecordDAO.addBorrowRecord(record);
      book.setAvailable(book.isAvailable() - 1); // Đánh dấu sách là không còn khả dụng
      // Cập nhật sách trong database
      bookDAO.updateBook(book); // Gọi phương thức cập nhật
    } else {
      System.out.println("Book is not available.");
    }
  }

  public void returnBook(BorrowRecord record) {
    record.setReturnDate(LocalDate.now());
    // Cập nhật trạng thái sách
    Book book = record.getBook();
    book.setAvailable(book.isAvailable() + 1); // Đánh dấu sách là khả dụng
    bookDAO.updateBook(book); // Gọi phương thức cập nhật
  }
}
