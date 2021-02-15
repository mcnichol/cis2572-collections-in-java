package org.mcnichol.utility;

import javafx.application.Platform;
import org.mcnichol.DirectoryFileCounterController;
import org.mcnichol.concurrency.counter.DIRECTORY_COUNTER;
import org.mcnichol.concurrency.counter.FILE_COUNTER;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * A Custom {@link FileVisitor} used for incrementing ENUM counters without populating full {@link File} objects
 * <p>
 *
 * @param <T>
 */
public class CountingFileVisitor<T> implements FileVisitor<T> {

    /**
     * Controller passed via dependency injection
     */
    private final DirectoryFileCounterController directoryFileCounterController;

    /**
     * Constructor taking the UI Controller and providing a handle for testing and usability purposes.
     * <p>
     *
     * @param directoryFileCounterController UI Controller of the MVC format
     */
    public CountingFileVisitor(DirectoryFileCounterController directoryFileCounterController) {
        this.directoryFileCounterController = directoryFileCounterController;
    }

    @Override
    public FileVisitResult preVisitDirectory(Object dir, BasicFileAttributes attrs) throws IOException {
        DIRECTORY_COUNTER.count();

        Platform.runLater(() -> directoryFileCounterController.updateCurrentDirectory(dir.toString()));

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Object file, BasicFileAttributes attrs) throws IOException {
        if (!Files.isSymbolicLink(Paths.get(file.toString()))) {
            FILE_COUNTER.count();
            FILE_TYPE.incrementType(getFileType(getRelativePathName(file)));
        }

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Object file, IOException exc) throws IOException {
        System.out.println("AccessDenied: Unable to visit file " + file.toString());
        return FileVisitResult.SKIP_SUBTREE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Object dir, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    public static String getRelativePathName(Object dir) {
        String[] value = dir.toString().split(File.separator);

        return value.length > 1 ? value[value.length - 1] : "/";
    }

    public static FILE_TYPE getFileType(String currentFile) {
        return FILE_TYPE.getType(getFileExtension(splitForFileExtension(currentFile)));
    }

    public static String[] splitForFileExtension(String currentFile) {
        return currentFile.split("\\.");
    }

    public static String getFileExtension(String[] fileWithExtension) {
        return fileWithExtension.length > 1 ? fileWithExtension[fileWithExtension.length - 1] : fileWithExtension[0];
    }
}
