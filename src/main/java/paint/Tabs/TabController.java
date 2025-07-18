package paint.Tabs;

import paint.fileAndServerManagment.FileController;
import paint.drawTools.DrawController;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import paint.fileAndServerManagment.webServer;
import paint.threadedLogger;

import java.io.IOException;
import java.util.Optional;
import java.util.Stack;

/**
 * The type Tab controller.
 */
public class TabController {
    private final Tab tab;
    private final MenuBar menuBar;
    private final ToolBar toolBar;
    private final Canvas canvas;
    private final FileController fileController;
    private final DrawController drawController;
    private Boolean recentlySaved;
    private WritableImage currentState;
    private Stack<WritableImage> undoStack;
    private Stack<WritableImage> redoStack;
    private final threadedLogger logger;

    /**
     * Instantiates a new Tab controller.
     *
     * @param T the tab
     */
    public TabController(Tab T){
        tab = T;
        menuBar = null;
        toolBar = null;
        canvas = null;
        fileController = null;
        drawController = null;
        recentlySaved = true;

        logger = null;
    }

    /**
     * Instantiates a new Tab controller.
     *
     * @param T      the tab to control
     * @param server the web server
     * @param Logger the logger
     */
    public TabController(Tab T, webServer server, threadedLogger Logger) {
        tab = T;
        AnchorPane pane = (AnchorPane) tab.getContent();
        menuBar = (MenuBar) pane.getChildren().get(0);
        toolBar = (ToolBar) pane.getChildren().get(1);
        canvas = (Canvas) ((Pane) ((StackPane) ((ScrollPane) pane.getChildren().get(2)).getContent()).getChildren().get(0)).getChildren().get(0);

        logger = Logger;


        fileController = new FileController(canvas, server, logger);

        /*---------------------------------- MENU BAR STUFF ---------------------------------------*/
        // File menu
        menuBar.getMenus().get(0).getItems().get(0).setOnAction(event -> openFile());
        menuBar.getMenus().get(0).getItems().get(2).setOnAction(event -> saveTab());
        menuBar.getMenus().get(0).getItems().get(3).setOnAction(event -> saveAsTab());

        // Edit menu
        menuBar.getMenus().get(1).getItems().get(0).setOnAction(event -> openResizeWindow());

        // Polygon Side Spinner in Edit menu
        Spinner<Integer> polySideSpinner = (Spinner<Integer>) ((HBox) ((CustomMenuItem) menuBar.getMenus().get(1).getItems().get(3)).getContent()).getChildren().get(1);
        polySideSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 5));

        // Star Side Spinner in Edit Menu
        Spinner<Integer> starSideSpinner = (Spinner<Integer>) ((HBox) ((CustomMenuItem) menuBar.getMenus().get(1).getItems().get(4)).getContent()).getChildren().get(1);
        starSideSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 5));

        // Help menu
        menuBar.getMenus().get(2).getItems().get(0).setOnAction(event -> help());


        /*---------------------------------- TOOL BAR STUFF ---------------------------------------*/
        // clear screen button
        ((Button) ((GridPane) ((VBox) toolBar.getItems().get(2)).getChildren().get(0)).getChildren().get(3)).setOnAction(
                event -> clearScreen()
        );
        // resizeCanvas canvas button
        ((Button) ((GridPane) ((VBox) toolBar.getItems().get(2)).getChildren().get(0)).getChildren().get(2)).setOnAction(
                event -> openResizeWindow()
        );

        /*----------------------------------- CANVAS STUFF ----------------------------------------*/
        // Live Draw Canvas
        Canvas liveDrawCanvas = new Canvas(canvas.getWidth(), canvas.getHeight());
        liveDrawCanvas.setLayoutX(canvas.getLayoutX());
        liveDrawCanvas.setLayoutY(canvas.getLayoutY());
        ((Pane) ((StackPane) ((ScrollPane) pane.getChildren().get(2)).getContent()).getChildren().get(0)).getChildren().add(
                liveDrawCanvas);
        liveDrawCanvas.toBack();

        // GET READY TO DRAW
        canvas.setOnMouseEntered(event -> setListeners());

        // recently Saved bool for smart closing
        recentlySaved = true;

        // Undo and redo Stacks for Canvas State
        currentState = canvas.snapshot(null, null);
        undoStack = new Stack<>();
        redoStack = new Stack<>();

        /*----------------------------------- DRAWING STUFF ---------------------------------------*/
        drawController = new DrawController(
                canvas.getGraphicsContext2D(),
                liveDrawCanvas.getGraphicsContext2D(),
                polySideSpinner, starSideSpinner,
                toolBar, logger
        );


        /*----------------------------------- CLOSING TABS ---------------------------------------*/
        tab.setOnCloseRequest(e -> {
            try {deleteTab(e);} catch (IOException ex) {throw new RuntimeException(ex);}
        });
    }

    /**
     * Get the tab.
     *
     * @return the tab
     */
    protected Tab getTab(){return tab;}

    /**
     * Gets menu bar.
     *
     * @return the menu bar
     */
    protected MenuBar getMenuBar() {return menuBar;}

    /**
     * Gets file controller.
     *
     * @return the file controller
     */
    public FileController getFileController() {return fileController;}


    /**
     * Sets notifications for saving.
     *
     * @param on a bool representing true if notifications are on and false if not
     */
    public void setNotifications(boolean on){fileController.setNotifications(on);}

    /**
     * open Open file dialogue.
     */
    protected void openFile(){
        fileController.openFile((Stage) menuBar.getScene().getWindow());
        tab.setText(fileController.getCurrentFile());
    }

    /**
     * open save tab dialogue.
     */
    protected void saveTab(){
        recentlySaved = fileController.saveFile((Stage) menuBar.getScene().getWindow());
        tab.setText(fileController.getCurrentFile());
    }

    private void saveAsTab(){
        recentlySaved = fileController.saveAsFile((Stage) menuBar.getScene().getWindow());
        tab.setText(fileController.getCurrentFile());
    }

    private void help(){fileController.showHelp();}

    /**
     * returns true if the tab was recently saved
     */
    public Boolean wasRecentlySaved() {return recentlySaved;}

    private void setListeners(){
        drawController.updateTool();
        canvas.setOnMousePressed(drawController::getPressEvent);
        canvas.setOnMouseDragged(drawController::getDragEvent);
        canvas.setOnMouseReleased(e -> {
                drawController.getReleaseEvent(e);
                recentlySaved = false;
                undoStack.push(currentState);
                currentState = canvas.snapshot(null, null);
        });
    }

    private void clearScreen() {
        if(!recentlySaved){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Clear Screen?");
            alert.setHeaderText("This tab is not saved.");
            alert.setContentText("Are you sure you would like to clear the screen?");

            ButtonType buttonTypeSAC = new ButtonType("Save and clear");
            ButtonType buttonTypeClear = new ButtonType("Clear anyway");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonTypeSAC, buttonTypeClear, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeSAC){
                // ... user chose "Save and clear"
                fileController.saveFile((Stage) menuBar.getScene().getWindow());
                alert.close();
                clearScreen();
            } else if (result.get() == buttonTypeClear) {
                recentlySaved = true;
                alert.close();
                clearScreen();
            } else{
                // ... user chose anything else
                alert.close();
            }
        } else{
            drawController.clearScreen();
        }
    }

    private void deleteTab(Event event) throws IOException {
        event.consume();
        if(!recentlySaved){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Clear Screen?");
            alert.setHeaderText("This tab is not saved.");
            alert.setContentText("Are you sure you would like to close the project?");

            ButtonType buttonTypeSAC = new ButtonType("Save and Close");
            ButtonType buttonTypeClear = new ButtonType("Close anyway");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonTypeSAC, buttonTypeClear, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeSAC){
                // ... user chose "Save and Close"
                fileController.saveFile((Stage) menuBar.getScene().getWindow());
                TabPaneController.removeTab(this, tab);
            } else if (result.get() == buttonTypeClear) {
                // ... user chose "Close anyway"
                TabPaneController.removeTab(this, tab);
            }
            alert.close();
        } else {
            TabPaneController.removeTab(this, tab);
        }
    }

    /**
     * Undo the previous action
     */
    protected void undo(){
        if(!undoStack.isEmpty()){
            canvas.getGraphicsContext2D().drawImage(undoStack.pop(),0,0);
            redoStack.push(currentState);
            currentState = canvas.snapshot(null, null);
        }
    }

    /**
     * redo the previous undo action
     */
    protected void redo(){
        if(!redoStack.isEmpty()){
            canvas.getGraphicsContext2D().drawImage(redoStack.pop(),0,0);
            undoStack.push(currentState);
            currentState = canvas.snapshot(null, null);
        }
    }

    /**
     * Resizes tab nodes to fit a resized window
     *
     * @param widthChange  the change in window width
     * @param heightChange the change in window height
     */
    public void resizeWindow(double widthChange, double heightChange){
        AnchorPane ap = (AnchorPane) menuBar.getParent();
        double apWidth = ap.getPrefWidth()+widthChange;
        double apHeight = ap.getPrefHeight()+heightChange;
        ap.setPrefWidth(apWidth);
        ap.setPrefHeight(apHeight);
        ap.resize(apWidth,apHeight);


        double mbWidth = menuBar.getPrefWidth()+widthChange;
        menuBar.setPrefWidth(mbWidth);
        menuBar.resize(mbWidth, menuBar.getHeight());

        double tbWidth = toolBar.getPrefWidth()+widthChange;
        toolBar.setPrefWidth(tbWidth);
        toolBar.resize(tbWidth, toolBar.getHeight());

        double spWidth = ((ScrollPane) ap.getChildren().get(2)).getWidth()+widthChange;
        double spHeight = ((ScrollPane) ap.getChildren().get(2)).getHeight()+heightChange;
        ((ScrollPane) ap.getChildren().get(2)).setPrefWidth(spWidth);
        ((ScrollPane) ap.getChildren().get(2)).setPrefHeight(spHeight);
        ap.getChildren().get(2).resize(spWidth, spHeight);

        ((ScrollPane) ap.getChildren().get(2)).getContent().resize(spWidth*0.90, spHeight*0.90);
    }

    /**
     * Open resize window dialog to resize the canvas.
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
        resizeCanvas(result.get()[0],result.get()[1]);
    }

    /**
     * Resizes the canvas.
     *
     * @param newWidth the new width of the canvas
     * @param newHeight the new Height of the canvas
     */
    public void resizeCanvas(double newWidth, double newHeight){
        ScrollPane scp = (ScrollPane) ((AnchorPane) tab.getContent()).getChildren().get(2);
        StackPane sp = (StackPane) scp.getContent();
        Pane pane = (Pane) sp.getChildren().get(0);

        sp.setPrefWidth(newWidth);sp.setPrefHeight(newHeight);
        sp.resize(newWidth, newHeight);
        pane.setPrefWidth(newWidth);pane.setPrefHeight(newHeight);
        pane.resize(newWidth, newHeight);
        canvas.setWidth(newWidth);canvas.setHeight(newHeight);
        pane.getChildren().get(0).resize(newWidth, newHeight);
        Canvas ldCanvas = (Canvas) pane.getChildren().get(1);
        ldCanvas.setWidth(newWidth);ldCanvas.setHeight(newHeight);
        ldCanvas.resize(newWidth, newHeight);
    }
}