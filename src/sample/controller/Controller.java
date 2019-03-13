package sample.controller;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;
import sample.service.FileGenerator;

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
    private Label outputLabel;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    void initialize() {
        FileGenerator fileGenerator = new FileGenerator();

        generateButton.setOnAction(event -> {
            progressIndicator.setProgress((double) 0);
            generateButton.setDisable(true);
            outputLabel.setText("Generating...");
            fileGenerator.run();
            progressIndicator.setProgress((double) 1);
            outputLabel.setText("Completed");
            generateButton.setDisable(false);
        });

        combineButton.setOnAction(event -> {
            Parent tableViewParent = null;
            try {
                tableViewParent = FXMLLoader.load(getClass().getResource("/sample/fxml/combine.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene tableViewScene = new Scene(tableViewParent);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(tableViewScene);
            window.show();

//            combineButton.getScene().getWindow().hide();
//            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(getClass().getResource("/sample/fxml/combine.fxml"));
//
//            try {
//                loader.load();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            Parent root = loader.getRoot();
//            Stage stage = new Stage();
//            stage.setScene(new Scene(root));
//            stage.show();
        });
    }

    public void setOutputLabelText(final String text) {
        outputLabel.setText(text);
    }

    public void setProgressIndicatorValue(double value){
        progressIndicator.setProgress(value);
    }
}
