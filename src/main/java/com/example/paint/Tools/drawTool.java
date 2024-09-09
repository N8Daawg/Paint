package com.example.paint.Tools;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.awt.*;

public abstract class drawTool {
    protected GraphicsContext gc;
    protected Color currentColor;
    protected double size;
    private ColorPicker colorPicker;
    private TextField brushWidthChooser;

    protected double anchorX;
    protected double anchorY;

    public drawTool(GraphicsContext g, TextField BrushWidthChooser, ColorPicker colorPicker1, CheckBox eraser){
        gc = g;
        brushWidthChooser = BrushWidthChooser;
        colorPicker = colorPicker1;
        setup();
    }
    protected void setup(){
        setColor();
        setSize();

        gc.getCanvas().setOnMousePressed(e -> getPressEvent(e));
        gc.getCanvas().setOnMouseDragged(e -> {
            try {
                getDragEvent(e);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
        gc.getCanvas().setOnMouseReleased(e -> getReleaseEvent(e));
    }

    /*---------------------------------------------------------------------------*/
    /*-------------------------------Drawing Controls----------------------------*/
    /*---------------------------------------------------------------------------*/

    private void setColor(){
        currentColor = colorPicker.getValue();
    }
    private void setSize(){
        size = Double.parseDouble(brushWidthChooser.getText());
    }
    public abstract void setAttributes();

    /*---------------------------------------------------------------------------*/
    /*-------------------------------Abstract Events-----------------------------*/
    /*---------------------------------------------------------------------------*/

    protected abstract void getPressEvent(javafx.scene.input.MouseEvent e);
    protected abstract void getDragEvent(javafx.scene.input.MouseEvent e) throws InterruptedException;
    protected abstract void getReleaseEvent(javafx.scene.input.MouseEvent e);

}
