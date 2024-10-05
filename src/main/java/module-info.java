module com.exam {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.json;

    opens com.exam to javafx.fxml;

    exports com.exam;
}
