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
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
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

    private TableView table = new TableView();


    // better put logic inside controller. no problem
    boolean choosen_0 = false;
    boolean choosen_1 = false;



    static class Item {
        int index;

        Item(int index) {
            this.index = index;
        }
    }


    @Override
    public void start(Stage stage) throws Exception{
        Scene scene = new Scene(new Group());
        stage.setTitle("Table View Sample");
        table.setEditable(false);
        ArrayList<Item> items = new ArrayList<>();
        Solution sol = solutions.get(0);
        for (int i = 0; i < sol.samples.size(); ++i) items.add(new Item(i));

        // table is dynamic no need in fxml file for now
        table.getItems().setAll(items);
        TableView.TableViewSelectionModel model = table.getSelectionModel();
        table.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                TableColumn column = null;


                if ((!choosen_0 || !choosen_1) && event.getClickCount() == 1) {
                    event.consume();

                    TableColumnHeader header = (TableColumnHeader)event.getTarget();
                    System.out.println("hello");
                }
            }
        });
        TableColumn<Item, Integer> sampleIndexCol = new TableColumn<>("Sample #");
        Node node = sampleIndexCol.getGraphic();
        sampleIndexCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().index));
        ArrayList<TableColumn<Item, Double>> solutionCols = new ArrayList<>();
        for (Solution s : solutions) {
            TableColumn<Item, Double> col = new TableColumn<>(s.title);
            col.setCellValueFactory(param -> {
                Item item = param.getValue();
                return new ReadOnlyObjectWrapper<>(s.samples.get(item.index).score);
            });
            col.setCellFactory(new Callback<TableColumn<Item, Double>, TableCell<Item, Double>>() {
                @Override
                public TableCell<Item, Double> call(TableColumn<Item, Double> param) {
                    return new TableCell<Item, Double>() {
                        @Override
                        protected void updateItem(Double item, boolean empty) {
                            // so here we actually able to change style as we want
                            int row = getIndex();
                            int col = getTableView().getColumns().indexOf(getTableColumn());

                            // probably cache column index inside column itself
                            // should have chosen stuff somewhere nearby to know which bg to apply
                            // probably should put this shit to controller
                            // especially logic

                            super.updateItem(item, empty);
                        }
                    };
                }
            });
            solutionCols.add(col);
        }
        ObservableList<TableColumn> list = (ObservableList<TableColumn>)table.getColumns();
        list.addAll(sampleIndexCol);
        list.addAll(solutionCols);

        ((Group) scene.getRoot()).getChildren().addAll(table);

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
