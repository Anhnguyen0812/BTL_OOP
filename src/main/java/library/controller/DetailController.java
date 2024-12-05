/**
 * hiển thị thông tin chi tiết của sách.
 */

package library.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javafx.fxml.FXML;
import javafx.application.HostServices;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import library.dao.BookReviewDAO;
import library.model.Book;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

public class DetailController {
    @FXML
    private ImageView bookImageView, qrCodeImageView;
    @FXML
    private Label titleLabel, authorLabel, isbnLabel, category, ratingLabel, previewLinkLabel;
    @FXML
    private Text description;
    @FXML
    private ScrollPane scrollPane;

    @FXML
    VBox v2;
    @FXML
    private Canvas star1, star2, star3, star4, star5;
    private Book book;

    /**
     * Initializes the controller class.
     */
    @FXML
    public void initialize() {
        scrollPane.setFitToWidth(true);
        description.setWrappingWidth(1020);
    }

    /**
     * Initializes the comments section with comments from the database.
     * 
     * @throws IOException if an I/O error occurs.
     */
    public void initComment() throws IOException {
        v2.getChildren().clear();
        BookReviewDAO bookReviewDAO = BookReviewDAO.getBookReviewDao();
        try {
            List<String> comments = bookReviewDAO.getAllCommentBook(book.getId());
            for (String comment : comments) {
                Text text = new Text(comment);
                v2.getChildren().add(text);
                text.setStyle("-fx-font-size: 15px;");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Generates a QR code image from the given text.
     * 
     * @param text   the text to encode in the QR code.
     * @param width  the width of the QR code image.
     * @param height the height of the QR code image.
     * @return the generated QR code as an Image.
     * @throws WriterException if an error occurs while generating the QR code.
     * @throws IOException     if an I/O error occurs.
     */
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

    /**
     * Displays the book rating using star icons.
     * 
     * @param rateAvg    the average rating of the book.
     * @param isHaveRate whether the book has any ratings.
     */
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

    /**
     * Draws a star with the specified color and fill ratio.
     * 
     * @param gc        the GraphicsContext to draw on.
     * @param color     the color of the star.
     * @param fillRatio the ratio of the star to fill with the specified color.
     */
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

    /**
     * Sets the book data and updates the UI components with the book details.
     * 
     * @param books        the book object containing the book details.
     * @param hostServices the HostServices instance to open URLs.
     * @throws IOException if an I/O error occurs.
     */
    public void setBookData(Book books, HostServices hostServices) throws IOException {
        this.book = books;
        initComment();
        titleLabel.setText(books.getTitle());
        authorLabel.setText("By    " + books.getAuthor());
        isbnLabel.setText(books.getIsbn());
        category.setText(books.getCategories());
        description.setText("Description: " + books.getDescription());

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
            previewLinkLabel.setText("Preview link");

            previewLinkLabel.setOnMouseClicked(event -> {
                hostServices.showDocument(books.getQRcode());
            });
        }

    }

}
