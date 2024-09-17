module com.example.paint {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;


    opens com.example.paint to javafx.fxml;

    exports com.example.paint;
    exports com.example.paint.drawTools;
    opens com.example.paint.drawTools to javafx.fxml;
    exports com.example.paint.drawTools.ZeroSides;
    opens com.example.paint.drawTools.ZeroSides to javafx.fxml;
    exports com.example.paint.drawTools.OneSide;
    opens com.example.paint.drawTools.OneSide to javafx.fxml;
    exports com.example.paint.drawTools.FourSides;
    opens com.example.paint.drawTools.FourSides to javafx.fxml;
    exports com.example.paint.drawTools.ThreeSides;
    opens com.example.paint.drawTools.ThreeSides to javafx.fxml;
    exports com.example.paint.drawTools.FreeHand;
    opens com.example.paint.drawTools.FreeHand to javafx.fxml;
    exports com.example.paint.Tabs;
    opens com.example.paint.Tabs to javafx.fxml;
}