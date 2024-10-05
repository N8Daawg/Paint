package paint;

import javafx.scene.control.SelectionModel;
import javafx.scene.control.Tab;
import paint.Tabs.TabController;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class threadedLogger {
    private static final String loggername = "Paint logger";
    private static Logger logger;
    private SelectionModel<Tab> tabSelector;
    private ArrayList<TabController> tabs;
    public threadedLogger() {
        logger = Logger.getLogger(loggername);
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
                            + getFilename()
                            + ": "
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

    public void updateTabs(SelectionModel<Tab> newSelector, ArrayList<TabController> newTabs) {
        tabSelector = newSelector;
        tabs = newTabs;
    }
    private String getFilename(){
        if(tabSelector != null){
            return tabs.get(tabSelector.getSelectedIndex()).getFileController().getCurrentFile();
        } else {
            return "No file selected";
        }

    }

    public void sendMessage(String message) {logger.info(message);}
}
