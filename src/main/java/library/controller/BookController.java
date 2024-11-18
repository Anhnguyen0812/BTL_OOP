package library.controller;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import library.api.GoogleBooksAPI;
import library.dao.BookDAO;
import library.model.*;
import library.util.DBConnection;

public class BookController {
  private final GoogleBooksAPI googleBooksAPI;
  private BookDAO bookDAO = BookDAO.getBookDAO();
  public static List<Book> list = new ArrayList<>();
  // private ObservableList<Book> books = FXCollections.observableArrayList();

  private final DBConnection connection = DBConnection.getInstance();

  public BookController() {
    this.googleBooksAPI = new GoogleBooksAPI();
  }

  public ObservableList<Book> searchBook(String query) throws IOException, SQLException {
    String response = googleBooksAPI.searchBook(query);
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
            new ConcreteBook(-1, title, authorName, isbn, false, description, imageUrl, bookUrl);
        temp.setCategories(categories);
        Book b = (Book) temp;
        books.add(b);
        Book temp2 = null;
        categories = categories.toLowerCase();
        if (categories.contains("art")) {
          temp2 = new ArtBook(-1, title, authorName, isbn, true, description, imageUrl, bookUrl);

        } else if (categories.contains("thesis")) {
          temp2 = new ThesisBook(-1, title, authorName, isbn, true, description, imageUrl, bookUrl);
        } else if (categories.contains("technology")) {
          temp2 = new TechnologyBook(-1, title, authorName, isbn, true, description, imageUrl, bookUrl);
        } else if (categories.contains("science")) {
          temp2 = new ScienceBook(-1, title, authorName, isbn, true, description, imageUrl, bookUrl);
        } else if (categories.contains("computer")) {
          temp2 = new ComputerBook(-1, title, authorName, isbn, true, description, imageUrl, bookUrl);
        } else if (categories.contains("history")) {
          temp2 = new HistoryBook(-1, title, authorName, isbn, true, description, imageUrl, bookUrl);
        } else if (categories.contains("EBook")) {
          temp2 = new ConcreteBook(-1, title, authorName, isbn, true, description, imageUrl, bookUrl);
        }
    if (temp2 != null) {
      try {
        bookDAO.addBook(temp2);  
      } catch (SQLException e) {
        System.err.println("Error while adding book to database: " + e.getMessage());
        e.printStackTrace();
      }
    }
      }
    } else {
      System.out.println("No books found in JSON data.");
    }

    return books;
  }

}
