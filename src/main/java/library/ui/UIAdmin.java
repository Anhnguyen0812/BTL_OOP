package library.ui;

import java.io.IOException;

import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import library.controller.Dash_AdminController;
import library.model.User;

public class UIAdmin implements UIInterface {

    public User user;
    public HostServices hostServices;

    public UIAdmin(User user, HostServices hostServices) {
        this.user = user;
        this.hostServices = hostServices;
    }

    @Override
    public Parent getDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/dash_admin.fxml"));
            // Truyền dữ liệu vào controller
            Dash_AdminController controller = new Dash_AdminController(user, hostServices);
            loader.setController(controller);
            Parent root = loader.load();
            return root;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error in getDashboard");
            return null;
        }
    }
}
