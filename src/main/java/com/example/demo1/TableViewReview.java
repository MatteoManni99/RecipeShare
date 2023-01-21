package com.example.demo1;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class TableViewReview {
    public TableView<Review> table;
    private ObservableList<Review> reviews;
    private TableColumn description;
    private TableColumn authorNameCol;
    private Stage stage;
    private DataSingleton data = DataSingleton.getInstance();

}
