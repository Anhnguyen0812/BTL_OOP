package library.model;

import com.google.zxing.*;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import library.controller.BookController;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

/**
 * QRScannerWithUI class provides functionality to scan QR codes using a camera
 * and display the video feed in a JavaFX UI.
 */
public class QRScannerWithUI {

    private static final int CAMERA_ID = 0; // Camera mặc định
    private volatile boolean running = true; // Biến điều khiển vòng lặp camera
    private VideoCapture camera; // Camera
    private ImageView imageView; // Hiển thị hình ảnh video

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // Load OpenCV
    }
    private OnScanCompleteListener listener;

    /**
     * Interface for handling scan completion events.
     */
    public interface OnScanCompleteListener {
        void onScanComplete(String isbn);
    }

    /**
     * Sets the listener for scan completion events.
     * 
     * @param listener the listener to set
     */
    public void setOnScanCompleteListener(OnScanCompleteListener listener) {

        this.listener = listener;

    }

    /**
     * Displays the camera feed in the provided Pane.
     * 
     * @param root the Pane to display the camera feed in
     */
    public void show(Pane root) {
        // Thiết lập giao diện
        imageView = new ImageView();
        root.getChildren().add(imageView);
        imageView.fitWidthProperty().bind(root.widthProperty());
        imageView.fitHeightProperty().bind(root.heightProperty());

        // Mở camera và bắt đầu quét
        startCamera();

        // Đóng camera khi ứng dụng thoát
        Platform.runLater(() -> {
            Stage primaryStage = (Stage) root.getScene().getWindow();
            primaryStage.setOnCloseRequest(event -> {
                running = false;
                if (camera != null && camera.isOpened()) {
                    camera.release();
                }
            });
        });
    }

    /**
     * Starts the camera and begins scanning for QR codes.
     */
    private void startCamera() {
        camera = new VideoCapture(CAMERA_ID);
        if (!camera.isOpened()) {
            System.out.println("Không thể mở camera.");
            return;
        }
        running = true;
        Thread cameraThread = new Thread(() -> {
            Mat frame = new Mat();
            while (running) {
                if (camera.read(frame)) {
                    WritableImage fxImage = matToWritableImage(frame);
                    Platform.runLater(() -> imageView.setImage(fxImage));

                    // Giải mã QR Code
                    BufferedImage bufferedImage = matToBufferedImage(frame);
                    if (bufferedImage != null) {
                        String qrCode = decodeQRCode(bufferedImage);
                        if (qrCode != null && isValidURL(qrCode)) {
                            Platform.runLater(() -> {
                                System.out.println("QR Code phát hiện: " + qrCode);
                                running = false; // Dừng sau khi phát hiện mã QR
                                // Play success sound

                                // try {
                                // java.awt.Desktop.getDesktop().browse(new java.net.URI(qrCode));
                                // } catch (Exception e) {
                                // e.printStackTrace();
                                // }
                                if (listener != null) {
                                    listener.onScanComplete(qrCode);
                                }
                                camera.release(); // Giải phóng camera
                                return;

                            });
                        }
                    }
                }
            }
        });

        cameraThread.setDaemon(true);
        cameraThread.start();
    }

    /**
     * Converts a Mat object to a WritableImage.
     * 
     * @param mat the Mat object to convert
     * @return the converted WritableImage
     */
    private WritableImage matToWritableImage(Mat mat) {
        BufferedImage bufferedImage = matToBufferedImage(mat);
        if (bufferedImage != null) {
            return SwingFXUtils.toFXImage(bufferedImage, null);
        }
        return null;
    }

    /**
     * Converts a Mat object to a BufferedImage.
     * 
     * @param mat the Mat object to convert
     * @return the converted BufferedImage
     */
    private BufferedImage matToBufferedImage(Mat mat) {
        try {
            MatOfByte mob = new MatOfByte();
            Imgcodecs.imencode(".jpg", mat, mob);
            return ImageIO.read(new ByteArrayInputStream(mob.toArray()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Decodes a QR code from a BufferedImage.
     * 
     * @param image the BufferedImage to decode
     * @return the decoded QR code text, or null if no QR code is found
     */
    private String decodeQRCode(BufferedImage image) {
        try {
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (NotFoundException e) {
            return null;
        }
    }

    /**
     * Checks if a given URL is valid.
     * 
     * @param url the URL to check
     * @return true if the URL is valid, false otherwise
     */
    private boolean isValidURL(String url) {
        try {
            new java.net.URI(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void setOnScanCompleteListener(Object object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setOnScanCompleteListener'");
    }

    /**
     * Stops the camera and releases resources.
     */
    public void stopCamera() {
        running = false;
        if (camera != null && camera.isOpened()) {
            camera.release();
        }
    }
}
