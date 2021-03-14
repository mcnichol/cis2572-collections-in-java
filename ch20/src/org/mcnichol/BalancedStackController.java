package org.mcnichol;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Stack;

/**
 * <h1>ShuntingYardController</h1>
 * This application uses a JavaFX UI to allow the user to select a file and determine if it has a balanced set of (), {}, and [].
 * <p>
 * <b>Description:</b> This application leverages an {@link javafx.fxml.FXMLLoader} to construct a user interface and map
 * text, labels, and register event handlers.
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
 * @since 2021/03/07
 */
public class BalancedStackController  {


    @FXML
    public TextField txtArea_fileLoad;
    @FXML
    public Button btn_fileLoad;
    @FXML
    public Label lbl_isBalanced;


    /**
     * On button event handler for loading file and walking through each character testing for balance of stack
     *
     * @param event Event handler
     * @throws IOException If issues with file handling
     */
    public void loadFile(ActionEvent event) throws IOException {
        Stack<Character> balancedStack = new Stack<>();
        int lineNum = 0;
        boolean isBalanced = true;
        Character unbalancedChar = null;

        File selectedFile = getUserFile();

        List<String> fileLines = Files.readAllLines(Paths.get(selectedFile.getPath()));

        for (int i = 0; i < fileLines.size() && isBalanced; i++) {
            char[] chars = fileLines.get(i).toCharArray();
            for (char aChar : chars) {
                if (aChar == '{' || aChar == '(' || aChar == '[') {
                    balancedStack.push(aChar);
                } else if (aChar == '}' || aChar == ')' || aChar == ']') {
                    if (balancedStack.isEmpty()) {
                        isBalanced = false;
                        lineNum = i + 1;
                        unbalancedChar = aChar;
                        break;
                    }

                    if (aChar == '}' && balancedStack.peek() == '{') {
                        balancedStack.pop();
                    } else if (aChar == ')' && balancedStack.peek() == '(') {
                        balancedStack.pop();
                    } else if (aChar == ']' && balancedStack.peek() == '[') {
                        balancedStack.pop();
                    } else {
                        isBalanced = false;
                        lineNum = i + 1;
                        unbalancedChar = aChar;
                    }
                }
            }
        }

        displayResults(balancedStack, lineNum, isBalanced, unbalancedChar);

    }

    private File getUserFile() {
        FileChooser fc = new FileChooser();

        return fc.showOpenDialog(btn_fileLoad.getScene().getWindow());
    }

    private void displayResults(Stack<Character> balancedStack, int lineNum, boolean isBalanced, Character unbalancedChar) {
        if (balancedStack.size() == 0 && isBalanced) {
            lbl_isBalanced.setText("File has Balanced (), {}, and []");
        } else if (balancedStack.size() == 0) {
            lbl_isBalanced.setText(String.format("Stack is not balanced. Issue with %c on line: %d", unbalancedChar, lineNum));
        } else {
            lbl_isBalanced.setText(String.format("Stack is not balanced. Issue with %c found", balancedStack.peek()));
        }
    }
}