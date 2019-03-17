package sample.service;

import javafx.scene.control.TextField;
import sample.controller.CombineController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CombineService {

    public void combineFiles(final TextField textInput){
        File combined = new File("combined.txt");
        File outputDir = new File("generated");
        File[] sourceFiles = outputDir.listFiles((dir, name) -> name.endsWith(".txt"));


        PrintWriter writer = null;
        String pattern = textInput.getText().trim();
        Pattern ptrn = Pattern.compile(pattern);
        Matcher matcher;
        try {
            writer = new PrintWriter(combined);
            Scanner scanner;

            assert sourceFiles != null;
            for (final File sourceFile : sourceFiles) {
                String currentLine;
                try {
                    scanner = new Scanner(sourceFile);
                    while (scanner.hasNextLine()) {
                        currentLine = scanner.nextLine();
                        if (!pattern.equals("")) {
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
    }
}
