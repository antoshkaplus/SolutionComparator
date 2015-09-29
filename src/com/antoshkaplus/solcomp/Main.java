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
    static ArrayList<Solution> solutions = new ArrayList<>();

    static class Sample {
        int index;
        double score;

        Sample(int index, double score) {
            this.index = index;
            this.score = score;
        }
    }

    static class Solution {
        ArrayList<Sample> samples;
        String title;
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
                    Solution solution = new Solution();
                    solution.title = file.getName();
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (line.isEmpty() || line.charAt(0) == '#') continue;
                        String[] res = line.split(":");
                        if (res.length != 2) {
                            throw new RuntimeException();
                            // something went wrong
                        }
                        int sampleIndex = Integer.parseInt(res[0]);
                        double sampleScore = Double.parseDouble(res[1]);
                        solution.samples.add(new Sample(sampleIndex, sampleScore));
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
