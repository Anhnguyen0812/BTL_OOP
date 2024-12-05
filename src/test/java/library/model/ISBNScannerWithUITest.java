
package library.model;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

public class ISBNScannerWithUITest extends ApplicationTest {

    private ISBNScannerWithUI isbnScannerWithUI;

    @Override
    public void start(Stage stage) {
        isbnScannerWithUI = new ISBNScannerWithUI();
        Pane root = new Pane();
        isbnScannerWithUI.show(root);
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void testISBNScanning() {
        isbnScannerWithUI.setOnScanCompleteListener(isbn -> {
            assertNotNull(isbn);
            assertTrue(isbn.matches("\\d{13}"));
        });
        // Simulate ISBN scanning here
    }
}