package com.example.paint.drawTools.MiscTools;

import com.example.paint.drawTools.drawTool;
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
    public void getPressEvent(MouseEvent e) {
        anchorX = e.getX(); anchorY = e.getY();

    }

    @Override
    public void getDragEvent(MouseEvent e) {

    }

    @Override
    public void getReleaseEvent(MouseEvent e) {
        TextField userinput = new TextField("Your text here");
        userinput.setLayoutX(e.getX());userinput.setLayoutY(e.getY()+50);

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
