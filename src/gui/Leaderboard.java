package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Leaderboard extends Scene
{
    private static Pane root = new Pane();
    public Leaderboard(Stage primaryStage)
    {
        super(root,800,600);
        primaryStage.setTitle("NPE Hero - Leaderboard");

        ListView<String> scores = new ListView<String>();
        ObservableList<String> scoreList= FXCollections.observableArrayList ("Test Score 1", "Test Score 2", "Test Score 3", "Test Score 4");
        scores.setItems(scoreList);

        Button exit = new Button();
        exit.setText("Exit");
        exit.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            {
                primaryStage.setScene(new MainMenu(primaryStage));
            }
        });
        
        VBox centerMenu3 = new VBox();
        centerMenu3.minWidthProperty().bind(primaryStage.widthProperty()); 
        centerMenu3.minHeightProperty().bind(primaryStage.heightProperty());
        centerMenu3.setAlignment(Pos.CENTER);
        centerMenu3.getChildren().addAll(scores,exit);
        root.getChildren().add(centerMenu3);
    }
}
