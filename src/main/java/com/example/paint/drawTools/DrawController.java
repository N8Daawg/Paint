package com.example.paint.drawTools;

import com.example.paint.FileController;
import com.example.paint.drawTools.MiscTools.selectorTool;
import com.example.paint.drawTools.MiscTools.textTool;
import com.example.paint.drawTools.ShapeTools.FourSides.rectangleTool;
import com.example.paint.drawTools.ShapeTools.FourSides.squareTool;
import com.example.paint.drawTools.ShapeTools.FreeHand.eraserTool;
import com.example.paint.drawTools.ShapeTools.FreeHand.freeDrawTool;
import com.example.paint.drawTools.ShapeTools.ManySides.polygonTool;
import com.example.paint.drawTools.ShapeTools.OneSide.lineTool;
import com.example.paint.drawTools.ShapeTools.ThreeSides.rightTriangleTool;
import com.example.paint.drawTools.ShapeTools.ThreeSides.triangleTool;
import com.example.paint.drawTools.ShapeTools.ZeroSides.circleTool;
import com.example.paint.drawTools.ShapeTools.ZeroSides.ellipseTool;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class DrawController {
    protected GraphicsContext gc;
    protected GraphicsContext ldgc;
    private ToolBar toolBar;
    private ColorPicker colorPicker;
    private ChoiceBox<drawTool> shapeChooser;
    private CheckBox dashedlineChecker;
    private TextField brushWidthChooser;
    private drawTool currentTool;
    private drawTool[] toolsList;
    private FileController fileController;
    private boolean recentlySaved;
    selectorTool selectorTool;
    polygonTool polygonTool;
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
     * @param recentlySaved
     */
    public void setListeners(Boolean recentlySaved){
        setCurrentTool(recentlySaved);
    }

    /**
     *
     * @param recentlySaved
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
    public void getPressEvent(javafx.scene.input.MouseEvent e){currentTool.getPressEvent(e);}
    public void getDragEvent(javafx.scene.input.MouseEvent e){ currentTool.getDragEvent(e);}
    public void getReleaseEvent(javafx.scene.input.MouseEvent e){
        currentTool.getReleaseEvent(e);
    }
    public void setRecentlySaved() {
        recentlySaved = fileController.wasRecentlySaved();
        currentTool.setRecentlySaved(recentlySaved);
    }
    public boolean wasRecentlySaved() {
        recentlySaved = currentTool.wasRecentlySaved();
        return recentlySaved;
    }

    public Image getClipBoard(){
        return selectorTool.getClipBoard();
    }
    public void paste(MouseEvent me) {
        selectorTool.paste(me);
    }
}