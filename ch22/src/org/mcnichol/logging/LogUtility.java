package org.mcnichol.logging;

import java.sql.Timestamp;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Logging Singleton with custom formatter
 */
public class LogUtility {

    private static Logger logger;

    public static Logger getInstance() {
        ConsoleHandler consoleHandler = configureLogFormat();

        if (logger == null) {
            logger = configureAndConstructLogger(consoleHandler);
        }

        return logger;
    }

    private static Logger configureAndConstructLogger(ConsoleHandler handler) {
        Logger logger = Logger.getLogger(LogUtility.class.toString());

        logger.setUseParentHandlers(false);
        logger.addHandler(handler);

        return logger;
    }

    private static ConsoleHandler configureLogFormat() {
        ConsoleHandler consoleHandler = new ConsoleHandler();

        consoleHandler.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                return String.format("%-23s %s - %s - %s%n", new Timestamp(record.getMillis()), record.getSourceClassName(), record.getLevel(), record.getMessage());
            }
        });

        return consoleHandler;
    }
}
