package org.mcnichol;

import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <h1>StateLexiconController</h1>
 * This application uses a JavaFX UI to allow the user to enter a state and then populates it into a prefilled JavaFX
 * Button ComboBox.  The newly filled values are automatically selected and input fields are cleared.
 * <p>
 * <b>Description:</b> This application leverages an {@link javafx.fxml.FXMLLoader} to construct a user interface and map
 * text, labels, and register event handlers.  It then utilizes {@link javafx.beans.property.Property} beans for
 * one-way bindings to offload events from the JavaFX UI Thread and instead update through a {@link Timeline}.
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
public class StateLexiconController implements Initializable {
    private static final String RESOURCES_STATES_DAT = "resources/states.dat";

    @FXML
    public TextField txtArea_stateName;
    @FXML
    public TextField txtArea_stateAbbreviation;
    @FXML
    public TextField txtArea_stateCapital;
    @FXML
    public ComboBox<State> btnCombo_states;
    @FXML
    public TextArea txtArea_console;
    @FXML
    public Button btn_addNewState;
    @FXML
    public TextField txtArea_stateArea;
    @FXML
    public TextField txtArea_statePopulation;
    @FXML
    public TextField txtArea_stateMotto;

    private Map<String, State> stateMap;

    private List<State> stateList;

    private Alert validFieldAlert = new Alert(Alert.AlertType.NONE);

    /**
     * Called to initialize a controller after its root element has been completely processed.
     * Params:
     *
     * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateDropdown();
    }

    /**
     * Initializes the Maps List and populates Dropdown
     */
    private void populateDropdown() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(RESOURCES_STATES_DAT)))) {
            Stream<String> lines = br.lines();

            stateList = lines.map(line -> new State().fromString(line)).collect(Collectors.toList());
            stateMap = stateList.stream().collect(Collectors.toMap(State::getName, value -> value));
        } catch (IOException e) {
            e.printStackTrace();
        }
        btnCombo_states.setConverter(new State());
        btnCombo_states.setItems(FXCollections.observableList(stateList));
        btnCombo_states.getSelectionModel().selectFirst();

        txtArea_console.setText(btnCombo_states.getSelectionModel().getSelectedItem().toString());
    }

    /**
     * Method called when dropdown is selected.  Updates State in user interface and clears console
     */
    @FXML
    public void updateStateDisplay() {
        txtArea_console.setText(btnCombo_states.getSelectionModel().getSelectedItem().toString());
    }

    /**
     * Method called when clicking 'Add State' button and submits new state to Map.  Ensures values are non-null
     *
     * @param event The actionevent from button press
     */
    @FXML
    public void addNewState(ActionEvent event) {
        State.Builder builder = new State.Builder();

        if (isNotValidText(txtArea_stateAbbreviation.getText())) return;
        if (isNotValidText(txtArea_stateArea.getText())) return;
        if (isNotValidText(txtArea_stateCapital.getText())) return;
        if (isNotValidText(txtArea_stateMotto.getText())) return;
        if (isNotValidText(txtArea_stateName.getText())) return;
        if (isNotValidText(txtArea_statePopulation.getText())) return;

        State newState = builder
                .abbreviation(txtArea_stateAbbreviation.getText())
                .area(BigDecimal.valueOf(Long.parseLong(txtArea_stateArea.getText())))
                .capital(txtArea_stateCapital.getText())
                .motto(txtArea_stateMotto.getText())
                .name(txtArea_stateName.getText())
                .population(Long.parseLong(txtArea_statePopulation.getText()))
                .build();

        stateMap.put(newState.getName(), newState);

        btnCombo_states.getItems().add(newState);
        btnCombo_states.getSelectionModel().selectLast();

        clearInputFields();
    }

    private void clearInputFields() {
        txtArea_stateAbbreviation.setText("");
        txtArea_stateArea.setText("");
        txtArea_stateCapital.setText("");
        txtArea_stateMotto.setText("");
        txtArea_stateName.setText("");
        txtArea_statePopulation.setText("");
    }

    private boolean isNotValidText(String text) {
        if (text.isEmpty()) {
            validFieldAlert.setAlertType(Alert.AlertType.ERROR);
            validFieldAlert.setContentText("All Fields are required");
            validFieldAlert.show();
            return true;
        }
        return false;
    }
}