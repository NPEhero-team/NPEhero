package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import main.Level;

public class LevelDetails extends VBox
{
    public LevelDetails(Level level)
    {
        VBox rightBox = new VBox();
        rightBox.prefWidthProperty().bind(super.prefWidthProperty());
        rightBox.prefHeightProperty().bind(super.prefHeightProperty().multiply(0.75));
        rightBox.getStyleClass().add("textBox");

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

            rightBox.setAlignment(Pos.CENTER);
            rightBox.getChildren().addAll(desc);
        }

        else
        {
            VBox details = new VBox();

            ScrollPane detailsScroll = new ScrollPane(details);
            detailsScroll.prefHeightProperty().bind(rightBox.prefHeightProperty());
            detailsScroll.prefWidthProperty().bind(rightBox.prefWidthProperty());
            detailsScroll.getStyleClass().remove("scroll-pane");

            Text title = new Text();
            title.setText(level.title);
            title.setFill(Color.WHITE);
            title.setStyle("-fx-font-size: 50;");

            Text artist = new Text();
            artist.setText(level.aritst);
            artist.setFill(Color.WHITE);
            artist.setStyle("-fx-font-size: 30;");

            Text desc = new Text();
            desc.setText(level.desc);
            desc.setFill(Color.WHITE);

            ImageView previewView = new ImageView();
            Image preview = level.preview;
            previewView.setImage(preview);
            previewView.fitWidthProperty().bind(super.prefWidthProperty().multiply(0.5));
            previewView.setPreserveRatio(true);

            FlowPane diffSelector = new FlowPane();
            diffSelector.setAlignment(Pos.CENTER);
            ToggleGroup diffToggleGroup = new ToggleGroup();
            for (String diff : level.diffList)
            {
                RadioButton temp = new RadioButton();
                temp.getStyleClass().remove("radio-button");
                temp.getStyleClass().add("button");
                temp.getStyleClass().add("custom-radio-button");
                temp.setText(diff);
                temp.setUserData(diff);
                diffToggleGroup.getToggles().add(temp);
                diffSelector.getChildren().add(temp);
            }
            play.disableProperty().bind(diffToggleGroup.selectedToggleProperty().isNull());
            play.setOnAction(e -> Driver.setMenu(new LevelSurround(level, (String)diffToggleGroup.getSelectedToggle().getUserData(), Driver.getMenu())));
            HBox diffBox = new HBox();
            diffSelector.prefWidthProperty().bind(diffBox.widthProperty());
            diffBox.getChildren().add(diffSelector);
            
            details.setSpacing(10);
            details.getChildren().addAll(new TextFlow(title), new TextFlow(artist), new TextFlow(desc), previewView, diffBox);
            detailsScroll.setFitToWidth(true);

            rightBox.getChildren().add(detailsScroll);
            rightBox.setPadding(new Insets(5));
        }

        VBox rightSide = new VBox();
        rightSide.setAlignment(Pos.CENTER_RIGHT);
        rightSide.setSpacing(10);
        rightSide.getChildren().addAll(rightBox,play);

        super.setAlignment(Pos.CENTER_RIGHT);
        super.getChildren().add(rightSide);
    }
}
