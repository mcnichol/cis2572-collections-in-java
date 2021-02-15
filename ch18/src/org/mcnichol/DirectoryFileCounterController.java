package org.mcnichol;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;
import javafx.util.Duration;
import org.mcnichol.concurrency.counter.*;
import org.mcnichol.utility.CountingFileVisitor;
import org.mcnichol.utility.FILE_TYPE;
import org.mcnichol.utility.PredicateUtility;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

/**
 * <h1>DirectoryFileCounterController</h1>
 * This application uses a JavaFX UI to allow the user to select a directory and recursively or through the use of a
 * {@link java.nio.file.FileVisitor} walks the directory structure and dispalys a count of all files in the directory.
 * <p>
 * <b>Description:</b> This application leverages an {@link javafx.fxml.FXMLLoader} to construct a user interface and map
 * text and labels and register event handlers.  It then utilizes {@link javafx.beans.property.Property} beans for
 * one-waya bindings to offload events from the JavaFX UI Thread and instead update through a {@link Timeline}. These
 * beans are updated via a custom {@link CountingFileVisitor} which implements the {@link java.nio.file.FileVisitor} to
 * leverage a more optimized depth approach.
 * <p>
 * The default behavior is to leverage {@link DirectoryFileCounterController#countRecursivelyReturningTotals(Path)} method call that
 * recursively walks a {@link Stream<Path>} returning a summation of all files through leveraging the Streaming API.
 * <p>
 * <b>Usage:</b> This application can be run from the terminal by navigating to the root directory and running the
 * packaged JAR in the build directory or by compiling the classes and executing {@link Main#main(String[])} in a native
 * fashion.
 * <p>
 * Because this was written for a course 'Collections in Java' with Barry Speller from College of DuPage,
 * you must ensure you are using JDK 1.8 which is only packaged with Oracle, Coretto, or a handful of other 1.8 JDK's.
 * (Not AdoptOpenJDK). If you attempt to run the JAR with AOJDK 1.8 you will see the error:
 * <p>
 * <code>
 * $> /Users/compute/.sdkman/candidates/java/current/bin/java -jar /Users/compute/workspace/cis2572/ch18/out/artifacts/ch18/ch18.jar
 * Error: Could not find or load main class org.mcnichol.Main
 * </code>
 * <p>
 * If you have issues ensure your JAVA_HOME environment variable is set and leverage the full classpath to Java and the JAR file.
 * <ul>
 *     <li>{@code $> FULL_PATH_TO_1.8_JRE/bin/java -jar /FULL_PATH_TO_JAR/ch18.jar} (*Nix)</li>
 *     <li>{@code C:\FULL_PATH_TO_1.8_JRE\bin\java -jar FULL_PATH_TO_JAR\ch18.jar} (Win)</li>
 * </ul>
 * <p>
 * <b>Course Name:</b> Collections in Java
 * <b>Section:</b> CIS-2572-NET02
 * <b>Instructor:</b> Barry Speller
 *
 * @author Michael McNichol
 * @version 1.0
 * @since 2021/02/14
 */
public class DirectoryFileCounterController implements Initializable {

    private static final ExecutorService exec = Executors.newFixedThreadPool(10);

    @FXML
    public TextField txt_startingRoot;
    @FXML
    public TextField txt_totalFiles;
    @FXML
    public TextField txt_dirCount;
    @FXML
    public TextField txt_docCount;
    @FXML
    public TextField txt_imgCount;
    @FXML
    public TextField txt_musicCount;
    @FXML
    public TextField txt_otherCount;
    @FXML
    public TextField txt_videoCount;
    @FXML
    public TextField txt_currentDir;
    @FXML
    public ImageView imageView_processing;
    @FXML
    public Label lbl_readyState;
    @FXML
    public CheckBox checkbox_useFileVisitor;

    private final StringProperty fileCounter = new SimpleStringProperty();
    private final StringProperty dirCounter = new SimpleStringProperty();
    private final StringProperty imageCounter = new SimpleStringProperty();
    private final StringProperty musicCounter = new SimpleStringProperty();
    private final StringProperty videoCounter = new SimpleStringProperty();
    private final StringProperty docCounter = new SimpleStringProperty();
    private final StringProperty otherCounter = new SimpleStringProperty();
    private final BooleanProperty imageView_property = new SimpleBooleanProperty(false);
    private final StringProperty currentDirectoryProperty = new SimpleStringProperty();

    /**
     * Method called when button click event is fired
     *
     * @param event Event handler
     */
    @FXML
    void showDialog(ActionEvent event) {
        File directory = showDialogAndGetDirectoryFromUser(event);
        if (null == directory) return;

        resetCountersToZero();
        txt_startingRoot.setText(directory.getAbsolutePath());

        if (checkbox_useFileVisitor.isSelected()) {
            countFilesUsingFileVisitor(Paths.get(directory.getPath()));
        } else {
            exec.submit(() -> {
                imageView_property.set(true);
                FILE_COUNTER.set(countRecursivelyReturningTotals(Paths.get(directory.getPath())));
                imageView_property.set(false);
            });
        }
    }

