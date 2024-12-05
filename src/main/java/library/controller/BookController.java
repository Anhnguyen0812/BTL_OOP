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
import library.model.Book;
import library.model.ConcreteBook;
import library.util.DBConnection;

public class BookController {
  private final GoogleBooksAPI googleBooksAPI;

  // private ObservableList<Book> books = FXCollections.observableArrayList();

  private final DBConnection connection = DBConnection.getInstance();

  /**
   * Constructor for BookController.
   */
  public BookController() {
    this.googleBooksAPI = new GoogleBooksAPI();
  }

  /**
   * Searches for books by title using the Google Books API.
   *
   * @param query the title of the book to search for
   * @return an ObservableList of Book objects
   * @throws IOException if an I/O error occurs
   * @throws SQLException if a database access error occurs
   */
  public ObservableList<Book> searchBookByTitle(String query) throws IOException, SQLException {
    String response = googleBooksAPI.searchBook("" + query);
    return parseBooksNoCheck(response);
  }

  /**
   * Searches for books by title with a maximum result limit using the Google Books API.
   *
   * @param query the title of the book to search for
   * @param maxResult the maximum number of results to return
   * @return an ObservableList of Book objects
   * @throws IOException if an I/O error occurs
   * @throws SQLException if a database access error occurs
   */
  public ObservableList<Book> searchBookByTitleMaxResult(String query, int maxResult) throws IOException, SQLException {
    String response = googleBooksAPI.searchBookMaxResult("" + query, maxResult);
    return parseBooksNoCheck(response);
  }

  /**
   * Searches for books by title with a start index and maximum result limit using the Google Books API.
   *
   * @param query the title of the book to search for
   * @param startIndex the index of the first result to return
   * @param maxResult the maximum number of results to return
   * @return an ObservableList of Book objects
   * @throws IOException if an I/O error occurs
   * @throws SQLException if a database access error occurs
   */
  public ObservableList<Book> searchBookByTitleWithStartIndex(String query, int startIndex, int maxResult)
      throws IOException, SQLException {
    String response = googleBooksAPI.searchBookMaxResultWithStartIndex("" + query, startIndex, maxResult);
    return parseBooksNoCheck(response);
  }

  /**
   * Searches for books by subject using the Google Books API.
   *
   * @param query the subject of the book to search for
   * @return an ObservableList of Book objects
   * @throws IOException if an I/O error occurs
   * @throws SQLException if a database access error occurs
   */
  public ObservableList<Book> searchBook(String query) throws IOException, SQLException {
    String response = googleBooksAPI.searchBook("subject:" + query);
    return parseBooks(response);
  }

  /**
   * Searches for a book by its ISBN using the Google Books API.
   *
   * @param isbn the ISBN of the book to search for
   * @return a Book object if found, otherwise null
   * @throws IOException if an I/O error occurs
   * @throws SQLException if a database access error occurs
   */
  public Book searchBookByISBN(String isbn) throws IOException, SQLException {
    String response = googleBooksAPI.getBookByISBN(isbn);
    ObservableList<Book> books = parseBooksNoCheck(response);
    return books.size() > 0 ? books.get(0) : null;
  }

  /**
   * Parses JSON data into an ObservableList of Book objects without checking for the presence of items.
   *
   * @param jsonData the JSON data to parse
   * @return an ObservableList of Book objects
   * @throws SQLException if a database access error occurs
   */
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
      String bookUrl = volumeInfo.has("previewLink")
          ? (String) volumeInfo.get("previewLink")
          : null;
      Double rateAvg = volumeInfo.has("averageRating") ? volumeInfo.getDouble("averageRating") : null;
      Book temp = new ConcreteBook(title, authorName, isbn, description, imageUrl, bookUrl);
      temp.setRateAvg(rateAvg);
      temp.setCategories(categories);
      books.add(temp);
    }

    return books;
  }

  /**
   * Parses JSON data into an ObservableList of Book objects.
   *
   * @param jsonData the JSON data to parse
   * @return an ObservableList of Book objects
   * @throws SQLException if a database access error occurs
   */
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
        String bookUrl = volumeInfo.has("previewLink")
            ? (String) volumeInfo.get("previewLink")
            : null;
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

  /**
   * Retrieves a book by its ISBN from the database.
   *
   * @param isbn the ISBN of the book to retrieve
   * @return a Book object if found, otherwise null
   * @throws SQLException if a database access error occurs
   */
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

  /**
   * Retrieves all books from the database.
   *
   * @return an ObservableList of all Book objects in the database
   * @throws SQLException if a database access error occurs
   */
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
