
package library.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NotiDAOTest {
    private NotiDAO notiDAO;

    @BeforeEach
    public void setUp() {
        notiDAO = NotiDAO.geNotiDAO();
    }

    @Test
    public void testAddNotificationFromAdminToUser() throws SQLException {
        notiDAO.addNotificationFromAdminToUser(1, "Test message");
        List<String> notifications = notiDAO.getNotificationsFromAdminToUser(1);
        assertTrue(notifications.contains("Test message"));
    }

    @Test
    public void testDeleteNotification() throws SQLException {
        notiDAO.addNotificationFromAdminToUser(1, "Test message");
        notiDAO.deleteNoti("Test message");
        List<String> notifications = notiDAO.getNotificationsFromAdminToUser(1);
        assertFalse(notifications.contains("Test message"));
    }
}