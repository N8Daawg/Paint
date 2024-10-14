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
import javafx.scene.input.MouseEvent;
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
    private static final double canvasInitialY=69;
    private WritableImage currentState;
    private Stack<WritableImage> undoStack;
    private Stack<WritableImage> redoStack;
    private final threadedLogger logger;

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

        logger = null;
    }

    /**
     * Instantiates a new Tab controller.
     *
     * @param T      the tab
     * @param server the server
     */
    public TabController(Tab T, webServer server, threadedLogger Logger) {
        tab = T;
        AnchorPane pane = (AnchorPane) tab.getContent();
        menuBar = (MenuBar) pane.getChildren().get(0);
        toolBar = (ToolBar) pane.getChildren().get(1);
        canvas = (Canvas) ((StackPane) ((ScrollPane) pane.getChildren().get(2)).getContent()).getChildren().get(0);

        logger = Logger;


        fileController = new FileController(menuBar, canvas, server, logger);


        ((Button) ((GridPane) ((VBox) toolBar.getItems().get(2)).getChildren().get(0)).getChildren().get(3)).setOnAction( // clear screen button
                event -> clearScreen()
        );

        Canvas liveDrawCanvas = new Canvas(canvas.getWidth(), canvas.getHeight());
        liveDrawCanvas.setLayoutX(canvas.getLayoutX());
        liveDrawCanvas.setLayoutY(canvas.getLayoutY());
        ((StackPane) ((ScrollPane) pane.getChildren().get(2)).getContent()).getChildren().add(
                liveDrawCanvas);
        liveDrawCanvas.toBack();

        Spinner<Integer> polySideSpinner = (Spinner<Integer>) ((HBox) ((CustomMenuItem) menuBar.getMenus().get(1).getItems().get(2)).getContent()).getChildren().get(1);
        polySideSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 5));

        Spinner<Integer> starSideSpinner = (Spinner<Integer>) ((HBox) ((CustomMenuItem) menuBar.getMenus().get(1).getItems().get(3)).getContent()).getChildren().get(1);
        starSideSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 5));

        drawController = new DrawController(
                canvas.getGraphicsContext2D(),
                liveDrawCanvas.getGraphicsContext2D(),
                polySideSpinner, starSideSpinner,
                toolBar, fileController, logger
        );


        currentState = canvas.snapshot(null, null);

        recentlySaved = true;
        undoStack = new Stack<>();
        redoStack = new Stack<>();

        if(pane.getChildren().get(2).getScene() != null) {
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

        //file menu
        menuBar.getMenus().get(0).getItems().get(0).setOnAction(event -> fileController.openFile());
        menuBar.getMenus().get(0).getItems().get(2).setOnAction(event -> saveTab());
        menuBar.getMenus().get(0).getItems().get(3).setOnAction(event -> saveAsTab());

        //edit menu
        menuBar.getMenus().get(1).getItems().get(0).setOnAction(event -> openResizeWindow());

        //help menu
        menuBar.getMenus().get(2).getItems().get(0).setOnAction(event -> fileController.showHelp());

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
     * openFile.
     */
    protected void openFile(){
        fileController.openFile();
    }

    /**
     * Save.
     */
    protected void saveTab(){
        recentlySaved = fileController.saveFile();
        tab.setText(fileController.getCurrentFile());
    }

    private void saveAsTab(){
        recentlySaved = fileController.saveAsFile();
        tab.setText(fileController.getCurrentFile());
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
     * Delete tab.
     *
     * @param e the e
     * @throws IOException the io exception
     */
    protected void deleteTab(Event e) throws IOException {
        e.consume();
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