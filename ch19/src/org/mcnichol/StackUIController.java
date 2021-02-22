package org.mcnichol;

import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.mcnichol.utility.FormattedDate;
import org.mcnichol.utility.STACK_TOGGLE;
import org.mcnichol.utility.UserInterfaceUtility;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * <h1>StackUIController</h1>
 * This application uses a JavaFX UI to allow the user to randomly generate and populate a {@link MyGenericStack}. Once
 * as each value is placed on the stack the minimum and maximum objects are updated showing min and max values when all
 * values have been added. Finally the stack is unloaded to display all items in reverse order.
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
 * @since 2021/02/14
 */
public class StackUIController implements Initializable, UserInterfaceUtility {

    private final MyGenericStack<Integer> integerStack = new MyGenericStack<>();
    private final MyGenericStack<Character> characterStack = new MyGenericStack<>();
    private final MyGenericStack<Date> dateStack = new MyGenericStack<>();

    private final StringProperty genericStackTypedTitleProperty = new SimpleStringProperty();
    private final StringProperty integerStackProperty = new SimpleStringProperty();
    private final StringProperty characterStackProperty = new SimpleStringProperty();
    private final StringProperty dateStackProperty = new SimpleStringProperty();
    private final StringProperty minValueProperty = new SimpleStringProperty();
    private final StringProperty maxValueProperty = new SimpleStringProperty();

    @FXML
    public TextArea txtArea_stackContents;
    @FXML
    public TextField txtArea_max;
    @FXML
    public TextField txtArea_min;
    @FXML
    public TextArea txtArea_console;
    @FXML
    public ToggleGroup toggleGroup_stackSelector;
    @FXML
    public Label lbl_genericStackTitle;

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

        Timeline uiAnimationLoop = createUpdatingAnimationLoop(createBackgroundEventHandlers());

