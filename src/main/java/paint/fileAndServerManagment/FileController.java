package paint.fileAndServerManagment;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import paint.threadedLogger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.Optional;

/**
 * The type File controller.
 */
public class FileController {
    private Stage stage;

    private final Canvas canvas;
    private File currentFile;
    private Boolean recentlySaved;
    private final webServer server; //the connected webserver to display saved images
    private final threadedLogger logger;
    private boolean notificationsOn;

    /**
     * Instantiates a new File controller.
     *
     * @param newMenuBar the menu bar
     * @param newCanvas  the canvas to be saved
     * @param Server     the server storing the images
     */
    public FileController(MenuBar newMenuBar, Canvas newCanvas, webServer Server, threadedLogger Logger) {

        canvas = newCanvas;
        recentlySaved = true;
        server = Server;
        logger = Logger;
        turnNotificationsOn();
    }

    /**
     * Was recently saved boolean.
     *
     * @return returns whether the file was recently saved or not
     */
    public Boolean wasRecentlySaved(){
        return recentlySaved;
    }

    /**
     *
     * @return the name of the current file
     */
    public String getCurrentFile() {
        if (currentFile == null){
            return "No file selected";
        } else {
            return currentFile.getName();
        }
    }

    /**
     * opens a new file
     */
    public void openFile(){
        open();
    }

    /**
     * saves the current file.
     *
     * @return if the file was saved successfully
     */
    public boolean saveFile(){
        recentlySaved = save();
        return recentlySaved;
    }

    /**
     * saves the current file as a new name
     *
     * @return if the file was saved successfully
     */
    public boolean saveAsFile(){
        recentlySaved = saveAsFile();
        return recentlySaved;
    }

    /**
     * opens the help window
     */
    public void showHelp(){
        showHelpWindow();
    }

    /**
     * turns notifications on
     */
    public void turnNotificationsOn(){notificationsOn = true;}

    /**
     * turns notifications off
     */
    public void turnNotificationsOff(){notificationsOn = false;}

    /*---------------------------------------------------------------------------*/
    /*------------------------------File Method Control--------------------------*/
    /*---------------------------------------------------------------------------*/

    private void open() {
        FileChooser fileChooser = new FileChooser(); //create a fileChooser to show the open dialog
        fileChooserSetup(fileChooser, currentFile, "File Selection"); // format the FileChooser
        currentFile = fileChooser.showOpenDialog(stage); //grab a file

        if(currentFile != null){
            try {
                Image currentImage = new Image(String.valueOf(currentFile.toURI().toURL()));//create Image from file that was grabbed
                canvas.getGraphicsContext2D().drawImage(currentImage,
                        (canvas.getWidth()-currentImage.getWidth())/2,
                        (canvas.getHeight()-currentImage.getHeight())/2
                );
                logger.sendMessage("new image: " + currentFile.getName() + " was opened.");
            } catch (MalformedURLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }


    private Boolean save() {
        if(currentFile != null) { // if we are already working on an existing file
            String extension = getExtension(currentFile.toPath()); //get file extension

            try {
                Image currentImage = canvas.snapshot(null,null);
                BufferedImage saveFile = SwingFXUtils.fromFXImage(currentImage, null); //transform image to buffered image
                ImageIO.write(saveFile, extension, currentFile); //write the buffered image
                if(notificationsOn){
                    Notifications.create()
                            .title(currentFile.toString())
                            .text(currentFile.toString() + " was saved")
                            .showInformation();
                }
                logger.sendMessage(currentFile.toString() + " was saved.");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            updateServer();

        } else { //there is no file being worked on
            System.out.println("some error message saying you dont have a file to savetab yet");
            return saveAsFile();
        }
        return true;
    }

    private boolean saveAs() {
        File previousFile=null;
        if(currentFile !=null){
            previousFile = currentFile;
        }
        FileChooser fileChooser = new FileChooser(); //create a fileChooser to show the open dialog
        fileChooserSetup(fileChooser, currentFile, "Save"); // format the FileChooser
        currentFile = fileChooser.showSaveDialog(stage); //Create the file name that is being saved
        if(currentFile != null){
            String extension = getExtension(currentFile.toPath()); //get file extension

            if(previousFile != null && !getExtension(previousFile.toPath()).equals(extension)){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText("Look, a Confirmation Dialog");
                alert.setContentText("Are you ok with this?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    return saveFile();
                } else {
                    // ... user chose CANCEL or closed the dialog
                    currentFile = previousFile;
                    return false;
                }
            } else{
                return saveFile();
            }
        }
        return saveAsFile();
    }

    /*---------------------------------------------------------------------------*/
    /*------------------------------Help window control--------------------------*/
    /*---------------------------------------------------------------------------*/

    private void showHelpWindow() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("I'm a help window. I tell you stuff about your program");

        alert.showAndWait();
        logger.sendMessage("User activated the help menu");
    }

    /*---------------------------------------------------------------------------*/
    /*---------------------------------File Utility------------------------------*/
    /*---------------------------------------------------------------------------*/

    private void updateServer(){
        server.updateServerFile(currentFile);
    }

    // Nifty method I created to format all FileChoosers the program creates
    private static void fileChooserSetup(FileChooser f, File currentfile, String title){
        f.setTitle(title); // set FileChooser's title to a string "title" that is passed to the function
        f.getExtensionFilters().add( //add ExtensionFilter for Image files
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.bmp")
        );
        if(currentfile != null) { //check and see if there is a file currently being worked on
            f.setInitialDirectory(currentfile.getParentFile()); //open up directory in the same folder where the current file being worked on is
            f.setInitialFileName(currentfile.getName()); //automatically set the File name to the current file that's being worked on
        }
    }

    /**
     * Gets an extension of a file.
     *
     * @param path the path of a file
     * @return the extension of a file
     */
    // method to read file extensions found from GFG website:
    // https://www.geeksforgeeks.org/how-to-get-the-file-extension-in-java/
    protected static String getExtension(Path path) {
        String fileName = path.getFileName().toString();
        int dotIndex = fileName.lastIndexOf(".");

        // handle cases with no extension or multiple dots
        if (dotIndex == -1 || dotIndex == fileName.length() -1) {
            return "";
        } else {
            return fileName.substring(dotIndex + 1);
        }
    }
}