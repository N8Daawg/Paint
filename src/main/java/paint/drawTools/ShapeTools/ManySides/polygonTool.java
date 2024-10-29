package paint.drawTools.ShapeTools.ManySides;

import paint.drawTools.ShapeTools.shapeTool;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

/**
 * The type Polygon tool.
 */
public class polygonTool extends shapeTool {
    /**
     * The Sides.
     */
    int sides;

    /**
     * Instantiates a new Polygon tool.
     *
     * @param g    the Graphics Context
     * @param LDGC the Live Draw Graphics Context
     */
    public polygonTool(GraphicsContext g, GraphicsContext LDGC) {
        super(g, LDGC);
    }

    /**
     * Set polygon sides.
     *
     * @param Sides the sides
     */
    public void setPolygonSides(int Sides){
        sides = Sides;
    }

    protected double[][] generatePolyArray(double R){
        double[] x = new double[sides];
        double[] y = new double[sides];

        double outerTheta = ((double) (180*(sides-2))/sides)*(Math.PI/180);
        double innerTheta = (90 - (outerTheta)/2)*(Math.PI/180);


        x[0] = R + (Math.pow(R,2)*Math.sin(innerTheta));
        y[0] = R + (Math.pow(R,2)*Math.cos(innerTheta));

        for(int i=1; i<sides; i++){
            x[i] = R + (Math.pow(R,2)*Math.sin(innerTheta+(i*(Math.PI-outerTheta))));
            y[i] = R + (Math.pow(R,2)*Math.cos(innerTheta+(i*(Math.PI-outerTheta))));
        }

        for(int i=0; i<x.length;i++){
            x[i] +=anchorX;
        }
        for(int i=0; i<y.length;i++){
            y[i] +=anchorY;
        }
        return new double[][]{x,y};
    }

    /**
     * <p>
     *     NEEDS TO BE UPDATED TO USE LINEAR GROWTH AND NOT EXPONENTIAL
     * </p>
     * <p>
     *     NEEDS TO HAVE BOUNDS UPDATED
     * </p>
     * Draw polygon.
     *
     * @param currentgc the currentgc
     * @param xcoors    the xcoors
     * @param ycoors    the ycoors
     */
    protected void drawPolygon(GraphicsContext currentgc, double[] xcoors, double[] ycoors){
        if(isDashedLine){
            double perimeter = (
                    (Math.abs(xcoors[2]-xcoors[1])*2)+(Math.abs(ycoors[1]-ycoors[0])*2)
            );
            currentgc.setLineDashes(createLineDashes(perimeter));
        }
        currentgc.strokePolygon(
                xcoors,
                ycoors,
                sides
        );
    }

    /**
     * Live draw polygon.
     *
     * @param xcoors the xcoors
     * @param ycoors the ycoors
     */
    protected void liveDrawPolygon(double[] xcoors, double[] ycoors){
        clearCanvas(ldgc);
        drawPolygon(ldgc, xcoors, ycoors);
    }

    @Override
    public void getPressEvent(MouseEvent mouseEvent) {
        anchorX = mouseEvent.getX();
        anchorY = mouseEvent.getY();
    }

    @Override
    public void getDragEvent(MouseEvent mouseEvent) {
        double deltaX = mouseEvent.getX() - anchorX;
        double R = Math.abs(deltaX)/2;
        double[][] xy = generatePolyArray(R);
        liveDrawPolygon(xy[0],xy[1]);
    }

    @Override
    public void getReleaseEvent(MouseEvent mouseEvent) {
        clearCanvas(ldgc);

        double deltaX = mouseEvent.getX() - anchorX;
        double R = Math.abs(deltaX)/2;
        double[][] xy = generatePolyArray(R);
        drawPolygon(gc, xy[0],xy[1]);
    }

    @Override
    public String toString() {
        return "Polygon of sides " + sides;
    }
}
