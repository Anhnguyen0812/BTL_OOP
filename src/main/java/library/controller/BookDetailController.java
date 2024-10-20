package library.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import library.model.Book;

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

    @FXML
    private ImageView qrCodeImageView;  // ImageView để hiển thị mã QR


    public Image generateQRCode(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        // Chuyển BitMatrix thành hình ảnh
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(pngOutputStream.toByteArray());
        
        return new Image(inputStream);  // Trả về hình ảnh QR dưới dạng Image
    }

    public void setBookDetails(Book book) {
        titleLabel.setText(book.getTitle());
        authorLabel.setText(book.getAuthor());
        // isbnLabel.setText(book.getIsbn());

        // Hiển thị mô tả nếu có
        if (book.getDescription() != null) {
            descriptionLabel.setText(book.getDescription());
        } else {
            descriptionLabel.setText("No description available.");
        }

        // Hiển thị hình ảnh nếu có
        if (book.getImageUrl() != null) {
            Image image = new Image(book.getImageUrl());
            bookImageView.setImage(image);
        } else {
            bookImageView.setImage(null);
        }

        if (book.getQRcode() != null) {
            // Tạo mã QR từ URL sách
            try {
                Image qrCodeImage = generateQRCode(book.getQRcode(), 150, 150);  // Kích thước 150x150 pixel
                qrCodeImageView.setImage(qrCodeImage);  // Gửi mã QR sang BookDetailController
            } catch (WriterException | IOException e) {
                e.printStackTrace();
                // Handle the exception, e.g., show an error message to the user
            }
        }
    }

    // public void setQRCodeImage(Image qrCodeImage) {
    //     qrCodeImageView.setImage(qrCodeImage);  // Hiển thị mã QR trong ImageView
    // }

    public void showBookDetails(Book book) {
        if (book != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/BookDetail.fxml"));
                Parent root = loader.load();
                ((BookDetailController) loader.getController()).setBookDetails(book);
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Book Details");
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to load book details.");
                alert.showAndWait();
            }
        }
    }
    
    public Parent asParent(Book book) throws IOException {
        if (book == null) {
            return null;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/BookDetail.fxml"));
        Parent bookDetail = loader.load();
        BookDetailController controller = loader.getController();
        controller.setBookDetails(book);
        return bookDetail;
    }
    
}
