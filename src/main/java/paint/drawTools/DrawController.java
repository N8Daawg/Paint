package paint.drawTools;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import paint.drawTools.ShapeTools.ManySides.starTool;
import paint.fileAndServerManagment.FileController;
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
import paint.threadedLogger;

/**
 * The type Draw controller.
 */
public class DrawController {
    private final GraphicsContext gc;
    private final ColorPicker colorPicker;
    private final Spinner<Integer> brushWidthSpinner;
    private final ToggleButton dashToggle;
    private drawTool currentTool;
    private final drawTool[] toolsList;
    private final FileController fileController;
    private final selectorTool selectorTool;
    private final polygonTool polygonTool;
    private final Spinner<Integer> polySideSpinner;
    private int polyToolSides = 5;
    private final starTool starTool;
    private final Spinner<Integer> starSideSpinner;
    private int starToolSides = 5;
    private final threadedLogger logger;

    /**
     * Instantiates a new Draw controller.
     *
     * @param g     the graphics context
     * @param LDGC  the live draw graphics context
     * @param tb    the tool bar
     * @param filec the file controller
     */
    public DrawController(GraphicsContext g, GraphicsContext LDGC, Spinner<Integer> PolySideSpinner, Spinner<Integer> StarSideSpinner, ToolBar tb,
                          FileController filec, threadedLogger Logger){
        gc = g;
        fileController = filec;
        logger = Logger;
        polySideSpinner = PolySideSpinner;
        starSideSpinner = StarSideSpinner;

        selectorTool = new selectorTool(gc, LDGC);
        polygonTool = new polygonTool(gc, LDGC);
        starTool = new starTool(gc, LDGC);
        toolsList = new drawTool[]{
                selectorTool,                    //  0 Selector
                new freeDrawTool(gc, LDGC),      //  1 Pencil
                new eraserTool(gc, LDGC),        //  2 Eraser
                new textTool(gc, LDGC),          //  3 Text
                new lineTool(gc, LDGC),          //  4 Line
                new circleTool(gc, LDGC),        //  5 Circle
                new ellipseTool(gc, LDGC),       //  6 Ellipse
                new squareTool(gc, LDGC),        //  7 Square
                new rectangleTool(gc, LDGC),     //  8 Rectangle
                polygonTool,                     //  9 Polygon
                new triangleTool(gc, LDGC),      // 10 Triangle
                new rightTriangleTool(gc, LDGC), // 11 Right Triangle
                starTool                         // 12 Star
        };
        currentTool = toolsList[1];

        // Selection tool
        VBox selectionContainer = (VBox) tb.getItems().get(0);
        ((Button) selectionContainer.getChildren().get(0)).setOnAction(
                event -> selectTool(0)
        );

        // Canvas Tools
        VBox canvasToolsContainer = (VBox) tb.getItems().get(2);
        ((Button) ((GridPane) canvasToolsContainer.getChildren().get(0)).getChildren().get(0)).setOnAction( // rotate screen button
                event -> rotateCanvas()
        );
        ((Button) ((GridPane) canvasToolsContainer.getChildren().get(0)).getChildren().get(1)).setOnAction( // mirror screen button
                event -> mirrorCanvas()
        );
        ((Button) ((GridPane) canvasToolsContainer.getChildren().get(0)).getChildren().get(2)).setOnAction( // resize screen button
                event -> resizeCanvas()
        );
        ((Button) ((GridPane) canvasToolsContainer.getChildren().get(0)).getChildren().get(3)).setOnAction( // clear screen button
                event -> clearScreen()
        );

        // Tools tool
        VBox toolsToolsContainer = (VBox) tb.getItems().get(4);
        HBox toolsContainer = (HBox) toolsToolsContainer.getChildren().get(0);
        ((Button) toolsContainer.getChildren().get(0)).setOnAction( // pencil tool button
                event -> selectTool(1)
        );
        ((Button) toolsContainer.getChildren().get(1)).setOnAction( // eraser tool button
                event -> selectTool(2)
        );
        ((Button) toolsContainer.getChildren().get(2)).setOnAction( // text tool button
                event -> selectTool(3)
        );
        dashToggle = (ToggleButton) toolsContainer.getChildren().get(3);

        // Brush tool
        VBox brushToolsContainer = (VBox) tb.getItems().get(6);
        brushWidthSpinner = (Spinner<Integer>) brushToolsContainer.getChildren().get(0);
        brushWidthSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,50));
        brushWidthSpinner.valueProperty().addListener(event -> {});

        // Shape tools
        VBox shapeToolsContainer = (VBox) tb.getItems().get(8);
        GridPane shapeGridPane = (GridPane)((ScrollPane) shapeToolsContainer.getChildren().get(0)).getContent();
        ((Button) shapeGridPane.getChildren().get(0)).setOnAction( // line tool Button
                event -> selectTool(4)
        );
        ((Button) shapeGridPane.getChildren().get(1)).setOnAction( // Circle tool Button
                event -> selectTool(5)
        );
        ((Button) shapeGridPane.getChildren().get(2)).setOnAction( // Oval tool Button
                event -> selectTool(6)
        );
        ((Button) shapeGridPane.getChildren().get(3)).setOnAction( // Square tool Button
                event -> selectTool(7)
        );
        ((Button) shapeGridPane.getChildren().get(4)).setOnAction( // Rectangle tool Button
                event -> selectTool(8)
        );
        ((Button) shapeGridPane.getChildren().get(5)).setOnAction( // polygon tool Button
                event -> selectTool(9)
        );
        ((Button) shapeGridPane.getChildren().get(6)).setOnAction( // Triangle tool Button
                event -> selectTool(10)
        );
        ((Button) shapeGridPane.getChildren().get(7)).setOnAction( // right Triangle toolButton
                event -> selectTool(11)
        );
        ((Button) shapeGridPane.getChildren().get(8)).setOnAction( // Star tool Button
                event -> selectTool(12)
        );


        // color tool
        VBox colorToolContainer = (VBox) tb.getItems().get(10);
        colorPicker = (ColorPicker) colorToolContainer.getChildren().get(0);
        colorPicker.setValue(Color.BLACK);

        setListeners(true);
    }

    private void selectTool(int toolIndex){
        currentTool = toolsList[toolIndex];
        if(currentTool == polygonTool){ //prompt user for number of polygon sides
            polygonTool.setPolygonSides(polySideSpinner.getValue());
        } else if(currentTool == starTool){
            starTool.setStarSides(starSideSpinner.getValue());
        }
        logger.sendMessage(currentTool.toString() + " was selected");
    }
    public void setPolyToolSides(int polySides){this.polyToolSides = polySides;}
    public void setStarToolSides(int starSides){this.starToolSides = starSides;}

    /**
     * Clear screen.
     */
    public void clearScreen(){currentTool.clearCanvas(gc);}

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
        if(currentTool==polygonTool){
            setPolyToolSides(polyToolSides);
        } else if(currentTool==starTool){
            setStarToolSides(starToolSides);
        }
        currentTool.setAttributes(
                colorPicker.getValue(),
                brushWidthSpinner.getValue(),
                dashToggle.isSelected(),
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
    public void getReleaseEvent(MouseEvent mouseEvent){currentTool.getReleaseEvent(mouseEvent);}

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

    private void resizeCanvas(){

    }

    private int rotation = 0;
    private void rotateCanvas(){
        rotation+=90;
        gc.getCanvas().rotateProperty().setValue(rotation);
    }

    private void mirrorCanvas(){
        Image currentstate = gc.getCanvas().snapshot(null, null);
        clearScreen();
        gc.save();
        gc.translate(gc.getCanvas().getWidth(), 0);
        gc.scale(-1,1);
        gc.drawImage(currentstate, 0, 0);
        gc.restore();
    }
}