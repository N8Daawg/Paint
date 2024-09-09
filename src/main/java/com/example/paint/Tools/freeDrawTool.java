package com.example.paint.Tools;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class freeDrawTool extends drawTool{
    public freeDrawTool(GraphicsContext g, TextField BrushWidthChooser, ColorPicker colorPicker1, CheckBox eraser) {
        super(g, BrushWidthChooser, colorPicker1, eraser);
    }
    @Override
    public void setAttributes() {
        setup();

        gc.setFill(currentColor);
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

        gc.fillRect(anchorX,anchorY,size,size);

    }
    @Override
    protected void getReleaseEvent(MouseEvent e) {

    }
}
