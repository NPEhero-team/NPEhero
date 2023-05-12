package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LevelSelector extends Pane
{   
    public LevelSelector()
    {
        ListView<String> levels = new ListView<String>();
        ObservableList<String> levelList= FXCollections.observableArrayList ("Test Level 1", "Test Level 2", "Test Level 3", "Test Level 4");
        levels.setItems(levelList);
        //super.setAlignment(Pos.CENTER);
        Button exit = new Button();
        exit.setText("Exit");
        exit.setOnAction(e -> Driver.switchMenu("MainMenu"));

        super.getChildren().addAll(levels,exit);
    }

}
