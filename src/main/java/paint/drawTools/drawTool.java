package paint.drawTools;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * The type Draw tool.
 */
public abstract class drawTool {
    protected GraphicsContext gc;
    protected GraphicsContext ldgc;
    protected double brushSize;
    protected double anchorX;
    protected double anchorY;
    protected boolean isDashedLine;

    /**
     * Instantiates a new Draw tool.
     *
     * @param g    the g
     * @param LDGC the ldgc
     */
    public drawTool(GraphicsContext g, GraphicsContext LDGC){
        gc = g;
        ldgc = LDGC;
        setAttributes(Color.BLACK, 6, false);
    }

    /*---------------------------------------------------------------------------*/
    /*-------------------------------Drawing Controls----------------------------*/
    /*---------------------------------------------------------------------------*/

    /**
     * Set attributes.
     *
     * @param color      the color
     * @param size       the size
     * @param dashedLine the dashed line
     */
    public void setAttributes(Color color, double size, Boolean dashedLine){
        setColor(color);
        setSize(size);
        setDashedLine(dashedLine);
    }

    /**
     * Sets color for draw tool
     *
     * @param color the color to be set
     */
    protected abstract void setColor(Color color);

    /**
     * Sets size for draw tool
     *
     * @param size the size of the line
     */
    protected abstract void setSize(double size);

    /**
     * Sets dashed line boolean for draw tool
     *
     * @param dashedLine if the shape uses dashed lines
     */
    protected abstract void setDashedLine(Boolean dashedLine);

    /**
     * clears the canvas of whatever graphics context is given
     *
     * @param graphicsContext the graphics context relating to the canvas that is to be cleared
     */
    public void clearCanvas(GraphicsContext graphicsContext){
        graphicsContext.clearRect(0,0,graphicsContext.getCanvas().getWidth(), graphicsContext.getCanvas().getHeight());
    }

    /*---------------------------------------------------------------------------*/
    /*-------------------------------Abstract Events-----------------------------*/
    /*---------------------------------------------------------------------------*/


    /**
     * Used to get the Draw Event from the current tool.
     *
     * @param mouseEvent the mouse event
     */
    public abstract void getPressEvent(MouseEvent mouseEvent);

    /**
     * Used to get the Draw Event from the current tool.
     *
     * @param mouseEvent the mouse event
     */
    public abstract void getDragEvent(MouseEvent mouseEvent);

    /**
     * Used to get the Draw Event from the current tool.
     *
     * @param mouseEvent the mouse event
     */
    public abstract void getReleaseEvent(MouseEvent mouseEvent);

    public String toString(){
        return "Tool";
    }
}
