package paint.drawTools;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * The type Draw tool.
 */
public abstract class drawTool {
    /**
     * The Gc.
     */
    protected GraphicsContext gc;
    /**
     * The Ldgc.
     */
    protected GraphicsContext ldgc;
    /**
     * The Current color.
     */
    protected Color currentColor;
    /**
     * The Brush size.
     */
    protected double brushSize;
    /**
     * The Anchor x.
     */
    protected double anchorX;
    /**
     * The Anchor y.
     */
    protected double anchorY;
    /**
     * The Is dashed line.
     */
    protected boolean isDashedLine;
    /**
     * The Recently saved.
     */
    protected boolean recentlySaved;

    /**
     * Instantiates a new Draw tool.
     *
     * @param g    the graphics context
     * @param LDGC the live draw graphics context
     */
    public drawTool(GraphicsContext g, GraphicsContext LDGC){
        gc = g;
        ldgc = LDGC;
        currentColor = Color.BLACK;
        brushSize = 6;
        recentlySaved=true;
    }

    /**
     * Setup.
     *
     * @param color            the color
     * @param size             the size
     * @param dashedLine       the dashed line
     * @param wasRecentlySaved the was recently saved
     */
    protected void setup(Color color, double size, Boolean dashedLine, boolean wasRecentlySaved){
        setColor(color);
        setSize(size);
        setDashedLine(dashedLine);
        setRecentlySaved(wasRecentlySaved);
    }

    /*---------------------------------------------------------------------------*/
    /*-------------------------------Drawing Controls----------------------------*/
    /*---------------------------------------------------------------------------*/

    /**
     * Sets color.
     *
     * @param color the color
     */
    protected abstract void setColor(Color color);

    /**
     * Sets size.
     *
     * @param size the size
     */
    protected abstract void setSize(double size);

    /**
     * Sets dashed line.
     *
     * @param dashedLine the dashed line
     */
    protected abstract void setDashedLine(Boolean dashedLine);

    /**
     * Sets attributes.
     *
     * @param color         the color
     * @param size          the size
     * @param dashedLine    the dashed line
     * @param recentlySaved the recently saved
     */
    public abstract void setAttributes(Color color, double size, Boolean dashedLine, Boolean recentlySaved);

    /*---------------------------------------------------------------------------*/
    /*-------------------------------Abstract Events-----------------------------*/
    /*---------------------------------------------------------------------------*/

    /**
     * Gets press event.
     *
     * @param mouseEvent
     */
    public abstract void getPressEvent(MouseEvent mouseEvent);

    /**
     * Gets drag event.
     *
     * @param mouseEvent
     */
    public abstract void getDragEvent(MouseEvent mouseEvent);

    /**
     * Gets release event.
     *
     * @param mouseEvent
     */
    public abstract void getReleaseEvent(MouseEvent mouseEvent);

    /**
     * Was recently saved boolean.
     *
     * @return the boolean
     */
    public boolean wasRecentlySaved() {
        return recentlySaved;
    }

    /**
     * Set recently saved.
     *
     * @param wasRecentlySaved the was recently saved
     */
    public void setRecentlySaved(Boolean wasRecentlySaved){ recentlySaved = wasRecentlySaved;}

    /**
     * Clear canvas.
     *
     * @param graphicsContext the graphics context
     */
    public void clearCanvas(GraphicsContext graphicsContext){
        graphicsContext.clearRect(0,0,graphicsContext.getCanvas().getWidth(), graphicsContext.getCanvas().getHeight());
    }

    public String toString(){
        return "Tool";
    }
}
