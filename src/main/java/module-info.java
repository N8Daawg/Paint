module com.example.paint {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;


    opens com.example.paint to javafx.fxml;

    exports com.example.paint;
    exports com.example.paint.Tools;
    opens com.example.paint.Tools to javafx.fxml;
}