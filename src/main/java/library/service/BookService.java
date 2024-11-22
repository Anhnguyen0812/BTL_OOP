package library.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import library.dao.BookDAO;
import library.model.Book;

public class BookService {
  private BookDAO bookDAO;

  public BookService(BookDAO bookDAO) {
    this.bookDAO = bookDAO;
  }

  public Book getBookById(int id) {
    try {
      return bookDAO.getBookById(id);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public Book getBookByISBN(String isbn) throws InterruptedException {
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

  // public void updateBook(int id, String title, String author, String isbn) {
  //   try {
  //     Book book = bookDAO.getBookById(id);
  //     if (book != null) {
  //       Book updatedBook = new ConcreteBook(id, title, author, isbn, 1, "defaultCategory", true);
  //       bookDAO.updateBook(book);
  //     }
  //   } catch (SQLException e) {
  //     e.printStackTrace();
  //   }
  // }

  public void deleteBook(int id, Connection connection) throws SQLException {
    String sql = "DELETE FROM books WHERE id = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, id);
      statement.executeUpdate();
    }
  }
}
