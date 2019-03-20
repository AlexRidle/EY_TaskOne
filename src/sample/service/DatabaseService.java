package sample.service;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatabaseService {

    //mysql server setup
    private String userName = "root";
    private String password = "root";
    private String connectionURL = "jdbc:mysql://localhost:3306/EY_Task_One?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

    public void setupDatabase() {
        createDatabase();
        createTables();
    }

    private void createDatabase() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", userName, password);
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS EY_Task_One");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTables() {

        try (Connection connection = DriverManager.getConnection(connectionURL, userName, password);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `ey_task_one`.`random_lines` (`id` INT NOT NULL AUTO_INCREMENT," +
                    "  `random_date` VARCHAR(45) NOT NULL," +
                    "  `eng_chars` VARCHAR(45) NOT NULL," +
                    "  `rus_chars` VARCHAR(45) NOT NULL," +
                    "  `even_num` INT NOT NULL," +
                    "  `double_num` DECIMAL(10,8) NOT NULL," +
                    "  PRIMARY KEY (`id`)," +
                    "  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE)");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void addToTable(String rnd_date, String eng, String rus, int even, double doub) {
        try (Connection connection = DriverManager.getConnection(connectionURL, userName, password);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("insert into random_lines (random_date, eng_chars, rus_chars, even_num, double_num) values("
                    + "'" + rnd_date + "'" + ", "
                    + "'" + eng + "'" + ", "
                    + "'" + rus + "'" + ", "
                    + "'" + even + "'" + ", "
                    + "'" + doub + "'" + ")"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized void uploadInDB(final TextArea textoutput){
        //column search setup
        Pattern pattern;
        Matcher matcher;
        //creating regex patterns for each column
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
            File[] sourceFiles = outputDir.listFiles((dir, name) -> name.endsWith(".txt")); //searching for txt files in "generated" directory
            assert sourceFiles != null;
            int totalFile = sourceFiles.length + 1;
            int totalLines = countTotalLines("generated") + 1;
            int currentCopyingFile = 0;
            int currentCopyingLine = 0;
            for (final File sourceFile : sourceFiles) { //checking all founded files

                currentCopyingFile++;
                scanner = new Scanner(sourceFile);
                while (scanner.hasNextLine()) {
                    currentLine = scanner.nextLine();
                    currentCopyingLine++;
                    System.out.println("Lines remained: " + (totalLines - currentCopyingLine) + ". " + "Files remained: " + (totalFile - currentCopyingFile));
                    updateLabel(textoutput, "Lines remained: " + (totalLines - currentCopyingLine) + ". " + "Files remained: " + (totalFile - currentCopyingFile));
                    //don't know why i cant update text output. Program always freezes until all lines and files were uploaded.
                    Object[] db_line = new Object[5]; //creating an array for founded columns to upload them into DB
                    for (int i = 0; i < 5; i++) { //running through all regex patterns step by step
                        pattern = Pattern.compile(patterns[i]);
                        matcher = pattern.matcher(currentLine);
                        if (matcher.find()) {
                            db_line[i] = matcher.group();
                        } else {
                            db_line[i] = 0;
                        }
                    }
                    addToTable((String) db_line[0], (String) db_line[1], (String) db_line[2], Integer.valueOf((String) db_line[3]), Double.valueOf(((String) db_line[4]).replaceAll(",", ".")));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        textoutput.setText("Successfully added to database.");
    }

    private int countTotalLines(String srcDir) throws IOException { //sum of all lines of each file
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
                // Loop just in case the file is higher than Long.MAX_VALUE or skip() decides to not read the entire file
            }
            return count.getLineNumber();
        }
    }

    private synchronized void updateLabel(TextArea textArea, String text) {
        textArea.setText(text);
    }

}