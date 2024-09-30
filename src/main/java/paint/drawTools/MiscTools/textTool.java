package paint.drawTools.MiscTools;

import paint.drawTools.drawTool;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

/**
 * The type Text tool.
 */
public class textTool extends drawTool {
    /**
     * Instantiates a new Text tool.
     *
     * @param g    the g
     * @param LDGC the ldgc
     */
    public textTool(GraphicsContext g, GraphicsContext LDGC) {
        super(g, LDGC);
        gc.setLineWidth(1);
    }

    @Override
    protected void setColor(Color color) {
        gc.setStroke(color);
    }

    @Override
    protected void setSize(double size) {
        gc.setLineWidth(size);
    }

    @Override
    protected void setDashedLine(Boolean dashedLine) {

    }

    @Override
    public void setAttributes(Color color, double size, Boolean dashedLine, Boolean recentlySaved) {
        setup(color,1, false, wasRecentlySaved());
    }

    @Override
    public void getPressEvent(MouseEvent mouseEvent) {
        anchorX = mouseEvent.getX(); anchorY = mouseEvent.getY();

    }

    @Override
    public void getDragEvent(MouseEvent mouseEvent) {

    }

    @Override
    public void getReleaseEvent(MouseEvent mouseEvent) {
        TextField userinput = new TextField("Your text here");
        userinput.setLayoutX(mouseEvent.getX());userinput.setLayoutY(mouseEvent.getY()+50);

        AnchorPane parent = (AnchorPane) gc.getCanvas().getParent();
        parent.getChildren().add(userinput);
        parent.getScene().addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> { //add event filter to fish for shortcuts
            if(keyEvent.getCode()==KeyCode.ENTER){
                gc.strokeText(userinput.getText(), anchorX, anchorY);
                parent.getChildren().remove(userinput);
            }
        });
    }

    public String toString(){
        return "text";
    }
}
