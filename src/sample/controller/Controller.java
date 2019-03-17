package sample.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;
import sample.service.DatabaseService;
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
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
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

        uploadButton.setOnAction(event -> {
            DatabaseService databaseService = new DatabaseService();
            databaseService.setupDatabase();

            File combined = new File("combined.txt");
            Pattern pattern;
            Matcher matcher;
            String[] patterns = {
                    "\\d{1,2}\\.\\d{1,2}\\.\\d{4}", //date
                    "([a-zA-Z])+", //eng chars
                    "([а-яА-ЯёЁ])+", //rus chars
                    "(?<=\\|)([0-9]{1,10})(?=\\|)", //even num
                    "[0-9]{1,2}[,][0-9]+" //double num
            };

            try {
                Scanner scanner;
                String currentLine;
                File outputDir = new File("generated");
                File[] sourceFiles = outputDir.listFiles((dir, name) -> name.endsWith(".txt"));
                assert sourceFiles != null;
                int totalFile = sourceFiles.length + 1;
                int totalLines = countTotalLines("generated") + 1;
                int currentCopyingFile = 0;
                int currentCopyingLine = 0;
                for (final File sourceFile : sourceFiles) {

                    currentCopyingFile++;
                    scanner = new Scanner(sourceFile);
                    while (scanner.hasNextLine()) {
                        currentLine = scanner.nextLine();
                        currentCopyingLine++;
                        System.out.println("Lines remained: " + (totalLines - currentCopyingLine) + ". " + "Files remained: " + (totalFile - currentCopyingFile));
                        outputLabel.setText("Lines remained: " + (totalLines - currentCopyingLine) + ". " + "Files remained: " + (totalFile - currentCopyingFile));
                        Object[] db_line = new Object[5];
                        for (int i = 0; i < 5; i++) {
                            pattern = Pattern.compile(patterns[i]);
                            matcher = pattern.matcher(currentLine);
                            if (matcher.find()) {
                                db_line[i] = matcher.group();
                            }
                        }
                        databaseService.addToTable((String) db_line[0], (String) db_line[1], (String) db_line[2], Integer.valueOf((String) db_line[3]), Double.valueOf(((String) db_line[4]).replaceAll(",", ".")));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            progressIndicator.setProgress((double) 1);
            outputLabel.setText("Successfully added to database.");
        });
    }

    public void setOutputLabelText(final String text) {
        outputLabel.setText(text);
    }

    public void setProgressIndicatorValue(double value) {
        progressIndicator.setProgress(value);
    }

    private int countTotalLines(String srcDir) throws IOException {
        int result = 0;
        File outputDir = new File(srcDir);
        File[] sourceFiles = outputDir.listFiles((dir, name) -> name.endsWith(".txt"));
        for (final File sourceFile : sourceFiles) {
            result += countLines(sourceFile.getAbsolutePath());
        }
        return result;
    }

    private int countLines(String filename) throws IOException {
        try (FileReader input = new FileReader(filename);
                LineNumberReader count = new LineNumberReader(input)) {
            while (count.skip(Long.MAX_VALUE) > 0) {
                // Loop just in case the file is > Long.MAX_VALUE or skip() decides to not read the entire file
            }
            return count.getLineNumber();
        }
    }

}
