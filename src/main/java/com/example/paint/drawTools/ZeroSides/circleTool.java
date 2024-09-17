package com.example.paint.drawTools.ZeroSides;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

public class circleTool extends ellipseTool {
    public circleTool(GraphicsContext g) {
        super(g);
    }

    @Override
    protected void getReleaseEvent(MouseEvent e) {
        if (e.getX() < e.getY()) {
            drawEllipse(anchorX, anchorY, e.getX(), anchorY+(e.getX()-anchorX));
        } else {
            drawEllipse(anchorX, anchorY,anchorX+(e.getY()-anchorY), e.getY());
        }
    }

    @Override
    public String toString() {
        return "Circle";
    }
}
