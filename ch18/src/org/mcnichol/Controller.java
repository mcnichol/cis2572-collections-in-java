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

public class Controller implements Initializable {

    private static final ExecutorService exec = Executors.newFixedThreadPool(2000);

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
                countRecursivelyWithSentinel(Paths.get(directory.getPath()));
                imageView_property.set(false);
            });

            exec.submit(() -> {
                imageView_property.set(true);
                FILE_COUNTER.set(countRecursivelyReturningTotals(Paths.get(directory.getPath())));
                imageView_property.set(false);
            });
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bindProperties();

        Timeline timeline = createTimelineUpdater();

        timeline.play();
    }

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

    private long countRecursivelyReturningTotals(Path filePath) {
        if (!Files.isDirectory(filePath)) {
            incrementFileType(filePath.toString());
            return 1;
        }

        DIRECTORY_COUNTER.count();
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

    public void updateCurrentDirectory(String updatedDirectory) {
        currentDirectoryProperty.set(updatedDirectory);
    }
}