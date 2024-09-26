package paint.drawTools.ShapeTools.ZeroSides;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

/**
 * The type Circle tool.
 */
public class circleTool extends ellipseTool {
    /**
     * Instantiates a new Circle tool.
     *
     * @param g    the g
     * @param LDGC the ldgc
     */
    public circleTool(GraphicsContext g, GraphicsContext LDGC) {
        super(g, LDGC);
    }

    @Override
    public void getDragEvent(MouseEvent e) {
        if (e.getX() < e.getY()) {
            liveDrawEllipse( anchorX, anchorY, e.getX(), anchorY+(e.getX()-anchorX));
        } else {
            liveDrawEllipse( anchorX, anchorY,anchorX+(e.getY()-anchorY), e.getY());
        }
    }
    @Override
    public void getReleaseEvent(MouseEvent e) {
        clearCanvas(ldgc);
        if (e.getX() < e.getY()) {
            drawEllipse(gc, anchorX, anchorY, e.getX(), anchorY+(e.getX()-anchorX));
        } else {
            drawEllipse(gc, anchorX, anchorY,anchorX+(e.getY()-anchorY), e.getY());
        }
    }

    @Override
    public String toString() {
        return "Circle";
    }
}
