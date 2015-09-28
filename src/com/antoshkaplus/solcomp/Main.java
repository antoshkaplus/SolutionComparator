package com.antoshkaplus.solcomp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.io.File;



public class Main extends Application {

    static String solScoresPath;


    class Sample {
        int index;
        double score;
    }

    class Solution {
        ArrayList<Sample> samples;
    }


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        // should pass directory with solutions as an argument
        // or can make a menu later to choose the directory

        if (args.length == 1) {
            solScoresPath = "../sol_scores";
        } else {
            solScoresPath = args[1];
        }

        File dir = new File(solScoresPath);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File file : directoryListing) {
                if (file.isDirectory()) continue;
                // may be that file is not found
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (line.charAt(0) == '#') continue;
                        String[] res = line.split(":");
                        if (res.length != 2) {
                            // something went wrong
                        }
                        int sampleIndex = Integer.parseInt(res[0]);
                        double sampleScore = Double.parseDouble(res[1]);
                        // process the line.
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            // Handle the case where dir is not really a directory.
            // Checking dir.isDirectory() above would not be sufficient
            // to avoid race conditions with another process that deletes
            // directories.
        }




        launch(args);
    }
}
