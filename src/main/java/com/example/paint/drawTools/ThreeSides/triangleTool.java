package com.example.paint.drawTools.ThreeSides;

import com.example.paint.drawTools.shapeTool;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

public class triangleTool extends shapeTool {
    public triangleTool(GraphicsContext g) {
        super(g);
    }
    protected void drawTriangle(double[] xcoors, double[] ycoors){
        recentlySaved = false;
        if(isDashedLine){
            double perimeter = (
                    Math.sqrt(Math.pow((xcoors[1]-xcoors[0]) ,2)+Math.pow(ycoors[1]-ycoors[0],2)) +
                    Math.sqrt(Math.pow((xcoors[2]-xcoors[1]) ,2)+Math.pow(ycoors[2]-ycoors[1],2)) +
                    Math.sqrt(Math.pow((xcoors[2]-xcoors[0]) ,2)+Math.pow(ycoors[2]-ycoors[0],2))
                    );
            gc.setLineDashes(createLineDashes(perimeter));
        }
        gc.strokePolygon(
                xcoors,
                ycoors,
                3);
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
        recentlySaved=true;
        drawTriangle(
                new double[]{anchorX, anchorX+((e.getX()-anchorX)/2), e.getX()},
                new double[]{anchorY, e.getY(), anchorY}
        );
    }

    @Override
    public String toString() {
        return "Triangle";
    }
}
