package com.example.paint.drawTools.OneSide;

import com.example.paint.drawTools.shapeTool;
import javafx.scene.canvas.GraphicsContext;

public class lineTool extends shapeTool {
    public lineTool(GraphicsContext g) {
        super(g);
    }
    private void drawLine(double initialX, double initialY, double finalX, double finalY){
        recentlySaved = false;
        if(isDashedLine){
            double perimeter = Math.sqrt(Math.pow(finalX - initialX, 2) + Math.pow((finalY-initialY), 2));
            gc.setLineDashes(createLineDashes(perimeter));
        }

        gc.beginPath();
        gc.lineTo(initialX, initialY);
        gc.lineTo(finalX, finalY);
        gc.stroke();
        gc.closePath();
    }

    /*---------------------------------------------------------------------------*/
    /*-----------------------------------Line Events-----------------------------*/
    /*---------------------------------------------------------------------------*/
    @Override
    protected void getPressEvent(javafx.scene.input.MouseEvent e) {
        anchorX = e.getX();
        anchorY = e.getY();
    }

    @Override
    protected void getDragEvent(javafx.scene.input.MouseEvent e) {
    }

    @Override
    protected void getReleaseEvent(javafx.scene.input.MouseEvent e) {
        drawLine(anchorX, anchorY, e.getX(), e.getY());
    }

    @Override
    public String toString() {
        return "Line";
    }
}
