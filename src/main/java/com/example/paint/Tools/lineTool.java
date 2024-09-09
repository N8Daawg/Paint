package com.example.paint.Tools;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;

public class lineTool extends drawTool {
    public lineTool(GraphicsContext g, TextField BrushWidthChooser, ColorPicker colorPicker1, CheckBox eraser) {
        super(g, BrushWidthChooser, colorPicker1, eraser);
    }

    @Override
    public void setAttributes() {
        setup();

        gc.setStroke(currentColor);
        gc.setLineWidth(size);
    }

    /*---------------------------------------------------------------------------*/
    /*-----------------------------------Line Events-----------------------------*/
    /*---------------------------------------------------------------------------*/

    @Override
    protected void getPressEvent(javafx.scene.input.MouseEvent e) {
        anchorX = e.getX();
        anchorY = e.getY();

        gc.beginPath();
        gc.moveTo(anchorX, anchorY);
    }
    @Override
    protected void getDragEvent(javafx.scene.input.MouseEvent e) throws InterruptedException {

    }
    @Override
    protected void getReleaseEvent(javafx.scene.input.MouseEvent e) {

        gc.moveTo(e.getX(), e.getY());
        gc.moveTo(anchorX, anchorY);
        gc.closePath();
        gc.stroke();

        //gc.strokeLine(anchorX,anchorY,e.getX(),e.getY());
    }
}
