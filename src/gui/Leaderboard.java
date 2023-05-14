package gui;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class Leaderboard extends Pane
{
    public Leaderboard()
    {
        ListView<String> scores = new ListView<String>();
        ObservableList<String> scoreList= FXCollections.observableArrayList ("Test Score 1", "Test Score 2", "Test Score 3", "Test Score 4");
        scores.setItems(scoreList);
        scores.minWidthProperty().bind(super.widthProperty().multiply(0.25)); 
        scores.minHeightProperty().bind(super.heightProperty().multiply(0.75));

        Button exit = new Button();
        exit.setText("Exit");
        exit.setOnAction(e -> Driver.setMenu("MainMenu"));

        VBox centerBox = new VBox();
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setSpacing(10);
        centerBox.getChildren().addAll(scores,exit);

        HBox rootBox = new HBox();
        rootBox.minWidthProperty().bind(super.widthProperty()); 
        rootBox.minHeightProperty().bind(super.heightProperty());
        rootBox.getChildren().add(centerBox);
        rootBox.setAlignment(Pos.CENTER);

        super.getChildren().add(rootBox);
    }
}
