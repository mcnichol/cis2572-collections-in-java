package org.mcnichol;

import java.io.*;
import java.util.Arrays;
import java.util.OptionalDouble;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.IntStream;

/**
 * <h1>BinaryFileReadWriter</h1>
 * This application writes fifty random integers to a file in binary format and then reads all values from same file
 * and prints the max, min, mean, and sum statistics of the file.
 * <p>
 * <b>Description:</b> This application leverages a static initializer block to configure formatting for the root
 * {@link Logger} in addition to the default {@link java.util.logging.FileHandler} and {@link java.util.logging.ConsoleHandler}.
 * It accepts a system property of <b>logLevel</b> which can be set to any of the logging levels defined in {@link Level}.
 * <p>
 * It then initializes a {@link BinaryFileReadWriter#statistics} array leveraging
 * {@link BinaryFileReadWriter#initializeStatsArray()} which populates the associated indexes with default starting
 * values. An enum {@link STATISTICS} is used for meaningful indexing to ease readability and maintenance for the user.
 * <p>
 * The {@link BinaryFileReadWriter#generateRandomIntsAndWriteToFile()} method is called where a {@link FileOutputStream}
 * is created sourcing the {@link BinaryFileReadWriter#DATA_FILE} location with the append flag set to true. This is
 * passed to a {@link DataOutputStream} which will be written to by an {@link IntStream} of 50 random integers populated
 * by {@link BinaryFileReadWriter#generateRandomIntStream()}.
 * <p>
 * <b>Note:</b> During the write operation to the {@link DataOutputStream} an {@link IOException} can be thrown from
 * within the {@link IntStream}. To handle this checked exception a {@link FunctionalInterface}
 * {@link java.util.function.IntConsumer} is created and the {@link IntStream} is cast to this to ensure proper handling.
 * <p>
 * The {@link BinaryFileReadWriter#readIntegerFileAndPopulateStatistics()} ()} method is called where an
 * {@link IntStream#builder()} is created for storing all integers read from a binary file. A {@link FileInputStream}
 * is created using the binary {@link BinaryFileReadWriter#DATA_FILE} which is passed into a {@link DataInputStream}.
 * For each {@link Integer} read from this file, it runs a check on whether it is the max or min and summed using:<br>
 *     <ul>
 *         <li>{@link BinaryFileReadWriter#checkIfMaximumValueAndUpdateStatistics(int)}</li>
 *         <li>{@link BinaryFileReadWriter#checkIfMinimumValueAndUpdateStatistics(int)}</li>
 *         <li>{@link BinaryFileReadWriter#incrementSumAndUpdateStatistics(int)}</li>
 *     </ul>
 * <p>
 * This file is read from until hitting an {@link EOFException} where it is caught and the mean is computed using
 * {@link BinaryFileReadWriter#computeMeanOfIntStreamAndUpdateStatistics(IntStream)}.
 * <p>
 * <b>Note:</b> If an {@link IOException} occurs during the read process the exception is caught and the system will exit.
 * <p>
 * At the completion of the program the statistics are formatted and printed to the active {@link java.util.logging.Handler}
 * by calling {@link BinaryFileReadWriter#printStatistics()}
 * <p>
 * <b>Usage:</b> This application can be run from the terminal by navigating to the root directory and running the gradle
 * wrapper on both *Nix and Windows systems. If you have issues ensure your JAVA_HOME environment variable is set and
 * can be seen from your PATH.
 * <ul>
 *     <li>{@code ./gradlew run} (*Nix)</li>
 *     <li>{@code gradlew.bat run } (win)</li>
 * </ul>
 * <p>
 * <b>Note: </b> Logging levels can be configured by setting the gradle property  <code>./gradlew run -DlogLevel=FINE</code>
 * <p>
 * <b>Course Name:</b> Collections in Java
 * <b>Section:</b> CIS-2572-NET02
 * <b>Instructor:</b> Barry Speller
 *
 * @author Michael McNichol
 * @version 1.0
 * @since 2021/01/29
 */
public class BinaryFileReadWriter {
    static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT - %4$s - %5$s %n");
        String logLevel = System.getProperty("logLevel");
        Level configuredLevel = logLevel.isEmpty() ? Level.INFO : Level.parse(logLevel);

