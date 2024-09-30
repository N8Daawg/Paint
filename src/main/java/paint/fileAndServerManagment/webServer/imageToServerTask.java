package paint.fileAndServerManagment.webServer;

import com.sun.net.httpserver.*;
import javafx.scene.image.Image;
import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * The type Image to server task.
 */
public class imageToServerTask implements Runnable {
    private final HttpExchange exchange;
    private final File file;

    /**
     * Instantiates a new Image to server task.
     *
     * @param Exchange the HTTP exchange
     * @param file     the file being displayed
     */
    public imageToServerTask(HttpExchange Exchange, File file){
        exchange = Exchange;
        this.file = file;
    }

    @Override
    public void run() {
        try {
            String extension = FileController.getExtension(file.toPath());
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(new Image(String.valueOf(file.toURI().toURL())), null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ImageIO.write(bufferedImage, extension, baos);
            byte[] bytes = baos.toByteArray();

            exchange.getResponseHeaders().set("Content-Type", "image/"+extension);
            exchange.sendResponseHeaders(200, bytes.length);

            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();

        } catch (IOException ioException){
            System.out.println("there was a problem with writing to the Byte Array Output Stream... :(  " + ioException.getMessage());
        }
    }
}
