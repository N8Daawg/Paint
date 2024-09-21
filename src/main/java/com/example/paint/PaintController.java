package com.example.paint;

import com.example.paint.Tabs.TabPaneController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.WindowEvent;

public class PaintController {
    private static final int initialWindowSizeX = 637;
    private static final int initialWindowSizeY = 498;

    @FXML
    private TabPane tabPane;
    private static TabPaneController tpc;
    /*---------------------------------------------------------------------------*/
    /*--------------------------Controller initialization------------------------*/
    /*---------------------------------------------------------------------------*/
    @FXML
    private void initialize() {
        // creates a file controller to manage the menuBar

        Tab tabAdder = tabPane.getTabs().get(0);
        Tab initialTab = tabPane.getTabs().get(1);
        tpc = new TabPaneController(tabPane, tabAdder, initialTab);
        tabPane.getSelectionModel().select(initialTab);
    }

    public static void shortcutsSetup(){
        tpc.shortCutSetup();
    }
    public static void smartCLoseWindow(WindowEvent e){
        tpc.tryCloseAll(e);
    }
    public static void resize(){
        tpc.resize(initialWindowSizeX, initialWindowSizeY);
    }
}