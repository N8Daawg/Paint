module com.example.paint {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires javafx.web;
    requires jdk.httpserver;


    opens com.example.paint to javafx.fxml;
    exports com.example.paint;
    exports com.example.paint.drawTools;
    opens com.example.paint.drawTools to javafx.fxml;
    exports com.example.paint.drawTools.ShapeTools.ZeroSides;
    opens com.example.paint.drawTools.ShapeTools.ZeroSides to javafx.fxml;
    exports com.example.paint.drawTools.ShapeTools.OneSide;
    opens com.example.paint.drawTools.ShapeTools.OneSide to javafx.fxml;
    exports com.example.paint.drawTools.ShapeTools.FourSides;
    opens com.example.paint.drawTools.ShapeTools.FourSides to javafx.fxml;
    exports com.example.paint.drawTools.ShapeTools.ThreeSides;
    opens com.example.paint.drawTools.ShapeTools.ThreeSides to javafx.fxml;
    exports com.example.paint.drawTools.ShapeTools.FreeHand;
    opens com.example.paint.drawTools.ShapeTools.FreeHand to javafx.fxml;
    exports com.example.paint.Tabs;
    opens com.example.paint.Tabs to javafx.fxml;
    exports com.example.paint.drawTools.ShapeTools;
    opens com.example.paint.drawTools.ShapeTools to javafx.fxml;
    exports com.example.paint.drawTools.MiscTools;
    opens com.example.paint.drawTools.MiscTools to javafx.fxml;
    exports com.example.paint.Timer;
    opens com.example.paint.Timer to javafx.fxml;
    exports com.example.paint.webServer;
    opens com.example.paint.webServer to javafx.fxml;
}