package library;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import library.dao.BookDAO;
import library.dao.BorrowRecordDAO;
import library.dao.UserDAO;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import library.model.Book;
import library.model.ConcreteBook;
import library.model.User;

public class BorrowRecordDAOTest {

    private BorrowRecordDAO borrowRecordDAO;

    @BeforeEach
    public void setUp() {
        Connection mockConnection = mock(Connection.class);
        borrowRecordDAO = new BorrowRecordDAO(mockConnection);
    }

    @Test
    public void testIsBookBorrowed() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);

        BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAO(mockConnection);
        assertTrue(borrowRecordDAO.isBookBorrowed(237));

        verify(mockConnection).prepareStatement("SELECT * FROM borrow_records WHERE book_id = ?");
        verify(mockPreparedStatement).setInt(1, 237);
        verify(mockPreparedStatement).executeQuery();
        verify(mockResultSet).next();
    }

    @Test
    public void testIsBookNotBorrowed() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAO(mockConnection);
        assertFalse(borrowRecordDAO.isBookBorrowed(237));

        verify(mockConnection).prepareStatement("SELECT * FROM borrow_records WHERE book_id = ?");
        verify(mockPreparedStatement).setInt(1, 237);
        verify(mockPreparedStatement).executeQuery();
        verify(mockResultSet).next();
    }

}
