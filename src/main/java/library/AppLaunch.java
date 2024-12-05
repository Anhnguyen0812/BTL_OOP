package library;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import library.controller.LoginController;

/**
 * The main class for launching the Library Management System application.
 */
public class AppLaunch extends Application {

    private static Scene scene;

    /**
     * Starts the JavaFX application.
     *
     * @param stage the primary stage for this application
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void start(Stage stage) throws IOException {

        HostServices hostServices = getHostServices();
        scene = new Scene(loadFXML("Login", hostServices));
        stage.setScene(scene);
        stage.getIcons().add(new Image("/imgs/appIcon1.png"));
        stage.setResizable(false);
        stage.setTitle("Library Management System");
        stage.show();

    }

    /**
     * Sets the root of the scene to the specified FXML file.
     *
     * @param fxml         the name of the FXML file
     * @param hostServices the HostServices instance
     * @throws IOException if an I/O error occurs
     */
    static void setRoot(String fxml, HostServices hostServices) throws IOException {
        scene.setRoot(loadFXML(fxml, hostServices));
    }

    /**
     * Loads the specified FXML file and sets the HostServices instance to the
     * controller.
     *
     * @param fxml         the name of the FXML file
     * @param hostServices the HostServices instance
     * @return the loaded Parent node
     */
    private static Parent loadFXML(String fxml, HostServices hostServices) {
        try {
            System.out.println("Loading FXML from: " + "/library/" + fxml + ".fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(AppLaunch.class.getResource("/library/" + fxml + ".fxml"));
            Parent root = fxmlLoader.load();
            LoginController controller = fxmlLoader.getController();
            controller.setHostServices(hostServices);
            return root;
        } catch (IOException e) {
            e.printStackTrace(); // In ra thông báo lỗi chi tiết
            return null; // Trả về null nếu có lỗi
        }
    }

    /**
     * The main method to launch the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
