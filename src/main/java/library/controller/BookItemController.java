package library.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class BookItemController {
    @FXML
    private Label title, author, date, isbn;

    @FXML
    private ImageView bookimage;

    @FXML
    private Button borrowButton;

}
