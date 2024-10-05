package paint.Tabs;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import paint.PaintApplication;
import paint.Timer.autoSaveTimer;
import paint.fileAndServerManagment.webServer;
import paint.threadedLogger;

import javax.swing.*;

/**
 * The type Tab pane controller.
 */
public class TabPaneController {
    private static threadedLogger logger;
    private static TabPane tabPane;
    private static SelectionModel<Tab> tabSelector;
    private static ArrayList<TabController> tabs;
    private final Button tabAdderButton;
    private autoSaveTimer timer;
    private final webServer server;

    /**
     * Instantiates a new Tab pane controller.
     *
     * @param tp         the tab pane
     * @param ta         the tab adder button
     */
    public TabPaneController(TabPane tp, Tab ta) {
        logger = new threadedLogger();
        logger.sendMessage("Starting Paint");

        server = new webServer(logger); // creation of web server to display all saved images
        server.run();

        tabPane = tp;
        tabSelector = tp.getSelectionModel();

        tabs = new ArrayList<>();
        tabs.add(new TabController(ta));
        addTab();

        tabAdderButton = (Button) tabs.get(0).getTab().getGraphic();
    }

    /**
     * Post initialization setup.
     */
    public void postInitializationSetup(){
        BorderPane borderPane = (BorderPane) tabPane.getParent();
        Label timerLabel = (Label) borderPane.getBottom();
        timer = new autoSaveTimer(timerLabel);
        int delay = 30000;
        ActionListener tick = e -> {
            Platform.runLater(timer);
            if (timer.ended()) {
                Platform.runLater(() -> {
                    for (TabController tab : tabs) {
                        if (tab.getFileController() != null) {
                            tab.getFileController().saveFile();
                        }
                    }
                });
            }
        };
        // AutoSaver turned off for now cause it was annoying af while testing
        new Timer(delay,tick).start();

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
        try{
            AnchorPane root = new FXMLLoader(PaintApplication.class.getResource("Tab-view.fxml")).load();
            nt.setContent(root);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        logger.sendMessage("A new tab was opened");
        tabPane.getTabs().add(nt);
        tabSelector.select(nt);
        tabs.add(new TabController(nt, server, timer, logger));
        tabs.get(tabSelector.getSelectedIndex()).getMenuBar().getMenus().get(0).getItems().get(1).setOnAction(event -> {importImage();});
        logger.updateTabs(tabSelector, tabs);
    }
    /**
     * removes a given tab
     *
     * @param tc the controller being removed
     * @param t  the tab being removed
     */
    public static void removeTab(TabController tc, Tab t){
        logger.sendMessage("A tab was closed");
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
            ButtonType buttonTypeExit = new ButtonType("Exit anyway");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonTypeSAE, buttonTypeExit, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeSAE){
                // ... user chose "One"
                for(TabController tab:modifiedTabs){
                    tab.getFileController().saveFile();
                }
                alert.close();
                closingWindow.close();
            } else if (result.get() == buttonTypeExit) {
                // ... user chose "Two"
                alert.close();
                closingWindow.close();
            } else {
                // ... user chose CANCEL or closed the dialog
                alert.close();
            }
        } else {
            logger.sendMessage("Exiting Paint");
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
     * @param width the new width of the canvas
     * @param height the height of the canvas
     */
    public static void resize(double width, double height){
        TabController currenTab = tabs.get(tabSelector.getSelectedIndex());
        currenTab.resize(width, height);
    }

    protected void importImage(){
        addTab();
        tabs.get(tabSelector.getSelectedIndex()).openfile();
    }

    private final KeyCombination openCombo = new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN);
    /**
     * The Save combo key combination.
     */
    private final KeyCombination saveCombo = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
    /**
     * The J pun key combination.
     */
    private final KeyCombination JPun = new KeyCodeCombination(KeyCode.J, KeyCombination.CONTROL_DOWN);
    /**
     * The Undo combo key combination.
     */
    private final KeyCombination undoCombo = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN);
    /**
     * The Redo combo key combination.
     */
    private final KeyCombination redoCombo = new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN);
    /**
     * The Paste combo key combination.
     */
    private final KeyCombination pasteCombo = new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN);


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
            tabs.get(tabSelector.getSelectedIndex()).save();
        } else if(openCombo.match(ke)) { // A wild openCombo appeared!
            tabs.get(tabSelector.getSelectedIndex()).openfile();
        } else if(JPun.match(ke)){ // A wild JPun appeared!
            Group root = new Group();
            WebView webView = new WebView();
            root.getChildren().add(webView);

            Scene easterEgg = new Scene(root, 720, 460);
            Stage easterWindow = new Stage();
            easterWindow.setScene(easterEgg);
            easterWindow.show();

            webView.getEngine().load("https://blackbirdvalpo.com/");
            logger.sendMessage("They found my easter egg ^o^");
        } else if(undoCombo.match(ke)){ //
            tabs.get(tabSelector.getSelectedIndex()).undo();
        } else if(redoCombo.match(ke)){
            tabs.get(tabSelector.getSelectedIndex()).redo();
        } else if(pasteCombo.match(ke)){
            //tabs.get(tabSelector.getSelectedIndex()).paste(me);
        }
    }
}