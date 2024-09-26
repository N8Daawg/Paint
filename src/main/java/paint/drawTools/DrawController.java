package paint.drawTools;

import paint.FileController;
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

/**
 * The type Draw controller.
 */
public class DrawController {

    private GraphicsContext gc;
    private GraphicsContext ldgc;
    private ToolBar toolBar;
    private ColorPicker colorPicker;
    private ChoiceBox<drawTool> shapeChooser;
    private CheckBox dashedlineChecker;
    private TextField brushWidthChooser;
    private drawTool currentTool;
    private drawTool[] toolsList;
    private FileController fileController;
    private boolean recentlySaved;
    /**
     * The Selector tool.
     */
    selectorTool selectorTool;
    /**
     * The Polygon tool.
     */
    polygonTool polygonTool;

    /**
     * Instantiates a new Draw controller.
     *
     * @param g     the g
     * @param LDGC  the ldgc
     * @param tb    the tb
     * @param filec the filec
     */
    public DrawController(GraphicsContext g, GraphicsContext LDGC, ToolBar tb, FileController filec){
        gc = g;
        ldgc = LDGC;
        toolBar = tb;
        fileController = filec;

        HBox toolbarContainer = (HBox) toolBar.getItems().get(0);

        HBox colorPickerContainer = (HBox) toolbarContainer.getChildren().get(0);
        colorPicker = (ColorPicker) colorPickerContainer.getChildren().get(1);
        colorPicker.setValue(Color.BLACK);

        HBox shapeChooserContainer = (HBox) toolbarContainer.getChildren().get(1);
        shapeChooser = (ChoiceBox<drawTool>) shapeChooserContainer.getChildren().get(1);

        selectorTool = new selectorTool(gc,ldgc);
        polygonTool = new polygonTool(gc, ldgc);
        toolsList = new drawTool[]{selectorTool, polygonTool, new textTool(gc,ldgc), new eraserTool(gc,ldgc), new freeDrawTool(gc,ldgc), new lineTool(gc,ldgc),
                new squareTool(gc,ldgc), new rectangleTool(gc,ldgc), new triangleTool(gc,ldgc), new rightTriangleTool(gc,ldgc),
                new circleTool(gc,ldgc), new ellipseTool(gc,ldgc)};
        shapeChooser.getItems().addAll(toolsList);
        shapeChooser.setValue(toolsList[1]);


        HBox dashedLineCheckContainer = (HBox) toolbarContainer.getChildren().get(2);
        dashedlineChecker = (CheckBox) dashedLineCheckContainer.getChildren().get(1);

        HBox brushWidthContainer = (HBox) toolbarContainer.getChildren().get(3);
        brushWidthChooser = (TextField) brushWidthContainer.getChildren().get(1);
        brushWidthChooser.setPrefWidth(66);
        brushWidthChooser.setText("6");


        recentlySaved = false;
        setListeners(true);

    }

    /**
     * Clears the current Screen
     */
    public void clearScreen(){
        gc.clearRect(0,0,gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
    }

    /**
     * wrapper method for when the cursor enters the canvas
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
        currentTool= shapeChooser.getValue();
        if(currentTool==polygonTool){
            polygonTool.setPolygonSides(9);
        }

        currentTool.setAttributes(
                colorPicker.getValue(),
                Double.parseDouble(brushWidthChooser.getText()),
                dashedlineChecker.isSelected(),
                recentlySaved
        );
    }

    /**
     * Get press event.
     *
     * @param e the mouse event
     */
    public void getPressEvent(javafx.scene.input.MouseEvent e){currentTool.getPressEvent(e);}

    /**
     * Get drag event.
     *
     * @param e the mouse event
     */
    public void getDragEvent(javafx.scene.input.MouseEvent e){ currentTool.getDragEvent(e);}

    /**
     * Get release event.
     *
     * @param e the mouse event
     */
    public void getReleaseEvent(javafx.scene.input.MouseEvent e){
        currentTool.getReleaseEvent(e);
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
     * @return the boolean
     */
    public boolean wasRecentlySaved() {
        recentlySaved = currentTool.wasRecentlySaved();
        return recentlySaved;
    }

    /**
     * Get clip board image.
     *
     * @return the image
     */
    public Image getClipBoard(){
        return selectorTool.getClipBoard();
    }

    /**
     * Paste.
     *
     * @param me the mouse event
     */
    public void paste(MouseEvent me) {
        selectorTool.paste(me);
    }
}