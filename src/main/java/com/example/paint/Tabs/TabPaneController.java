package com.example.paint.Tabs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;



import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * The type Tab pane controller.
 */
public class TabPaneController {
    private static TabPane tabPane;
    private static SelectionModel<Tab> tabSelector;
    private static ArrayList<TabController> tabs;
    private final Button tabAdderButton;
    private final TabController templateTab;

    /**
     * Instantiates a new Tab pane controller.
     *
     * @param tp         the tp
     * @param ta         the ta
     * @param initialTab the initial tab
     */
    public TabPaneController(TabPane tp, Tab ta, Tab initialTab) {
        tabPane = tp;
        tabSelector = tp.getSelectionModel();

        AnchorPane p = (AnchorPane) initialTab.getContent();
        MenuBar templateMenuBar = (MenuBar) p.getChildren().get(0);
        ToolBar templateToolBar = (ToolBar) p.getChildren().get(1);
        Canvas templateCanvas = (Canvas) p.getChildren().get(2);

        templateTab = new TabController(initialTab, templateMenuBar, templateToolBar, templateCanvas);

        tabs = new ArrayList<>();
        tabs.add(new TabController(ta));
        tabs.add(templateTab);

        tabAdderButton = (Button) tabs.get(0).getTab().getGraphic();
    }

    public void postInitializationSetup(){
        tabAdderButton.setOnAction(e -> addTab());
        shortCutSetup();
        tabs.get(1).postInitializationSetup();
    }

    /**
     * adds a tab to the tabPane
     */
    protected void addTab(){
        Tab nt = new Tab();
        nt.setText("New Project");

        AnchorPane p = TabCopyer.copyAnchorPane();
        nt.setContent(p);

        MenuBar mb  = TabCopyer.copyMenubar(templateTab.getMenuBar());
        ToolBar tb = TabCopyer.copyToolBar(templateTab.getToolBar());
        Canvas cv = TabCopyer.copyCanvas(templateTab.getCanvas());

        p.getChildren().addAll(mb, tb, cv);

        tabPane.getTabs().add(nt);
        tabSelector.select(nt);

        tabs.add(new TabController(nt, mb, tb, cv));
    }

    /**
     * The Save combo.
     */
    final KeyCombination saveCombo = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
    /**
     * The J pun.
     */
    final KeyCombination JPun = new KeyCodeCombination(KeyCode.J, KeyCombination.CONTROL_DOWN);
    /**
     * The Undo combo.
     */
    final KeyCombination undoCombo = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN);
    /**
     * The Redo combo.
     */
    final KeyCombination redoCombo = new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN);
    /**
     * The Paste combo.
     */
    final KeyCombination pasteCombo = new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN);

    /**
     * a wrapper to set up the keyboard shortcuts
     */
    public void shortCutSetup(){
        tabPane.getScene().addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> { //add event filter to fish for shortcuts
            try { fishForShortcuts(keyEvent);
            } catch (IOException e) { throw new RuntimeException(e);
            }
        });
    }

    /**
     * Fish for some shortcuts
     *
     * @param ke the key event being tracked
     * @throws IOException throws an IO exception sometimes with the saveFile method
     */
    private void fishForShortcuts(KeyEvent ke) throws IOException { //creates key combos and fish for them
        if(saveCombo.match(ke)){ //A wild saveCombo appeared!
            tabs.get(tabSelector.getSelectedIndex()).getFileController().saveFile();
        } else if(JPun.match(ke)){
            Group root = new Group();
            WebView webView = new WebView();
            root.getChildren().add(webView);

            Scene easterEgg = new Scene(root, 720, 460);
            Stage easterWindow = new Stage();
            easterWindow.setScene(easterEgg);
            easterWindow.show();

            webView.getEngine().load("https://blackbirdvalpo.com/");
        } else if(undoCombo.match(ke)){
            tabs.get(tabSelector.getSelectedIndex()).undo();
        } else if(redoCombo.match(ke)){
            tabs.get(tabSelector.getSelectedIndex()).redo();
        } else if(pasteCombo.match(ke)){
            //tabs.get(tabSelector.getSelectedIndex()).paste(me);
        }
    }

    /**
     * removes a given tab
     *
     * @param tc the controller being removed
     * @param t  the tab being removed
     */
    public static void removeTab(TabController tc, Tab t){
        tabPane.getTabs().remove(t);
        tabs.remove(tc);
    }

    /**
     * opens a dialog to get the user to save all of their projects before closing
     *
     * @param windowEvent the closing window event
     */
    public void tryCloseAll(WindowEvent windowEvent){
        ArrayList<TabController> modifiedTabs = findModifiedTabs();
        Stage closingWindow = (Stage) windowEvent.getSource();

        if(!modifiedTabs.isEmpty()){
            windowEvent.consume();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Exit Program?");
            alert.setHeaderText("You have one or more unsaved projects");
            alert.setContentText("Are you sure you would like to exit?");

            ButtonType buttonTypeSAE = new ButtonType("Save and Exit");
            ButtonType buttonTypeClear = new ButtonType("Exit anyway");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonTypeSAE, buttonTypeClear, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeSAE){
                // ... user chose "One"
                for(TabController tab:modifiedTabs){
                    try {
                        tab.getFileController().saveFile();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                alert.close();
                closingWindow.close();
            } else if (result.get() == buttonTypeClear) {
                // ... user chose "Two"
                alert.close();
                closingWindow.close();
            } else {
                // ... user chose CANCEL or closed the dialog
                alert.close();
            }
        } else {
            System.exit(0);
        }
    }

    /**
     *
     * @return returns a list of modified tabs
     */
    private static ArrayList<TabController> findModifiedTabs(){
        ArrayList<TabController> modifiedTabs = new ArrayList<>();
        for(TabController tabController:tabs){
            if(!tabController.wasRecentlySaved()){
                modifiedTabs.add(tabController);
            }
            tabController.setRecentlySaved();
        }
        return modifiedTabs;
    }

    /**
     * Resized the canvas
     *
     * @param x the x
     * @param y the y
     */
    public static void resize(double x, double y){
        TabController currenTab = tabs.get(tabSelector.getSelectedIndex());
        currenTab.resize(x, y);
    }
}