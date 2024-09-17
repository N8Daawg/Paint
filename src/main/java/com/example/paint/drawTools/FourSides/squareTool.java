package com.example.paint.drawTools.FourSides;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

import static java.lang.Math.abs;

public class squareTool extends rectangleTool {
    public squareTool(GraphicsContext g) {
        super(g);
    }
    @Override
    protected void getReleaseEvent(MouseEvent e) {
        double deltaX = (e.getX()-anchorX); double finalX;
        double deltaY = (e.getY()-anchorY); double finalY;

        if (abs(deltaX) < abs(deltaY)){
            if(deltaY<0 && !(deltaX <0)){
                deltaY = -deltaX;
            } else {
                deltaY = deltaX;
            }
        } else {
            if(deltaX<0 && !(deltaY <0)){
                deltaX = -deltaY;
            } else {
                deltaX = deltaY;
            }
        }

        finalX = anchorX+deltaX;
        finalY = anchorY+deltaY;

        drawRectangle(
                new double[]{anchorX, finalX, finalX, anchorX},
                new double[]{anchorY, anchorY, finalY, finalY}
        );
    }

    @Override
    public String toString() {
        return "Square";
    }
}
