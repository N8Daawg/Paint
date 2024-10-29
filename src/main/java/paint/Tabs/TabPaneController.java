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

        tabAdderButton = (Button) tabs.get(0).getTab().getGraphic();
    }

    /**
     * Post initialization setup.
     */
    public void postInitializationSetup(){
        BorderPane borderPane = (BorderPane) tabPane.getParent();
        Label timerLabel = (Label) borderPane.getBottom();
        timer = new autoSaveTimer(timerLabel);
        int delay = 1000;
        ActionListener tick = e -> {
            Platform.runLater(timer);
            if (timer.ended()) {
                Platform.runLater(() -> {
                    for (TabController tab : tabs) {
                        if (tab.getFileController() != null) {
                            tab.saveTab();
                        }
                    }
                });
            }
        };
        // AutoSaver turned off for now cause it was annoying af while testing
        new Timer(delay,tick).start();

        tabAdderButton.setOnAction(e -> addTab());
        shortCutSetup();
        addTab();

        Stage stage = (Stage) tabPane.getScene().getWindow();
        stage.widthProperty().addListener((observable, oldValue, newValue) -> resizeWindow(stage, (Double) oldValue, (Double) newValue, stage.getHeight(), stage.getHeight()));
        stage.heightProperty().addListener(((observable, oldValue, newValue) -> resizeWindow(stage, stage.getWidth(), stage.getWidth(), (Double) oldValue, (Double) newValue)));
        //resizeWindow(stage, stage.getWidth(), stage.getWidth(), stage.getHeight(), stage.getHeight());
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
        tabs.add(new TabController(nt, server, logger));

        tabs.get(tabSelector.getSelectedIndex()).getMenuBar().getMenus().get(0).getItems().get(1).setOnAction(event -> importImage());
        tabs.get(tabSelector.getSelectedIndex()).getMenuBar().getMenus().get(1).getItems().get(1).setOnAction(event -> setTimerVisibility());
        tabs.get(tabSelector.getSelectedIndex()).getMenuBar().getMenus().get(1).getItems().get(2).setOnAction(event -> setSaveNotifications());

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
     * opens a dialog to get the user to savetab all of their projects before closing
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
                    tab.saveTab();
                }
                closingWindow.close();
            } else if (result.get() == buttonTypeExit) {
                // ... user chose "Two"
                closingWindow.close();
                logger.sendMessage("Exiting Paint");
                System.exit(0);
            }
            alert.close();
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
        }
        return modifiedTabs;
    }

    /**
     * Resized the canvas
     *

     */
    public static void resizeWindow(Stage stage, double oldWidth, double newWidth, double oldHeight, double newHeight){
        double heightChange = newHeight - oldHeight;
        double widthChange = newWidth - oldWidth;

        stage.setWidth(newWidth);
        stage.setHeight(newHeight);
        ((BorderPane) tabPane.getParent()).setPrefHeight(newHeight);
        ((BorderPane) tabPane.getParent()).setPrefWidth(newWidth);
        tabPane.getParent().resize(newWidth, newHeight);

        tabPane.setPrefWidth(tabPane.getPrefWidth()+widthChange);
        tabPane.setPrefHeight(tabPane.getPrefHeight()+heightChange);
        tabs.get(tabSelector.getSelectedIndex()).resizeWindow(widthChange, heightChange);
    }

    private void importImage(){
        addTab();
        tabs.get(tabSelector.getSelectedIndex()).openFile();
    }

    private void setSaveNotifications() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Save Notifications");
        alert.setHeaderText(null);
        alert.setContentText("Choose whether you would like to see Notifications when saving");

        ButtonType buttonTypeON = new ButtonType("ON");
        ButtonType buttonTypeOFF = new ButtonType("OFF");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeON, buttonTypeOFF, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeON) {
            // ... user chose "On"
            turnNotificationsOn();
            alert.close();
        } else if (result.get() == buttonTypeOFF) {
            // ... user chose "Off"
            turnNotificationsOff();
            alert.close();
        } else {
            // ... user chose CANCEL or closed the dialog
            alert.close();
        }
    }

    private void turnNotificationsOn(){
        for(TabController tab:tabs){
            tab.setNotifications(true);
        }
    }
    private void turnNotificationsOff(){
        for(TabController tab:tabs){
            tab.setNotifications(false);
        }
    }

    private void setTimerVisibility() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Timer Visible");
        alert.setHeaderText(null);
        alert.setContentText("Choose whether you would like to see the autoSave timer");

        ButtonType buttonTypeVisible = new ButtonType("Visible");
        ButtonType buttonTypeInvisible = new ButtonType("Invisible");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeVisible, buttonTypeInvisible, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeVisible) {
            // ... user chose "One"
            makeTimerVisible();
            alert.close();
        } else if (result.get() == buttonTypeInvisible) {
            // ... user chose "Two"
            makeTimerInvisible();
            alert.close();
        } else {
            // ... user chose CANCEL or closed the dialog
            alert.close();
        }
    }

    private void makeTimerVisible(){timer.setVisibility(true);}
    private void makeTimerInvisible(){timer.setVisibility(false);}

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
            tabs.get(tabSelector.getSelectedIndex()).saveTab();
        } else if(openCombo.match(ke)) { // A wild openCombo appeared!
            tabs.get(tabSelector.getSelectedIndex()).openFile();
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
        } else if(undoCombo.match(ke)){ // a wild undoCombo appeared!
            tabs.get(tabSelector.getSelectedIndex()).undo();
        } else if(redoCombo.match(ke)){ // a wile redoCombo appeared!
            tabs.get(tabSelector.getSelectedIndex()).redo();
        }
    }
}