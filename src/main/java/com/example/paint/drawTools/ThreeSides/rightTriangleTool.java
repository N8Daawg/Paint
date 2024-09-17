package com.example.paint.drawTools.ThreeSides;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

public class rightTriangleTool extends triangleTool{

    public rightTriangleTool(GraphicsContext g) {
        super(g);
    }

    @Override
    protected void getReleaseEvent(MouseEvent e) {
        drawTriangle(
                new double[]{anchorX, e.getX(), anchorX},
                new double[]{anchorY, anchorY, e.getY()}
        );
    }

    @Override
    public String toString() {
        return "Right Triangle";
    }
}
