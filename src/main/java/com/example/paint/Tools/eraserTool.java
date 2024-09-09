package com.example.paint.Tools;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class eraserTool extends drawTool{
    public eraserTool(GraphicsContext g, TextField BrushWidthChooser, ColorPicker colorPicker1, CheckBox eraser) {
        super(g, BrushWidthChooser, colorPicker1, eraser);
    }

    @Override
    public void setAttributes() {
        setup();
    }

    /*---------------------------------------------------------------------------*/
    /*-------------------------------Free Draw Events----------------------------*/
    /*---------------------------------------------------------------------------*/
    @Override
    protected void getPressEvent(MouseEvent e) {

    }
    @Override
    protected void getDragEvent(MouseEvent e) {
        anchorX = e.getX()-size/2;
        anchorY = e.getY()-size/2;

        gc.clearRect(anchorX,anchorY,size,size);

    }
    @Override
    protected void getReleaseEvent(MouseEvent e) {

    }
}
