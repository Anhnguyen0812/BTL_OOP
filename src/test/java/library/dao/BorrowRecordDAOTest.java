
package library.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import library.model.Book;
import library.model.BorrowRecord;
import library.model.ConcreteBook;
import library.model.User;

public class BorrowRecordDAOTest {
    private BorrowRecordDAO borrowRecordDAO;

    @BeforeEach
    public void setUp() {
        borrowRecordDAO = BorrowRecordDAO.getBorrowRecordDAO();
    }

    @Test
    public void testAddBorrowRecord() {
        User user = new User(1, "Test User", "test@example.com", "password", "member", "salt");
        Book book = new ConcreteBook(1, "Test Book", "Author", "Publisher", "url", "ISBN", 10);
        BorrowRecord record = new BorrowRecord(1, user, book, LocalDate.now(), null, 1);
        borrowRecordDAO.addBorrowRecord(record);
        BorrowRecord retrievedRecord = borrowRecordDAO.getBorrowRecordById(1);
        assertEquals(record.getUser().getId(), retrievedRecord.getUser().getId());
    }

    @Test
    public void testDeleteBorrowRecord() {
        User user = new User(1, "Test User", "test@example.com", "password", "member", "salt");
        Book book = new ConcreteBook(1, "Test Book", "Author", "Publisher", "url", "ISBN", 10);
        BorrowRecord record = new BorrowRecord(1, user, book, LocalDate.now(), null, 1);
        borrowRecordDAO.deleteBorrowRecord(record);
        BorrowRecord retrievedRecord = borrowRecordDAO.getBorrowRecordById(1);
        assertNull(retrievedRecord);
    }
}