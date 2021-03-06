package org.mcnichol;

import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.mcnichol.driver.LoanScheduleDriver;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * <h1>LoanScheduleController</h1>
 * This application uses a JavaFX UI to allow the user to enter a Loan Amount, Number of Years, and Annual Interest
 * Rate.  The user then clicks Display Loan Schedule and the number of payments are listed for the user.
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
public class LoanScheduleController implements Initializable {
    @FXML
    public TextField txtArea_loanAmount;
    @FXML
    public TextField txtArea_numberOfYears;
    @FXML
    public TextField txtArea_annualInterestRate;
    @FXML
    public ComboBox<PrintableLocale> btnCombo_locale;
    @FXML
    public TextArea txtArea_console;
    @FXML
    public Button btn_displayLoanSchedule;
    @FXML
    public Label lbl_LoanAmount;
    @FXML
    public Label lbl_numberOfYears;
    @FXML
    public Label lbl_enterLoanInfoTitle;
    @FXML
    public Label lbl_annualInterestRate;

    private ResourceBundle uiResourceBundle;

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
        uiResourceBundle = ResourceBundle.getBundle("org.mcnichol.resources.menus");

        btnCombo_locale.getItems().setAll(FXCollections.observableArrayList(
                new PrintableLocale(Locale.US),
                new PrintableLocale(Locale.GERMANY),
                new PrintableLocale(Locale.ITALY)
        ));

        btnCombo_locale.getSelectionModel().selectFirst();
    }

    /**
     * Setup initial bindings and event listeners
     */
    private void initializeBindings() {
        txtArea_loanAmount.textProperty().addListener((o, oldVal, newVal) -> System.out.println(newVal));
        txtArea_numberOfYears.textProperty().addListener((o, oldVal, newVal) -> System.out.println(newVal));
        txtArea_annualInterestRate.textProperty().addListener((o, oldVal, newVal) -> System.out.println(newVal));
    }

    /**
     * Method called when button click event is fired. Gets the Loan Schedule from Loan Schedule Driver
     * and Prints to Console
     */
    @FXML
    void printLoanSchedule() {
        LoanScheduleDriver loanScheduleDriver = new LoanScheduleDriver(txtArea_loanAmount.getText(), txtArea_numberOfYears.getText(), txtArea_annualInterestRate.getText());
        String loanSchedule = loanScheduleDriver.getLoanSchedule(uiResourceBundle);
        txtArea_console.setText(loanSchedule);
    }

    /**
     * Method called when Locale dropdown is selected.  Updates language in user interface and clears console
     *
     */
    @FXML
    public void updateLocale() {
        uiResourceBundle = ResourceBundle.getBundle("org.mcnichol.resources.menus", btnCombo_locale.getValue().getThisLocale());
        txtArea_console.setText("");

        Platform.runLater(() -> {
            btn_displayLoanSchedule.setText(uiResourceBundle.getString("loan.displayLoan"));
            lbl_LoanAmount.setText(uiResourceBundle.getString("loan.amount"));
            lbl_numberOfYears.setText(uiResourceBundle.getString("loan.numberYears"));
            lbl_enterLoanInfoTitle.setText(uiResourceBundle.getString("loan.enterLoanInfoTitle"));
            lbl_annualInterestRate.setText(uiResourceBundle.getString("loan.annualInterestRate"));
        });

    }
}