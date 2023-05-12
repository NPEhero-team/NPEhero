package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class Leaderboard extends Pane
{
    public Leaderboard()
    {
        ListView<String> scores = new ListView<String>();
        ObservableList<String> scoreList= FXCollections.observableArrayList ("Test Score 1", "Test Score 2", "Test Score 3", "Test Score 4");
        scores.setItems(scoreList);

        Button exit = new Button();
        exit.setText("Exit");
        exit.setOnAction(e -> Driver.switchMenu("MainMenu"));

        VBox centerMenu3 = new VBox();
        centerMenu3.minWidthProperty().bind(super.widthProperty()); 
        centerMenu3.minHeightProperty().bind(super.heightProperty());
        centerMenu3.setAlignment(Pos.CENTER);
        centerMenu3.getChildren().addAll(scores,exit);
        super.getChildren().add(centerMenu3);
    }
}
