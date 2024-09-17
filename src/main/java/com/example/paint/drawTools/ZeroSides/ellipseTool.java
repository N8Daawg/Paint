package com.example.paint.drawTools.ZeroSides;

import com.example.paint.drawTools.shapeTool;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

public class ellipseTool extends shapeTool {
    public ellipseTool(GraphicsContext g) {
        super(g);
    }
    protected void drawEllipse(double anchorX, double anchorY, double finalX, double finalY){
        if(isDashedLine){
            double perimeter = Math.PI*Math.sqrt(2*(Math.pow((finalY-anchorY)/2,2)+Math.pow((finalX-anchorX)/2,2)));
            gc.setLineDashes(createLineDashes(perimeter));
        }


        if(finalX >= anchorX && finalY >= anchorY){
            gc.strokeOval(anchorX, anchorY, finalX-anchorX, finalY-anchorY);
        } else if (finalX >= anchorX && finalY <= anchorY) {
            gc.strokeOval(anchorX, anchorY, finalX-anchorX, finalY+anchorY);
        } else if (finalX <= anchorX && finalY >= anchorY) {
            gc.strokeOval(anchorX, anchorY, finalX+anchorX, finalY-anchorY);
        } else {
            gc.strokeOval(anchorX, anchorY, finalX-anchorX, finalY-anchorY);
        }
    }

    @Override
    protected void getPressEvent(MouseEvent e) {
        anchorX = e.getX();
        anchorY = e.getY();
    }

    @Override
    protected void getDragEvent(MouseEvent e) {

    }

    @Override
    protected void getReleaseEvent(MouseEvent e) {
        drawEllipse(anchorX, anchorY, e.getX(), e.getY());
    }

    @Override
    public String toString() {
        return "Ellipse";
    }
}
