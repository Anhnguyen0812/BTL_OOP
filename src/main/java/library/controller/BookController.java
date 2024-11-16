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
import library.model.ComputerBook;
import library.model.ConcreteBook;
import library.util.DBConnection;

public class BookController {
  private final GoogleBooksAPI googleBooksAPI;

  // private ObservableList<Book> books = FXCollections.observableArrayList();

  private final DBConnection connection = DBConnection.getInstance();

  public BookController() {
    this.googleBooksAPI = new GoogleBooksAPI();
  }

  public ObservableList<Book> searchBook(String query) throws IOException, SQLException {
    String response = googleBooksAPI.searchBook("subject:" + query);
    return parseBooks(response);
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
        String isbn =
            volumeInfo.has("industryIdentifiers")
                ? volumeInfo
                    .getJSONArray("industryIdentifiers")
                    .getJSONObject(0)
                    .getString("identifier")
                : "N/A";
        String authorName =
            volumeInfo.has("authors") ? volumeInfo.getJSONArray("authors").getString(0) : "N/A";
        String categories =
            volumeInfo.has("categories")
                ? volumeInfo.getJSONArray("categories").getString(0)
                : "N/A";
        String description =
            volumeInfo.has("description") ? volumeInfo.getString("description") : null;
        String imageUrl =
            volumeInfo.has("imageLinks")
                ? volumeInfo.getJSONObject("imageLinks").getString("thumbnail")
                : null;
        String bookUrl = volumeInfo.has("infoLink") ? (String) volumeInfo.get("infoLink") : null;
        ConcreteBook temp =
            new ConcreteBook(title, authorName, isbn, false, description, imageUrl, bookUrl);
        temp.setCategories(categories);
        Book b = (Book) temp;
        books.add(b);
        Book temp2;
        // temp2 = switch (categories) {
        //   case "Art" -> new ArtBook(0, title, authorName, isbn, true, description, imageUrl, bookUrl);
        //   case "TechnologyBook" -> new TechnologyBook(0, title, authorName, isbn, true, description, imageUrl, bookUrl);
        //   case "Science" -> new ScienceBook(0, title, authorName, isbn, true, description, imageUrl, bookUrl);
        //   case "Computer" -> new ComputerBook(0, title, authorName, isbn, true, description, imageUrl, bookUrl);
        //   case "HistoryBook" -> new HistoryBook(0, title, authorName, isbn, true, description, imageUrl, bookUrl);
        //   case "EBook" -> new ConcreteBook(0, title, authorName, isbn, true, description, imageUrl, bookUrl);
        //   case "Thesis" -> new ThesisBook(0, title, authorName, isbn, true, description, imageUrl, bookUrl);
        //   default -> new ConcreteBook(0, title, authorName, isbn, true, description, imageUrl, bookUrl);
        // };
        if (categories.equalsIgnoreCase("computer") || categories.equalsIgnoreCase("computers")) {
          temp2 = new ComputerBook(0, title, authorName, isbn, true, description, imageUrl, bookUrl);
        BookDAO bookDAO = BookDAO.getBookDAO();
        bookDAO.addBook(temp2);
        }
      }
    } else {
      System.out.println("No books found in JSON data.");
    }

    return books;
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
              rs.getBoolean("available"),
              rs.getString("description"),
              rs.getString("imageUrl"),
              rs.getString("QRcode")));
              rs.getString("categories");
    }
    return books;
  }

}
