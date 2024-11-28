package library.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.FileAlreadyExistsException;

public class ImageHandler {

    private static final String USER_LIBRARY_PATH = "src/main/resources/imgs/user/";

    public ImageHandler() {
        // Create the directory if it doesn't exist
        File directory = new File(USER_LIBRARY_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public void saveImage(Stage stage, int id) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            try {
                Path destination = Paths.get(USER_LIBRARY_PATH + id + ".png");
                Files.copy(selectedFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
            } catch (FileAlreadyExistsException e) {
                System.out.println("File already exists: " + e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ImageView loadImage(String fileName) {
        File file = new File(USER_LIBRARY_PATH + fileName);
        if (file.exists()) {
            Image image = new Image(file.toURI().toString());
            return new ImageView(image);
        } else {
            System.out.println("File not found: " + fileName);
            return null;
        }
    }

    public Image loadImage(int id) {
        File file = new File(USER_LIBRARY_PATH + id + ".png");
        if (file.exists()) {
            return new Image(file.toURI().toString());
        } else {
            System.out.println("File not found: " + id + ".png");
            return null;
        }
    }
}