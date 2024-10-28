package paint;

import paint.Tabs.TabPaneController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.WindowEvent;

public class PaintController {

    @FXML
    private BorderPane borderPane;
    private static TabPaneController tpc;
    /*---------------------------------------------------------------------------*/
    /*--------------------------Controller initialization------------------------*/
    /*---------------------------------------------------------------------------*/
    @FXML
    private void initialize() {
        // creates a file controller to manage the menuBar
        TabPane tabPane = (TabPane) borderPane.getCenter();
        Tab tabAdder = tabPane.getTabs().get(0);
        tpc = new TabPaneController(tabPane, tabAdder);
    }

    /**
     * Post initialization setup.
     */
    public static void postInitializationSetup(){
        tpc.postInitializationSetup();
    }

    /**
     * close the window with a smart close feature
     *
     * @param windowEvent the window event
     */
    public static void smartCloseWindow(WindowEvent windowEvent){
        tpc.tryCloseAll(windowEvent);
    }
}