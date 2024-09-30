package paint.drawTools.ShapeTools.OneSide;

import javafx.scene.input.MouseEvent;
import paint.drawTools.ShapeTools.shapeTool;
import javafx.scene.canvas.GraphicsContext;

/**
 * The type Line tool.
 */
public class lineTool extends shapeTool {
    /**
     * Instantiates a new Line tool.
     *
     * @param g    the g
     * @param LDGC the ldgc
     */
    public lineTool(GraphicsContext g, GraphicsContext LDGC) {
        super(g, LDGC);
    }
    private void drawLine(GraphicsContext currentgc, double initialX, double initialY, double finalX, double finalY){
        recentlySaved = false;
        if(isDashedLine){
            double perimeter = Math.sqrt(Math.pow(finalX - initialX, 2) + Math.pow((finalY-initialY), 2));
            currentgc.setLineDashes(createLineDashes(perimeter));
        }

        currentgc.beginPath();
        currentgc.lineTo(initialX, initialY);
        currentgc.lineTo(finalX, finalY);
        currentgc.stroke();
        currentgc.closePath();
    }

    private void liveDrawLine(double initialX, double initialY, double finalX, double finalY){
        clearCanvas(ldgc);
        drawLine(ldgc, initialX, initialY, finalX, finalY);
    }

    /*---------------------------------------------------------------------------*/
    /*-----------------------------------Line Events-----------------------------*/
    /*---------------------------------------------------------------------------*/
    @Override
    public void getPressEvent(MouseEvent mouseEvent) {
        anchorX = mouseEvent.getX();
        anchorY = mouseEvent.getY();
    }

    @Override
    public void getDragEvent(MouseEvent mouseEvent) {
        liveDrawLine(anchorX, anchorY, mouseEvent.getX(), mouseEvent.getY());
    }

    @Override
    public void getReleaseEvent(MouseEvent mouseEvent) {
        recentlySaved = false;
        clearCanvas(ldgc);
        drawLine(gc, anchorX, anchorY, mouseEvent.getX(), mouseEvent.getY());
    }

    @Override
    public String toString() {
        return "Line";
    }
}
