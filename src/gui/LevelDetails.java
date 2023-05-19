package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import main.Level;

public class LevelDetails extends VBox
{
    public LevelDetails(Level level)
    {
        VBox details = new VBox();
        details.prefWidthProperty().bind(super.prefWidthProperty()); 
        details.prefHeightProperty().bind(super.prefHeightProperty().multiply(0.75));
        details.maxWidthProperty().bind(super.prefWidthProperty()); 
        details.maxHeightProperty().bind(super.prefHeightProperty().multiply(0.75));
        details.getStyleClass().add("textBox");
        details.setPadding(new Insets(10));

        Button play = new Button();
        play.setDisable(true);
        play.setText("Play");

        if (level == null)
        {
            Text desc = new Text();
            desc.setText("Select a level from the left pane");
            desc.setFill(Color.WHITE);
            desc.wrappingWidthProperty().bind(super.prefWidthProperty().subtract(10));
            desc.setTextAlignment(TextAlignment.CENTER);
            details.setAlignment(Pos.CENTER);
            details.getChildren().addAll(desc);
        }

        else
        {
            Text title = new Text();
            title.setText(level.title);
            title.setFill(Color.WHITE);
            title.setFont(new Font(50));
            title.wrappingWidthProperty().bind(super.prefWidthProperty().subtract(10));

            Text desc = new Text();
            desc.setText(level.desc);
            desc.setFill(Color.WHITE);
            desc.wrappingWidthProperty().bind(super.prefWidthProperty().subtract(10));

            ImageView previewView = new ImageView();
            Image preview = level.preview;
            previewView.setImage(preview);
            previewView.fitWidthProperty().bind(super.prefWidthProperty().multiply(0.5));
            previewView.setPreserveRatio(true);

            HBox diffBox = new HBox();
            diffBox.setPadding(new Insets(30,0,0,0));
            HBox diffSelector = new HBox();
            diffSelector.setAlignment(Pos.CENTER);
            ToggleGroup diffToggleGroup = new ToggleGroup();
            for (String diff : level.diffList)
            {
                ToggleButton temp = new ToggleButton();
                temp.setText(diff);
                diffToggleGroup.getToggles().add(temp);
                diffSelector.getChildren().add(temp);
            }
            play.disableProperty().bind(diffToggleGroup.selectedToggleProperty().isNull());

            diffBox.getChildren().add(diffSelector);
            details.getChildren().addAll(title,desc,previewView, diffBox);
            play.setOnAction(e -> Driver.setMenu(new LevelSurround(level, "easy", Driver.getMenu())));
        }

        VBox rightBox = new VBox();
        rightBox.setAlignment(Pos.CENTER_RIGHT);
        rightBox.setSpacing(10);
        rightBox.getChildren().addAll(details,play);

        super.setAlignment(Pos.CENTER_RIGHT);
        super.getChildren().add(rightBox);
    }
}
