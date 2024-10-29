package paint.fileAndServerManagment;

import com.sun.net.httpserver.HttpServer;
import paint.threadedLogger;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * The type Web server.
 */
public class webServer {
    private File file;
    private webHandler handler;
    private final threadedLogger logger;

    /**
     * Instantiates a new Web server.
     */
    public webServer(threadedLogger logger) {
        file = null;
        this.logger = logger;
    }

    /**
     * void to start the webserver.
     */
    public void run() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
            handler = new webHandler();
            server.createContext("/Tab_Image", handler);
            server.setExecutor(null);
            server.start();
            logger.sendMessage("Server is running on port 8000.../net localhost:8000/Tab_Image");
        } catch (IOException e) {
            logger.sendMessage("Error starting the web server: " + e.getMessage());
        }
    }

    /**
     * Update server image to display a specific file.
     *
     * @param file the file
     */
    public void updateServerFile(File file) {
        this.file = file;
        handler.setTransferredfile(file);
        logger.sendMessage("Server updated file: " + file.toString());
    }
}
