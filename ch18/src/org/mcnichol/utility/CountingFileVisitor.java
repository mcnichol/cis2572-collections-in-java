package org.mcnichol.utility;

import javafx.application.Platform;
import org.mcnichol.Controller;
import org.mcnichol.concurrency.counter.DIRECTORY_COUNTER;
import org.mcnichol.concurrency.counter.FILE_COUNTER;
import org.mcnichol.utility.FILE_TYPE;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

public class CountingFileVisitor<T> implements FileVisitor<T> {

    private final Controller controller;

    public CountingFileVisitor(Controller controller) {
        this.controller = controller;
    }

    @Override
    public FileVisitResult preVisitDirectory(Object dir, BasicFileAttributes attrs) throws IOException {
        DIRECTORY_COUNTER.count();

        String currentDir = getRelativePathName(dir);

        Platform.runLater(() -> controller.updateCurrentDirectory(currentDir));

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
        FILE_TYPE type = FILE_TYPE.getType(getFileExtension(splitForFileExtension(currentFile)));
        if (type == FILE_TYPE.OTHER) {
            try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream("recursive_other_files.dat", true))) {
                osw.write(String.format("%s\n", currentFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return type;
    }

    public static String[] splitForFileExtension(String currentFile) {
        return currentFile.split("\\.");
    }

    public static String getFileExtension(String[] fileWithExtension) {
        return fileWithExtension.length > 1 ? fileWithExtension[fileWithExtension.length - 1] : fileWithExtension[0];
    }
}
