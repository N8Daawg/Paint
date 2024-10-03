package paint.drawTools;

import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import paint.PaintApplication;
import paint.drawTools.ShapeTools.ManySides.starTool;
import paint.fileAndServerManagment.webServer.FileController;
import paint.drawTools.MiscTools.selectorTool;
import paint.drawTools.MiscTools.textTool;
import paint.drawTools.ShapeTools.FourSides.rectangleTool;
import paint.drawTools.ShapeTools.FourSides.squareTool;
import paint.drawTools.ShapeTools.FreeHand.eraserTool;
import paint.drawTools.ShapeTools.FreeHand.freeDrawTool;
import paint.drawTools.ShapeTools.ManySides.polygonTool;
import paint.drawTools.ShapeTools.OneSide.lineTool;
import paint.drawTools.ShapeTools.ThreeSides.rightTriangleTool;
import paint.drawTools.ShapeTools.ThreeSides.triangleTool;
import paint.drawTools.ShapeTools.ZeroSides.circleTool;
import paint.drawTools.ShapeTools.ZeroSides.ellipseTool;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.util.logging.Logger;

/**
 * The type Draw controller.
 */
public class DrawController {
    private static final Logger logger = Logger.getLogger(PaintApplication.loggerName);
    private final GraphicsContext gc;
    private final GraphicsContext ldgc;
    private final ToolBar toolBar;
    private final ColorPicker colorPicker;
    private drawTool currentTool;
    private final drawTool[] toolsList;
    private final FileController fileController;
    private boolean recentlySaved;
    private final selectorTool selectorTool;
    private final polygonTool polygonTool;

    /**
     * Instantiates a new Draw controller.
     *
     * @param g     the graphics context
     * @param LDGC  the live draw graphics context
     * @param tb    the tool bar
     * @param filec the file controller
     */
    public DrawController(GraphicsContext g, GraphicsContext LDGC, ToolBar tb, FileController filec){
        gc = g;
        ldgc = LDGC;
        toolBar = tb;
        fileController = filec;

        selectorTool = new selectorTool(gc,ldgc);
        polygonTool = new polygonTool(gc, ldgc);
        toolsList = new drawTool[]{
                selectorTool,                    //  0 Selector
                new freeDrawTool(gc, ldgc),      //  1 Pencil
                new eraserTool(gc, ldgc),        //  2 Eraser
                new textTool(gc, ldgc),          //  3 Text
                new lineTool(gc, ldgc),          //  4 Line
                new circleTool(gc, ldgc),        //  5 Circle
                new ellipseTool(gc, ldgc),       //  6 Ellipse
                new squareTool(gc, ldgc),        //  7 Square
                new rectangleTool(gc, ldgc),     //  8 Rectangle
                polygonTool,                     //  9 Polygon
                new triangleTool(gc, ldgc),      // 10 Triangle
                new rightTriangleTool(gc, ldgc), // 11 Right Triangle
                new starTool(gc, ldgc)           // 12 Star
        };
        currentTool = toolsList[1];

        // Selection tool
        VBox selectionContainer = (VBox) toolBar.getItems().get(0);
        ((Button) selectionContainer.getChildren().get(0)).setOnAction(
                event -> {selectTool(0);}
        );

        // Canvas Tools
        VBox canvasToolsContainer = (VBox) toolBar.getItems().get(2);
        ((Button) ((GridPane) canvasToolsContainer.getChildren().get(0)).getChildren().get(0)).setOnAction(
                event -> {clearScreen();}
        );

        // Tools tool
        VBox toolsToolsContainer = (VBox) toolBar.getItems().get(4);
        HBox toolsContainer = (HBox) toolsToolsContainer.getChildren().get(0);
        ((Button) toolsContainer.getChildren().get(0)).setOnAction( // pencil tool button
                event -> {selectTool(1);}
        );
        ((Button) toolsContainer.getChildren().get(1)).setOnAction( // eraser tool button
                event -> {selectTool(2);}
        );
        ((Button) toolsContainer.getChildren().get(2)).setOnAction( // text tool button
                event -> {selectTool(3);}
        );

        // Brush tool
        VBox brushToolsContainer = (VBox) toolBar.getItems().get(6);
        Spinner<Integer> brushWidthSpinner = (Spinner<Integer>) brushToolsContainer.getChildren().get(0);

        // Shape tools
        VBox shapeToolsContainer = (VBox) toolBar.getItems().get(8);
        GridPane shapeGridPane = (GridPane)((ScrollPane) shapeToolsContainer.getChildren().get(0)).getContent();
        ((Button) shapeGridPane.getChildren().get(0)).setOnAction( // line tool Button
                event -> {selectTool(4);}
        );
        ((Button) shapeGridPane.getChildren().get(1)).setOnAction( // Circle tool Button
                event -> {selectTool(5);}
        );
        ((Button) shapeGridPane.getChildren().get(2)).setOnAction( // Oval tool Button
                event -> {selectTool(6);}
        );
        ((Button) shapeGridPane.getChildren().get(3)).setOnAction( // Square tool Button
                event -> {selectTool(7);}
        );
        ((Button) shapeGridPane.getChildren().get(4)).setOnAction( // Rectangle tool Button
                event -> {selectTool(8);}
        );
        ((Button) shapeGridPane.getChildren().get(5)).setOnAction( // polygon tool Button
                event -> {selectTool(9);}
        );
        ((Button) shapeGridPane.getChildren().get(6)).setOnAction( // Triangle tool Button
                event -> {selectTool(10);}
        );
        ((Button) shapeGridPane.getChildren().get(7)).setOnAction( // right Triangle toolButton
                event -> {selectTool(11);}
        );
        ((Button) shapeGridPane.getChildren().get(8)).setOnAction( // Star tool Button
                event -> {/*selectTool(12)*/}
        );


        // color tool
        VBox colorToolContainer = (VBox) toolBar.getItems().get(10);
        colorPicker = (ColorPicker) colorToolContainer.getChildren().get(0);
        colorPicker.setValue(Color.BLACK);


        recentlySaved = false;
        //setListeners(true);
    }

