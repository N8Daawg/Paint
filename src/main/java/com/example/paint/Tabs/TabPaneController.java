package com.example.paint.Tabs;

import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class TabPaneController {
    private static TabPane tabPane;
    private static SelectionModel<Tab> tabSelector;
    private static ArrayList<TabController> tabs;
    private Button tabAdderButton;
    private TabController templateTab;
    public TabPaneController(TabPane tp, Tab ta, Tab initialTab) {
        tabPane = tp;
        tabSelector = tp.getSelectionModel();

        AnchorPane p = (AnchorPane) initialTab.getContent();
        MenuBar templateMenuBar = (MenuBar) p.getChildren().get(0);
        ToolBar templateToolBar = (ToolBar) p.getChildren().get(1);
        Canvas templateCanvas = (Canvas) p.getChildren().get(2);

        templateTab = new TabController(initialTab, templateMenuBar, templateToolBar, templateCanvas);

        tabs = new ArrayList<TabController>();
        tabs.add(new TabController(ta));
        tabs.add(templateTab);

        tabAdderButton = (Button) tabs.get(0).getTab().getGraphic();
        setup();
    }

    /**
     * setup wrapper for the tabAdderButton
     */
    private void setup() {
        tabAdderButton.setOnAction(e -> addTab());
    }

    /**
     * adds a tab to the tabPane
     */
    protected void addTab(){
        Tab nt = new Tab();
        HBox tabLabel = TabCopyer.copyTabLabel();
        nt.setGraphic(tabLabel);

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
     * a wrapper to set up the keyboard shortcuts
     */
    public void shortCutSetup(){
        tabPane.getScene().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent){ try {//add event filter to fish for shortcuts
                fishForShortcuts(keyEvent);
            } catch (IOException e) { throw new RuntimeException(e);
            }}
        });
    }

    /**
     * Fish for some shortcuts
     * @param ke the keyevent being tracked
     * @throws IOException
     */
    private void fishForShortcuts(KeyEvent ke) throws IOException { //creates key combos and fish for them
        final KeyCombination saveCombo = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
        if(saveCombo.match(ke)){ //A wild saveCombo appeared!
            tabs.get(tabSelector.getSelectedIndex()).getFileController().saveFile();
        }
    }

    /**
     * removes a given tab
     * @param tc the controller being removed
     * @param t the tab being removed
     */
    public static void removeTab(TabController tc, Tab t){
        tabPane.getTabs().remove(t);
        tabs.remove(tc);
    }

    /**
     * opens a dialog to get the user to save all of thier projects before closing
     * @param windowEvent the closing window event
     */
    public void tryCloseAll(WindowEvent windowEvent){
        ArrayList<TabController> modifiedTabs = findModifiedTabs();
        Stage closingWindow = (Stage) windowEvent.getSource();

        if(!modifiedTabs.isEmpty()){
            windowEvent.consume();

            Group root = new Group();
            Button SAEButton = new Button("Save and exit");
            Button closeButton = new Button("Close anyway");
            Button cancelButton = new Button("Cancel");

            root.getChildren().add(new VBox(
                    new Label("You have unsaved projects. Are you sure you would like to exit?"),
                    new HBox(SAEButton, closeButton, cancelButton)
                    )
            );

            Scene unsavedTabs = new Scene(root, 340, 65);
            Stage tryCloseWindow = new Stage();
            tryCloseWindow.setTitle("Close whole Project?");
            tryCloseWindow.setScene(unsavedTabs);
            tryCloseWindow.show();

            SAEButton.setOnAction(e -> {
                for(TabController tab:modifiedTabs){
                    try {
                        tab.getFileController().saveFile();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                closeAll(e, closingWindow);
            });
            closeButton.setOnAction(e -> closeAll(e, closingWindow));
            cancelButton.setOnAction(e -> {
                tryCloseWindow.close();
            });
        }
    }

    /**
     * Saves all tabs and closes the window
     * @param buttonEvent the event happening
     * @param closingWindow the window wanting to be closed
     */
    private void closeAll(ActionEvent buttonEvent, Stage closingWindow) {
        closingWindow.close();

        Node source = (Node) buttonEvent.getSource();
        Stage saveWindow = (Stage) source.getScene().getWindow();
        saveWindow.close();
    }

    /**
     *
     * @return returns a list of modified tabs
     */
    private static ArrayList<TabController> findModifiedTabs(){
        ArrayList<TabController> modifiedTabs = new ArrayList<>();
        for(TabController tabController:tabs){
            tabController.setRecentlySaved();
            if(!tabController.wasRecentlySaved()){
                modifiedTabs.add(tabController);
            }
        }
        return modifiedTabs;
    }

    /**
     * Resized the canvas
     * @param x
     * @param y
     */
    public static void resize(double x, double y){
        TabController currenTab = tabs.get(tabSelector.getSelectedIndex());
        currenTab.resize(x, y);
    }
}
