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
    public void getDragEvent(MouseEvent mouseEvent) {
        if (mouseEvent.getX() < mouseEvent.getY()) {
            liveDrawEllipse( anchorX, anchorY, mouseEvent.getX(), anchorY+(mouseEvent.getX()-anchorX));
        } else {
            liveDrawEllipse( anchorX, anchorY,anchorX+(mouseEvent.getY()-anchorY), mouseEvent.getY());
        }
    }
    @Override
    public void getReleaseEvent(MouseEvent mouseEvent) {
        clearCanvas(ldgc);
        if (mouseEvent.getX() < mouseEvent.getY()) {
            drawEllipse(gc, anchorX, anchorY, mouseEvent.getX(), anchorY+(mouseEvent.getX()-anchorX));
        } else {
            drawEllipse(gc, anchorX, anchorY,anchorX+(mouseEvent.getY()-anchorY), mouseEvent.getY());
        }
    }

    @Override
    public String toString() {
        return "Circle";
    }
}
