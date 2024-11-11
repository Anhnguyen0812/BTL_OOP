package library;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Hello world!
 *
 */
public class App extends Application {

    public void start(Stage primaryStage) throws IOException {
        // Thiết lập giao diện người dùng tại đây
        Button btn = new Button("Hello, Library Manager!");
        btn.setOnAction(e -> System.out.println("Button Clicked!"));

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/library/dash.fxml"));

        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 1300, 600);
        primaryStage.setTitle("Library Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
