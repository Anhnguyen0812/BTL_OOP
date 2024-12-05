
package library.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javafx.scene.image.Image;
import library.dao.BookDAO;
import library.model.Book;
import library.model.ISBNScannerWithUI;

public class AddBookControllerTest {

    @Mock
    private BookDAO bookDAO;

    @Mock
    private ISBNScannerWithUI scanner;

    @InjectMocks
    private AddBookController addBookController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // public void testGotoFindBook() throws Exception {
    // Book book = new Book("Title", "Author", "ISBN", "Description", "ImageUrl",
    // "QRcode");
    // when(bookDAO.getBookByISBN(anyString())).thenReturn(book);

    // addBookController.isbnFind.setText("1234567890");
    // addBookController.gotoFindBook();

    // assertEquals("Title", addBookController.title2.getText());
    // }

    @Test
    public void testGotoScanBook() throws Exception {
        doNothing().when(scanner).show(any());
        doNothing().when(scanner).setOnScanCompleteListener(any());

        addBookController.gotoScanBook();

        verify(scanner, times(1)).show(any());
    }

    // public void testGotoAddBook1() throws Exception {
    // addBookController.title.setText("Title");
    // addBookController.author.setText("Author");
    // addBookController.isbn.setText("ISBN");
    // addBookController.availble.setText("10");

    // addBookController.gotoAddBook1();

    // verify(bookDAO, times(1)).addBookNoDuplicateIsbn(any(Book.class));
    // }

    // public void testGotoAddBook2() throws Exception {
    // Book book = new Book("Title", "Author", "ISBN", "Description", "ImageUrl",
    // "QRcode");
    // addBookController.book2 = book;
    // addBookController.availbleScan.setText("10");

    // addBookController.gotoAddBook2();

    // verify(bookDAO, times(1)).addBookNoDuplicateIsbn(book);
    // }

    @Test
    public void testGotoCancelScan() throws Exception {
        doNothing().when(scanner).stopCamera();

        addBookController.gotoCancelScan();

        verify(scanner, times(1)).stopCamera();
    }
}