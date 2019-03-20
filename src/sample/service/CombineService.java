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
        File[] sourceFiles = outputDir.listFiles((dir, name) -> name.endsWith(".txt")); //gets all txt files from dir


        PrintWriter writer = null;
        String pattern = textInput.getText().trim(); //gets pattern
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
                        if (!pattern.equals("")) { //just in case if our pattern is empty
                            matcher = ptrn.matcher(currentLine);
                            if (matcher.find()) {
                                continue; //if matcher found a coincidence we mustn't write it in file
                            }
                        }
                        writer.println(currentLine); //if all is ok, the line writes in file
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
