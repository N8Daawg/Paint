package com.example.paint;

import com.example.paint.Tools.*;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.Objects;

public class PaintController {
    @FXML
    private Pane pane;
    @FXML
    private MenuBar menuBar;
    @FXML
    private MenuItem openFile; //open file button
    @FXML
    private MenuItem saveFile; //save file button
    @FXML
    private MenuItem saveAsFile; //save as file button
    @FXML
    private MenuItem about; //opens the help window

    @FXML
    private ToolBar tb;
    @FXML
    private ChoiceBox<String> shapeChooser;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private TextField brushWidthChooser;
    @FXML
    private CheckBox eraserBox;

    @FXML
    private Canvas canvas;

    private boolean recentlySaved = false;
    /*---------------------------------------------------------------------------*/
    /*-------------------------------Canvas Controls-----------------------------*/
    /*---------------------------------------------------------------------------*/
    int lineSwitch = 0;

    @FXML
    private void initialize() {

        FileController fc = new FileController(menuBar, openFile, saveFile, saveAsFile, about, tb, canvas);
        //canvas.setHeight(pane.getHeight() - menuBar.getHeight() - tb.getHeight()+50);
        //canvas.setWidth(pane.getWidth());



        shapeChooser.getItems().addAll("free", "line", "rectangle");
        shapeChooser.setValue("rectangle");
        colorPicker.setValue(Color.BLACK);
        brushWidthChooser.setText("6");
        setListeners();


    }



    @FXML
    protected void setListeners() {
        if(eraserBox.isSelected()){
            drawTool tool = new eraserTool(
                    canvas.getGraphicsContext2D(),
                    brushWidthChooser,
                    colorPicker,
                    eraserBox);
            tool.setAttributes();
        } else if (Objects.equals(shapeChooser.getValue(), "free")) {
            drawTool tool = new freeDrawTool(
                    canvas.getGraphicsContext2D(),
                    brushWidthChooser,
                    colorPicker,
                    eraserBox);
            tool.setAttributes();
        } else if(Objects.equals(shapeChooser.getValue(), "line")){
            drawTool tool = new lineTool(
                    canvas.getGraphicsContext2D(),
                    brushWidthChooser,
                    colorPicker,
                    eraserBox);
            tool.setAttributes();
        } else if (Objects.equals(shapeChooser.getValue(), "rectangle")) {
            drawTool tool = new rectangleTool(
                    canvas.getGraphicsContext2D(),
                    brushWidthChooser,
                    colorPicker,
                    eraserBox);
            tool.setAttributes();
        }
    }

    /*---------------------------------------------------------------------------*/
    /*-------------------------------Drawing Controls----------------------------*/
    /*---------------------------------------------------------------------------*/

}