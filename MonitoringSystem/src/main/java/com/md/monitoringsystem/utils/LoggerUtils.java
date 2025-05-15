package com.md.monitoringsystem.utils;

import java.io.IOException;
import java.util.logging.*;

public class LoggerUtils {
    private static Logger logger = Logger.getLogger(LoggerUtils.class.getName());

    static {
        try {

            FileHandler fileHandler = new FileHandler("application.log", true);


            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);

            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void main(String[] args) {
        Logger logger = LoggerUtils.getLogger();
        logger.info("hello");
    }
}

