package paint.drawTools;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public abstract class drawTool {
    protected GraphicsContext gc;
    protected GraphicsContext ldgc;
    protected double brushSize;
    protected double anchorX;
    protected double anchorY;
    protected boolean isDashedLine;

    public drawTool(GraphicsContext g, GraphicsContext LDGC){
        gc = g;
        ldgc = LDGC;
        setAttributes(Color.BLACK, 6, false);
    }

    /*---------------------------------------------------------------------------*/
    /*-------------------------------Drawing Controls----------------------------*/
    /*---------------------------------------------------------------------------*/
    public void setAttributes(Color color, double size, Boolean dashedLine){
        setColor(color);
        setSize(size);
        setDashedLine(dashedLine);
    }

    protected abstract void setColor(Color color);

    protected abstract void setSize(double size);

    protected abstract void setDashedLine(Boolean dashedLine);

    public void clearCanvas(GraphicsContext graphicsContext){
        graphicsContext.clearRect(0,0,graphicsContext.getCanvas().getWidth(), graphicsContext.getCanvas().getHeight());
    }

    /*---------------------------------------------------------------------------*/
    /*-------------------------------Abstract Events-----------------------------*/
    /*---------------------------------------------------------------------------*/

    public abstract void getPressEvent(MouseEvent mouseEvent);

    public abstract void getDragEvent(MouseEvent mouseEvent);

    public abstract void getReleaseEvent(MouseEvent mouseEvent);

    public String toString(){
        return "Tool";
    }
}
