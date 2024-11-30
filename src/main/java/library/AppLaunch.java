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

public class AppLaunch extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {

        HostServices hostServices = getHostServices();
        scene = new Scene(loadFXML("Login", hostServices));
        stage.setScene(scene);
        stage.getIcons().add(new Image("/imgs/appIcon1.png"));
        stage.setTitle("Library Management System");
        stage.show();

    }

    static void setRoot(String fxml, HostServices hostServices) throws IOException {
        scene.setRoot(loadFXML(fxml, hostServices));
    }

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

    public static void main(String[] args) {
        launch(args);
    }
}
