package paint.fileAndServerManagment.webServer;

import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * The type Web server.
 */
public class webServer{
    private File file;
    private webHandler handler;

    /**
     * Instantiates a new Web server.
     */
    public webServer(){file = null;}

    /**
     * void to start the webserver.
     */
    public void run(){
        try{
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
            handler = new webHandler();
            server.createContext("/Tab_Image", handler);
            server.setExecutor(null);
            server.start();
            System.out.println("Server is running on port 8000.../net localhost:8000/Tab_Image");
        } catch (IOException e){
            System.out.println("Error starting the web server: " + e.getMessage());
        }
    }

    /**
     * Update server image to display a specific file.
     *
     * @param file the file
     */
    public void updateServerFile(File file){
        this.file = file;
        handler.setTransferredfile(file);
    }
}
