package sample.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CombineController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label headerLabel;

    @FXML
    private TextField textInput;

    @FXML
    private Label hintLabel;

    @FXML
    private Button combineButton;

    @FXML
    private Button cancelButton;

    @FXML
    void initialize() {
        cancelButton.setOnAction(event -> {
            try {
                switchScene(event, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        combineButton.setOnAction(event -> {
            File combined = new File("combined.txt");
            File outputDir = new File("generated");
            File[] sourceFiles = outputDir.listFiles((dir, name) -> {
                if (name.endsWith(".txt")) {
                    return true;
                } else {
                    return false;
                }
            });


            PrintWriter writer = null;
            String pattern = textInput.getText().trim();
            Pattern ptrn = Pattern.compile(pattern);
            Matcher matcher;
            try {
                writer = new PrintWriter(combined);
                Scanner scanner;

                for (int i = 0; i < sourceFiles.length; i++) {
                    String currentLine;
                    try {
                        scanner = new Scanner(sourceFiles[i]);
                        while (scanner.hasNextLine()) {
                            currentLine = scanner.nextLine();
                            if(!pattern.equals("")) {
                                matcher = ptrn.matcher(currentLine);
                                if (matcher.find()) {
                                    continue;
                                }
                            }
                            writer.println(currentLine);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                writer.flush();
                writer.close();
            }
            try {
                switchScene(event, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void switchScene(ActionEvent event, boolean isCanceled) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/fxml/sample.fxml"));
        Parent tableViewParent = loader.load();

        Scene tableViewScene = new Scene(tableViewParent);

        if(!isCanceled) {
            Controller controller = loader.getController();
            controller.setOutputLabelText("Successfully combined!");
            controller.setProgressIndicatorValue(1);
        }

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(tableViewScene);
        window.show();
    }
}
