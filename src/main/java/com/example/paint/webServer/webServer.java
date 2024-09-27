package com.example.paint.webServer;

import com.sun.net.httpserver.HttpServer;
import javafx.scene.image.Image;

import java.io.IOException;
import java.net.InetSocketAddress;

public class webServer{
    private Image im;
    public webServer(Image image){
        im = image;
    }
    public void run(){
        try{
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
            server.createContext("/Tab_Image", new webHandler(im));
            server.setExecutor(null);
            server.start();
            System.out.println("Server is running on port 8000...\net localhost:8000/Tab_Image");
        } catch (IOException e){
            System.out.println("Error starting the web server: " + e.getMessage());
        }
    }
}
