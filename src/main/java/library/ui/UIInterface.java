package library.ui;

import javafx.scene.Parent;

/**
 * UIInterface defines the contract for UI components.
 */
public interface UIInterface {
    /**
     * Gets the dashboard for the UI component.
     * 
     * @return the dashboard as a Parent node
     */
    Parent getDashboard();
}
