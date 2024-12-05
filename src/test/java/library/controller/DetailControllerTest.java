
package library.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javafx.collections.FXCollections;
import javafx.scene.image.Image;
import library.dao.BookReviewDAO;
import library.model.Book;

public class DetailControllerTest {

    @Mock
    private BookReviewDAO bookReviewDAO;

    @InjectMocks
    private DetailController detailController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // public void testInitComment() throws IOException {
    // when(bookReviewDAO.getAllCommentBook(anyInt())).thenReturn(FXCollections.observableArrayList("Great
    // book!", "Loved it!"));

    // detailController.initComment();

    // assertEquals(2, detailController.v2.getChildren().size());
    // }

    @Test
    public void testGenerateQRCode() throws Exception {
        Image qrCode = detailController.generateQRCode("test", 125, 125);
        assertNotNull(qrCode);
    }

    // public void testSetBookData() throws IOException {
    // Book book = new Book("Title", "Author", "ISBN", "Description", "ImageUrl",
    // "QRcode");
    // detailController.setBookData(book, null);

    // assertEquals("Title", detailController.titleLabel.getText());
    // }
}