package paint.drawTools.ShapeTools.ManySides;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import paint.drawTools.ShapeTools.shapeTool;

public class starTool extends shapeTool {
    private int sides = 5;
    /**
     * Instantiates a new Shape tool.
     *
     * @param g    the g
     * @param LDGC the ldgc
     */
    public starTool(GraphicsContext g, GraphicsContext LDGC) {
        super(g, LDGC);
    }
    public void setStarSides(int Sides){
        sides = Sides;
    }

    private double[][] generateShapeArrays(double initialX, double initialY, double finalX, double finalY){
        int n = sides;
        double deltaX = finalX-initialX;
        double deltaY = finalY-initialY;

        double originX = deltaX/2;
        double originY = deltaX/2;

        double[] x = new double[sides];
        double[] y = new double[sides];

        double outerTheta = ((double) (180*(n-2))/n)*(Math.PI/180);
        double innerTheta = (90 - (outerTheta)/2)*(Math.PI/180);
        double R = Math.abs(deltaX)/2;

        x[0] = R + (Math.pow(R,2)*Math.sin(innerTheta));
        y[0] = R + (Math.pow(R,2)*Math.cos(innerTheta));

        for(int i=1; i<n; i++){
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
     * Draw polygon.
     *
     * @param currentgc the currentgc
     * @param xcoors    the xcoors
     * @param ycoors    the ycoors
     */
    protected void drawStar(GraphicsContext currentgc, double[] xcoors, double[] ycoors){
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
                sides
        );
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
    public void getPressEvent(MouseEvent mouseEvent) {
        anchorX = mouseEvent.getX();
        anchorY = mouseEvent.getY();
    }

    @Override
    public void getDragEvent(MouseEvent mouseEvent) {
        double[][] xy = generateShapeArrays(anchorX,anchorY,mouseEvent.getX(),mouseEvent.getY());
        liveDrawStar(xy[0],xy[1]);
    }

    @Override
    public void getReleaseEvent(MouseEvent mouseEvent) {
        clearCanvas(ldgc);
        double[][] xy = generateShapeArrays(anchorX, anchorY, mouseEvent.getX(), mouseEvent.getY());
        drawStar(gc, xy[0],xy[1]);
    }

    @Override
    public String toString() {
        return "Star";
    }
}