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

        Text title = new Text();
        title.setText("Test level 1");
        title.setFill(Color.WHITE);
        title.setFont(new Font(50));
        title.wrappingWidthProperty().bind(super.widthProperty().multiply(0.37));

        Text desc = new Text();
        desc.setText("long description with lots of words. what we write does not actually need to be long i just wan t make sure it can word wrap");
        desc.setFill(Color.WHITE);
        desc.wrappingWidthProperty().bind(super.widthProperty().multiply(0.37));

        ImageView previewView = new ImageView();
        Image preview = new Image("assets/pico.png");
        previewView.setImage(preview);
        //previewView.setFitHeight(100);
        previewView.fitWidthProperty().bind(super.widthProperty().multiply(0.25));
        previewView.setPreserveRatio(true);

        VBox details = new VBox();
        details.minWidthProperty().bind(super.widthProperty().multiply(0.37)); 
        details.minHeightProperty().bind(super.heightProperty().multiply(0.75));
        details.maxWidthProperty().bind(super.widthProperty().multiply(0.37)); 
        details.maxHeightProperty().bind(super.heightProperty().multiply(0.75));
        details.getStyleClass().add("textBox");
        details.getChildren().addAll(title,desc,previewView);

        Button play = new Button();
        play.setText("Play");

        VBox rightBox = new VBox();
        rightBox.setAlignment(Pos.CENTER_RIGHT);
        rightBox.setSpacing(10);
        rightBox.getChildren().addAll(details,play);

        HBox rootBox = new HBox();
        rootBox.minWidthProperty().bind(super.widthProperty()); 
        rootBox.minHeightProperty().bind(super.heightProperty());
        rootBox.getChildren().addAll(leftBox, rightBox);
        rootBox.setAlignment(Pos.CENTER);
        rootBox.setSpacing(10);

        super.getChildren().add(rootBox);
    }

}
