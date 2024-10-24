package paint.drawTools.ShapeTools.ManySides;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

public class starTool extends polygonTool {
    /**
     * Instantiates a new Shape tool.
     *
     * @param g    the g
     * @param LDGC the ldgc
     */
    public starTool(GraphicsContext g, GraphicsContext LDGC) {
        super(g, LDGC);
    }
    public void setStarSides(int Sides){ setPolygonSides(Sides);}

    private double[][] generateStarArray(double R){
        double[][] innercoors = generatePolyArray(R/4);
        double[][] outercoors = generatePolyArray(R/8);

        double[][] xy = new double[2][sides*2];

        for (int i = 0; i < (sides*2); i+=2) {
            xy[0][i] = innercoors[0][i/2];
            xy[0][i+1] = outercoors[0][(i/2)];
            xy[1][i] = innercoors[1][i/2];
            xy[1][i+1] = outercoors[1][(i/2)];
        }

        return xy;
    }

    /**
     * Draw polygon.
     *
     * @param currentgc the currentgc
     * @param xcoors    the xcoors
     * @param ycoors    the ycoors
     */
    protected void drawStar(GraphicsContext currentgc, double[] xcoors, double[] ycoors){
        currentgc.strokePolygon(xcoors, ycoors, sides*2);
    }

    /**
     * Live draw polygon.
     *
     * @param xcoors the xcoors
     * @param ycoors the ycoors
     */
    protected void liveDrawStar(double[] xcoors, double[] ycoors){
        clearCanvas(ldgc);
        drawStar(ldgc, xcoors, ycoors);
    }

    @Override
    public void getDragEvent(MouseEvent mouseEvent) {
        double R = Math.abs((anchorX - mouseEvent.getX())/2);
        double[][] xy = generateStarArray(R);
        liveDrawStar(xy[0],xy[1]);
    }

    @Override
    public void getReleaseEvent(MouseEvent mouseEvent) {
        clearCanvas(ldgc);
        double R = Math.abs((anchorX - mouseEvent.getX())/2);
        double[][] xy = generateStarArray(R);
        drawStar(gc, xy[0],xy[1]);
    }

    @Override
    public String toString() {
        return "Star of sides " + sides;
    }
}
