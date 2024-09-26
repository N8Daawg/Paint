package com.example.paint.drawTools.MiscTools;

import com.example.paint.drawTools.drawTool;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.io.IOException;

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
    }

    @Override
    protected void setColor(Color color) {

    }

    @Override
    protected void setSize(double size) {

    }

    @Override
    protected void setDashedLine(Boolean dashedLine) {

    }

    @Override
    public void setAttributes(Color color, double size, Boolean dashedLine, Boolean recentlySaved) {

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
        parent.getScene().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent){ //add event filter to fish for shortcuts
                if(keyEvent.getCode()==KeyCode.ENTER){
                    parent.getChildren().remove(userinput);
                    gc.strokeText(userinput.getText(), anchorX, anchorY);
                }
            }
        });

        parent.getChildren().add(userinput);
    }

    public String toString(){
        return "text";
    }
}
