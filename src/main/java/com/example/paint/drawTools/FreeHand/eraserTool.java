package com.example.paint.drawTools.FreeHand;

import com.example.paint.drawTools.drawTool;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class eraserTool extends drawTool {
    public eraserTool(GraphicsContext g) {
        super(g);
    }

    @Override
    protected void setColor(Color color) {}
    @Override
    protected void setSize(double size) {
        brushSize = size;
    }
    @Override
    protected void setDashedLine(Boolean dashedLine) {
        isDashedLine = dashedLine;
    }
    @Override
    public void setAttributes(Color color, double size, Boolean dottedLine) {
        setup(color, size, dottedLine);
    }

    /*---------------------------------------------------------------------------*/
    /*-------------------------------Free Draw Events----------------------------*/
    /*---------------------------------------------------------------------------*/
    @Override
    protected void getPressEvent(MouseEvent e) {

    }
    @Override
    protected void getDragEvent(MouseEvent e) {
        anchorX = e.getX()-brushSize/2;
        anchorY = e.getY()-brushSize/2;

        gc.clearRect(anchorX,anchorY,brushSize,brushSize);

    }
    @Override
    protected void getReleaseEvent(MouseEvent e) {
        recentlySaved = false;
    }

    @Override
    public String toString() {
        return "Eraser";
    }
}
