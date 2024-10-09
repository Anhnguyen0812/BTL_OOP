package library.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import library.model.*;

public class BookDetailController {

    @FXML
    private Label titleLabel;

    @FXML
    private Label authorLabel;

    @FXML
    private Label isbnLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private ImageView bookImageView;

    public void setBookDetails(Book book) {
        titleLabel.setText(book.getTitle());
        authorLabel.setText(book.getAuthor());
        isbnLabel.setText(book.getIsbn());

        // Hiển thị mô tả nếu có
        if (book.getDescription() != null) {
            descriptionLabel.setText(book.getDescription());
        } else {
            descriptionLabel.setText("No description available.");
        }

        // Enable text wrapping for descriptionLabel
        descriptionLabel.setWrapText(true);

        // Hiển thị hình ảnh nếu có
        if (book.getImageUrl() != null) {
            Image image = new Image(book.getImageUrl());
            bookImageView.setImage(image);
        } else {
            bookImageView.setImage(null);
        }
    }
}
