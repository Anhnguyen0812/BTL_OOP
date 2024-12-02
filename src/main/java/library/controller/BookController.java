package library.controller;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import library.api.GoogleBooksAPI;
import library.dao.BookDAO;
import library.model.Book;
import library.model.ConcreteBook;
import library.util.DBConnection;

public class BookController {
  private final GoogleBooksAPI googleBooksAPI;

  // private ObservableList<Book> books = FXCollections.observableArrayList();

  private final DBConnection connection = DBConnection.getInstance();

  public BookController() {
    this.googleBooksAPI = new GoogleBooksAPI();
  }

  public ObservableList<Book> searchBookByTitle(String query) throws IOException, SQLException {
    String response = googleBooksAPI.searchBook("" + query);
    return parseBooksNoCheck(response);
  }

  public ObservableList<Book> searchBookByTitleMaxResult(String query, int maxResult) throws IOException, SQLException {
    String response = googleBooksAPI.searchBookMaxResult("" + query, maxResult);
    return parseBooksNoCheck(response);
  }

  public ObservableList<Book> searchBookByTitleWithStartIndex(String query, int startIndex, int maxResult)
      throws IOException, SQLException {
    String response = googleBooksAPI.searchBookMaxResultWithStartIndex("" + query, startIndex, maxResult);
    return parseBooksNoCheck(response);
  }

  public ObservableList<Book> searchBook(String query) throws IOException, SQLException {
    String response = googleBooksAPI.searchBook("subject:" + query);
    return parseBooks(response);
  }

  public Book searchBookByISBN(String isbn) throws IOException, SQLException {
    String response = googleBooksAPI.getBookByISBN(isbn);
    ObservableList<Book> books = parseBooksNoCheck(response);
    return books.size() > 0 ? books.get(0) : null;
  }

  private ObservableList<Book> parseBooksNoCheck(String jsonData) throws SQLException {
    ObservableList<Book> books = FXCollections.observableArrayList();
    JSONObject jsonObject = new JSONObject(jsonData);
    for (int i = 0; i < jsonObject.getJSONArray("items").length(); i++) {
      JSONObject volumeInfo = jsonObject.getJSONArray("items").getJSONObject(i).getJSONObject("volumeInfo");
      String title = volumeInfo.getString("title");
      String authorName = volumeInfo.has("authors") ? volumeInfo.getJSONArray("authors").getString(0) : "N/A";
      String isbn = volumeInfo.has("industryIdentifiers")
          ? volumeInfo.getJSONArray("industryIdentifiers").getJSONObject(0).getString("identifier")
          : "N/A";
      String categories = volumeInfo.has("categories") ? volumeInfo.getJSONArray("categories").getString(0) : "N/A";
      String description = volumeInfo.has("description") ? volumeInfo.getString("description") : null;
      String imageUrl = volumeInfo.has("imageLinks") ? volumeInfo.getJSONObject("imageLinks").getString("thumbnail")
          : null;
      String bookUrl = volumeInfo.has("infoLink") ? (String) volumeInfo.get("infoLink") : null;
      Double rateAvg = volumeInfo.has("averageRating") ? volumeInfo.getDouble("averageRating") : null;
      Book temp = new ConcreteBook(title, authorName, isbn, description, imageUrl, bookUrl);
      temp.setRateAvg(rateAvg);
      temp.setCategories(categories);
      books.add(temp);
    }

    return books;
  }

  private ObservableList<Book> parseBooks(String jsonData) throws SQLException {
    // ObservableList<Book> books = FXCollections.observableArrayList();
    ObservableList<Book> books = FXCollections.observableArrayList();
    JSONObject jsonObject = new JSONObject(jsonData);

    // Kiểm tra xem có trường "items" trong JSON không
    if (jsonObject.has("items")) {
      JSONArray booksArray = jsonObject.getJSONArray("items");
      for (int i = 0; i < booksArray.length(); i++) {
        JSONObject volumeInfo = booksArray.getJSONObject(i).getJSONObject("volumeInfo");
        String title = volumeInfo.getString("title");
        String isbn = volumeInfo.has("industryIdentifiers")
            ? volumeInfo
                .getJSONArray("industryIdentifiers")
                .getJSONObject(0)
                .getString("identifier")
            : "N/A";
        String authorName = volumeInfo.has("authors") ? volumeInfo.getJSONArray("authors").getString(0) : "N/A";
        String categories = volumeInfo.has("categories")
            ? volumeInfo.getJSONArray("categories").getString(0)
            : "N/A";
        String description = volumeInfo.has("description") ? volumeInfo.getString("description") : null;
        String imageUrl = volumeInfo.has("imageLinks")
            ? volumeInfo.getJSONObject("imageLinks").getString("thumbnail")
            : null;
        String bookUrl = volumeInfo.has("infoLink") ? (String) volumeInfo.get("infoLink") : null;
        Book temp = new ConcreteBook(title, authorName, isbn, description, imageUrl, bookUrl);

        temp.setCategories(categories);

        books.add(temp);
        temp.setCategories(categories);
      }
    } else {
      System.out.println("No books found in JSON data.");
    }

    return books;
  }

  public Book getBookByISBN(String isbn) throws SQLException {
    String query = "SELECT * FROM books WHERE isbn = '" + isbn + "'";
    Statement stmt = connection.getConnection().createStatement();
    ResultSet rs = stmt.executeQuery(query);
    if (rs.next()) {
      return new ConcreteBook(
          rs.getInt("id"),
          rs.getString("title"),
          rs.getString("author"),
          rs.getString("isbn"),
          rs.getInt("available"),
          rs.getString("description"),
          rs.getString("imageUrl"),
          rs.getString("QRcode"),
          rs.getDouble("rate_avg"));
    }
    return null;
  }

  public ObservableList<Book> getAllBooks() throws SQLException {
    ObservableList<Book> books = FXCollections.observableArrayList();
    String query = "SELECT * FROM books";
    Statement stmt = connection.getConnection().createStatement();
    ResultSet rs = stmt.executeQuery(query);
    while (rs.next()) {
      books.add(
          new ConcreteBook(
              rs.getInt("id"),
              rs.getString("title"),
              rs.getString("author"),
              rs.getString("isbn"),
              rs.getInt("available"),
              rs.getString("description"),
              rs.getString("imageUrl"),
              rs.getString("QRcode"),
              rs.getDouble("rate_avg")));
    }
    return books;
  }
}