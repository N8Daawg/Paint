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
    private Label timerLabel;
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
     * Shortcuts setup.
     */
    public static void shortcutsSetup(){
        tpc.shortCutSetup();
    }

    /**
     * Smart c lose window.
     *
     * @param e the e
     */
    public static void smartCLoseWindow(WindowEvent e){
        tpc.tryCloseAll(e);
    }

    /**
     * Resize.
     */
    public static void resize(){
        tpc.resize(initialWindowSizeX, initialWindowSizeY);
    }
}