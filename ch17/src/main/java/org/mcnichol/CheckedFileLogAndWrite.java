package org.mcnichol;

import java.io.IOException;
import java.util.function.IntConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

@FunctionalInterface
public interface CheckedFileLogAndWrite extends IntConsumer {
    @Override
    default void accept(final int val) {
        Logger logger = Logger.getLogger(this.getClass().getName());

        try {
            logger.log(Level.INFO, String.format("From Class %s > Writing %d to file", this.getClass().getName(), val));
            acceptThrows(val);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Unable to write record to file");
        }
    }

    void acceptThrows(int val) throws IOException;
}
