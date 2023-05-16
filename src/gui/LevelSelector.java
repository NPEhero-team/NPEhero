package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class LevelSelector extends Pane
{   
    public LevelSelector()
    {
        ListView<String> levels = new ListView<String>();
        ObservableList<String> levelList= FXCollections.observableArrayList ("Test Level 1", "Test Level 2", "Test Level 3", "Test Level 4");
        levels.setItems(levelList);
        levels.minWidthProperty().bind(super.widthProperty().multiply(0.25)); 
        levels.minHeightProperty().bind(super.heightProperty().multiply(0.75));

        Button exit = new Button();
        exit.setText("Exit");
        exit.setOnAction(e -> Driver.setMenu("MainMenu"));

        VBox leftBox = new VBox();
        leftBox.setAlignment(Pos.CENTER_LEFT);
        leftBox.setSpacing(10);
        leftBox.getChildren().addAll(levels,exit);

        Pane rightBox = new Pane();
        addDetails(rightBox, levels);


        HBox rootBox = new HBox();
        rootBox.minWidthProperty().bind(super.widthProperty()); 
        rootBox.minHeightProperty().bind(super.heightProperty());
        rootBox.getChildren().addAll(leftBox, rightBox);
        rootBox.setAlignment(Pos.CENTER);
        rootBox.setSpacing(10);

        levels.setOnMouseClicked(e -> addDetails(rightBox, levels));
        super.getChildren().add(rootBox);
    }

    private void addDetails(Pane rightBox, ListView<String> levels)
    {
        VBox details = new LevelDetails(levels);
        if (! rightBox.getChildren().isEmpty())
        {
            rightBox.getChildren().remove(0);
        }
        rightBox.getChildren().add(details);
        details.minWidthProperty().bind(super.widthProperty().multiply(0.37)); 
        details.minHeightProperty().bind(super.heightProperty());
        details.maxWidthProperty().bind(super.widthProperty().multiply(0.37)); 
        details.maxHeightProperty().bind(super.heightProperty());
    }

}
