package paint.drawTools.MiscTools;

import paint.drawTools.ShapeTools.FourSides.rectangleTool;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.*;

/**
 * The type Selector tool.
 */
public class selectorTool extends rectangleTool {
    private Image clipboard;
    private Boolean selecting;
    private double cornerX;
    private double cornerY;
    private double grabbingx;
    private double grabbingy;

    /**
     * Instantiates a new Selector tool.
     *
     * @param g    the Graphics Context
     * @param LDGC the Live Draw Graphics Context
     */
    public selectorTool(GraphicsContext g, GraphicsContext LDGC) {
        super(g, LDGC);
        clipboard = null;
        selecting = true;
    }

    @Override
    public void getPressEvent(MouseEvent mouseEvent) {
        if(selecting){
            super.getPressEvent(mouseEvent);
        } else{
            grabbingx = mouseEvent.getX();
            grabbingy = mouseEvent.getY();
        }
    }

    @Override
    public void getDragEvent(MouseEvent mouseEvent) {
        if(selecting) {
            super.getDragEvent(mouseEvent);
        } else{
            double translateX = mouseEvent.getX()-grabbingx;
            double translateY = mouseEvent.getY()-grabbingy;
            clearCanvas(ldgc);
            ldgc.drawImage(clipboard, cornerX+translateX, cornerY+translateY);
        }
    }

    @Override
    public void getReleaseEvent(MouseEvent mouseEvent) {
        clearCanvas(ldgc);
        if(selecting){
            double deltaX = mouseEvent.getX()-anchorX; double deltaY = mouseEvent.getY()-anchorY;
            double width; double height;

            if ((deltaX<=0)&&(deltaY<=0)) {
                cornerX = anchorX+deltaX; cornerY = anchorY+deltaY;
                width = -deltaX; height = -deltaY;
            } else if ((deltaX<=0)&&(deltaY>=0)){
                cornerX = anchorX+deltaX; cornerY = anchorY;
                width = -deltaX; height = deltaY;
            } else if ((deltaX>=0)&&(deltaY<=0)){
                cornerX = anchorX; cornerY = anchorY+deltaY;
                width = deltaX; height = -deltaY;
            } else {
                cornerX = anchorX; cornerY = anchorY;
                width = deltaX; height = deltaY;
            }

            SnapshotParameters sp = new SnapshotParameters();
            sp.setViewport(new Rectangle2D(
                    cornerX+gc.getCanvas().getLayoutX(),
                    cornerY+gc.getCanvas().getLayoutY(),
                    width, height));

            clipboard = gc.getCanvas().snapshot(sp,null);
            ldgc.drawImage(clipboard, cornerX, cornerY);
            selecting = false;
        } else {
            double translateX = mouseEvent.getX()-grabbingx;
            double translateY = mouseEvent.getY()-grabbingy;

            gc.drawImage(clipboard, cornerX+translateX,cornerY+translateY);
            selecting = true;
        }
    }

    @Override
    public String toString() {
        return "selector";
    }
}
