package library.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import library.model.Book;
import library.model.ConcreteBook;
import library.util.DBConnection;


public class BookDAO {
    
    private Connection connection;

    public BookDAO() {
        this.connection = DBConnection.getInstance().getConnection();
    }

    public BookDAO(Connection connection) {
        this.connection = connection;
    }

    public void updateBook(Book book) {
        // Implementation for updating the book in the database
        // This is a placeholder implementation

        System.out.println("Updating book: " + book.getTitle());
    }

    public boolean isbnExists(String isbn) {
        String sql = "SELECT COUNT(*) FROM books WHERE isbn = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, isbn);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public void addBook(Book book) throws SQLException {

        if (isbnExists(book.getIsbn())) {
            return;
        }
        
        String query = "INSERT INTO books (title, author, isbn, available, description, imageUrl, QRcode) VALUES ( ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, book.getTitle());
        stmt.setString(2, book.getAuthor());
        stmt.setString(3, book.getIsbn());
        stmt.setBoolean(4, book.isAvailable());
        stmt.setString(5, book.getDescription());
        stmt.setString(6, book.getImageUrl());
        stmt.setString(7, book.getQRcode());
        stmt.executeUpdate();
    }

    public Book getBookById(int id) throws SQLException {
        String query = "SELECT * FROM books WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return new ConcreteBook(rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getString("isbn"), rs.getBoolean("available"), 
            rs.getString("description"), rs.getString("imageUrl"), rs.getString("QRcode"));
        }
        return null;
    }

    public Book getBookByISBN(String isbn) throws SQLException, InterruptedException {
        String query = "SELECT * FROM books WHERE isbn = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, isbn);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return new ConcreteBook(rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getString("isbn"), rs.getBoolean("available"), 
            rs.getString("description"), rs.getString("imageUrl"), rs.getString("QRcode"));
        }
        return null;
    }

    public ObservableList<Book> getBookByTitle(String title) throws IOException, SQLException {
        ObservableList<Book> books = FXCollections.observableArrayList();
        String query = "SELECT * FROM books WHERE title LIKE ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, "%" + title + "%");
        
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            books.add(new ConcreteBook(rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getString("isbn"), rs.getBoolean("available"), 
            rs.getString("description"), rs.getString("imageUrl"), rs.getString("QRcode")));
        }
        return books;
    }

    public ObservableList<Book> getBookByAuthor(String author) throws IOException, SQLException {
        ObservableList<Book> books = FXCollections.observableArrayList();
        String query = "SELECT * FROM books WHERE author = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, author);
        
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            books.add(new ConcreteBook(rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getString("isbn"), rs.getBoolean("available"), 
            rs.getString("description"), rs.getString("imageUrl"), rs.getString("QRcode")));
        }
        return books;
    }

    public List<Book> getAllBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            books.add(new ConcreteBook(rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getString("isbn"), rs.getBoolean("available"), 
            rs.getString("description"), rs.getString("imageUrl"), rs.getString("QRcode")));
        }
        return books;
    }

    public boolean borrowBook(Book book) throws SQLException {
        // Implement the logic to borrow a book
        // For example, update the book's availability in the database
        Book dbBook = getBookById(book.getId());
        if (dbBook.isAvailable()) {
            String sql = "UPDATE books SET available = ? WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setBoolean(1, false);
                statement.setInt(2, book.getId());
                return true;
            }
        }
        return false;
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

