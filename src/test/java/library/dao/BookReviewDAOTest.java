
package library.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BookReviewDAOTest {
    private BookReviewDAO bookReviewDAO;

    @BeforeEach
    public void setUp() {
        bookReviewDAO = BookReviewDAO.getBookReviewDao();
    }

    @Test
    public void testAddReview() throws SQLException {
        bookReviewDAO.addReview(1, 1, "Great book!", 5);
        boolean reviewExists = bookReviewDAO.getReviewBookByUser(1, 1);
        assertTrue(reviewExists);
    }

    @Test
    public void testDeleteReview() throws SQLException {
        bookReviewDAO.addReview(1, 1, "Great book!", 5);
        bookReviewDAO.deleteReview(1, 1);
        boolean reviewExists = bookReviewDAO.getReviewBookByUser(1, 1);
        assertFalse(reviewExists);
    }
}