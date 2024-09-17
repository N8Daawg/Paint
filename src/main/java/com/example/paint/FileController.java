package com.example.paint;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class FileController {
    private Stage stage;
    MenuBar menuBar;
    private MenuItem openFile; //open file button
    private MenuItem saveFile; //save file button
    private MenuItem saveAsFile; //save as file button
    private MenuItem about; //opens the help window
    private ToolBar toolBar;
    private Canvas canvas;
    private File currentFile;
    private Boolean recentlySaved;
    public FileController(MenuBar newMenuBar, MenuItem newOpenFile, MenuItem newSaveFile, MenuItem newSaveAsFile,
                          MenuItem newAbout, ToolBar newToolBar, Canvas newCanvas){

        menuBar = newMenuBar;
        openFile = newOpenFile;
        saveFile = newSaveFile;
        saveAsFile = newSaveAsFile;
        about = newAbout;
        canvas = newCanvas;
        toolBar = newToolBar;
        recentlySaved = true;

        openFile.setOnAction(e -> {
            openFile();
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
    public Boolean wasRecentlySaved(){
        return recentlySaved;
    }

    /*---------------------------------------------------------------------------*/
    /*------------------------------Help window control--------------------------*/
    /*---------------------------------------------------------------------------*/

    private void openFile(){
        if (stage == null){ // in case the program hasn't grabbed the stage yet, grab it.
            stage = (Stage) menuBar.getScene().getWindow();
        }

        FileChooser fileChooser = new FileChooser(); //create a fileChooser to show the open dialog
        fileChooserSetup(fileChooser, currentFile, "File Selection"); // format the FileChooser
        currentFile = fileChooser.showOpenDialog(stage); //grab a file

        if(currentFile != null){
            Image currentImage = new Image(String.valueOf(currentFile)); //create Image from file that was grabbed

            canvas.setHeight(currentImage.getHeight()); //resize canvas to new image constraints
            canvas.setWidth(currentImage.getWidth());

            //resize the stage to fit the new image (have not tested if image is larger than the Monitor's screen)
            stage.setHeight(canvas.getHeight() + menuBar.getHeight() + toolBar.getHeight() + 50);
            stage.setWidth(currentImage.getWidth()+25);

            canvas.getGraphicsContext2D().drawImage(currentImage, 0,0);
        }
    }

    public Boolean saveFile() throws IOException {
        if(currentFile != null) { // if we are already working on an existing file
            String extension = getExtension(currentFile.toPath()); //get file extension

            Image currentImage = canvas.snapshot(null,null);
            BufferedImage saveFile = SwingFXUtils.fromFXImage(currentImage, null); //transform image to buffered image
            ImageIO.write(saveFile, extension, currentFile); //write the buffered image
        } else { //there is no file being worked on
            System.out.println("some error message saying you dont have a file to save yet");
            saveAsFile();
        }
        return true;
    }

    private boolean saveAsFile() throws IOException {
        if (stage == null){ // in case the program hasn't grabbed the stage yet, grab it.
            stage = (Stage) menuBar.getScene().getWindow();
        }

        FileChooser fileChooser = new FileChooser(); //create a fileChooser to show the open dialog
        fileChooserSetup(fileChooser, currentFile, "Save"); // format the FileChooser
        currentFile = fileChooser.showSaveDialog(stage); //Create the file name that is being saved
        if(currentFile != null){
            String extension = getExtension(currentFile.toPath()); //get file extension

            Image currentImage = canvas.snapshot(null, null);
            BufferedImage saveFile = SwingFXUtils.fromFXImage(currentImage, null); //transform image to buffered image
            ImageIO.write(saveFile, extension, currentFile); //write the buffered image
        }
        return true;
    }


    private void showHelpWindow() {
        if (stage == null){ // in case the program hasn't grabbed the stage yet, grab it.
            stage = (Stage) menuBar.getScene().getWindow();
        }

        Group root = new Group();
        Scene helpscene = new Scene(root, 320, 480);
        Label helpLabel = new Label();
        helpLabel.setText("This is a help window. I tell you what to do.");
        root.getChildren().add(helpLabel);

        Stage helpwindow = new Stage();
        helpwindow.setTitle("Help");
        helpwindow.setScene(helpscene);
        helpwindow.show();
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