package com.example.paint.drawTools.ShapeTools.ThreeSides;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

/**
 * The type Right triangle tool.
 */
public class rightTriangleTool extends triangleTool{

    /**
     * Instantiates a new Right triangle tool.
     *
     * @param g    the g
     * @param LDGC the ldgc
     */
    public rightTriangleTool(GraphicsContext g, GraphicsContext LDGC) {
        super(g, LDGC);
    }


    @Override
    public void getDragEvent(MouseEvent e) {
        liveDrawTriangle(
                new double[]{anchorX, e.getX(), anchorX},
                new double[]{anchorY, anchorY, e.getY()}
        );
    }

    @Override
    public void getReleaseEvent(MouseEvent e) {
        clearCanvas(ldgc);
        drawTriangle(gc,
                new double[]{anchorX, e.getX(), anchorX},
                new double[]{anchorY, anchorY, e.getY()}
        );
    }

    @Override
    public String toString() {
        return "Right Triangle";
    }
}
