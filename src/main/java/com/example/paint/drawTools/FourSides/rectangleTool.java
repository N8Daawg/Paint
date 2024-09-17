package com.example.paint.drawTools.FourSides;

import com.example.paint.drawTools.shapeTool;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

public class rectangleTool extends shapeTool {
    public rectangleTool(GraphicsContext g) {
        super(g);
    }

    protected void drawRectangle(double[] xcoors, double[] ycoors){
        recentlySaved=true;
        if(isDashedLine){
            double perimeter = (
                    (Math.abs(xcoors[2]-xcoors[1])*2)+(Math.abs(ycoors[1]-ycoors[0])*2)
                    );
            gc.setLineDashes(createLineDashes(perimeter));
        }
        gc.strokePolygon(
                xcoors,
                ycoors,
                4
        );
    }


    /*---------------------------------------------------------------------------*/
    /*-------------------------------Drawing Controls----------------------------*/
    /*---------------------------------------------------------------------------*/

    @Override
    protected void getPressEvent(MouseEvent e) {
        anchorX = e.getX();
        anchorY = e.getY();

    }

    @Override
    protected void getDragEvent(MouseEvent e) {
        /*
        for (double i = anchorX; i < e.getX(); i++) {
            gc.clearRect(i, anchorY, brushSize, brushSize);
            gc.clearRect(i, anchorY-brushSize, brushSize, brushSize);

            gc.clearRect(i, e.getY(), brushSize, brushSize);
            gc.clearRect(i, e.getY()-brushSize, brushSize, brushSize);
        }
        for (double i = anchorY; i < e.getY(); i++) {
            gc.clearRect(anchorX, i, brushSize, brushSize);
            gc.clearRect(anchorX-brushSize, i, brushSize, brushSize);
            gc.clearRect(e.getX(), i, brushSize, brushSize);
            gc.clearRect(e.getX()-brushSize, i, brushSize, brushSize);
        }

        drawRectangle(anchorX, anchorY, e.getX(), e.getY());
        */
    }

    @Override
    protected void getReleaseEvent(MouseEvent e) {
        drawRectangle(
                new double[]{anchorX, anchorX, e.getX(), e.getX()},
                new double[]{anchorY, e.getY(), e.getY(), anchorY}
        );
    }

    @Override
    public String toString() {
        return "Rectangle";
    }
}
