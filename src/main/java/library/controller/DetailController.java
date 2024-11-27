
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
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import library.model.Book;

public class DetailController {
    @FXML
    private ImageView bookImageView, qrCodeImageView;
    @FXML
    private Label titleLabel, authorLabel, isbnLabel, category, ratingLabel, comment;
    @FXML
    private Text description;
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Canvas star1, star2, star3, star4, star5;

    @FXML
    public void initialize() {
        scrollPane.setFitToWidth(true);
        description.setWrappingWidth(1020);
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

    public void displayBookRate(double rateAvg, boolean isHaveRate) {
        if (!isHaveRate) {
            ratingLabel.setText("No rate");
            rateAvg = 0.0;
        } else {
            ratingLabel.setText("rate  " + String.format("%.1f", rateAvg));
        }
        ratingLabel.setUnderline(true);

        Canvas[] stars = { star1, star2, star3, star4, star5 };
        for (int i = 0; i < stars.length; i++) {
            GraphicsContext gc = stars[i].getGraphicsContext2D();
            gc.clearRect(0, 0, stars[i].getWidth(), stars[i].getHeight());
            if (rateAvg >= i + 0.8) {
                drawStar(gc, Color.YELLOW, 1.0);
            } else if (rateAvg >= i + 0.2) {
                drawStar(gc, Color.YELLOW, 0.5);
            } else {
                drawStar(gc, Color.GRAY, 1.0);
            }
        }
    }

    private void drawStar(GraphicsContext gc, Color color, double fillRatio) {
        double width = 15;
        double height = 15;
        gc.setFill(color);
        gc.beginPath();
        gc.moveTo(width * 0.5, 0);
        gc.lineTo(width * 0.61, height * 0.35);
        gc.lineTo(width, height * 0.35);
        gc.lineTo(width * 0.68, height * 0.57);
        gc.lineTo(width * 0.79, height);
        gc.lineTo(width * 0.5, height * 0.75);
        gc.lineTo(width * 0.21, height);
        gc.lineTo(width * 0.32, height * 0.57);
        gc.lineTo(0, height * 0.35);
        gc.lineTo(width * 0.39, height * 0.35);
        gc.closePath();
        gc.fill();

        if (fillRatio < 1.0) {
            gc.setFill(Color.GRAY);
            gc.beginPath();
            gc.moveTo(width * 0.5, 0);
            gc.lineTo(width * 0.61, height * 0.35);
            gc.lineTo(width, height * 0.35);
            gc.lineTo(width * 0.68, height * 0.57);
            gc.lineTo(width * 0.79, height);
            gc.lineTo(width * 0.5, height * 0.79);
            // gc.lineTo(width * 0.5, height);
            // gc.lineTo(width * 0.32, height * 0.57);
            // gc.lineTo(0, height * 0.35);
            // gc.lineTo(width * 0.39, height * 0.35);
            gc.closePath();
            gc.fill();
        }
    }

    public void setBookData(Book books) {

        titleLabel.setText(books.getTitle());
        authorLabel.setText("By    " + books.getAuthor());
        isbnLabel.setText(books.getIsbn());
        category.setText(books.getCategories());
        description.setText("Description: " + books.getDescription());
        comment.setText(books.getComment());

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
