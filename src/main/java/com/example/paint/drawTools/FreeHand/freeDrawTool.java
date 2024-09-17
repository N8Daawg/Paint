package com.example.paint.drawTools.FreeHand;

import com.example.paint.drawTools.shapeTool;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;

public class freeDrawTool extends shapeTool {
    public freeDrawTool(GraphicsContext g) {
        super(g);
    }
    /*---------------------------------------------------------------------------*/
    /*-------------------------------Free Draw Events----------------------------*/
    /*---------------------------------------------------------------------------*/
    @Override
    protected void getPressEvent(MouseEvent e) {
        isDashedLine = false; gc.beginPath();
    }
    @Override
    protected void getDragEvent(MouseEvent e) {
        gc.setLineCap(StrokeLineCap.ROUND);
        gc.lineTo(e.getX(), e.getY());
        gc.stroke();
    }
    @Override
    protected void getReleaseEvent(MouseEvent e) {
        gc.lineTo(e.getX(), e.getY());
        gc.stroke();
        gc.closePath();
        recentlySaved=false;
    }

    @Override
    public String toString() {
        return "Free Draw";
    }
}