    /**
     * Called to initialize a controller after its root element has been completely processed.
     * Params:
     *
     * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bindProperties();

        Timeline timeline = createTimelineUpdater();

        timeline.play();
    }

    /**
     * This method duplicates behavior for recursively counting is reserved for showing the multiple approaches.
     * This one will be preferred over {@link DirectoryFileCounterController#countRecursivelyReturningTotals(Path)} for its ability to update
     * totals in realtime without looking like a kluge
     * <p>
     *
     * @param filePath Path of current file or directory
     */
    private void countRecursivelyWithSentinel(Path filePath) {
        if (Files.isDirectory(filePath)) {
            DIRECTORY_COUNTER.count();
            try (Stream<Path> list = Files.list(filePath)) {
                list.forEach(thisFilePath -> {
                    if (Files.isDirectory(thisFilePath) && !Files.isSymbolicLink(thisFilePath)) {
                        countRecursivelyWithSentinel(thisFilePath);
                    } else {
                        FILE_COUNTER.count();
                        incrementFileType(thisFilePath.toString());
                    }
                });
            } catch (IOException e) {
                System.out.println("AccessDenied: Unable to visit file " + filePath.toString());

            }
        }
    }

    /**
     * This method counts recursively over all directories and sums the result. This is returned and updated in the user
     * interfaace. Upon each file returned, the associated file-type is incremented.
     * <p>
     *
     * @param filePath Path of current file or directory
     * @return count of files in directory
     */
    private long countRecursivelyReturningTotals(Path filePath) {
        if (!Files.isDirectory(filePath)) {
            incrementFileType(filePath.toString());
            return 1;
        }

        DIRECTORY_COUNTER.count();
        Platform.runLater(() -> updateCurrentDirectory(filePath.toString()));
        long sum = 0;

        try (Stream<Path> list = Files.list(filePath)) {
            sum = list.filter(PredicateUtility.not(Files::isSymbolicLink))
                    .mapToLong(this::countRecursivelyReturningTotals)
                    .sum();
        } catch (IOException e) {
            System.out.println("AccessDenied: Unable to visit file " + filePath.toString());
        }

        return sum;
    }

    /**
     * This method duplicates behavior but leverages a {@link java.nio.file.FileVisitor} as is recommended in more current
     * versions of JDK documentation.
     *
     * @param path Path of the file being leveraged.
     */
    private void countFilesUsingFileVisitor(Path path) {
        imageView_property.set(true);
        exec.submit(() -> {
            try {
                Files.walkFileTree(path, new CountingFileVisitor<>(this));
            } catch (IOException | UncheckedIOException e) {
                e.printStackTrace();
            }
            imageView_property.set(false);
        });
    }

    private File showDialogAndGetDirectoryFromUser(ActionEvent event) {
        Window callingWindow = ((Node) event.getSource()).getScene().getWindow();
        return new DirectoryChooser().showDialog(callingWindow);
    }

    private Timeline createTimelineUpdater() {
        KeyFrame keyFrame = new KeyFrame(
                Duration.seconds(.5),
                registerEventHandlers()
        );

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.getKeyFrames().add(keyFrame);
        return timeline;
    }

    private EventHandler<ActionEvent> registerEventHandlers() {
        return e -> Platform.runLater(this::updateTextAreaCounters);
    }

    private void resetCountersToZero() {
        DIRECTORY_COUNTER.reset();
        DOCUMENT_COUNTER.reset();
        FILE_COUNTER.reset();
        IMAGE_COUNTER.reset();
        MUSIC_COUNTER.reset();
        OTHER_COUNTER.reset();
        VIDEO_COUNTER.reset();
    }

    private void incrementFileType(String thisFile) {
        String currentFile = CountingFileVisitor.getRelativePathName(thisFile);
        FILE_TYPE fileType = CountingFileVisitor.getFileType(currentFile);
        FILE_TYPE.incrementType(fileType);
    }

    private void bindProperties() {
        updateTextAreaCounters();

        txt_totalFiles.textProperty().bind(fileCounter);
        txt_dirCount.textProperty().bind(dirCounter);
        txt_docCount.textProperty().bind(docCounter);
        txt_imgCount.textProperty().bind(imageCounter);
        txt_musicCount.textProperty().bind(musicCounter);
        txt_otherCount.textProperty().bind(otherCounter);
        txt_videoCount.textProperty().bind(videoCounter);
        txt_currentDir.textProperty().bind(currentDirectoryProperty);

        imageView_processing.visibleProperty().bind(imageView_property);
        lbl_readyState.visibleProperty().bind(imageView_property.not());
    }

    private void updateTextAreaCounters() {
        fileCounter.set(usingFormat(FILE_COUNTER.toLong()));
        dirCounter.set(usingFormat(DIRECTORY_COUNTER.toLong()));
        docCounter.set(usingFormat(DOCUMENT_COUNTER.toLong()));
        imageCounter.set(usingFormat(IMAGE_COUNTER.toLong()));
        musicCounter.set(usingFormat(MUSIC_COUNTER.toLong()));
        otherCounter.set(usingFormat(OTHER_COUNTER.toLong()));
        videoCounter.set(usingFormat(VIDEO_COUNTER.toLong()));
    }

    private String usingFormat(long l) {
        return String.format("%,d", l);
    }

    /**
     * This method is called for updating the User Interface with the current directory being walked.
     * <p>
     *
     * @param updatedDirectory The directory to update the User Interface with
     */
    public void updateCurrentDirectory(String updatedDirectory) {
        currentDirectoryProperty.set(CountingFileVisitor.getRelativePathName(updatedDirectory));
    }
}