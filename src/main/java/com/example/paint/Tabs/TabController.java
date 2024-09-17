package com.example.paint.Tabs;

import com.example.paint.FileController;
import com.example.paint.drawTools.DrawController;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;


public class TabController {
    private Tab t;
    private MenuBar menuBar;
    private ToolBar toolBar;
    private Canvas canvas;
    private FileController fileController;
    private DrawController drawController;
    private Boolean recentlySaved;
    private Button closeTab;
    private static double canvasInitialY=69;
    public TabController(Tab T){
        t = T;
        menuBar = null;
        toolBar = null;
        canvas = null;
        fileController = null;
        drawController = null;
    }
    public TabController(Tab T, MenuBar mb, ToolBar tb, Canvas c){
        t = T;

        menuBar = mb;
        toolBar = tb;
        canvas = c;
        canvas.getGraphicsContext2D().setFill(Color.BLUEVIOLET);
        canvas.getGraphicsContext2D().strokeRect(0,0,canvas.getWidth(), canvas.getHeight());

        HBox cbcontainer = (HBox) t.getGraphic();
        closeTab = (Button) cbcontainer.getChildren().get(1);

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
                toolBar, fileController
        );

        recentlySaved = true;

        clearScreen.setOnAction(e -> drawController.clearScreen());
        toolBar.setOnMouseMoved(e -> drawController.setListeners(recentlySaved));
        canvas.setOnMouseEntered(e -> drawController.setListeners(recentlySaved));
        closeTab.setOnAction(e -> deleteTab());
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
    public void setRecentlySaved() {
        if (this.drawController==null){
            recentlySaved = true;
        } else{
            drawController.setRecentlySaved();
            recentlySaved = drawController.wasRecentlySaved();
        }
    }

    protected void deleteTab(){
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

            SAEButton.setOnAction(buttonEvent -> {try { fileController.saveFile();
                } catch (IOException e) { throw new RuntimeException(e);
                }
            });
            closeButton.setOnAction(buttonEvent -> {
                TabPaneController.removeTab(this, getTab());
                tryCloseWindow.close();
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
        canvas.getGraphicsContext2D().strokeRect(0,0,canvas.getWidth(),canvas.getHeight());
    }
}
