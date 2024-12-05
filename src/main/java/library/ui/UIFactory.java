package library.ui;

import javafx.application.HostServices;
import library.model.User;

/**
 * UIFactory class provides a factory method to get UIInterface implementations.
 */
public class UIFactory {
    /**
     * Returns an instance of UIInterface based on the user type.
     * 
     * @param userType     the type of user (USER or ADMIN)
     * @param data         the user data
     * @param hostServices the host services
     * @return an instance of UIInterface
     * @throws IllegalArgumentException if the user type is invalid
     */
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
