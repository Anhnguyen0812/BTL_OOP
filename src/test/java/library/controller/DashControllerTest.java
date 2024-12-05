
package library.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import library.dao.BookDAO;
import library.dao.BorrowRecordDAO;
import library.dao.NotiDAO;
import library.model.Member;

public class DashControllerTest {

    @Mock
    private BookDAO bookDAO;

    @Mock
    private BorrowRecordDAO borrowRecordDAO;

    @Mock
    private NotiDAO notiDAO;

    @InjectMocks
    private DashController dashController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // public void testInitialize() throws SQLException {
    // Member user = new Member("testUser", "test@example.com", "password", "salt");
    // dashController.user = user;

    // dashController.initialize();

    // assertEquals("Welcome testUser!", dashController.welcome.getText());
    // }

    // public void testGotoHome() {
    // dashController.gotoHome();
    // assertTrue(dashController.home.isVisible());
    // }

    // public void testGotoBooks() {
    // dashController.gotoBooks();
    // assertTrue(dashController.books.isVisible());
    // }

    // public void testGotoReturnBooks() throws SQLException {
    // dashController.gotoReturnBooks();
    // assertTrue(dashController.returnBooks.isVisible());
    // }

    // public void testGotoIssueBooks() {
    // dashController.gotoIssueBooks();
    // assertTrue(dashController.issueBooks.isVisible());
    // }

    // public void testGotoSettings() {
    // dashController.gotoSettings();
    // assertTrue(dashController.settings.isVisible());
    // }
}