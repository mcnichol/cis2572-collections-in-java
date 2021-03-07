package org.mcnichol;

import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.mcnichol.utility.UserInterfaceUtility;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * <h1>ShuntingYardController</h1>
 * This application uses a JavaFX UI to allow the user to determine if a file has a balanced set of (), {}, and [].
 * <p>
 * <b>Description:</b> This application leverages an {@link javafx.fxml.FXMLLoader} to construct a user interface and map
 * text, labels, and register event handlers.  It then utilizes {@link javafx.beans.property.Property} beans for
 * one-way bindings to offload events from the JavaFX UI Thread and instead update through a {@link Timeline}.
 * <p>
 * Application will generate 15 random Integer, Character, or Date objects adding them to associated stacks and
 * updating the user interface to reflect contents.  Then user can pop individual items from the stack or all items
 * at once.  These items are popped and displayed into the user interface in LIFO order.
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
 * @since 2021/03/06
 */
public class ShuntingYardController implements Initializable, UserInterfaceUtility {


    @FXML
    public TextField txtArea_fileLoad;
    @FXML
    public Button btn_fileLoad;

    /**
     * Called to initialize a controller after its root element has been completely processed.
     * Params:
     *
     * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeBindings();

        Timeline uiAnimationLoop = createUpdatingAnimationLoop((event) -> {

        });

        uiAnimationLoop.play();
    }

    /**
     * Setup initial bindings to be updated when RadioButton focusing stack triggers
     */
    private void initializeBindings() {

    }

    public void loadFile(ActionEvent event) {
        FileChooser fc = new FileChooser();
        File selectedFile = fc.showOpenDialog(btn_fileLoad.getScene().getWindow());
    }
}