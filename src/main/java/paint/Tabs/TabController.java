package paint.Tabs;

import paint.PaintApplication;
import paint.fileAndServerManagment.webServer.FileController;
import paint.Timer.autoSaveTimer;
import paint.drawTools.DrawController;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import paint.fileAndServerManagment.webServer.webServer;

import java.io.IOException;
import java.util.Optional;
import java.util.Stack;
import java.util.logging.Logger;


/**
 * The type Tab controller.
 */
public class TabController {
    private static final Logger logger = Logger.getLogger(PaintApplication.loggerName);
    private final Tab tab;
    private final MenuBar menuBar;
    private final ToolBar toolBar;
    private final Canvas canvas;
    private final FileController fileController;
    private final DrawController drawController;
    private Boolean recentlySaved;
    private static final double canvasInitialY=69;
    private WritableImage currentState;
    private Stack<WritableImage> undoStack;
    private Stack<WritableImage> redoStack;
    private final autoSaveTimer timer;

    /**
     * Instantiates a new Tab controller.
     *
     * @param T the t
     */
    public TabController(Tab T){
        tab = T;
        menuBar = null;
        toolBar = null;
        canvas = null;
        fileController = null;
        drawController = null;
        recentlySaved = true;
        timer = null;
    }

    /**
     * Instantiates a new Tab controller.
     *
     * @param T      the tab
     * @param server the server
     * @param Timer  the timer
     */
    public TabController(Tab T, webServer server, autoSaveTimer Timer) {
        tab = T;
        AnchorPane pane = (AnchorPane) tab.getContent();
        menuBar = (MenuBar) pane.getChildren().get(0);
        toolBar = (ToolBar) pane.getChildren().get(1);
        canvas = (Canvas) ((StackPane) ((ScrollPane) pane.getChildren().get(2)).getContent()).getChildren().get(0);

        Canvas liveDrawCanvas = new Canvas(canvas.getWidth(), canvas.getHeight());
        liveDrawCanvas.setLayoutX(canvas.getLayoutX());
        liveDrawCanvas.setLayoutY(canvas.getLayoutY());
        ((StackPane) ((ScrollPane) pane.getChildren().get(2)).getContent()).getChildren().add(
                liveDrawCanvas);
        liveDrawCanvas.toBack();

        timer = Timer;

        fileController = new FileController(menuBar, canvas, server);
        menuBar.getMenus().get(1).getItems().get(0).setOnAction(e -> openResizeWindow());
        menuBar.getMenus().get(1).getItems().get(1).setOnAction(e -> setTimerVisibility());

        drawController = new DrawController(
                canvas.getGraphicsContext2D(),
                liveDrawCanvas.getGraphicsContext2D(),
                toolBar, fileController
        );

        currentState = canvas.snapshot(null, null);

        recentlySaved = true;
        undoStack = new Stack<>();
        redoStack = new Stack<>();


        /*
        System.out.println(
                ((ScrollPane)((StackPane) canvas.getParent()).getParent())
        );
         */
        if(canvas.getScene() != null) {
            System.out.println("test");
            postInitializationSetup();
        }
    }

    /**
     * Post initialization setup.
     */
    public void postInitializationSetup(){
        canvas.setOnMouseEntered(event -> setListeners());
        getTab().setOnCloseRequest(e -> {
            try {deleteTab(e);} catch (IOException ex) {throw new RuntimeException(ex);}
        });
        fileController.postInitSetup();
    }

    /**
     * Get tab tab.
     *
     * @return the tab
     */
    protected Tab getTab(){
        return tab;
    }

    /**
     * Gets menu bar.
     *
     * @return the menu bar
     */
    protected MenuBar getMenuBar() { return menuBar; }

    /**
     * Gets tool bar.
     *
     * @return the tool bar
     */
    protected ToolBar getToolBar() { return toolBar; }

    /**
     * Gets canvas.
     *
     * @return the canvas
     */
    protected Canvas getCanvas() { return canvas; }

    /**
     * Gets file controller.
     *
     * @return the file controller
     */
    public FileController getFileController() {
        return fileController;
    }

