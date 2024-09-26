package com.example.paint.Tabs;

import com.example.paint.FileController;
import com.example.paint.Timer.autoSaveTimer;
import com.example.paint.drawTools.DrawController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Stack;


public class TabController {
    private Tab t;
    private MenuBar menuBar;
    private ToolBar toolBar;
    private Canvas canvas;
    private Canvas liveDrawCanvas;
    private FileController fileController;
    private DrawController drawController;
    private Boolean recentlySaved;
    private static double canvasInitialY=69;
    private WritableImage currentState;
    private Stack<WritableImage> undoStack;
    private Stack<WritableImage> redoStack;
    private Label timerLabel;
    private autoSaveTimer timer;
    public TabController(Tab T){
        t = T;
        menuBar = null;
        toolBar = null;
        canvas = null;
        fileController = null;
        drawController = null;
        recentlySaved = true;
    }
    public TabController(Tab T, MenuBar mb, ToolBar tb, Canvas c){
        t = T;

        menuBar = mb;
        toolBar = tb;
        canvas = c;

        liveDrawCanvas = new Canvas(canvas.getWidth(), canvas.getHeight());
        liveDrawCanvas.setLayoutX(canvas.getLayoutX());
        liveDrawCanvas.setLayoutY(canvas.getLayoutY());
        AnchorPane parent = (AnchorPane) canvas.getParent();
        parent.getChildren().add(liveDrawCanvas);
        liveDrawCanvas.toBack();

        HBox tbcontainer = (HBox) tb.getItems().get(0);
        Button clearScreen = (Button) tbcontainer.getChildren().get(4);


        fileController = new FileController(
                menuBar,
                menuBar.getMenus().get(0).getItems().get(0),
                menuBar.getMenus().get(0).getItems().get(1),
                menuBar.getMenus().get(0).getItems().get(2),
                menuBar.getMenus().get(2).getItems().get(0),
                toolBar, canvas);
        menuBar.getMenus().get(1).getItems().get(0).setOnAction(e -> { openResizeWindow(); });

        drawController = new DrawController(
                canvas.getGraphicsContext2D(),
                liveDrawCanvas.getGraphicsContext2D(),
                toolBar, fileController
        );

        currentState = canvas.snapshot(null, null);

        recentlySaved = true;
        undoStack = new Stack<>();
        redoStack = new Stack<>();

        clearScreen.setOnAction(e -> clearScreen());
        toolBar.setOnMouseMoved(e -> drawController.setListeners(recentlySaved));
        canvas.setOnMouseEntered(e -> setListeners());

        getTab().setOnCloseRequest(e -> deleteTab(e));

        BorderPane borderPane = (BorderPane) getTab().getTabPane().getParent();
        timerLabel = (Label) borderPane.getBottom();
        timer = new autoSaveTimer(timerLabel);
        int delay = 1000;
        ActionListener tick = e -> {
            if(getTab().isSelected()) {
                Platform.runLater(timer);
                if (timer.ended()) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try { fileController.saveFile();
                            } catch (IOException ex) { throw new RuntimeException(ex);
                            }
                        }
                    });
                }
            }
        };
        new Timer(delay,tick).start();
    }

    protected Tab getTab(){
        return t;
    }
    protected MenuBar getMenuBar() { return menuBar; }
    protected ToolBar getToolBar() { return toolBar; }
    protected Canvas getCanvas() { return canvas; }
    public FileController getFileController() {
        return fileController;
    }
    public Boolean wasRecentlySaved() {
        return recentlySaved;
    }
    private void setListeners(){
        drawController.setListeners(recentlySaved);
        canvas.setOnMousePressed(e -> drawController.getPressEvent(e));
        canvas.setOnMouseDragged(e -> drawController.getDragEvent(e));
        canvas.setOnMouseReleased(e -> {
                drawController.getReleaseEvent(e);
                recentlySaved = false;
                undoStack.push(currentState);
                currentState = canvas.snapshot(null, null);
        });
    }
    private void clearScreen(){
        if(!recentlySaved){
            Group root = new Group();
            Button SAEButton = new Button("Save and clear");
            Button closeButton = new Button("Clear anyway");
            Button cancelButton = new Button("Cancel");
            root.getChildren().add(new VBox(
                            new Label("This tab is not saved. Are you sure you would like to clear the screen?"),
                            new HBox(SAEButton, closeButton, cancelButton)
                    )
            );

            Scene clearingScreen = new Scene(root, 340, 65);
            Stage clearingWindow = new Stage();
            clearingWindow.setTitle("Clear Screen?");
            clearingWindow.setScene(clearingScreen);
            clearingWindow.show();

            SAEButton.setOnAction(buttonEvent -> {try {
                fileController.saveFile();
                drawController.clearScreen();
            } catch (IOException ex) { throw new RuntimeException(ex);
            }
            });
            closeButton.setOnAction(buttonEvent -> {
                clearingWindow.close();
                drawController.clearScreen();
            });
            cancelButton.setOnAction(buttonEvent -> {
                clearingWindow.close();
            });
        } else{
            drawController.clearScreen();
        }
    }
    public void setRecentlySaved() {
        if (this.drawController==null){
            recentlySaved = true;
        } else{
            recentlySaved = drawController.wasRecentlySaved();
            drawController.setRecentlySaved();
        }
    }

    protected void deleteTab(Event e){
        e.consume();
        setRecentlySaved();
        if(!recentlySaved){
            Group root = new Group();
            Button SAEButton = new Button("Save and exit");
            Button closeButton = new Button("Close anyway");
            Button cancelButton = new Button("Cancel");
            root.getChildren().add(new VBox(
                            new Label("This tab is not saved. Are you sure you would like to exit?"),
                            new HBox(SAEButton, closeButton, cancelButton)
                    )
            );
            Scene unsavedTabs = new Scene(root, 340, 65);
            Stage tryCloseWindow = new Stage();
            tryCloseWindow.setTitle("Close whole Project?");
            tryCloseWindow.setScene(unsavedTabs);
            tryCloseWindow.show();

            SAEButton.setOnAction(buttonEvent -> {try {
                fileController.saveFile();
                TabPaneController.removeTab(this, getTab());
                } catch (IOException ex) { throw new RuntimeException(ex);
                }
            });
            closeButton.setOnAction(buttonEvent -> {
                tryCloseWindow.close();
                TabPaneController.removeTab(this, getTab());
            });
            cancelButton.setOnAction(buttonEvent -> {
                tryCloseWindow.close();
            });
        } else {
            TabPaneController.removeTab(this, getTab());
        }
    }
    public void openResizeWindow(){
        Group root = new Group();
        Button SAEButton = new Button("Save and exit");
        Button cancelButton = new Button("Cancel");
        TextField xinput = new TextField();
        TextField yinput = new TextField();
        root.getChildren().add(new VBox(
                        new Label("Choose new bounds for the canvas"),
                        new HBox(new Label("New Width: "),xinput,new Label("New Height: "),yinput),
                        new HBox(SAEButton, cancelButton)
                )
        );

        Scene resizing = new Scene(root, 340, 65);
        Stage resizeWindow = new Stage();
        resizeWindow.setTitle("Resizing");
        resizeWindow.setScene(resizing);

        SAEButton.setOnAction(e -> {
            resize(Double.parseDouble(xinput.getText()), Double.parseDouble(yinput.getText()));
            resizeWindow.close();
        });
        cancelButton.setOnAction(e -> {resizeWindow.close();});

        resizeWindow.show();
    }
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
    protected void undo(){
        if(!undoStack.isEmpty()){
            canvas.getGraphicsContext2D().drawImage(undoStack.pop(),0,0);
            redoStack.push(currentState);
            currentState = canvas.snapshot(null, null);
        }
    }
    protected void redo(){
        if(!redoStack.isEmpty()){
            canvas.getGraphicsContext2D().drawImage(redoStack.pop(),0,0);
            undoStack.push(currentState);
            currentState = canvas.snapshot(null, null);
        }
    }

    protected void paste(MouseEvent me) {
        if(drawController.getClipBoard() != null){
            drawController.paste(me);
        }
    }
}