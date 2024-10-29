package paint.drawTools.ShapeTools.ThreeSides;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

/**
 * The type Right triangle tool.
 */
public class rightTriangleTool extends triangleTool{

    /**
     * Instantiates a new Right triangle tool.
     *
     * @param g    the Graphics Context
     * @param LDGC the Live Draw Graphics Context
     */
    public rightTriangleTool(GraphicsContext g, GraphicsContext LDGC) {
        super(g, LDGC);
    }


    @Override
    public void getDragEvent(MouseEvent mouseEvent) {
        liveDrawTriangle(
                new double[]{anchorX, mouseEvent.getX(), anchorX},
                new double[]{anchorY, anchorY, mouseEvent.getY()}
        );
    }

    @Override
    public void getReleaseEvent(MouseEvent mouseEvent) {
        clearCanvas(ldgc);
        drawTriangle(gc,
                new double[]{anchorX, mouseEvent.getX(), anchorX},
                new double[]{anchorY, anchorY, mouseEvent.getY()}
        );
    }

    @Override
    public String toString() {
        return "Right Triangle";
    }
}
