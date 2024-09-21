package com.example.paint;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class PaintApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PaintApplication.class.getResource("Paint-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 720, 480);

        stage.setTitle("Paint");
        stage.setScene(scene);
        PaintController.shortcutsSetup();
        stage.setOnCloseRequest(e -> PaintController.smartCLoseWindow(e)); //setup for smart save
        //PaintController.resize();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}