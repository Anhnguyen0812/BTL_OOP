// package library;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;

// import java.io.IOException;
// import java.time.LocalDate;

// import com.google.zxing.WriterException;
// import javafx.embed.swing.JFXPanel;
// import javafx.scene.Parent;
// import javafx.scene.image.Image;
// import library.dao.BookDAO;
// import library.dao.BorrowRecordDAO;
// import library.model.Book;
// import library.model.User;
// import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;

// class BookDetailControllerTest {

//     @InjectMocks
//     private BookDetailController controller;

//     @Mock
//     private BookDAO bookDAO;

//     @Mock
//     private BorrowRecordDAO borrowRecordDAO;

//     private Book book;
//     private User user;

//     @BeforeAll
//     static void initJFX() {
//         // Initialize JavaFX environment
//         new JFXPanel();
//     }

//     @BeforeEach
//     void setUp() {
//         MockitoAnnotations.openMocks(this);

//         // Mock Book and User data
//         book = new Book();
//         book.setTitle("Sample Book");
//         book.setAuthor("John Doe");
//         book.setDescription("A sample description");
//         book.setImageUrl("http://example.com/image.jpg");
//         book.setQRcode("http://example.com/qrcode");

//         user = new User();
//         user.setName("Test User");

//         // Inject mocks
//         controller = new BookDetailController();
//     }

//     @Test
//     void testSetBookDetails() {
//         // Check if method sets labels correctly
//         controller.setBookDetails(book);

//         assertEquals("Sample Book", controller.titleLabel.getText());
//         assertEquals("John Doe", controller.authorLabel.getText());
//         assertEquals("A sample description", controller.descriptionLabel.getText());
//     }

//     @Test
//     void testGenerateQRCode() {
//         try {
//             Image qrCodeImage = controller.generateQRCode("http://example.com", 150, 150);
//             assertNotNull(qrCodeImage, "QR code image should not be null");
//         } catch (WriterException | IOException e) {
//             fail("Exception should not occur in generateQRCode method");
//         }
//     }

//     @Test
//     void testShowBookDetails() {
//         try {
//             controller.showBookDetails(book);
//             // If no exception occurs, assume the method works as expected
//             assertTrue(true);
//         } catch (Exception e) {
//             fail("Exception should not occur in showBookDetails method");
//         }
//     }

//     @Test
//     void testAddBookAction() {
//         // Test addbook button action
//         controller.addbook = new Button();
//         controller.addbook.fire();
//         verify(borrowRecordDAO, times(1)).addBorrowRecord(any());
//     }

//     @Test
//     void testReturnBookAction() {
//         // Test returnbook button action
//         controller.returnbook = new Button();
//         controller.returnbook.fire();
//         verify(borrowRecordDAO, times(1)).deleteBorrowRecord(book);
//     }

//     @Test
//     void testDeleteBookAction() {
//         // Test delete button action
//         controller.delete = new Button();
//         controller.delete.fire();
//         verify(bookDAO, times(1)).deleteBook(book.getId());
//     }

//     @Test
//     void testAsParent() {
//         try {
//             Parent parent = controller.asParent(book);
//             assertNotNull(parent, "Parent should not be null");
//         } catch (IOException e) {
//             fail("IOException should not occur in asParent method");
//         }
//     }

//     @Test
//     void testAddBookDetail() {
//         try {
//             Parent parent = controller.addBookDetail(book, user);
//             assertNotNull(parent, "Parent should not be null");
//         } catch (IOException e) {
//             fail("IOException should not occur in addBookDetail method");
//         }
//     }

//     @Test
//     void testReturnBookDetail() {
//         try {
//             Parent parent = controller.returnBookDetail(book, user);
//             assertNotNull(parent, "Parent should not be null");
//         } catch (IOException e) {
//             fail("IOException should not occur in returnBookDetail method");
//         }
//     }

//     @Test
//     void testInfoBook() {
//         try {
//             Parent parent = controller.infoBook(book, user);
//             assertNotNull(parent, "Parent should not be null");
//         } catch (IOException e) {
//             fail("IOException should not occur in infoBook method");
//         }
//     }
// }
