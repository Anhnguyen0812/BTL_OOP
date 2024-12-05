
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
import library.model.Admin;

public class Dash_AdminControllerTest {

    @Mock
    private BookDAO bookDAO;

    @Mock
    private BorrowRecordDAO borrowRecordDAO;

    @Mock
    private NotiDAO notiDAO;

    @InjectMocks
    private Dash_AdminController dashAdminController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // public void testInitialize() throws SQLException {
    // Admin user = new Admin("adminUser", "admin@example.com", "password", "salt");
    // dashAdminController.user = user;

    // dashAdminController.initialize();

    // assertEquals("Welcome adminUser!", dashAdminController.welcome.getText());
    // }

    // public void testGotoHome() {
    // dashAdminController.gotoHome();
    // assertTrue(dashAdminController.home.isVisible());
    // }

    // public void testGotoBooks() {
    // dashAdminController.gotoBooks();
    // assertTrue(dashAdminController.books.isVisible());
    // }

    // public void testGotoReturnBooks() throws SQLException {
    // dashAdminController.gotoReturnBooks();
    // assertTrue(dashAdminController.manageUsers.isVisible());
    // }

    // public void testGotoIssueBooks() {
    // dashAdminController.gotoIssueBooks();
    // assertTrue(dashAdminController.issueBooks.isVisible());
    // }

    // public void testGotoSettings() {
    // dashAdminController.gotoSettings();
    // assertTrue(dashAdminController.settings.isVisible());
    // }
}