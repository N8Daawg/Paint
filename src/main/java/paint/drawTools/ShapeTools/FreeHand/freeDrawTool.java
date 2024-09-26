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
     * @param g    the g
     * @param LDGC the ldgc
     */
    public freeDrawTool(GraphicsContext g, GraphicsContext LDGC) {
        super(g, LDGC);
    }
    /*---------------------------------------------------------------------------*/
    /*-------------------------------Free Draw Events----------------------------*/
    /*---------------------------------------------------------------------------*/
    @Override
    public void getPressEvent(MouseEvent e) {
        isDashedLine = false; gc.beginPath();
    }
    @Override
    public void getDragEvent(MouseEvent e) {
        gc.setLineCap(StrokeLineCap.ROUND);
        gc.lineTo(e.getX(), e.getY());
        gc.stroke();
    }
    @Override
    public void getReleaseEvent(MouseEvent e) {
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