        Logger rootLogger = LogManager.getLogManager().getLogger("");
        rootLogger.setLevel(configuredLevel);
        Arrays.stream(rootLogger.getHandlers()).forEach(handler -> handler.setLevel(configuredLevel));
    }

    private static final Logger logger = Logger.getLogger(BinaryFileReadWriter.class.getName());
    private static final String DATA_FILE = "chapter17.dat";
    private static final double[] statistics = initializeStatsArray();

    public static void main(String[] args) {
        generateRandomIntsAndWriteToFile();

        readIntegerFileAndPopulateStatistics();

        printStatistics();
    }

    private static double[] initializeStatsArray() {
        return new double[]{Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0};
    }

    /**
     * Creates a {@link DataOutputStream} from {@link BinaryFileReadWriter#DATA_FILE} and populates it with 50 random
     * integers passed via an {@link IntStream}. As each value in the {@link IntStream} is iterated over a, it is cast
     * to a {@link FunctionalInterface} of type {@link CheckedFileLogAndWrite} to handle the checked {@link IOException}
     */
    private static void generateRandomIntsAndWriteToFile() {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(DATA_FILE, true))) {
            logger.log(Level.INFO, "Generating 50 Random Integers");
            IntStream intStream = generateRandomIntStream();

            logger.log(Level.INFO, "Displaying and Writing 50 Random Integers");
            intStream.forEach((CheckedFileLogAndWrite) dos::writeInt);
        } catch (IOException e) {
            logger.log(Level.SEVERE, String.format("Error creating output stream from file %s", DATA_FILE));
        }
    }

    /**
     * @return An {@link IntStream} populated with 50 random integers between 0 (inclusive) and 100_000 (exclusive)
     */
    private static IntStream generateRandomIntStream() {
        IntStream.Builder builder = IntStream.builder();

        for (int i = 0; i < 50; i++) {
            builder.add(getRandomInt());
        }

        return builder.build();
    }

    private static int getRandomInt() {
        return (int) (Math.random() * 100_000);
    }

    /**
     * Creates a DataInputStream from {@link BinaryFileReadWriter#DATA_FILE} and populates {@link BinaryFileReadWriter#statistics} with
     * with max, min, mean, and average of included file.
     */
    private static void readIntegerFileAndPopulateStatistics() {
        IntStream.Builder builder = IntStream.builder();
        try (DataInputStream dis = new DataInputStream(new FileInputStream(DATA_FILE))) {
            while (true) {
                int currentValue = dis.readInt();
                logger.log(Level.INFO, String.format("Reading value %d from file %s", currentValue, DATA_FILE));
                builder.add(currentValue);

                checkIfMaximumValueAndUpdateStatistics(currentValue);
                checkIfMinimumValueAndUpdateStatistics(currentValue);
                incrementSumAndUpdateStatistics(currentValue);
            }
        } catch (EOFException e) {
            computeMeanOfIntStreamAndUpdateStatistics(builder.build());
            logger.log(Level.INFO, String.format("Completed reading all data from %s", DATA_FILE));
        } catch (IOException e) {
            logger.log(Level.SEVERE, String.format("Error creating input stream from file %s.%nProgram Exiting", DATA_FILE));
            System.exit(-1);
        }
    }

    /**
     * Checks for highest value and stores result in MAX statistics index location if change detected.
     * Logs at a FINE level when a change occurs
     *
     * @param currentValue Current value accessed from input stream
     */
    private static void checkIfMaximumValueAndUpdateStatistics(int currentValue) {
        if (currentValue > statistics[STATISTICS.MAX.index]) {
            logger.log(Level.FINE, String.format("New Maximum Value Found: Updating max value from %.0f to %d", statistics[STATISTICS.MAX.index], currentValue));
            statistics[STATISTICS.MAX.index] = currentValue;
        }
    }

    /**
     * Checks for lowest value and stores result in MIN statistics index location if change detected.
     * Logs at a FINE level when a change occurs
     *
     * @param currentValue Current value accessed from input stream
     */
    private static void checkIfMinimumValueAndUpdateStatistics(int currentValue) {
        if (currentValue < statistics[STATISTICS.MIN.index]) {
            logger.log(Level.FINE, String.format("New Minimum Value Found: Updating min value from %.0f to %d", statistics[STATISTICS.MIN.index], currentValue));
            statistics[STATISTICS.MIN.index] = currentValue;
        }
    }

    private static void incrementSumAndUpdateStatistics(int currentValue) {
        statistics[STATISTICS.SUM.index] += currentValue;
    }

    /**
     * Returns the mean from an IntStream returning a zero value if IntStream is empty
     *
     * @param stream An IntStream of values read from {@link BinaryFileReadWriter#DATA_FILE}
     */
    private static void computeMeanOfIntStreamAndUpdateStatistics(IntStream stream) {
        OptionalDouble optMean = stream.average();

        statistics[STATISTICS.MEAN.index] = optMean.isPresent() ? optMean.getAsDouble() : 0;
    }

    private static void printStatistics() {
        logger.log(Level.INFO, String.format("File Statistics:%n\tMax:\t%,11.0f%n\tMin:\t%,11.0f%n\tMean:\t%,11.2f%n\tSum:\t%,11.0f", statistics[STATISTICS.MAX.index], statistics[STATISTICS.MIN.index], statistics[STATISTICS.MEAN.index], statistics[STATISTICS.SUM.index]));
    }
}
