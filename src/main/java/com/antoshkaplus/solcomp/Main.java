package com.antoshkaplus.solcomp;

import com.sun.javafx.collections.ImmutableObservableList;
import com.sun.javafx.scene.control.skin.TableColumnHeader;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.io.File;
import java.util.DoubleSummaryStatistics;


public class Main extends Application {

    static String solScoresPath;
    static ArrayList<Solution> solutions = new ArrayList<>();

    private TableView table = null;
    private Controller controller = new Controller();

    // better put logic inside controller. no problem

    @Override
    public void start(Stage stage) throws Exception{
        URL location = ClassLoader.getSystemResource("table.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());

        Parent root = (Parent) fxmlLoader.load(location.openStream());
        VBox pane = (VBox)root;//fxmlLoader.load(getClass().getResource("table.fxml").openStream());
        Controller controller = (Controller) fxmlLoader.getController();
        controller.setSolutions(solutions);

        Scene scene = new Scene(pane);

        stage.setTitle("Solution Comparator");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        // should pass directory with solutions as an argument
        // or can make a menu later to choose the directory

        if (args.length == 0) {
            solScoresPath = "../sol_scores";
        } else {
            solScoresPath = args[0];
        }
        System.out.println("path: " + solScoresPath);

        File dir = new File(solScoresPath);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File file : directoryListing) {
                if (file.isDirectory()) continue;
                // may be that file is not found
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String title = file.getName();
                    ArrayList<Sample> samples = new ArrayList<>();
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
                        samples.add(new Sample(sampleIndex, sampleScore));
                    }
                    // could sort samples by index
                    // for now lets just continue
                    solutions.add(new Solution(title, samples));
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
