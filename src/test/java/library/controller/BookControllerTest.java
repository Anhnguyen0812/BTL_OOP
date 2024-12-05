
package library.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import library.api.GoogleBooksAPI;
import library.model.Book;

public class BookControllerTest {

    @Mock
    private GoogleBooksAPI googleBooksAPI;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSearchBookByTitle() throws IOException, SQLException {
        when(googleBooksAPI.searchBook(anyString())).thenReturn("{\"items\": []}");

        assertNotNull(bookController.searchBookByTitle("test"));
    }

    @Test
    public void testSearchBookByISBN() throws IOException, SQLException {
        when(googleBooksAPI.getBookByISBN(anyString())).thenReturn("{\"items\": []}");

        assertNull(bookController.searchBookByISBN("1234567890"));
    }

    // public void testGetBookByISBN() throws SQLException {
    // Book book = new Book("Title", "Author", "ISBN", "Description", "ImageUrl",
    // "QRcode");
    // when(bookController.getBookByISBN(anyString())).thenReturn(book);

    // assertEquals(book, bookController.getBookByISBN("1234567890"));
    // }

    @Test
    public void testGetAllBooks() throws SQLException {
        assertNotNull(bookController.getAllBooks());
    }
}