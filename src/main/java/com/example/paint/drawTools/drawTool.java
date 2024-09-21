package com.example.paint.drawTools;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class drawTool {
    protected GraphicsContext gc;
    protected GraphicsContext ldgc;
    protected Color currentColor;
    protected double brushSize;
    protected double anchorX;
    protected double anchorY;
    protected boolean isDashedLine;
    protected boolean recentlySaved;

    public drawTool(GraphicsContext g, GraphicsContext LDGC){
        gc = g;
        ldgc = LDGC;
        currentColor = Color.BLACK;
        brushSize = 6;
        recentlySaved=true;
    }
    protected void setup(Color color, double size, Boolean dashedLine, boolean wasRecentlySaved){
        setColor(color);
        setSize(size);
        setDashedLine(dashedLine);
        setRecentlySaved(wasRecentlySaved);
    }

    /*---------------------------------------------------------------------------*/
    /*-------------------------------Drawing Controls----------------------------*/
    /*---------------------------------------------------------------------------*/

    protected abstract void setColor(Color color);
    protected abstract void setSize(double size);
    protected abstract void setDashedLine(Boolean dashedLine);
    public abstract void setAttributes(Color color, double size, Boolean dashedLine, Boolean recentlySaved);

    /*---------------------------------------------------------------------------*/
    /*-------------------------------Abstract Events-----------------------------*/
    /*---------------------------------------------------------------------------*/

    public abstract void getPressEvent(javafx.scene.input.MouseEvent e);
    public abstract void getDragEvent(javafx.scene.input.MouseEvent e);
    public abstract void getReleaseEvent(javafx.scene.input.MouseEvent e);
    public boolean wasRecentlySaved() {
        return recentlySaved;
    }
    public void setRecentlySaved(Boolean wasRecentlySaved){ recentlySaved = wasRecentlySaved;}
    public void clearCanvas(GraphicsContext graphicsContext){
        graphicsContext.clearRect(0,0,graphicsContext.getCanvas().getWidth(), graphicsContext.getCanvas().getHeight());
    }
    public String toString(){
        return "Tool";
    }
}
