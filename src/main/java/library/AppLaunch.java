package library;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppLaunch extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {

        scene = new Scene(loadFXML("Login"), 500, 350);
        stage.setScene(scene);
        stage.setTitle("Library Management System");
        stage.show();

    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) {
        try {
            System.out.println("Loading FXML from: " + "/library/" + fxml + ".fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(AppLaunch.class.getResource("/library/" + fxml + ".fxml"));
            return fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace(); // In ra thông báo lỗi chi tiết
            return null; // Trả về null nếu có lỗi
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
