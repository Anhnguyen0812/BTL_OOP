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

/**
 * ImageHandler class provides methods to handle image saving and loading.
 */
public class ImageHandler {

    private static final String USER_LIBRARY_PATH = "src/main/resources/imgs/user/";

    /**
     * Constructor to initialize ImageHandler and create the user library path if it
     * doesn't exist.
     */
    public ImageHandler() {
        // Create the directory if it doesn't exist
        File directory = new File(USER_LIBRARY_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    /**
     * Saves the selected image to the user library path with the given id.
     * 
     * @param stage the stage to show the file chooser
     * @param id    the user id
     */
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

    /**
     * Loads an image as an ImageView from the user library path with the given file
     * name.
     * 
     * @param fileName the file name of the image
     * @return the ImageView of the loaded image
     */
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

    /**
     * Loads an image from the user library path with the given id.
     * 
     * @param id the user id
     * @return the loaded image
     */
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