
package library.model;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

public class QRScannerWithUITest extends ApplicationTest {

    private QRScannerWithUI qrScannerWithUI;

    @Override
    public void start(Stage stage) {
        qrScannerWithUI = new QRScannerWithUI();
        Pane root = new Pane();
        qrScannerWithUI.show(root);
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void testQRCodeScanning() {
        qrScannerWithUI.setOnScanCompleteListener(isbn -> {
            assertNotNull(isbn);
            assertTrue(isbn.startsWith("http"));
        });
        // Simulate QR code scanning here
    }
}