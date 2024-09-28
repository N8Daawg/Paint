package paint.fileAndServerManagment.webServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;

public class webHandler implements HttpHandler {
    private File transferredfile;

    public webHandler(){this.transferredfile = null;}

    public void setTransferredfile(File transferedfile) {
        this.transferredfile = transferedfile;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        imageToServerTask sendImageTask = new imageToServerTask(exchange, transferredfile);
        new Thread(sendImageTask).start();
    }
}
