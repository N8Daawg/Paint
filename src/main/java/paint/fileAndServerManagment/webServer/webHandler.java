package paint.fileAndServerManagment.webServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;

/**
 * The type Web handler.
 */
public class webHandler implements HttpHandler {
    private File transferredfile;

    /**
     * Instantiates a new Web handler.
     */
    public webHandler(){this.transferredfile = null;}

    /**
     * Sets transferredfile.
     *
     * @param transferedfile the file that is being transfered to the web server
     */
    public void setTransferredfile(File transferedfile) {
        this.transferredfile = transferedfile;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        imageToServerTask sendImageTask = new imageToServerTask(exchange, transferredfile);
        new Thread(sendImageTask).start();
    }
}
