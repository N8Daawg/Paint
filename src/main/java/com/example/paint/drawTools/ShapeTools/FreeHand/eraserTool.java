package com.example.paint.drawTools.ShapeTools.FreeHand;

import com.example.paint.drawTools.drawTool;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * The type Eraser tool.
 */
public class eraserTool extends drawTool {
    /**
     * Instantiates a new Eraser tool.
     *
     * @param g    the g
     * @param LDGC the ldgc
     */
    public eraserTool(GraphicsContext g, GraphicsContext LDGC) {
        super(g, LDGC);
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
    public void setAttributes(Color color, double size, Boolean dottedLine, Boolean recentlySaved) {
        setup(color, size, dottedLine, recentlySaved);
    }

    /*---------------------------------------------------------------------------*/
    /*-------------------------------Free Draw Events----------------------------*/
    /*---------------------------------------------------------------------------*/
    @Override
    public void getPressEvent(MouseEvent e) {

    }
    @Override
    public void getDragEvent(MouseEvent e) {
        anchorX = e.getX()-brushSize/2;
        anchorY = e.getY()-brushSize/2;

        gc.clearRect(anchorX,anchorY,brushSize,brushSize);

    }
    @Override
    public void getReleaseEvent(MouseEvent e) {
        recentlySaved = false;
    }

    @Override
    public String toString() {
        return "Eraser";
    }
}
