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
import sample.service.CombineService;

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
                switchScene(event, true); //switch window if button was pressed
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        combineButton.setOnAction(event -> {
            CombineService combineService = new CombineService();
            combineService.combineFiles(textInput);
            try {
                switchScene(event, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    private void switchScene(ActionEvent event, boolean isCanceled) throws IOException {
        //switch scene algorithm
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/fxml/sample.fxml"));
        Parent tableViewParent = loader.load();

        Scene tableViewScene = new Scene(tableViewParent);

        if(!isCanceled) {
            Controller controller = loader.getController();
            controller.appendText("Successfully combined!");
        }

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(tableViewScene);
        window.show();
    }
}
