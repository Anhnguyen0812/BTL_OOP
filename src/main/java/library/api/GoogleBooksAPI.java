package library.api;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GoogleBooksAPI {
  private final OkHttpClient client = new OkHttpClient();
  private static final String API_KEY = "AIzaSyBO9hgSSlQiaHH_agblC5W8W_afy9rugtA"; // Sử dụng API Key của bạn

  public String searchBook(String query) throws IOException {
    String url = "https://www.googleapis.com/books/v1/volumes?q=" + query + "&maxResults=40&key=" + API_KEY;

    Request request = new Request.Builder().url(url).build();

    try (Response response = client.newCall(request).execute()) {
      return response.body().string();
    }
  }

  public String searchBookWithStartIndex(String query, int startIndex) throws IOException {
    String url = "https://www.googleapis.com/books/v1/volumes?q=" + query + "&startIndex=" + startIndex
        + "&maxResults=40&key=" + API_KEY;

    Request request = new Request.Builder().url(url).build();

    try (Response response = client.newCall(request).execute()) {
      return response.body().string();
    }
  }

  public String searchBookMaxResult(String query, int maxResult) throws IOException {
    JSONArray allBooks = new JSONArray();
    int startIndex = 0;
    int remainingResults = maxResult;

    while (remainingResults > 0) {
      int currentMaxResults = Math.min(remainingResults, 40); // Google Books API giới hạn maxResults là 40
      String url = "https://www.googleapis.com/books/v1/volumes?q=" + query + "&startIndex=" + startIndex
          + "&maxResults=" + currentMaxResults + "&key=" + API_KEY;

      Request request = new Request.Builder().url(url).build();

      try (Response response = client.newCall(request).execute()) {
        String responseBody = response.body().string();
        JSONObject jsonResponse = new JSONObject(responseBody);
        JSONArray items = jsonResponse.optJSONArray("items");
        if (items != null) {
          for (int i = 0; i < items.length(); i++) {
            allBooks.put(items.getJSONObject(i));
          }
        }
      }

      startIndex += currentMaxResults;
      remainingResults -= currentMaxResults;
    }

    JSONObject result = new JSONObject();
    result.put("items", allBooks);
    return result.toString();
  }

  public String searchBookMaxResultWithStartIndex(String query, int startIndex, int maxResult) throws IOException {
    String url = "https://www.googleapis.com/books/v1/volumes?q=" + query + "&startIndex=" + startIndex
        + "&maxResults=" + maxResult + "&key=" + API_KEY;
    Request request = new Request.Builder().url(url).build();

    try (Response response = client.newCall(request).execute()) {
      return response.body().string();
    }
  }

  public String getBookByISBN(String isbn) throws IOException {
    String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn + "&key=" + API_KEY;

    Request request = new Request.Builder().url(url).build();

    try (Response response = client.newCall(request).execute()) {
      return response.body().string();
    }
  }

}