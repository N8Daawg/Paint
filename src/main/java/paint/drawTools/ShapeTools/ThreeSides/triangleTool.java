package paint.drawTools.ShapeTools.ThreeSides;

import paint.drawTools.ShapeTools.shapeTool;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

/**
 * The type Triangle tool.
 */
public class triangleTool extends shapeTool {
    /**
     * Instantiates a new Triangle tool.
     *
     * @param g    the g
     * @param LDGC the ldgc
     */
    public triangleTool(GraphicsContext g, GraphicsContext LDGC) {
        super(g, LDGC);
    }

    /**
     * Draw triangle.
     *
     * @param currentgc the currentgc
     * @param xcoors    the xcoors
     * @param ycoors    the ycoors
     */
    protected void drawTriangle(GraphicsContext currentgc, double[] xcoors, double[] ycoors){
        recentlySaved = false;
        if(isDashedLine){
            double perimeter = (
                    Math.sqrt(Math.pow((xcoors[1]-xcoors[0]) ,2)+Math.pow(ycoors[1]-ycoors[0],2)) +
                    Math.sqrt(Math.pow((xcoors[2]-xcoors[1]) ,2)+Math.pow(ycoors[2]-ycoors[1],2)) +
                    Math.sqrt(Math.pow((xcoors[2]-xcoors[0]) ,2)+Math.pow(ycoors[2]-ycoors[0],2))
                    );
            currentgc.setLineDashes(createLineDashes(perimeter));
        }
        currentgc.strokePolygon(
                xcoors,
                ycoors,
                3);
    }

    /**
     * Live draw triangle.
     *
     * @param xcoors the xcoors
     * @param ycoors the ycoors
     */
    protected void liveDrawTriangle(double[] xcoors, double[] ycoors){
        clearCanvas(ldgc);
        drawTriangle(ldgc, xcoors, ycoors);
    }

    @Override
    public void getPressEvent(MouseEvent e) {
        anchorX = e.getX();
        anchorY = e.getY();

    }

    @Override
    public void getDragEvent(MouseEvent e) {
        liveDrawTriangle(
                new double[]{anchorX, anchorX+((e.getX()-anchorX)/2), e.getX()},
                new double[]{anchorY, e.getY(), anchorY}
        );
    }

    @Override
    public void getReleaseEvent(MouseEvent e) {
        recentlySaved=true;
        clearCanvas(ldgc);
        drawTriangle(gc,
                new double[]{anchorX, anchorX+((e.getX()-anchorX)/2), e.getX()},
                new double[]{anchorY, e.getY(), anchorY}
        );
        clearCanvas(ldgc);
    }

    @Override
    public String toString() {
        return "Triangle";
    }
}
