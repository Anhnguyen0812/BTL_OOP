package library.dao;

import library.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class BookDAO {
    
    private Connection connection;

    public BookDAO(Connection connection) {
        this.connection = connection;
    }

    public void updateBook(Book book) {
        // Implementation for updating the book in the database
        // This is a placeholder implementation
        System.out.println("Updating book: " + book.getTitle());
    }

    public void addBook(Book book) throws SQLException {
        String query = "INSERT INTO books (title, author, isbn, available) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, book.getTitle());
        stmt.setString(2, book.getAuthor());
        stmt.setString(3, book.getIsbn());
        stmt.setBoolean(4, book.isAvailable());
        stmt.executeUpdate();
    }

    public Book getBookById(int id) throws SQLException {
        String query = "SELECT * FROM books WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return new ReferenceBook(rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getString("isbn"), rs.getBoolean("available"));
        }
        return null;
    }

    public Book getBookByISBN(String isbn) throws SQLException {
        String query = "SELECT * FROM books WHERE isbn = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, isbn);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return new ReferenceBook(rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getString("isbn"), rs.getBoolean("available"));
        }
        return null;
    }

    public List<Book> getAllBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            books.add(new ReferenceBook(rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getString("isbn"), rs.getBoolean("available")));
        }
        return books;
    }

    public List<Book> searchBooks(String query) {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchBooks'");
    }

    public boolean borrowBook(Book book) throws SQLException {
        // Implement the logic to borrow a book
        // For example, update the book's availability in the database
        String sql = "UPDATE books SET available = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, false);
            statement.setInt(2, book.getId());
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        }
    }

    public void deleteBook(int id) throws SQLException {
        String query = "DELETE FROM books WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }

    public boolean returnBook(Book book) throws SQLException {
        // Implement the logic to return a book
        // For example, update the book's availability in the database
        String sql = "UPDATE books SET available = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, true);
            statement.setInt(2, book.getId());
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        }
    }
}

