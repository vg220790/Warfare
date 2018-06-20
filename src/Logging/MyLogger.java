package Logging;


import java.io.IOException;
import java.util.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyLogger extends Logger {
    private ConsoleHandler consoleHandler;
    private Map<String,MyFileHandler> handlers;

    public MyLogger(String name) {
        super(name,null);
        consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new MyFormatter());
        setUseParentHandlers(false);
        addHandler(consoleHandler);
        handlers = new HashMap<>();
    }

    public void addHandler(String fileName, String sourceClassName){
        MyFileHandler theHandler;
        try {
            if(handlers.get(fileName) == null) {
                theHandler = new MyFileHandler( "logs//" + fileName, sourceClassName);
                theHandler.setFormatter(new MyFormatter());
                addHandler(theHandler);
                handlers.put(fileName,theHandler);
            }


        } catch (IOException e) {
            log(Level.SEVERE, " Exception " + e.getMessage());
        }
    }

    public Map<String,MyFileHandler> getMyFileHandlers(){
        return handlers;
    }


}
