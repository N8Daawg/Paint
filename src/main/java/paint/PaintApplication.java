package paint;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class PaintApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PaintApplication.class.getResource("Paint-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 700);
        stage.setTitle("Paint");
        stage.setScene(scene);

        stage.show();
        PaintController.postInitializationSetup();
        stage.setOnCloseRequest(PaintController::smartCloseWindow); //setup for smart savetab
    }


    public static void main(String[] args) {
        launch();
    }
}