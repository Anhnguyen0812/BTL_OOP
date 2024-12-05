package library.ui;

import java.io.IOException;

import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import library.controller.DashController;
import library.model.User;

public class UIUser implements UIInterface {

    public User user;
    public HostServices hostServices;

    public UIUser(User user, HostServices hostServices) {
        this.user = user;
        this.hostServices = hostServices;
    }

    @Override
    public Parent getDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/dash.fxml"));
            
            // Truyền dữ liệu vào controller
            DashController controller = new DashController(user, hostServices);
            // controller.setData(user, hostServices);
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
