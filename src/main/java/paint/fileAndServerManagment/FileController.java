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
    private final MenuBar menuBar;
    private final MenuItem openFile; //open file button
    private final MenuItem saveFile; //save file button
    private final MenuItem saveAsFile; //save as file button
    private final MenuItem about; //opens the help window
    private final Canvas canvas;
    private File currentFile;
    private Boolean recentlySaved;
    private final webServer server; //the connected webserver to display saved images
    private final threadedLogger logger;

    /**
     * Instantiates a new File controller.
     *
     * @param newMenuBar the menu bar
     * @param newCanvas  the canvas to be saved
     * @param Server     the server storing the images
     */
    public FileController(MenuBar newMenuBar, Canvas newCanvas, webServer Server, threadedLogger Logger) {
        menuBar = newMenuBar;

        Menu fileMenu = menuBar.getMenus().get(0);
        openFile = fileMenu.getItems().get(0);
        saveFile = fileMenu.getItems().get(2);
        saveAsFile = fileMenu.getItems().get(3);

        Menu helpMenu = menuBar.getMenus().get(2);
        about = helpMenu.getItems().get(0);

        canvas = newCanvas;
        recentlySaved = true;
        server = Server;
        logger = Logger;
        postInitSetup();
    }

    /**
     * Post init setup.
     */
    public void postInitSetup(){
        if (menuBar.getScene() != null){
            stage = (Stage) menuBar.getScene().getWindow(); // attempt to grab the stage.
            openFile.setOnAction(e -> {
                openFile();
            });
            saveFile.setOnAction(e -> {
                recentlySaved = saveFile();
            });
            saveAsFile.setOnAction(e -> {
                recentlySaved = saveAsFile();
            });
            about.setOnAction(e -> {
                showHelpWindow();
            });
        }
    }

    /**
     * Was recently saved boolean.
     *
     * @return returns whether the file was recently saved or not
     */
    public Boolean wasRecentlySaved(){
        return recentlySaved;
    }

    public String getCurrentFile() {
        if (currentFile == null){
            return "No file selected";
        } else {
            return currentFile.getName();
        }
    }

    /*---------------------------------------------------------------------------*/
    /*------------------------------Help window control--------------------------*/
    /*---------------------------------------------------------------------------*/

    public void openFile() {
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
            } catch (MalformedURLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * saves the current file.
     *
     * @return returns if the file saving was successful
     * @throws IOException the io exception
     */
    public Boolean saveFile() {
        if(currentFile != null) { // if we are already working on an existing file
            String extension = getExtension(currentFile.toPath()); //get file extension

            try {
                Image currentImage = canvas.snapshot(null,null);
                BufferedImage saveFile = SwingFXUtils.fromFXImage(currentImage, null); //transform image to buffered image
                ImageIO.write(saveFile, extension, currentFile); //write the buffered image
                Notifications.create()
                        .title(currentFile.toString())
                        .text(currentFile.toString() + "was saved")
                        .showInformation();

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            updateServer();

        } else { //there is no file being worked on
            System.out.println("some error message saying you dont have a file to save yet");
            return saveAsFile();
        }
        return true;
    }

    private boolean saveAsFile() {
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


    private void showHelpWindow() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("I'm a help window. I tell you stuff about your program");

        alert.showAndWait();
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