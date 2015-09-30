package com.antoshkaplus.solcomp;

import javafx.scene.control.TableCell;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;

public class Controller {

    ArrayList<Solution> solutions;
    boolean[] chosen = new boolean[2];


    void setData(ArrayList<Solution> solutions) {
        this.solutions = solutions;
    }

    class ScoreCell extends TableCell<Void, Double> {
        @Override
        protected void updateItem(Double item, boolean empty) {

            int row = getIndex();
            int col = getTableView().getColumns().indexOf(getTableColumn());

            super.updateItem(item, empty);
        }
    }





}
