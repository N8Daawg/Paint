package com.example.paint.Tools;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class rectangleTool extends drawTool{
    public rectangleTool(GraphicsContext g, TextField BrushWidthChooser, ColorPicker colorPicker1, CheckBox eraser) {
        super(g, BrushWidthChooser, colorPicker1, eraser);
    }

    @Override
    public void setAttributes() {
        setup();

        gc.setStroke(currentColor);
        gc.setLineWidth(size);
    }

    @Override
    protected void getPressEvent(MouseEvent e) {
        anchorX = e.getX();
        anchorY = e.getY();

        //gc.moveTo(50, 50);
    }

    @Override
    protected void getDragEvent(MouseEvent e) throws InterruptedException {
        for (double i = anchorX-size; i < anchorX + e.getX()+size*2; i++) {
            gc.clearRect(i, anchorY-size/2, size, size);
            gc.clearRect(i, e.getY()+size/2, size, size);
        }
        for (double i = anchorY-size; i < anchorY + e.getY()+size*2; i++) {
            gc.clearRect(anchorX-size/2, i, size, size);
            gc.clearRect(e.getX()+size/2, i, size, size);
        }

        //wait(100);

        gc.beginPath();
        gc.rect(anchorX,anchorY,e.getX()-anchorX,e.getY()-anchorY);
        gc.closePath();
        gc.stroke();
    }

    @Override
    protected void getReleaseEvent(MouseEvent e) {

    }
}
