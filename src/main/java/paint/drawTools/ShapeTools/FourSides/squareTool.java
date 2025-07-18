package paint.drawTools.ShapeTools.FourSides;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

import static java.lang.Math.abs;

/**
 * The type Square tool.
 */
public class squareTool extends rectangleTool {
    /**
     * Instantiates a new Square tool.
     *
     * @param g    the Graphics Context
     * @param LDGC the Live Draw Graphics Context
     */
    public squareTool(GraphicsContext g, GraphicsContext LDGC) {
        super(g, LDGC);
    }

    private double[] correctBounts(double[] deltas){
        double deltaX = deltas[0];double deltaY = deltas[1];

        if (abs(deltaX) < abs(deltaY)){
            if((deltaY<0 && !(deltaX <0)) || (deltaX<0 && !(deltaY<0))){
                deltaY = -deltaX;
            } else {
                deltaY = deltaX;
            }
        } else {
            if((deltaX<0 && !(deltaY <0)) || (deltaY<0 && !(deltaX<0))){
                deltaX = -deltaY;
            } else {
                deltaX = deltaY;
            }
        }
        return new double[]{deltaX, deltaY};

    }
    @Override
    public void getDragEvent(MouseEvent mouseEvent) {
        double finalX; double finalY;

        double[] deltas = correctBounts(new double[]{(mouseEvent.getX()-anchorX), (mouseEvent.getY()-anchorY)});
        finalX = anchorX+deltas[0];
        finalY = anchorY+deltas[1];

        liveDrawRectangle(
                new double[]{anchorX, finalX, finalX, anchorX},
                new double[]{anchorY, anchorY, finalY, finalY}
        );
    }

    @Override
    public void getReleaseEvent(MouseEvent mouseEvent) {
        clearCanvas(ldgc);

        double finalX; double finalY;

        double[] deltas = correctBounts(new double[]{(mouseEvent.getX()-anchorX), (mouseEvent.getY()-anchorY)});
        finalX = anchorX+deltas[0];
        finalY = anchorY+deltas[1];

        drawRectangle(
                gc,
                new double[]{anchorX, finalX, finalX, anchorX},
                new double[]{anchorY, anchorY, finalY, finalY}
        );
    }

    @Override
    public String toString() {
        return "Square";
    }
}
