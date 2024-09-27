package com.example.paint.webServer;

import com.sun.net.httpserver.*;
import javafx.scene.image.Image;
import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class imageToServerTask implements Runnable {
    private HttpExchange exchange;
    private Image image;
    public imageToServerTask(HttpExchange Exchange, Image Image){
        exchange = Exchange;
        image = Image;
    }

    @Override
    public void run() {
        try {
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, ".jpg", baos);
            byte[] bytes = baos.toByteArray();
            System.out.println(bytes);
        } catch (IOException ioException){
            System.out.println("there was a problem with writing to the Byte Array Output Stream... :(  " + ioException.getMessage());
        }
    }
}
