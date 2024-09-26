package com.example.paint.drawTools.MiscTools;

import com.example.paint.drawTools.ShapeTools.FourSides.rectangleTool;
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
    private double grabbingx;
    private double grabbingy;
    private double finalx;
    private double finaly;

    /**
     * Instantiates a new Selector tool.
     *
     * @param g    the g
     * @param LDGC the ldgc
     */
    public selectorTool(GraphicsContext g, GraphicsContext LDGC) {
        super(g, LDGC);
        clipboard = null;
        selecting = true;
    }

    @Override
    public void getPressEvent(MouseEvent e) {
        if(selecting){
            super.getPressEvent(e);
        } else{
            grabbingx = e.getX();
            grabbingy = e.getY();
        }
    }

    @Override
    public void getDragEvent(MouseEvent e) {
        if(selecting) {
            super.getDragEvent(e);
        } else{
            double translatex = e.getX()-grabbingx;
            double translatey = e.getY()-grabbingy;
            liveDrawRectangle(
                    new double[]{anchorX+translatex, anchorX+translatex, finalx+translatex, finalx+translatex},
                    new double[]{anchorY+translatey, finaly+translatey, finaly+translatey, anchorY+translatey}
            );
            ldgc.drawImage(clipboard, anchorX+translatex, anchorY+translatey);
        }
    }

    @Override
    public void getReleaseEvent(MouseEvent e) {
        clearCanvas(ldgc);
        if(selecting){
            finalx = e.getX(); finaly=e.getY();

            SnapshotParameters sp = new SnapshotParameters();
            sp.setViewport(new Rectangle2D(anchorX, anchorY+69.6, finalx-anchorX, finaly-anchorY));
            clipboard = gc.getCanvas().snapshot(sp,null);

            //gc.clearRect(anchorX,anchorY,e.getX(),e.getY());
            ldgc.drawImage(clipboard, anchorX, anchorY);
            selecting = false;
        } else {

            gc.drawImage(clipboard, anchorX+(e.getX()-grabbingx),anchorY+(e.getY()-grabbingy));
            selecting = true;
        }
    }

    /**
     * Get clip board image.
     *
     * @return the image
     */
    public Image getClipBoard(){
        return clipboard;
    }

    /**
     * Paste.
     *
     * @param me the mouse event
     */
    public void paste(MouseEvent me) {
        gc.drawImage(clipboard, me.getX(), me.getY());
    }

    @Override
    public String toString() {
        return "selector";
    }
}
