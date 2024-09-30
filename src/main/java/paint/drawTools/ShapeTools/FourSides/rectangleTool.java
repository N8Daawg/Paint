package paint.drawTools.ShapeTools.FourSides;

import paint.drawTools.ShapeTools.shapeTool;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

/**
 * The type Rectangle tool.
 */
public class rectangleTool extends shapeTool {
    /**
     * Instantiates a new Rectangle tool.
     *
     * @param g    the g
     * @param LDGC the ldgc
     */
    public rectangleTool(GraphicsContext g, GraphicsContext LDGC) {
        super(g, LDGC);
    }

    /**
     * Draw rectangle.
     *
     * @param currentgc the currentgc
     * @param xcoors    the xcoors
     * @param ycoors    the ycoors
     */
    protected void drawRectangle(GraphicsContext currentgc, double[] xcoors, double[] ycoors){
        recentlySaved=true;
        if(isDashedLine){
            double perimeter = (
                    (Math.abs(xcoors[2]-xcoors[1])*2)+(Math.abs(ycoors[1]-ycoors[0])*2)
                    );
            currentgc.setLineDashes(createLineDashes(perimeter));
        }
        currentgc.strokePolygon(
                xcoors,
                ycoors,
                4
        );
    }

    /**
     * Live draw rectangle.
     *
     * @param xcoors the xcoors
     * @param ycoors the ycoors
     */
    protected void liveDrawRectangle(double[] xcoors, double[] ycoors){
        clearCanvas(ldgc);
        drawRectangle(ldgc, xcoors, ycoors);
    }

    /*---------------------------------------------------------------------------*/
    /*-------------------------------Drawing Controls----------------------------*/
    /*---------------------------------------------------------------------------*/

    @Override
    public void getPressEvent(MouseEvent mouseEvent) {
        anchorX = mouseEvent.getX();
        anchorY = mouseEvent.getY();

    }

    @Override
    public void getDragEvent(MouseEvent mouseEvent) {
        liveDrawRectangle(
                new double[]{anchorX, anchorX, mouseEvent.getX(), mouseEvent.getX()},
                new double[]{anchorY, mouseEvent.getY(), mouseEvent.getY(), anchorY}
        );
    }

    @Override
    public void getReleaseEvent(MouseEvent mouseEvent) {
        drawRectangle(
                gc,
                new double[]{anchorX, anchorX, mouseEvent.getX(), mouseEvent.getX()},
                new double[]{anchorY, mouseEvent.getY(), mouseEvent.getY(), anchorY}
        );
        clearCanvas(ldgc);
    }

    @Override
    public String toString() {
        return "Rectangle";
    }
}
