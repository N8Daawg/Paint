package paint.drawTools.ShapeTools.FreeHand;

import paint.drawTools.drawTool;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * The type Eraser tool.
 */
public class eraserTool extends drawTool {
    /**
     * Instantiates a new Eraser tool.
     *
     * @param g    the g
     * @param LDGC the ldgc
     */
    public eraserTool(GraphicsContext g, GraphicsContext LDGC) {
        super(g, LDGC);
    }

    @Override
    protected void setColor(Color color) {}
    @Override
    protected void setSize(double size) {
        brushSize = size;
    }
    @Override
    protected void setDashedLine(Boolean dashedLine) {}

    /*---------------------------------------------------------------------------*/
    /*-------------------------------Free Draw Events----------------------------*/
    /*---------------------------------------------------------------------------*/
    @Override
    public void getPressEvent(MouseEvent mouseEvent) {

    }
    @Override
    public void getDragEvent(MouseEvent mouseEvent) {
        anchorX = mouseEvent.getX()-brushSize/2;
        anchorY = mouseEvent.getY()-brushSize/2;

        gc.clearRect(anchorX,anchorY,brushSize,brushSize);

    }
    @Override
    public void getReleaseEvent(MouseEvent mouseEvent) {}

    @Override
    public String toString() {
        return "Eraser";
    }
}
