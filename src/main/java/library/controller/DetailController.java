
/**
 * hiển thị thông tin chi tiết của sách.
 */

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import library.model.Book;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

public class DetailController {
    @FXML
    private ImageView bookImageView, qrCodeImageView;
    @FXML
    private Label titleLabel, authorLabel, isbnLabel, category, avaiLable, isBorrow;
    @FXML
    private Text description;
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox contentBox;

    @FXML
    public void initialize() {
        description.setWrappingWidth(630);
        // contentBox.getChildren().addAll(titleLabel, authorLabel, isbnLabel, category,
        // avaiLable, isBorrow, description,
        // bookImageView, qrCodeImageView);
        // scrollPane.setContent(contentBox);
    }

    public Image generateQRCode(String text, int width, int height)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        // Chuyển BitMatrix thành hình ảnh
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(pngOutputStream.toByteArray());

        return new Image(inputStream); // Trả về hình ảnh QR dưới dạng Image
    }

    public void setBookData(Book books) {

        titleLabel.setText(books.getTitle());
        authorLabel.setText("by    " + books.getAuthor());
        isbnLabel.setText("isbn:   " + books.getIsbn());
        category.setText("category    " + books.getCategories());
        avaiLable.setText("AvaiLabel: " + 10);
        isBorrow.setText("isBorrow");
        description.setText(books.getDescription());

        if (books.getImageUrl() != null) {
            Image image = new Image(books.getImageUrl(), true);
            bookImageView.setImage(image);
        }
        if (books.getQRcode() != null) {
            try {
                Image image = generateQRCode(books.getQRcode(), 125, 125);
                qrCodeImageView.setImage(image);
            } catch (WriterException | IOException e) {
                e.printStackTrace();
                // Handle the exception appropriately
            }
        }

    }

}
