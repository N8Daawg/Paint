package com.example.paint.drawTools.ShapeTools.OneSide;

import com.example.paint.drawTools.ShapeTools.shapeTool;
import javafx.scene.canvas.GraphicsContext;

public class lineTool extends shapeTool {
    public lineTool(GraphicsContext g, GraphicsContext LDGC) {
        super(g, LDGC);
    }
    private void drawLine(GraphicsContext currentgc, double initialX, double initialY, double finalX, double finalY){
        recentlySaved = false;
        if(isDashedLine){
            double perimeter = Math.sqrt(Math.pow(finalX - initialX, 2) + Math.pow((finalY-initialY), 2));
            currentgc.setLineDashes(createLineDashes(perimeter));
        }

        currentgc.beginPath();
        currentgc.lineTo(initialX, initialY);
        currentgc.lineTo(finalX, finalY);
        currentgc.stroke();
        currentgc.closePath();
    }

    private void liveDrawLine(double initialX, double initialY, double finalX, double finalY){
        clearCanvas(ldgc);
        drawLine(ldgc, initialX, initialY, finalX, finalY);
    }

    /*---------------------------------------------------------------------------*/
    /*-----------------------------------Line Events-----------------------------*/
    /*---------------------------------------------------------------------------*/
    @Override
    public void getPressEvent(javafx.scene.input.MouseEvent e) {
        anchorX = e.getX();
        anchorY = e.getY();
    }

    @Override
    public void getDragEvent(javafx.scene.input.MouseEvent e) {
        liveDrawLine(anchorX, anchorY, e.getX(), e.getY());
    }

    @Override
    public void getReleaseEvent(javafx.scene.input.MouseEvent e) {
        recentlySaved = false;
        clearCanvas(ldgc);
        drawLine(gc, anchorX, anchorY, e.getX(), e.getY());
    }

    @Override
    public String toString() {
        return "Line";
    }
}
