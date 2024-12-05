
package library.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AllDaoTest {
    private AllDao allDao;

    @BeforeEach
    public void setUp() {
        allDao = new AllDao();
    }

    @Test
    public void testGetTotalBooks() {
        int totalBooks = allDao.getTotalBooks();
        assertTrue(totalBooks >= 0);
    }

    @Test
    public void testGetTotalUsers() {
        int totalUsers = allDao.getTotalUsers();
        assertTrue(totalUsers >= 0);
    }

    @Test
    public void testGetBooksBorrowedOnDate() {
        int booksBorrowed = allDao.getBooksBorrowedOnDate(LocalDate.now());
        assertTrue(booksBorrowed >= 0);
    }
}