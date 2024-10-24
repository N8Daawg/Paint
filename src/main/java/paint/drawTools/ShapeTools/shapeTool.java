package paint.drawTools.ShapeTools;

import paint.drawTools.drawTool;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * The type Shape tool.
 */
public class shapeTool extends drawTool {
    /**
     * The constant numOfDashes.
     */
    protected static final int numOfDashes = 6;

    /**
     * Instantiates a new Shape tool.
     *
     * @param g    the g
     * @param LDGC the ldgc
     */
    public shapeTool(GraphicsContext g, GraphicsContext LDGC) {
        super(g, LDGC);
    }

    protected void setColor(Color color){
        gc.setStroke(color);
        ldgc.setStroke(color);
    }
    protected void setSize(double size){
        brushSize = size;
        gc.setLineWidth(brushSize);
        ldgc.setLineWidth(brushSize);
    }
    protected void setDashedLine(Boolean dashedLine){
        isDashedLine = dashedLine;
    }

    /**
     * Create line dashes double [ ].
     *
     * @param perimeter the perimeter
     * @return the double [ ]
     */
    protected double[] createLineDashes(double perimeter){
        double[] dashes = new double[(numOfDashes*2)-1];
        for (int i=0; i<(numOfDashes*2)-1;i+=2) {
            dashes[i] = perimeter/(numOfDashes+1);
            if(i+1 < dashes.length) {
                dashes[i + 1] = perimeter/((numOfDashes+1)*(numOfDashes-1));
            }
        }
        return dashes;
    }

    @Override
    public void getPressEvent(MouseEvent mouseEvent) {}

    @Override
    public void getDragEvent(MouseEvent mouseEvent) {}

    @Override
    public void getReleaseEvent(MouseEvent mouseEvent) {}

    @Override
    public String toString() {
        return "Shape";
    }
}
