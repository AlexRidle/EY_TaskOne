package sample.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import sample.service.DatabaseService;
import sample.service.FileGenerator;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button generateButton;

    @FXML
    private Button combineButton;

    @FXML
    private Button uploadButton;

    @FXML
    private TextArea textoutput;

    @FXML
    void initialize() {
        FileGenerator fileGenerator = new FileGenerator();

        generateButton.setOnAction(event -> { //generates files if btn was pressed
            generateButton.setDisable(true);
            textoutput.appendText("\r\nGenerating...");
            fileGenerator.run();
            textoutput.appendText("\r\nCompleted");
            generateButton.setDisable(false);
        });

        combineButton.setOnAction(event -> { //combines files in one if btn was pressed
            Parent tableViewParent = null;
            try {
                tableViewParent = FXMLLoader.load(getClass().getResource("/sample/fxml/combine.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene tableViewScene = new Scene(tableViewParent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(tableViewScene);
            window.show();

        });

        uploadButton.setOnAction(event -> {
            DatabaseService databaseService = new DatabaseService();
            databaseService.setupDatabase(); //creates DB if not exists
            Platform.runLater(() -> databaseService.uploadInDB(textoutput)); //uploads generated files into DB
        });
    }

    public void appendText(final String text) {
        textoutput.appendText(text);
    }

}
