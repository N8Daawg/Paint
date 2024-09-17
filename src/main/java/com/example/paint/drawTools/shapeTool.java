package com.example.paint.drawTools;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class shapeTool extends drawTool{
    protected static final int numOfDashes = 6;
    public shapeTool(GraphicsContext g) {
        super(g);
    }
    @Override
    public void setAttributes(Color color, double size, Boolean dashedLine) {
        setup(color, size, dashedLine);
    }

    protected void setColor(Color color){
        currentColor = color;
        gc.setStroke(color);
    }
    protected void setSize(double size){
        brushSize = size;
        gc.setLineWidth(size);
    }
    protected void setDashedLine(Boolean dashedLine){
        isDashedLine = dashedLine;
    }
    protected double[] createLineDashes(double perimeter){
        double[] dashes = new double[(numOfDashes*2)-1];
        for (int i=0; i<(numOfDashes*2)-1;i+=2) {
            dashes[i] = perimeter/(numOfDashes+1);
            if(i+1 < dashes.length) {
                dashes[i + 1] = perimeter/((numOfDashes+1)*(numOfDashes-1));
            }
        }
        return dashes;
    }

    @Override
    protected void getPressEvent(MouseEvent e) {

    }

    @Override
    protected void getDragEvent(MouseEvent e) {

    }

    @Override
    protected void getReleaseEvent(MouseEvent e) {

    }

    @Override
    public String toString() {
        return "Shape";
    }
}
