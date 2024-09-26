package paint.Tabs;

import paint.drawTools.drawTool;
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

/**
 * The type Tab copyer.
 */
public class TabCopyer {
    /**
     * Copy anchor pane.
     *
     * @return the anchor pane
     */
    /*---------------------------------------------------------------------------*/
    /*-----------------------------Copying AnchorPane----------------------------*/
    /*---------------------------------------------------------------------------*/
    protected static AnchorPane copyAnchorPane(){
        return new AnchorPane();
    }

    /**
     * Copy menubar menu bar.
     *
     * @param template the template
     * @return the menu bar
     */
    /*---------------------------------------------------------------------------*/
    /*-------------------------------Copying MenuBar-----------------------------*/
    /*---------------------------------------------------------------------------*/
    protected static MenuBar copyMenubar(MenuBar template){
        Menu file = copyMenu(template.getMenus().get(0));
        Menu edit = copyMenu(template.getMenus().get(1));
        Menu help = copyMenu(template.getMenus().get(2));
        MenuBar mb = new MenuBar(file, edit, help);
        mb.setLayoutX(0); mb.setLayoutY(0);
        return mb;
    }
    private static Menu copyMenu(Menu template){
        Menu newm = new Menu(template.getId());
        newm.setText(template.getText());
        ObservableList<MenuItem> items = newm.getItems();
        for (MenuItem item:template.getItems()){
            items.add(copyMenuItems(item));
        }
        return newm;
    }
    private static MenuItem copyMenuItems(MenuItem template){
        MenuItem item = new MenuItem();
        item.setText(template.getText());
        item.setId(template.getId());
        return item;
    }
    /*---------------------------------------------------------------------------*/
    /*-------------------------------Copying ToolBar-----------------------------*/
    /*---------------------------------------------------------------------------*/

    /**
     * Copy toolbar.
     *
     * @param toolBar the toolbar
     * @return the toolbar
     */
    protected static ToolBar copyToolBar(ToolBar toolBar){
        HBox container = copyContainer((HBox) toolBar.getItems().get(0));
        ToolBar tb = new ToolBar(container);
        tb.setLayoutX(0); tb.setLayoutY(26);

        return tb;
    }

    private static HBox copyContainer(HBox template){

        HBox colorContainer = new HBox(new Label("Color: "), new ColorPicker());
        HBox shapeContainer = new HBox(new Label("Shape: "), new ChoiceBox<drawTool>());
        HBox dottedLContainer = new HBox(new Label("Dashed Shape? "), new CheckBox());
        HBox brushWidthContainer = new HBox(new Label("Brush Width: "), new TextField());
        TextField brushWidth = (TextField) brushWidthContainer.getChildren().get(1);
        brushWidth.setPrefWidth(66);
        Button clearButton = new Button("Clear Canvas");

        return new HBox(template.getSpacing(), colorContainer, shapeContainer, dottedLContainer, brushWidthContainer, clearButton);
    }

    /*---------------------------------------------------------------------------*/
    /*-------------------------------Copying Canvas------------------------------*/
    /*---------------------------------------------------------------------------*/

    /**
     * Copy canvas.
     *
     * @param template the template
     * @return the canvas
     */
    protected static Canvas copyCanvas(Canvas template){
        Canvas c = new Canvas();
        c.setLayoutY(template.getLayoutY());
        c.setLayoutX(template.getLayoutX());
        c.setWidth(template.getWidth());
        c.setHeight(template.getWidth());

        return c;
    }
}
