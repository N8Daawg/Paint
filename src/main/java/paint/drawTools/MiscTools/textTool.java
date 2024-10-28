package paint.drawTools.MiscTools;

import javafx.scene.layout.Pane;
import paint.drawTools.drawTool;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.paint.Color;

/**
 * The type Text tool.
 */
public class textTool extends drawTool {
    private final TextField userInput;
    private static final String promptTxt = "Your text here";
    /**
     * Instantiates a new Text tool.
     *
     * @param g    the g
     * @param LDGC the ldgc
     */
    public textTool(GraphicsContext g, GraphicsContext LDGC) {
        super(g, LDGC);
        userInput = new TextField();
        userInput.setPromptText(promptTxt);
        userInput.setVisible(false);
        userInput.setDisable(true);
        Pane parent = (Pane) gc.getCanvas().getParent();
        parent.getChildren().add(userInput);

    }

    private void printTxt(String txt) {
        gc.strokeText(txt, anchorX, anchorY);
        userInput.setText("");
        userInput.setVisible(false);
        userInput.setDisable(true);
    }

    @Override
    protected void setColor(Color color) {
        gc.setStroke(color);
    }

    @Override
    protected void setSize(double size) {gc.setLineWidth(1);}

    @Override
    protected void setDashedLine(Boolean dashedLine) {}

    @Override
    public void getPressEvent(MouseEvent mouseEvent) {
        anchorX = mouseEvent.getX(); anchorY = mouseEvent.getY();

    }

    @Override
    public void getDragEvent(MouseEvent mouseEvent) {}

    @Override
    public void getReleaseEvent(MouseEvent mouseEvent) {
        userInput.setDisable(false);
        userInput.setText(promptTxt);
        //userInput.setLayoutX(anchorX - userInput.getLayoutBounds().getMinX());
        userInput.relocate(anchorX, anchorY-userInput.getHeight());
        userInput.setVisible(true);
        userInput.requestFocus();
        userInput.selectAll();

        gc.getCanvas().getScene().addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {//add event filter to fish for shortcuts
            if (keyEvent.getCode() == KeyCode.ENTER) {
                printTxt(userInput.getText());
                gc.getCanvas().getScene().addEventFilter(KeyEvent.KEY_PRESSED, otherKeyEvent -> {});
            }
        });
    }

    public String toString(){
        return "text";
    }
}
