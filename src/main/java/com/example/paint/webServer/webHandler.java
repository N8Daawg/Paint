package com.example.paint.webServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import javafx.scene.image.Image;

import java.io.IOException;

public class webHandler implements HttpHandler {
    private Image transferedIm;
    public webHandler(Image im){
        transferedIm = im;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        imageToServerTask sendImageTask = new imageToServerTask(exchange, transferedIm);
        new Thread(sendImageTask).start();
    }
}
