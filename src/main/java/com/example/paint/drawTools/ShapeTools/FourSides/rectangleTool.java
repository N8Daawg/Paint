package com.example.paint.drawTools.ShapeTools.FourSides;

import com.example.paint.drawTools.ShapeTools.shapeTool;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

public class rectangleTool extends shapeTool {
    public rectangleTool(GraphicsContext g, GraphicsContext LDGC) {
        super(g, LDGC);
    }

    protected void drawRectangle(GraphicsContext currentgc, double[] xcoors, double[] ycoors){
        recentlySaved=true;
        if(isDashedLine){
            double perimeter = (
                    (Math.abs(xcoors[2]-xcoors[1])*2)+(Math.abs(ycoors[1]-ycoors[0])*2)
                    );
            currentgc.setLineDashes(createLineDashes(perimeter));
        }
        currentgc.strokePolygon(
                xcoors,
                ycoors,
                4
        );
    }
    protected void liveDrawRectangle(double[] xcoors, double[] ycoors){
        clearCanvas(ldgc);
        drawRectangle(ldgc, xcoors, ycoors);
    }

    /*---------------------------------------------------------------------------*/
    /*-------------------------------Drawing Controls----------------------------*/
    /*---------------------------------------------------------------------------*/

    @Override
    public void getPressEvent(MouseEvent e) {
        anchorX = e.getX();
        anchorY = e.getY();

    }

    @Override
    public void getDragEvent(MouseEvent e) {
        liveDrawRectangle(
                new double[]{anchorX, anchorX, e.getX(), e.getX()},
                new double[]{anchorY, e.getY(), e.getY(), anchorY}
        );
    }

    @Override
    public void getReleaseEvent(MouseEvent e) {
        drawRectangle(
                gc,
                new double[]{anchorX, anchorX, e.getX(), e.getX()},
                new double[]{anchorY, e.getY(), e.getY(), anchorY}
        );
        clearCanvas(ldgc);
    }

    @Override
    public String toString() {
        return "Rectangle";
    }
}