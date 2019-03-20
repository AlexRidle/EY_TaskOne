package sample.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class FileGenerator {
    //file generating setup
    private final int fileAmount = 100;
    private final int lineAmount = 100_000_000;
    private LineGenerator lineGenerator = new LineGenerator();
    private File outputDir = new File("generated");  //name of output directory

    public void run(){
        if(!outputDir.exists()){ //if output dir. doesn't exists - creating new one
            outputDir.mkdir();
        }
        generateFiles(); //run generating
    }

    private void generateFiles(){
        for(int i = 0; i < fileAmount; i++){
            File output = new File(outputDir,"file_" + i + ".txt"); //creates new file with name
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(output);
                for(int line = 0; line < lineAmount; line++){
                    writer.print(lineGenerator.generateLine() + "\r\n"); //writing generated text by task pattern in each line
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
