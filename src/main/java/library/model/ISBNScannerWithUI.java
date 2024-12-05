package library.model;

import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Map;

public class ISBNScannerWithUI {

    private static final int CAMERA_ID = 0; // Camera mặc định
    private volatile boolean running = true; // Biến điều khiển vòng lặp camera
    private VideoCapture camera; // Camera
    private ImageView imageView; // Hiển thị hình ảnh video

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // Load OpenCV
    }
    private OnScanCompleteListener listener;

    public interface OnScanCompleteListener {
        void onScanComplete(String isbn);
    }

    public void setOnScanCompleteListener(OnScanCompleteListener listener) {

        this.listener = listener;

    }

    public void show(Pane root) {
        imageView = new ImageView();
        root.getChildren().add(imageView);
        imageView.fitWidthProperty().bind(root.widthProperty());
        imageView.fitHeightProperty().bind(root.heightProperty());
        startCamera();
    }

    String isbnFinal;

    private void startCamera() {
        running = true;
        camera = new VideoCapture(CAMERA_ID);
        if (!camera.isOpened()) {
            System.out.println("Không thể mở camera.");
            return;
        }

        Thread cameraThread = new Thread(() -> {
            Mat frame = new Mat();
            long lastScanTime = 0; // Thời gian lần quét mã ISBN thành công cuối cùng

            while (running) {
                if (camera.read(frame)) {
                    WritableImage fxImage = matToWritableImage(frame);
                    Platform.runLater(() -> imageView.setImage(fxImage));

                    // Giải mã mã ISBN (Barcode)
                    BufferedImage bufferedImage = matToBufferedImage(frame);
                    if (bufferedImage != null) {
                        String isbnCode = decodeBarcode(bufferedImage);
                        if (isbnCode != null) {
                            long currentTime = System.currentTimeMillis();
                            if (currentTime - lastScanTime >= 2000) { // Kiểm tra 2 giây đã trôi qua chưa
                                lastScanTime = currentTime; // Cập nhật thời gian quét
                                Platform.runLater(() -> {
                                    System.out.println("Mã ISBN phát hiện: " + isbnCode);
                                    isbnFinal = isbnCode;
                                    if (listener != null) {
                                        listener.onScanComplete(isbnCode);
                                    }
                                });
                            }
                        }
                    }
                }
            }
        });

        cameraThread.setDaemon(true);
        cameraThread.start();
    }

    public void stopCamera() {
        running = false;
        if (camera != null && camera.isOpened()) {
            camera.release();
        }
    }

    private WritableImage matToWritableImage(Mat mat) {
        BufferedImage bufferedImage = matToBufferedImage(mat);
        if (bufferedImage != null) {
            return SwingFXUtils.toFXImage(bufferedImage, null);
        }
        return null;
    }

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

    private String decodeBarcode(BufferedImage image) {
        try {
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Map<DecodeHintType, Object> hints = Map.of(
                    DecodeHintType.TRY_HARDER, Boolean.TRUE,
                    DecodeHintType.POSSIBLE_FORMATS, Arrays.asList(BarcodeFormat.EAN_13));
            Result result = new MultiFormatReader().decode(bitmap, hints);
            if (result.getBarcodeFormat() != BarcodeFormat.EAN_13) {
                return null; // Không phải mã ISBN
            }
            return result.getText();
        } catch (NotFoundException e) {
            return null; // Không tìm thấy mã barcode
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi khác nếu có
            return null;
        }
    }

    public String getIsbnFinal() {
        return isbnFinal;
    }

    // public static void main(String[] args) {
    // launch(args);
    // }

    public static Image generateISBN(String isbn) {
        try {
            MultiFormatWriter writer = new MultiFormatWriter();
            BitMatrix bitMatrix = writer.encode(isbn, BarcodeFormat.EAN_13, 400, 200);
            BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
            return SwingFXUtils.toFXImage(image, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
