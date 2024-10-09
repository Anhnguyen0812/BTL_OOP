package library.service;

import library.dao.*;
import library.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class BookService {
    private BookDAO bookDAO;

    public BookService(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    public void addBook(String title, String author, String isbn) {
        try {
            Book book = new ConcreteBook(0, title, author, isbn, "defaultPublisher", "defaultLanguage", 1, "defaultCategory", true); // Sách mới khả dụng
            bookDAO.addBook(book);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Book getBookById(int id) {
        try {
            return bookDAO.getBookById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Book getBookByISBN(String isbn) {
        try {
            return bookDAO.getBookByISBN(isbn);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Book> getAllBooks() {
        try {
            return bookDAO.getAllBooks();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
        public void updateBook(int id, String title, String author, String isbn) {
            try {
                Book book = bookDAO.getBookById(id);
                if (book != null) {
                    Book updatedBook = new ConcreteBook(id, title, author, isbn, 1, "defaultCategory", true);
                    bookDAO.updateBook(book);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        public void deleteBook(int id, Connection connection) throws SQLException {

            String sql = "DELETE FROM books WHERE id = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setInt(1, id);

                statement.executeUpdate();

            }

        }

    public List<Book> searchBooks(String query) {
        return bookDAO.searchBooks(query);
    }

    public boolean borrowBook(Book book) {
        try {
            // Implement the logic to borrow a book
            return bookDAO.borrowBook(book);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean returnBook(Book book) {
        // Implement the logic to return a book
        try {
            return bookDAO.returnBook(book);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}