        uiAnimationLoop.play();
    }

    private void initializeBindings() {
        lbl_genericStackTitle.textProperty().bind(genericStackTypedTitleProperty);

        txtArea_stackContents.textProperty().bind(integerStackProperty);
        txtArea_min.textProperty().bind(minValueProperty);
        txtArea_max.textProperty().bind(maxValueProperty);
    }

    @FXML
    private void updateStackBindings() {
        switch (STACK_TOGGLE.valueOf(getSelectedToggleName())) {
            case CHARACTER:
                txtArea_stackContents.textProperty().bind(characterStackProperty);
                break;
            case DATE:
                txtArea_stackContents.textProperty().bind(dateStackProperty);
                break;
            case INTEGER:
            default:
                txtArea_stackContents.textProperty().bind(integerStackProperty);
        }
    }

    /**
     * Method called when button click event is fired
     */
    @FXML
    void generateRandomStackValues() {
        RadioButton selectedToggle = (RadioButton) toggleGroup_stackSelector.getSelectedToggle();
        switch (STACK_TOGGLE.valueOf(selectedToggle.getText().toUpperCase())) {
            case CHARACTER:
                for (int i = 0; i < 15; i++) {
                    characterStack.push(generateRandomViewableCharacters());
                }
                break;
            case DATE:
                for (int i = 0; i < 15; i++) {
                    dateStack.push(generateRandomFormattedDates());
                }
                break;
            case INTEGER:
            default:
                for (int i = 0; i < 15; i++) {
                    integerStack.push(generateRandomIntegerValues(1000));
                }
        }
    }

    private int generateRandomIntegerValues(int upperLimitInclusive) {
        return (int) (Math.random() * upperLimitInclusive) + 1;
    }

    private char generateRandomViewableCharacters() {
        int upperLimit = 126;
        int lowerLimit = 33;

        return (char) (Math.random() * (upperLimit - lowerLimit) + lowerLimit);
    }

    private FormattedDate generateRandomFormattedDates() {
        int yearUpperLimit = 2070;
        int yearLowerLimit = 1970;
        double x = Math.random();

        int year = (int) (x * (yearUpperLimit - yearLowerLimit) + yearLowerLimit);
        int month = generateRandomIntegerValues(12);
        int dayOfMonth = generateRandomIntegerValues(28);

        return getFormattedDate(year, month, dayOfMonth);
    }

    private FormattedDate getFormattedDate(int year, int month, int dayOfMonth) {
        return new FormattedDate(Date.from(LocalDate.of(year, month, dayOfMonth).atStartOfDay().toInstant(ZoneOffset.ofHours(-5))));
    }

    /**
     * Method called when button click event is fired
     */
    @FXML
    void popOneFromStack() {
        String selectedToggle = String.valueOf(toggleGroup_stackSelector.getSelectedToggle().getUserData());

        switch (STACK_TOGGLE.valueOf(selectedToggle.toUpperCase())) {
            case CHARACTER:
                Character poppedCharacter = characterStack.pop();
                txtArea_console.appendText(String.format("Popped Character:\t%s\n $> ", poppedCharacter == null ? "Empty" : poppedCharacter));

                break;
            case DATE:
                Date poppedDate = dateStack.pop();
                txtArea_console.appendText(String.format("Popped Date:\t%11s\n $> ", poppedDate == null ? "Empty" : poppedDate));
                break;
            case INTEGER:
            default:
                Integer poppedInteger = integerStack.pop();
                txtArea_console.appendText(String.format("Popped Integer:\t%5s\n $> ", poppedInteger == null ? "Empty" : poppedInteger));
        }
    }

    /**
     * Method called when button click event is fired
     */
    @FXML
    void popAllFromStack() {
        String selectedToggle = String.valueOf(toggleGroup_stackSelector.getSelectedToggle().getUserData());

        switch (STACK_TOGGLE.valueOf(selectedToggle.toUpperCase())) {
            case CHARACTER:
                while (characterStack.size() > 0) {
                    Character poppedCharacter = characterStack.pop();
                    txtArea_console.appendText(String.format("Popped Character:\t%s\n $> ", poppedCharacter == null ? "Empty" : poppedCharacter));
                }
                break;
            case DATE:
                while (dateStack.size() > 0) {
                    Date poppedDate = dateStack.pop();
                    txtArea_console.appendText(String.format("Popped Date:\t%11s\n $> ", poppedDate == null ? "Empty" : poppedDate));
                }
                break;
            case INTEGER:
            default:
                while (integerStack.size() > 0) {
                    Integer poppedInteger = integerStack.pop();
                    txtArea_console.appendText(String.format("Popped Integer:\t%5s\n $> ", poppedInteger == null ? "Empty" : poppedInteger));
                }

        }

    }

    private void updateUserInterface() {
        String selectedToggleName = getSelectedToggleName();

        updateStackContentsInUserInterface(selectedToggleName);
        updateMinMaxInUserInterface(selectedToggleName);
        updatedStackTitleTypeInUserInterface(selectedToggleName);
    }

    private void updatedStackTitleTypeInUserInterface(String selectedToggleName) {
        switch (STACK_TOGGLE.valueOf(selectedToggleName.toUpperCase())) {
            case CHARACTER:
                genericStackTypedTitleProperty.setValue("MyGenericStack<Character>");
                break;
            case DATE:
                genericStackTypedTitleProperty.setValue("MyGenericStack<Date>");
                break;
            case INTEGER:
            default:
                genericStackTypedTitleProperty.setValue("MyGenericStack<Integer>");
        }

    }

    private void updateMinMaxInUserInterface(String selectedToggleName) {
        switch (STACK_TOGGLE.valueOf(selectedToggleName.toUpperCase())) {
            case CHARACTER:
                minValueProperty.set(characterStack.min() == null ? "Empty" : String.valueOf(characterStack.min()));
                maxValueProperty.set(characterStack.max() == null ? "Empty" : String.valueOf(characterStack.max()));
                break;
            case DATE:
                minValueProperty.set(dateStack.min() == null ? "Empty" : String.valueOf(dateStack.min()));
                maxValueProperty.set(dateStack.max() == null ? "Empty" : String.valueOf(dateStack.max()));
                break;
            case INTEGER:
            default:
                minValueProperty.set(integerStack.min() == null ? "Empty" : String.valueOf(integerStack.min()));
                maxValueProperty.set(integerStack.max() == null ? "Empty" : String.valueOf(integerStack.max()));
        }

    }

    private void updateStackContentsInUserInterface(String selectedToggleName) {
        switch (STACK_TOGGLE.valueOf(selectedToggleName.toUpperCase())) {
            case CHARACTER:
                characterStackProperty.set(characterStack.toString());
                break;
            case DATE:
                dateStackProperty.set(dateStack.toString());
                break;
            case INTEGER:
            default:
                integerStackProperty.set(integerStack.toString());
        }
    }

    private String getSelectedToggleName() {
        return String.valueOf(toggleGroup_stackSelector.getSelectedToggle().getUserData()).toUpperCase();
    }

    private EventHandler<ActionEvent> createBackgroundEventHandlers() {
        return e -> Platform.runLater(this::updateUserInterface);
    }
}