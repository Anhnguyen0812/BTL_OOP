
package library.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BookTest {

    @Test
    public void testBookCreation() {
        Book book = new ConcreteBook(1, "Test Book", "Author", "1234567890123", 1, "Description", "imageUrl", "QRcode",
                4.5);

        assertEquals(1, book.getId());
        assertEquals("Test Book", book.getTitle());
        assertEquals("Author", book.getAuthor());
        assertEquals("1234567890123", book.getIsbn());
        assertEquals(1, book.getAvailable());
        assertEquals("Description", book.getDescription());
        assertEquals("imageUrl", book.getImageUrl());
        assertEquals("QRcode", book.getQRcode());
        assertEquals(4.5, book.getRateAvg());
    }
}