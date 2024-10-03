package paint;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.*;

public class PaintApplication extends Application {
    public static final String loggerName = "Paint Logger";
    private static Logger logger = Logger.getLogger(loggerName);
    @Override
    public void start(Stage stage) throws IOException {
        loggerSetup();
        FXMLLoader fxmlLoader = new FXMLLoader(PaintApplication.class.getResource("test-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 720, 480);
        stage.setTitle("Paint");
        stage.setScene(scene);

        logger.info("Starting Paint");
        stage.show();

        //PaintController.postInitializationSetup();
        //stage.setOnCloseRequest(PaintController::smartCloseWindow); //setup for smart save
        //PaintController.resize();
    }
    public static void loggerSetup(){
        logger.setUseParentHandlers(false);
        FileHandler fh;

        try {
            // This block configure the logger with handler and formatter
            fh = new FileHandler(System.getProperty("user.dir") + "\\PaintLog.txt", true);
            fh.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord record) {
                    SimpleDateFormat logTime = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                    Calendar cal = new GregorianCalendar();
                    cal.setTimeInMillis(record.getMillis());
                    return record.getLevel()
                            + ": "
                            + logTime.format(cal.getTime())
                            + " || "
                            + record.getSourceClassName().substring(
                            record.getSourceClassName().lastIndexOf(".")+1,
                            record.getSourceClassName().length())
                            + "."
                            + record.getSourceMethodName()
                            + "() : "
                            + record.getMessage() + "\n";
                }
            });
            logger.addHandler(fh);

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}