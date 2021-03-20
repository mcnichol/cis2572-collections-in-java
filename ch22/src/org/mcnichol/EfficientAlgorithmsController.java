package org.mcnichol;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.mcnichol.algorithm.Listing22_5;
import org.mcnichol.algorithm.Listing22_6;
import org.mcnichol.algorithm.Listing22_7;
import org.mcnichol.algorithm.PrimeComputer;
import org.mcnichol.tableview.PrimeComputerTableRow;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * <h1>EfficientAlgorithmsController</h1>
 * Populate and display runtimes of algorithm implementations execution times for calculating Primes up to a specified limit
 * <p>
 * <b>Description:</b>  This application uses a JavaFX UI that builds and populates a table with run times for each algorithm implementation
 * for finding prime computers.  Each {@link PrimeComputer} running duration is captured and returned. This value alongside
 * the classname of each PrimeComputer implementation is mapped to a {@link PrimeComputerTableRow} and presented in the
 * JavaFX UI {@link TableView<PrimeComputerTableRow>}
 * <p>
 * <b>Usage:</b> This application can be run from the terminal by navigating to the root directory and running the
 * packaged JAR in the build directory or by compiling the classes and executing {@link Main#main(String[])} in a native
 * fashion.
 * <p>
 * <b>Note:</b>Due to this application being written for a course 'Collections in Java' with Barry Speller from College of DuPage,
 * you must ensure you are using JDK 1.8 which is only packaged with Oracle, Coretto, or a handful of other 1.8 JDK's.
 * (Not AdoptOpenJDK). If you attempt to run the JAR with AOJDK 1.8 you will see the error:
 * <p>
 * {@code
 * $> /Users/compute/.sdkman/candidates/java/current/bin/java -jar /Users/compute/workspace/cis2572/ch18/out/artifacts/ch18/ch18.jar
 * Error: Could not find or load main class org.mcnichol.Main
 * }
 * <p>
 * If you have issues ensure your JAVA_HOME environment variable is set and leverage the full classpath to Java and the JAR file.
 * <ul>
 *     <li>{@code $> FULL_PATH_TO_1.8_JRE/bin/java -jar /FULL_PATH_TO_JAR/application.jar} (*Nix)</li>
 *     <li>{@code C:\FULL_PATH_TO_1.8_JRE\bin\java -jar FULL_PATH_TO_JAR\application.jar} (Win)</li>
 * </ul>
 * <p>
 * <b>Course Name:</b> Collections in Java
 * <b>Section:</b> CIS-2572-NET02
 * <b>Instructor:</b> Barry Speller
 *
 * @author Michael McNichol
 * @version 1.0
 * @since 2021/03/19
 */
public class EfficientAlgorithmsController implements Initializable {
    ExecutorService es = Executors.newFixedThreadPool(4, createThreadPool());

    @FXML
    public TableView<PrimeComputerTableRow> tblView_allPrimes;

    @FXML
    public Label lbl_processingMessage;
    @FXML
    public Button btn_run;
    private PrimeComputer[] primeComputerArray;
    private Integer[] primeComputerRunLimit;
    private int runCount;

    /**
     * Called to initialize a controller after its root element has been completely processed.
     * Params:
     *
     * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        primeComputerRunLimit = new Integer[]{8_000_000, 10_000_000, 12_000_000, 14_000_000, 16_000_000, 18_000_000};
        primeComputerArray = generatePrimeComputerArray();
        mapTableViewColumnsToEntityFields();
    }

    private Long[] runPrimeComputerAndGenerateTimings(PrimeComputer computer, Integer[] primeComputerRunLimits) {
        return computer.getExecutionTimeOfPrimeComputations(primeComputerRunLimits);
    }

    private PrimeComputer[] generatePrimeComputerArray() {
        return new PrimeComputer[]{
                new Listing22_5(),
                new Listing22_6(),
                new Listing22_7()
        };
    }

    /**
     * Create Mapping of PrimeComputer domain entity execution times to a Table Row inside the TableView
     */
    private void mapTableViewColumnsToEntityFields() {
        ObservableList<TableColumn<PrimeComputerTableRow, ?>> columns = tblView_allPrimes.getColumns();

        TableColumn<PrimeComputerTableRow, ?> implementationColumn = columns.get(0);
        implementationColumn.setCellValueFactory(new PropertyValueFactory<>("implementation"));

        ObservableList<TableColumn<PrimeComputerTableRow, ?>> executionTimeColumns = columns.get(1).getColumns();
        TableColumn<PrimeComputerTableRow, ?> primeComputerColumnIndex0 = executionTimeColumns.get(0);
        primeComputerColumnIndex0.setCellValueFactory(new PropertyValueFactory<>("primeComputerColumnIndex0"));

        TableColumn<PrimeComputerTableRow, ?> primeComputerColumnIndex1 = executionTimeColumns.get(1);
        primeComputerColumnIndex1.setCellValueFactory(new PropertyValueFactory<>("primeComputerColumnIndex1"));

        TableColumn<PrimeComputerTableRow, ?> primeComputerColumnIndex2 = executionTimeColumns.get(2);
        primeComputerColumnIndex2.setCellValueFactory(new PropertyValueFactory<>("primeComputerColumnIndex2"));

        TableColumn<PrimeComputerTableRow, ?> primeComputerColumnIndex3 = executionTimeColumns.get(3);
        primeComputerColumnIndex3.setCellValueFactory(new PropertyValueFactory<>("primeComputerColumnIndex3"));

        TableColumn<PrimeComputerTableRow, ?> primeComputerColumnIndex4 = executionTimeColumns.get(4);
        primeComputerColumnIndex4.setCellValueFactory(new PropertyValueFactory<>("primeComputerColumnIndex4"));

        TableColumn<PrimeComputerTableRow, ?> primeComputerColumnIndex5 = executionTimeColumns.get(5);
        primeComputerColumnIndex5.setCellValueFactory(new PropertyValueFactory<>("primeComputerColumnIndex5"));

        TableColumn<PrimeComputerTableRow, ?> runTimeColumn = columns.get(2);
        runTimeColumn.setCellValueFactory(new PropertyValueFactory<>("runCount"));
    }

    /**
     * Execute Prime Computer in background thread and leverage {@link Platform#runLater(Runnable)} for updating JavaFX UI Threads
     */
    public void executePrimeComputers() {
        runCount++;
        btn_run.setDisable(true);
        Platform.runLater(() -> es.execute(() -> {
            for (PrimeComputer thisComputer : primeComputerArray) {
                Platform.runLater(() -> lbl_processingMessage.setText(String.format("Executing Algorithm Implementation:\n%s", thisComputer.getClass().getSimpleName())));

                Long[] executionTimeOfPrimeComputations = runPrimeComputerAndGenerateTimings(thisComputer, primeComputerRunLimit);

                PrimeComputerTableRow primeComputerTableRow = new PrimeComputerTableRow(thisComputer, executionTimeOfPrimeComputations, runCount);

                Platform.runLater(() -> tblView_allPrimes.getItems().add(primeComputerTableRow));
            }

            Platform.runLater(() -> lbl_processingMessage.setText("Completed.\nClick 'Run' to re-execute"));

            btn_run.setDisable(false);
        }));
    }

    /**
     * Make sure all threads run as daemons
     *
     * @return Daemon thread
     */
    private ThreadFactory createThreadPool() {
        return r -> {
            Thread t = Executors.defaultThreadFactory().newThread(r);
            t.setDaemon(true);
            return t;
        };
    }
}