    private void selectTool(int toolIndex){
        currentTool = toolsList[toolIndex];
        if(currentTool == polygonTool){ //prompt user for number of polygon sides
            polygonTool.setPolygonSides(6);
        }

        logger.info(currentTool.toString() + " wes selected");
    }

    /**
     * Clear screen.
     */
    public void clearScreen(){
        gc.clearRect(0,0,gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
    }

    /**
     * Set listeners.
     *
     * @param recentlySaved the recently saved
     */
    public void setListeners(Boolean recentlySaved){
        setCurrentTool(recentlySaved);
    }

    /**
     * Set current tool.
     *
     * @param recentlySaved the recently saved
     */
    protected void setCurrentTool(Boolean recentlySaved){
        currentTool.setAttributes(
                colorPicker.getValue(),
                6,
                false,
                recentlySaved
        );
    }

    /**
     * gets the selected tool's Press event
     *
     * @param mouseEvent the mouse event
     */
    public void getPressEvent(MouseEvent mouseEvent){currentTool.getPressEvent(mouseEvent);}

    /**
     * gets the selected tool's Drag event.
     *
     * @param mouseEvent the mouse event
     */
    public void getDragEvent(MouseEvent mouseEvent){currentTool.getDragEvent(mouseEvent);}

    /**
     *gets the selected tool's Release event
     *
     * @param mouseEvent the mouse event
     */
    public void getReleaseEvent(MouseEvent mouseEvent){
        currentTool.getReleaseEvent(mouseEvent);
    }

    /**
     * Sets recently saved.
     */
    public void setRecentlySaved() {
        recentlySaved = fileController.wasRecentlySaved();
        currentTool.setRecentlySaved(recentlySaved);
    }

    /**
     * Was recently saved boolean.
     *
     * @return whether the file was recently saved or not
     */
    public boolean wasRecentlySaved() {
        recentlySaved = currentTool.wasRecentlySaved();
        return recentlySaved;
    }

    /**
     * Get clip board image.
     *
     * @return the image on the clipboard
     */
    public Image getClipBoard(){
        return selectorTool.getClipBoard();
    }

    /**
     * Paste.
     *
     * @param mouseEvent the mouse event
     */
    public void paste(MouseEvent mouseEvent) {
        selectorTool.paste(mouseEvent);
    }
}