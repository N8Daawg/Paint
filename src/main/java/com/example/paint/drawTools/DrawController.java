package com.example.paint.drawTools;

import com.example.paint.FileController;
import com.example.paint.drawTools.FourSides.rectangleTool;
import com.example.paint.drawTools.FourSides.squareTool;
import com.example.paint.drawTools.FreeHand.eraserTool;
import com.example.paint.drawTools.FreeHand.freeDrawTool;
import com.example.paint.drawTools.OneSide.lineTool;
import com.example.paint.drawTools.ThreeSides.rightTriangleTool;
import com.example.paint.drawTools.ThreeSides.triangleTool;
import com.example.paint.drawTools.ZeroSides.circleTool;
import com.example.paint.drawTools.ZeroSides.ellipseTool;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class DrawController {
    protected GraphicsContext gc;
    private ToolBar toolBar;
    private ColorPicker colorPicker;
    private ChoiceBox<drawTool> shapeChooser;
    private CheckBox dashedlineChecker;
    private TextField brushWidthChooser;
    private drawTool currentTool;
    private drawTool[] toolsList;

    private FileController fileController;
    private boolean recentlySaved;
    public DrawController(GraphicsContext g, ToolBar tb, FileController filec){
        gc = g;
        toolBar = tb;
        fileController = filec;

        HBox toolbarContainer = (HBox) toolBar.getItems().get(0);

        HBox colorPickerContainer = (HBox) toolbarContainer.getChildren().get(0);
        colorPicker = (ColorPicker) colorPickerContainer.getChildren().get(1);
        colorPicker.setValue(Color.BLACK);

        HBox shapeChooserContainer = (HBox) toolbarContainer.getChildren().get(1);
        shapeChooser = (ChoiceBox<drawTool>) shapeChooserContainer.getChildren().get(1);

        toolsList = new drawTool[]{new eraserTool(gc), new freeDrawTool(gc), new lineTool(gc),
                new squareTool(gc), new rectangleTool(gc), new triangleTool(gc), new rightTriangleTool(gc),
                new circleTool(gc), new ellipseTool(gc)};
        shapeChooser.getItems().addAll(toolsList);
        shapeChooser.setValue(toolsList[2]);

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
        currentTool.setAttributes(
                colorPicker.getValue(),
                Double.parseDouble(brushWidthChooser.getText()),
                dashedlineChecker.isSelected()
        );
    }
    public void setRecentlySaved() {recentlySaved = currentTool.wasRecentlySaved();}
    public boolean wasRecentlySaved() {
        setRecentlySaved();
        return recentlySaved;
    }
}
