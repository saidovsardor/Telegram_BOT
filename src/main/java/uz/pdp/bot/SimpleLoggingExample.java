package uz.pdp.bot;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class SimpleLoggingExample {
    static {
        String file = SimpleLoggingExample.class.getClassLoader().getResource("logging.properties").getFile();
        System.setProperty("java.util.logging.config.file", file);
    }

    private static Logger logger = Logger.getLogger("MyLogger");

    public static void main(String[] args) {
        LogRecord logRecord = new LogRecord(Level.INFO, "Hello this is simple warning log");
        logger.log(logRecord);

    }
}