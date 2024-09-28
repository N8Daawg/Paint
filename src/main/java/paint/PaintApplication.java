package paint;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import paint.fileAndServerManagment.webServer.webServer;

import java.io.File;
import java.io.IOException;

public class PaintApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PaintApplication.class.getResource("Paint-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 720, 480);

        stage.setTitle("Paint");
        stage.setScene(scene);
        stage.show();

        PaintController.postInitializationSetup();
        stage.setOnCloseRequest(PaintController::smartCLoseWindow); //setup for smart save
        //PaintController.resize();
    }

    public static void main(String[] args) {
        launch();
    }
}