    /**
     * Was recently saved boolean.
     *
     * @return the boolean
     */
    public Boolean wasRecentlySaved() {
        return recentlySaved;
    }
    private void setListeners(){
        drawController.setListeners(recentlySaved);
        canvas.setOnMousePressed(drawController::getPressEvent);
        canvas.setOnMouseDragged(e1 -> drawController.getDragEvent(e1));
        canvas.setOnMouseReleased(e -> {
                drawController.getReleaseEvent(e);
                recentlySaved = false;
                undoStack.push(currentState);
                currentState = canvas.snapshot(null, null);
        });
    }
    private void clearScreen() throws IOException {
        if(!recentlySaved){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Clear Screen?");
            alert.setHeaderText("This tab is not saved.");
            alert.setContentText("Are you sure you would like to clear the screen?");

            ButtonType buttonTypeSAE = new ButtonType("Save and clear");
            ButtonType buttonTypeClear = new ButtonType("Clear anyway");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonTypeSAE, buttonTypeClear, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeSAE){
                // ... user chose "One"
                fileController.saveFile();
                drawController.clearScreen();
            } else if (result.get() == buttonTypeClear) {
                // ... user chose "Two"
                alert.close();
                drawController.clearScreen();
            } else {
                // ... user chose CANCEL or closed the dialog
                alert.close();
            }
        } else{
            drawController.clearScreen();
        }
    }

    /**
     * Sets recently saved.
     */
    public void setRecentlySaved() {
        if (this.drawController==null){
            recentlySaved = true;
        } else{
            recentlySaved = drawController.wasRecentlySaved();
            drawController.setRecentlySaved();
        }
    }

    /**
     * Delete tab.
     *
     * @param e the e
     * @throws IOException the io exception
     */
    protected void deleteTab(Event e) throws IOException {
        e.consume();
        setRecentlySaved();
        if(!recentlySaved){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Clear Screen?");
            alert.setHeaderText("This tab is not saved.");
            alert.setContentText("Are you sure you would like to close the project?");

            ButtonType buttonTypeSAE = new ButtonType("Save and clear");
            ButtonType buttonTypeClear = new ButtonType("Close anyway");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonTypeSAE, buttonTypeClear, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeSAE){
                // ... user chose "One"
                fileController.saveFile();
                TabPaneController.removeTab(this, getTab());
            } else if (result.get() == buttonTypeClear) {
                // ... user chose "Two"
                alert.close();
                TabPaneController.removeTab(this, getTab());
            } else {
                // ... user chose CANCEL or closed the dialog
                alert.close();
            }
        } else {
            TabPaneController.removeTab(this, getTab());
        }
    }

    private void setTimerVisibility(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Timer Visible");
        alert.setHeaderText(null);
        alert.setContentText("Choose whether you would like to see the autoSave timer");

        ButtonType buttonTypeVisible = new ButtonType("Visible");
        ButtonType buttonTypeInvisible = new ButtonType("Invisible");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeVisible, buttonTypeInvisible, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeVisible){
            // ... user chose "One"
            timer.setVisibility(true);
            alert.close();
        } else if (result.get() == buttonTypeInvisible) {
            // ... user chose "Two"
            timer.setVisibility(false);
            alert.close();
        } else {
            // ... user chose CANCEL or closed the dialog
            alert.close();
        }
    }

    /**
     * Open resize window.
     */
    public void openResizeWindow(){
        Dialog<double[]> dialog = new Dialog<>();
        dialog.setTitle("Resize window");
        dialog.setHeaderText("Choose new bounds for the canvas");

        // Set the button types.
        ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        // Create the width and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField width = new TextField();
        width.setPromptText(String.valueOf(canvas.getWidth()));
        TextField height = new TextField();
        height.setPromptText(String.valueOf(canvas.getHeight()));

        grid.add(new Label("New Width:"), 0, 0);
        grid.add(width, 1, 0);
        grid.add(new Label("New Height:"), 0, 1);
        grid.add(height, 1, 1);

        // Enable/Disable login button depending on whether a width was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(confirmButtonType);
        loginButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        width.textProperty().addListener((observable, oldValue, newValue) -> loginButton.setDisable(newValue.trim().isEmpty()));

        dialog.getDialogPane().setContent(grid);

        // Request focus on the width field by default.
        Platform.runLater(width::requestFocus);

        // Convert the result to a width-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                return new double[]{Double.parseDouble(width.getText()), Double.parseDouble(height.getText())};
            }
            return null;
        });

        Optional<double[]> result = dialog.showAndWait();
        resize(result.get()[0],result.get()[1]);
    }

    /**
     * Resize.
     *
     * @param x the x
     * @param y the y
     */
    public void resize(double x, double y){
        Stage window = (Stage) getTab().getTabPane().getScene().getWindow();
        window.setWidth(x);window.setHeight(y);

        getTab().getTabPane().setPrefWidth(window.getWidth());
        toolBar.setPrefWidth(window.getWidth());

        canvas.setWidth(window.getWidth()*0.97);
        canvas.setLayoutX((window.getWidth()-canvas.getWidth())/2);

        canvas.setLayoutY(canvasInitialY+(window.getWidth()-canvas.getWidth())/2);

        canvas.setHeight(window.getHeight()-canvasInitialY);

    }

    /**
     * Undo.
     */
    protected void undo(){
        if(!undoStack.isEmpty()){
            canvas.getGraphicsContext2D().drawImage(undoStack.pop(),0,0);
            redoStack.push(currentState);
            currentState = canvas.snapshot(null, null);
        }
    }

    /**
     * Redo.
     */
    protected void redo(){
        if(!redoStack.isEmpty()){
            canvas.getGraphicsContext2D().drawImage(redoStack.pop(),0,0);
            undoStack.push(currentState);
            currentState = canvas.snapshot(null, null);
        }
    }

    /**
     * Save.
     */
    protected void save(){

    }

    /**
     * Openfile.
     */
    protected void openfile(){

    }

    /**
     * Paste.
     *
     * @param me the me
     */
    protected void paste(MouseEvent me) {
        if(drawController.getClipBoard() != null){
            drawController.paste(me);
        }
    }
}