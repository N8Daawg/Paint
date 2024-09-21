package com.example.paint.drawTools.ShapeTools.ZeroSides;

import com.example.paint.drawTools.ShapeTools.shapeTool;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

public class ellipseTool extends shapeTool {
    public ellipseTool(GraphicsContext g, GraphicsContext LDGC) {
        super(g, LDGC);
    }
    protected void drawEllipse(GraphicsContext currentgc, double anchorX, double anchorY, double finalX, double finalY){
        if(isDashedLine){
            double perimeter = Math.PI*Math.sqrt(2*(Math.pow((finalY-anchorY)/2,2)+Math.pow((finalX-anchorX)/2,2)));
            currentgc.setLineDashes(createLineDashes(perimeter));
        }


        if(finalX >= anchorX && finalY >= anchorY){
            currentgc.strokeOval(anchorX, anchorY, finalX-anchorX, finalY-anchorY);
        } else if (finalX >= anchorX && finalY <= anchorY) {
            currentgc.strokeOval(anchorX, anchorY, finalX-anchorX, finalY+anchorY);
        } else if (finalX <= anchorX && finalY >= anchorY) {
            currentgc.strokeOval(anchorX, anchorY, finalX+anchorX, finalY-anchorY);
        } else {
            currentgc.strokeOval(anchorX, anchorY, finalX-anchorX, finalY-anchorY);
        }
    }

    protected void liveDrawEllipse(double anchorX, double anchorY, double finalX, double finalY){
        clearCanvas(ldgc);
        drawEllipse(ldgc, anchorX, anchorY, finalX, finalY);
    }
    @Override
    public void getPressEvent(MouseEvent e) {
        anchorX = e.getX();
        anchorY = e.getY();
    }

    @Override
    public void getDragEvent(MouseEvent e) {
        liveDrawEllipse(anchorX, anchorY, e.getX(), e.getY());

    }

    @Override
    public void getReleaseEvent(MouseEvent e) {
        clearCanvas(ldgc);
        drawEllipse(gc, anchorX, anchorY, e.getX(), e.getY()
        );
    }

    @Override
    public String toString() {
        return "Ellipse";
    }
}
