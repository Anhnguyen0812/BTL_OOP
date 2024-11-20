package library.api;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GoogleBooksAPI {
  private final OkHttpClient client = new OkHttpClient();
  private static final String API_KEY =
      "AIzaSyBO9hgSSlQiaHH_agblC5W8W_afy9rugtA"; // Sử dụng API Key của bạn

  public String searchBook(String query) throws IOException {
    String url = "https://www.googleapis.com/books/v1/volumes?q=" + query + "&maxResults=20&key=" + API_KEY;

    Request request = new Request.Builder().url(url).build();

    try (Response response = client.newCall(request).execute()) {
      return response.body().string();
    }
  }
}
