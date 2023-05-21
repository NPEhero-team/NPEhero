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
    /*
     * this class is a layout class, most of its purpose is to place UI elements like Buttons within Panes like VBoxes.
     * the creation of these UI elements are mostly not commented due to their repetitive and self explanatory nature.
     * style classes are defined in the style.css file.
     */
    public Leaderboard()
    {
        ListView<String> scores = new ListView<String>();
        ObservableList<String> scoreList= FXCollections.observableArrayList ("Test Score 1", "Test Score 2", "Test Score 3", "Test Score 4");
        scores.getStyleClass().remove("list-view");
        scores.getStyleClass().add("unselectable");
        scores.setItems(scoreList);
        scores.prefWidthProperty().bind(super.prefWidthProperty().multiply(0.25)); 
        scores.prefHeightProperty().bind(super.prefHeightProperty().multiply(0.75));

        Button exit = new Button();
        exit.setText("Back");
        exit.setOnAction(e -> Driver.setMenu(new MainMenu()));

        VBox centerBox = new VBox();
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setSpacing(10);
        centerBox.getChildren().addAll(scores,exit);
        centerBox.setMinWidth(400);

        HBox rootBox = new HBox();
        rootBox.prefWidthProperty().bind(super.prefWidthProperty()); 
        rootBox.prefHeightProperty().bind(super.prefHeightProperty());
        rootBox.getChildren().add(centerBox);
        rootBox.setAlignment(Pos.CENTER);

        super.getChildren().add(rootBox);
    }
}
