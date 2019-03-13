package sample.service;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class FileGenerator {
    private final int fileAmount = 10;
    private final int lineAmount = 100;
    private LineGenerator lineGenerator = new LineGenerator();
    private File outputDir = new File("generated");

    public void run(){
        if(!outputDir.exists()){
            outputDir.mkdir();
        }
        generateFiles();
    }

    private void generateFiles(){
        for(int i = 0; i < fileAmount; i++){
            File output = new File(outputDir,"file_" + i + ".txt");
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(output);
                for(int line = 0; line < lineAmount; line++){
                    writer.print(lineGenerator.generateLine() + "\r\n");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                writer.flush();
                writer.close();
            }
        }
    }
}
