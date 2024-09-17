package com.example.paint.drawTools;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class drawTool {
    protected GraphicsContext gc;
    protected Color currentColor;
    protected double brushSize;
    protected double anchorX;
    protected double anchorY;
    protected boolean isDashedLine;
    protected boolean recentlySaved;

    public drawTool(GraphicsContext g){
        gc = g;
        currentColor = Color.BLACK;
        brushSize = 6;
        recentlySaved=true;
    }
    protected void setup(Color color, double size, Boolean dashedLine){
        setColor(color);
        setSize(size);
        setDashedLine(dashedLine);

        gc.getCanvas().setOnMousePressed(e -> getPressEvent(e));
        gc.getCanvas().setOnMouseDragged(e -> getDragEvent(e));
        gc.getCanvas().setOnMouseReleased(e -> getReleaseEvent(e));
    }

    /*---------------------------------------------------------------------------*/
    /*-------------------------------Drawing Controls----------------------------*/
    /*---------------------------------------------------------------------------*/

    protected abstract void setColor(Color color);
    protected abstract void setSize(double size);
    protected abstract void setDashedLine(Boolean dashedLine);
    public abstract void setAttributes(Color color, double size, Boolean dashedLine);

    /*---------------------------------------------------------------------------*/
    /*-------------------------------Abstract Events-----------------------------*/
    /*---------------------------------------------------------------------------*/

    protected abstract void getPressEvent(javafx.scene.input.MouseEvent e);
    protected abstract void getDragEvent(javafx.scene.input.MouseEvent e);
    protected abstract void getReleaseEvent(javafx.scene.input.MouseEvent e);
    public boolean wasRecentlySaved() {
        return recentlySaved;
    }
    public void setRecentlyModified(Boolean wasRecentlySaved){ recentlySaved = wasRecentlySaved;}
    public String toString(){
        return "Tool";
    }
}
