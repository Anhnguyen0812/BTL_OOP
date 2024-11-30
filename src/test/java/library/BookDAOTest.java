package library;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import library.dao.BookDAO;
import library.model.Book;
import library.model.ConcreteBook;

public class BookDAOTest {

    private BookDAO bookDAO;

    @BeforeEach
    public void setUp() {
        bookDAO = BookDAO.getBookDAO();
    }

    @Test
    public void testAddBook() throws SQLException {
        Book book = new ConcreteBook(0, "Test Book", "Author", "1234567890123", 1, "Description", "http://example.com/image.jpg", "QRCODE");
        bookDAO.addBook(book);
        assertTrue(bookDAO.isbnExists(book.getIsbn()), "Book should exist in the database after adding");
    }

    @Test
    public void testGetBookByISBN() throws SQLException, InterruptedException {
        String isbn = "1234567890123";
        Book book = bookDAO.getBookByISBN(isbn);
        assertNotNull(book, "Book should be found by ISBN");
        assertEquals(isbn, book.getIsbn(), "ISBN should match");
    }

    @Test
    public void testUpdateBook() throws SQLException {
        Book book = new ConcreteBook(230, "Updated Book", "Updated Author", "1234567890123", 1, "Updated Description", "http://example.com/updated.jpg", "UPDATED_QRCODE");
        bookDAO.updateBook(book);
        Book updatedBook = bookDAO.getBookById(book.getId());
        assertEquals("Updated Book", updatedBook.getTitle(), "Title should be updated");
    }

    @Test
    public void testDeleteBook() throws SQLException {
        Book book = new ConcreteBook(2, "Book to Delete", "Author", "1234567890123", 1, "Description", "http://example.com/image.jpg", "QRCODE");
        bookDAO.addBook(book);
        int idToDelete = book.getId();
        bookDAO.deleteBook(idToDelete);
        assertNull(bookDAO.getBookById(idToDelete), "Book should not exist in the database after deletion");
    }

    @Test
    public void testGetAllBooks() throws SQLException {
        List<Book> books = bookDAO.getAllBooks();
        assertFalse(books.isEmpty(), "Book list should not be empty");
    }

    @Test
    public void testReturnBook() throws SQLException {
        Book book = bookDAO.getBookById(230); // Assuming a book with ID 1 exists
        int quantity = book.isAvailable();
        bookDAO.borrowBook(book); // Borrow the book first
        book = bookDAO.getBookById(230);
        boolean returned = bookDAO.returnBook(book);
        assertTrue(returned, "Book should be returned successfully");
        assertTrue(bookDAO.getBookById(book.getId()).isAvailable() == quantity, "Book should be available after returning");
    }

    @Test
    public void testGetBookByTitle() throws SQLException, IOException {
        ObservableList<Book> books = bookDAO.getBookByTitle("Test Book");
        assertFalse(books.isEmpty(), "Should return books matching the title");
    }

    @Test
    public void testGetBookByAuthor() throws SQLException {
        ObservableList<Book> books = bookDAO.getBookByAuthor("Author");
        assertFalse(books.isEmpty(), "Should return books matching the author");
    }
}
