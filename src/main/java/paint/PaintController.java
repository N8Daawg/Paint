package paint;

import paint.Tabs.TabPaneController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.WindowEvent;


/**
 * The type Paint controller.
 */
public class PaintController {
    private static final int initialWindowSizeX = 637;
    private static final int initialWindowSizeY = 498;

    @FXML
    private BorderPane borderPane;
    //private TabPane tabPane;
    private static TabPaneController tpc;
    /*---------------------------------------------------------------------------*/
    /*--------------------------Controller initialization------------------------*/
    /*---------------------------------------------------------------------------*/
    @FXML
    private void initialize() {
        // creates a file controller to manage the menuBar

        TabPane tabPane = (TabPane) borderPane.getCenter();
        Tab tabAdder = tabPane.getTabs().get(0);
        Tab initialTab = tabPane.getTabs().get(1);
        tpc = new TabPaneController(tabPane, tabAdder, initialTab);
        tabPane.getSelectionModel().select(initialTab);

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

    /**
     * Resize the window
     */
    public static void resize(){
        TabPaneController.resize(initialWindowSizeX, initialWindowSizeY);
    }
}