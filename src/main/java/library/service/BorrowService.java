package library.service;

import java.sql.SQLException;
import java.time.LocalDate;
import library.dao.*;
import library.model.*;

public class BorrowService {
    private BorrowRecordDAO borrowRecordDAO;
    private BookDAO bookDAO;

    public BorrowService(BorrowRecordDAO borrowRecordDAO, BookDAO bookDAO) {
        this.borrowRecordDAO = borrowRecordDAO;
        this.bookDAO = bookDAO;
    }

    public void borrowBook(User user, Book book) {
        if (book.isAvailable()) {
            // Tạo bản ghi mượn sách
            BorrowRecord record = new BorrowRecord(0, user, book, LocalDate.now(), LocalDate.now().plusWeeks(2));
            borrowRecordDAO.addBorrowRecord(record);
            book.setAvailable(false); // Đánh dấu sách là không còn khả dụng
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
        book.setAvailable(true);
        bookDAO.updateBook(book); // Gọi phương thức cập nhật
    }
}


