package com.exam;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GoogleSearchController {

    final int MAXRESULTS = 40;
    @FXML
    private AnchorPane AnchorPanelist;

    @FXML
    private TextField SearchBox;

    @FXML
    private Button SearchButton;

    @FXML
    private ListView<String> ListBook = new ListView<>();

    @FXML
    private ScrollPane ScrollPanelist;

    @FXML
    void initialize() {
        SearchButton.setDefaultButton(true);
        ScrollPanelist.setContent(ListBook);
        ListBook.setPrefWidth(925);
        ListBook.setPrefHeight(505);
        ListBook.setStyle("-fx-font-family: 'Calibri'; -fx-font-size: 16;");

    }

    @FXML
    public void Search() {
        String query = SearchBox.getText();
        new Thread(() -> {
            String jsonResponse = fetchBooks(query);
            if (jsonResponse != null) {
                JSONObject jsonObject = new JSONObject(jsonResponse);
                JSONArray items = jsonObject.getJSONArray("items");
                javafx.application.Platform.runLater(() -> {
                    ListBook.getItems().clear();
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        JSONObject volumeInfo = item.getJSONObject("volumeInfo");
                        String title = volumeInfo.getString("title");
                        StringBuilder titleWithAuthors = new StringBuilder();
                        titleWithAuthors.append((i + 1) + ". ").append(title);
                        if (volumeInfo.has("authors")) {
                            JSONArray authors = volumeInfo.getJSONArray("authors");
                            titleWithAuthors.append(" - ");
                            for (int j = 0; j < authors.length(); j++) {
                                titleWithAuthors.append(authors.getString(j));
                                if (j < authors.length() - 1) {
                                    titleWithAuthors.append(", ");
                                }
                            }
                        }
                        ListBook.getItems().add(titleWithAuthors.toString());

                    }
                });
            } else {
                System.out.println("No book found!");
            }
        }).start();

        ListBook.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                new Thread(() -> {
                    String selectedItem = ListBook.getSelectionModel().getSelectedItem();
                    selectedItem = selectedItem.substring(selectedItem.indexOf(".") + 2);
                    if (selectedItem != null) {
                        String jsonResponseInner = fetchBooks(SearchBox.getText());
                        if (jsonResponseInner != null) {
                            JSONObject jsonObject = new JSONObject(jsonResponseInner);
                            JSONArray itemsInner = jsonObject.getJSONArray("items");
                            for (int i = 0; i < itemsInner.length(); i++) {
                                JSONObject item = itemsInner.getJSONObject(i);
                                JSONObject volumeInfo = item.getJSONObject("volumeInfo");
                                String title = volumeInfo.getString("title");
                                if (selectedItem.startsWith(title)) {
                                    StringBuilder bookDetails = new StringBuilder();
                                    bookDetails.append("Tiêu đề: ").append(title).append("\n");
                                    if (volumeInfo.has("authors")) {
                                        JSONArray authors = volumeInfo.getJSONArray("authors");
                                        bookDetails.append("Tác giả: ");
                                        for (int j = 0; j < authors.length(); j++) {
                                            bookDetails.append(authors.getString(j));
                                            if (j < authors.length() - 1) {
                                                bookDetails.append(", ");
                                            }
                                        }
                                        bookDetails.append("\n");
                                    }
                                    if (volumeInfo.has("publishedDate")) {
                                        bookDetails.append("Ngày xuất bản: ")
                                                .append(volumeInfo.getString("publishedDate"))
                                                .append("\n");
                                    }
                                    if (volumeInfo.has("description")) {
                                        bookDetails.append("Mô tả: ").append(volumeInfo.getString("description"))
                                                .append("\n");
                                    }
                                    if (volumeInfo.has("industryIdentifiers")) {
                                        JSONArray identifiers = volumeInfo.getJSONArray("industryIdentifiers");
                                        for (int j = 0; j < identifiers.length(); j++) {
                                            JSONObject identifier = identifiers.getJSONObject(j);
                                            bookDetails.append(identifier.getString("type")).append(": ")
                                                    .append(identifier.getString("identifier")).append("\n");
                                        }
                                    }
                                    String imageUrl = volumeInfo.has("imageLinks")
                                            ? volumeInfo.getJSONObject("imageLinks").getString("thumbnail")
                                            : null;
                                    javafx.application.Platform
                                            .runLater(() -> showBookDetails(title, bookDetails.toString(), imageUrl));
                                    break;
                                }
                            }
                        }
                    }
                }).start();
            }
        });
    }

    private String fetchBooks(String query) {
        String apiKey = "AIzaSyAgG6PiSsdPilxQIy4QXtm9jfjAt76Rf5g"; // Thay thế bằng API key của bạn
        int maxResults = MAXRESULTS; // Số lượng kết quả tối đa bạn muốn nhận được
        String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=" + query + "&maxResults=" + maxResults + "&key="
                + apiKey;
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            connection.disconnect();
            return content.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showBookDetails(String title, String details, String imageUrl) {
        Stage detailStage = new Stage();
        detailStage.setTitle("Book Details");

        VBox content = new VBox(20); // Khoảng cách giữa các thành phần
        content.setAlignment(Pos.TOP_CENTER); // Căn giữa các thành phần theo chiều ngang

        if (imageUrl != null) {
            ImageView imageView = new ImageView(new Image(imageUrl));
            imageView.setFitHeight(300);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            content.getChildren().add(imageView);
        }

        Text detailsText = new Text(details);
        detailsText.setStyle("-fx-tab-size: 3;"); // Đặt kích thước tab là 4 spaces
        detailsText.setFont(Font.font("Calibri", 16));

        TextFlow textFlow = new TextFlow(detailsText);
        textFlow.setTextAlignment(TextAlignment.JUSTIFY); // Căn đều văn bản
        textFlow.setMaxWidth(600); // Đặt chiều rộng tối đa để văn bản có thể xuống dòng
        textFlow.setLineSpacing(5); // Đặt khoảng cách giữa các dòng

        content.getChildren().add(textFlow);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true); // Đặt cuộn theo chiều ngang
        scrollPane.setFitToHeight(true); // Đặt cuộn theo chiều dọc

        Scene scene = new Scene(scrollPane, 960, 720);
        detailStage.setScene(scene);
        detailStage.show();
    }
}