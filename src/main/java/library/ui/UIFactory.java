package library.ui;

import javafx.application.HostServices;
import library.model.User;

public class UIFactory {
    public static UIInterface getInterface(String userType, User data, HostServices hostServices) {
        if ("USER".equalsIgnoreCase(userType)) {
            return new UIUser(data, hostServices);
        } else if ("ADMIN".equalsIgnoreCase(userType)) {
            return new UIAdmin(data, hostServices);
        } else {
            throw new IllegalArgumentException("Invalid user type: " + userType);
        }
    }
}
