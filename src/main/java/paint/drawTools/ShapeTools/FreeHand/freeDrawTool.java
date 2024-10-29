package paint.drawTools.ShapeTools.FreeHand;

import paint.drawTools.ShapeTools.shapeTool;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.StrokeLineCap;

/**
 * The type Free draw tool.
 */
public class freeDrawTool extends shapeTool {
    /**
     * Instantiates a new Free draw tool.
     *
     * @param g    the Graphics Context
     * @param LDGC the Live Draw Graphics Context
     */
    public freeDrawTool(GraphicsContext g, GraphicsContext LDGC) {
        super(g, LDGC);
    }
    /*---------------------------------------------------------------------------*/
    /*-------------------------------Free Draw Events----------------------------*/
    /*---------------------------------------------------------------------------*/
    @Override
    public void getPressEvent(MouseEvent mouseEvent) {
        isDashedLine = false; gc.beginPath();
    }
    @Override
    public void getDragEvent(MouseEvent mouseEvent) {
        gc.setLineCap(StrokeLineCap.ROUND);
        gc.lineTo(mouseEvent.getX(), mouseEvent.getY());
        gc.stroke();
    }
    @Override
    public void getReleaseEvent(MouseEvent mouseEvent) {
        gc.lineTo(mouseEvent.getX(), mouseEvent.getY());
        gc.stroke();
        gc.closePath();
    }

    @Override
    public String toString() {
        return "Free Draw";
    }
}
