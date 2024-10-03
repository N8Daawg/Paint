module com.example.paint {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires javafx.web;
    requires jdk.httpserver;
    requires java.logging;


    opens paint to javafx.fxml;
    exports paint;
    exports paint.drawTools;
    opens paint.drawTools to javafx.fxml;
    exports paint.drawTools.ShapeTools.ZeroSides;
    opens paint.drawTools.ShapeTools.ZeroSides to javafx.fxml;
    exports paint.drawTools.ShapeTools.OneSide;
    opens paint.drawTools.ShapeTools.OneSide to javafx.fxml;
    exports paint.drawTools.ShapeTools.FourSides;
    opens paint.drawTools.ShapeTools.FourSides to javafx.fxml;
    exports paint.drawTools.ShapeTools.ThreeSides;
    opens paint.drawTools.ShapeTools.ThreeSides to javafx.fxml;
    exports paint.drawTools.ShapeTools.FreeHand;
    opens paint.drawTools.ShapeTools.FreeHand to javafx.fxml;
    exports paint.Tabs;
    opens paint.Tabs to javafx.fxml;
    exports paint.drawTools.ShapeTools;
    opens paint.drawTools.ShapeTools to javafx.fxml;
    exports paint.drawTools.MiscTools;
    opens paint.drawTools.MiscTools to javafx.fxml;
    exports paint.Timer;
    opens paint.Timer to javafx.fxml;
    exports paint.fileAndServerManagment.webServer;
    opens paint.fileAndServerManagment.webServer to javafx.fxml;
}