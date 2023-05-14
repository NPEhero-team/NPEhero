package gui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class LevelDetails extends VBox
{
    public LevelDetails(ListView<String> list)
    {
        VBox details = new VBox();
        details.minWidthProperty().bind(super.widthProperty()); 
        details.minHeightProperty().bind(super.heightProperty().multiply(0.75));
        details.maxWidthProperty().bind(super.widthProperty()); 
        details.maxHeightProperty().bind(super.heightProperty().multiply(0.75));
        details.getStyleClass().add("textBox");

        Button play = new Button();
        play.setText("Play");

        if (list.getSelectionModel().getSelectedItem() == null)
        {
            Text desc = new Text();
            desc.setText("Select a level from the left pane");
            desc.setFill(Color.WHITE);
            desc.wrappingWidthProperty().bind(super.widthProperty());
            desc.setTextAlignment(TextAlignment.CENTER);
            details.setAlignment(Pos.CENTER);
            details.getChildren().addAll(desc);
            play.setDisable(true);
        }

        else
        {
            Text title = new Text();
            title.setText("Test level 1");
            title.setFill(Color.WHITE);
            title.setFont(new Font(50));
            title.wrappingWidthProperty().bind(super.widthProperty());

            Text desc = new Text();
            desc.setText("long description with lots of words. what we write does not actually need to be long i just wan t make sure it can word wrap");
            desc.setFill(Color.WHITE);
            desc.wrappingWidthProperty().bind(super.widthProperty());

            ImageView previewView = new ImageView();
            Image preview = new Image("assets/pico.png");
            previewView.setImage(preview);
            //previewView.setFitHeight(100);
            previewView.fitWidthProperty().bind(super.widthProperty().multiply(0.5));
            previewView.setPreserveRatio(true);
            details.getChildren().addAll(title,desc,previewView);
        }

        VBox rightBox = new VBox();
        rightBox.setAlignment(Pos.CENTER_RIGHT);
        rightBox.setSpacing(10);
        rightBox.getChildren().addAll(details,play);

        super.setAlignment(Pos.CENTER_RIGHT);
        super.getChildren().add(rightBox);
    }
}
