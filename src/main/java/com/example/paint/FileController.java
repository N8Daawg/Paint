package com.example.paint;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
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

    /**
     * Instantiates a new File controller.
     *
     * @param newMenuBar the new menu bar
     * @param newCanvas  the new canvas
     */
    public FileController(MenuBar newMenuBar, Canvas newCanvas){
        menuBar = newMenuBar;

        Menu fileMenu = menuBar.getMenus().get(0);
        openFile = fileMenu.getItems().get(0);
        saveFile = fileMenu.getItems().get(1);
        saveAsFile = fileMenu.getItems().get(2);

        Menu helpMenu = menuBar.getMenus().get(2);
        about = helpMenu.getItems().get(0);

        canvas = newCanvas;
        recentlySaved = true;
        postInitSetup();
    }

    /**
     * Post init setup.
     */
    public void postInitSetup(){
        if (menuBar.getScene() != null){
            stage = (Stage) menuBar.getScene().getWindow(); // attempt to grab the stage.
            openFile.setOnAction(e -> {
                try { openFile();
                } catch (MalformedURLException ex) { throw new RuntimeException(ex);
                }
            });
            saveFile.setOnAction(e -> {
                try { recentlySaved = saveFile();
                } catch (IOException ex) { throw new RuntimeException(ex);
                }
            });
            saveAsFile.setOnAction(e -> {
                try { recentlySaved = saveAsFile();
                } catch (IOException ex) { throw new RuntimeException(ex);
                }
            });
            about.setOnAction(e -> {
                showHelpWindow();
            });
        }
    }

    /**
     * Was recently saved boolean.
     *
     * @return the boolean
     */
    public Boolean wasRecentlySaved(){
        return recentlySaved;
    }

    /*---------------------------------------------------------------------------*/
    /*------------------------------Help window control--------------------------*/
    /*---------------------------------------------------------------------------*/

    private void openFile() throws MalformedURLException {
        FileChooser fileChooser = new FileChooser(); //create a fileChooser to show the open dialog
        fileChooserSetup(fileChooser, currentFile, "File Selection"); // format the FileChooser
        currentFile = fileChooser.showOpenDialog(stage); //grab a file

        if(currentFile != null){
            Image currentImage = new Image(String.valueOf(currentFile.toURI().toURL()));//create Image from file that was grabbed
            canvas.getGraphicsContext2D().drawImage(currentImage, 0,0);
        }
    }

    /**
     * Save file boolean.
     *
     * @return the boolean
     * @throws IOException the io exception
     */
    public Boolean saveFile() throws IOException {
        if(currentFile != null) { // if we are already working on an existing file
            String extension = getExtension(currentFile.toPath()); //get file extension

            Image currentImage = canvas.snapshot(null,null);
            BufferedImage saveFile = SwingFXUtils.fromFXImage(currentImage, null); //transform image to buffered image
            ImageIO.write(saveFile, extension, currentFile); //write the buffered image
        } else { //there is no file being worked on
            System.out.println("some error message saying you dont have a file to save yet");
            return saveAsFile();
        }
        return true;
    }

    private boolean saveAsFile() throws IOException {
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
                    // ... user chose OK
                    Image currentImage = canvas.snapshot(null, null);
                    BufferedImage saveFile = SwingFXUtils.fromFXImage(currentImage, null); //transform image to buffered image
                    ImageIO.write(saveFile, extension, currentFile); //write the buffered image
                    return true;
                } else {
                    // ... user chose CANCEL or closed the dialog
                    currentFile = previousFile;
                    return false;
                }
            } else{
                Image currentImage = canvas.snapshot(null, null);
                BufferedImage saveFile = SwingFXUtils.fromFXImage(currentImage, null); //transform image to buffered image
                ImageIO.write(saveFile, extension, currentFile); //write the buffered image
                return true;
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

    // method to read file extensions found from GFG website:
    // https://www.geeksforgeeks.org/how-to-get-the-file-extension-in-java/
    private static String getExtension(Path path) {
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