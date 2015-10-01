package com.antoshkaplus.solcomp;

import com.sun.javafx.scene.control.skin.TableColumnHeader;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;

// later may think about builder pattern
public class Controller {

    ArrayList<Solution> solutions;
    boolean[] isChosen = new boolean[2];
    int[] chosenIndex = new int[2];

    String higherScoreBg = "-fx-background-color: green";
    String lowerScoreBg = "-fx-background-color: red";
    String unknownScoreBg = "-fx-background-color: yellow";

    void setData(ArrayList<Solution> solutions) {
        this.solutions = solutions;
    }

    public Callback<TableColumn<Item, Double>, TableCell<Item, Double>> getScoreCellFactory(int column) {
        return new Callback<TableColumn<Item, Double>, TableCell<Item, Double>>() {
            @Override
            public TableCell<Item, Double> call(TableColumn<Item, Double> param) {
                return new ScoreCell(column);
            }
        };
    }

    TableView buildTable() {
        TableView table = new TableView();
        table.setEditable(false);
        ArrayList<Item> items = new ArrayList<>();
        Solution sol = solutions.get(0);
        for (int i = 0; i < sol.samples.size(); ++i) items.add(new Item(i));
        table.getItems().setAll(items);

        TableColumn<Item, Integer> sampleIndexCol = new TableColumn<>("Sample #");
        sampleIndexCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().sampleIndex));
        ArrayList<TableColumn<Item, Double>> solutionCols = new ArrayList<>();
        int s_index = 0;
        for (Solution s : solutions) {
            TableColumn<Item, Double> col = new TableColumn<>(s.title);
            int s_index_inner = s_index;
            col.setCellFactory(getScoreCellFactory(s_index));
            col.setCellValueFactory(param -> {
                Item item = param.getValue();
                ReadOnlyObjectWrapper<Double> wrapper = new ReadOnlyObjectWrapper<>(item.score(s_index_inner));
                return wrapper;
            });
            solutionCols.add(col);
            ++s_index;
        }
        ObservableList<TableColumn> list = (ObservableList<TableColumn>)table.getColumns();
        list.addAll(sampleIndexCol);
        list.addAll(solutionCols);
        return table;
    }

    private class ScoreCell extends TableCell<Item, Double> implements EventHandler<MouseEvent>  {
        int column;

        ScoreCell(int column) {
            super();
            this.column = column;
            setOnMouseClicked(this);
        }

        @Override
        protected void updateItem(Double item, boolean empty) {
            int row = getIndex();
            // -1 and last are strange rows
            if (item == null) return;
            int col = column;
            // choosing color
            // if chosen only one of them color items yellow ???
            int cur = -1;
            int opp = -1;
            boolean init = false;
            if (isChosen[0] && chosenIndex[0] == col) {
                cur = 0;
                opp = 1;
                init = true;
            } else if (isChosen[1] && chosenIndex[1] == col) {
                cur = 1;
                opp = 0;
                init = true;
            }
            if (init) {
                // have to compare with another one
                if (!isChosen[opp]) {
                    setStyle(unknownScoreBg);
                } else {
                    if (solutions.get(chosenIndex[cur]).samples.get(row).score >
                            solutions.get(chosenIndex[opp]).samples.get(row).score) {

                        setStyle(higherScoreBg);
                    } else {
                        setStyle(lowerScoreBg);
                    }
                }
            } else {
                setStyle("");
            }
            try {
                setText(Double.toString(item));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void handle(MouseEvent event) {
            // if column already chosen don't do anything (maybe user checks rows)
            // if another column chosen: move shit from 0 to 1... and assign to 0
            int col = column;
            if ((!isChosen[0] || chosenIndex[0] != col) && (!isChosen[1] || chosenIndex[1] != col)) {
                isChosen[1] = isChosen[0];
                chosenIndex[1] = chosenIndex[0];
                isChosen[0] = true;
                chosenIndex[0] = col;
                // have to ask for update
                getTableView().getColumns().get(0).setVisible(false);
                getTableView().getColumns().get(0).setVisible(true);
            }
        }
    }

    class Item {

        int sampleIndex;

        Item(int sampleIndex) {
            this.sampleIndex = sampleIndex;
        }

        double score(int solutionIndex) {
            return solutions.get(solutionIndex).samples.get(sampleIndex).score;
        }
    }
}
