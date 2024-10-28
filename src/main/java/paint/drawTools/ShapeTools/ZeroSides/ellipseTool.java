package paint.drawTools.ShapeTools.ZeroSides;

import paint.drawTools.ShapeTools.shapeTool;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

/**
 * The type Ellipse tool.
 */
public class ellipseTool extends shapeTool {
    /**
     * Instantiates a new Ellipse tool.
     *
     * @param g    the g
     * @param LDGC the ldgc
     */
    public ellipseTool(GraphicsContext g, GraphicsContext LDGC) {
        super(g, LDGC);
    }

    /**
     * Draw ellipse.
     *
     * @param currentgc the currentgc
     * @param anchorX   the anchor x
     * @param anchorY   the anchor y
     * @param finalX    the final x
     * @param finalY    the final y
     */
    protected void drawEllipse(GraphicsContext currentgc, double anchorX, double anchorY, double finalX, double finalY){
        if(isDashedLine){
            double perimeter = Math.PI*Math.sqrt(2*(Math.pow((finalY-anchorY)/2,2)+Math.pow((finalX-anchorX)/2,2)));
            currentgc.setLineDashes(createLineDashes(perimeter));
        }
        double deltaX = finalX-anchorX;
        double deltaY = finalY-anchorY;



        if ((deltaX<=0)&&(deltaY<=0)) {
            currentgc.strokeOval(anchorX+deltaX, anchorY+deltaY, -deltaX, -deltaY);
        } else if ((deltaX<=0)&&(deltaY>=0)){
            currentgc.strokeOval(anchorX+deltaX, anchorY, -deltaX, deltaY);
        } else if ((deltaX>=0)&&(deltaY<=0)){
            currentgc.strokeOval(anchorX, anchorY+deltaY, deltaX, -deltaY);
        } else {
            currentgc.strokeOval(anchorX, anchorY, deltaX, deltaY);
        }
    }

    /**
     * Live draw ellipse.
     *
     * @param anchorX the anchor x
     * @param anchorY the anchor y
     * @param finalX  the final x
     * @param finalY  the final y
     */
    protected void liveDrawEllipse(double anchorX, double anchorY, double finalX, double finalY){
        clearCanvas(ldgc);
        drawEllipse(ldgc, anchorX, anchorY, finalX, finalY);
    }
    @Override
    public void getPressEvent(MouseEvent mouseEvent) {
        anchorX = mouseEvent.getX();
        anchorY = mouseEvent.getY();
    }

    @Override
    public void getDragEvent(MouseEvent mouseEvent) {
        liveDrawEllipse(anchorX, anchorY, mouseEvent.getX(), mouseEvent.getY());

    }

    @Override
    public void getReleaseEvent(MouseEvent mouseEvent) {
        clearCanvas(ldgc);
        drawEllipse(gc, anchorX, anchorY, mouseEvent.getX(), mouseEvent.getY()
        );
    }

    @Override
    public String toString() {
        return "Ellipse";
    }
}
