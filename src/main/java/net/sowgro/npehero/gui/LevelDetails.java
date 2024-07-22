package net.sowgro.npehero.gui;

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
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import net.sowgro.npehero.Driver;
import net.sowgro.npehero.main.Difficulty;
import net.sowgro.npehero.main.Level;
import net.sowgro.npehero.main.Sound;

public class LevelDetails extends VBox
{
    /**
     * this class is a layout class, most of its purpose is to place UI elements like Buttons within Panes like VBoxes.
     * the creation of these UI elements are mostly not commented due to their repetitive and self explanatory nature.
     * style classes are defined in the style.css file.
     * 
     * @param level: the selected level on the right side
     */
    public LevelDetails(Level level, LevelSelector ls)
    {
        VBox rightBox = new VBox();
        rightBox.prefWidthProperty().bind(super.prefWidthProperty());
        rightBox.prefHeightProperty().bind(super.prefHeightProperty().multiply(0.75));
        rightBox.setMinWidth(350);
        rightBox.getStyleClass().add("box");

        Button play = new Button();
        play.setDisable(true);
        play.setText("Play");

        Button leaderboard = new Button();
        leaderboard.setDisable(true);
        leaderboard.setText("Leaderboard");

        if (level == null) //if no level is selected from the list on the left
        {
            Text desc = new Text();
            desc.setText("Select a level from the left pane");
            desc.getStyleClass().add("t3");
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
            title.getStyleClass().add("t1");

            Text artist = new Text();
            artist.setText(level.artist);
            artist.getStyleClass().add("t2");

            Text desc = new Text();
            desc.setText(level.desc);
            desc.getStyleClass().add("t3");

            ImageView previewView = new ImageView();
            Image preview = level.preview;
            previewView.setImage(preview);
            previewView.fitWidthProperty().bind(super.prefWidthProperty().multiply(0.5));
            previewView.setPreserveRatio(true);

            FlowPane diffSelector = new FlowPane();
            diffSelector.setAlignment(Pos.CENTER);
            ToggleGroup diffToggleGroup = new ToggleGroup(); //allows only one to be selected at a time
            for (Difficulty diff : level.difficulties.validList) //adds a button for each diff
            {
                RadioButton temp = new RadioButton();
                temp.getStyleClass().remove("radio-button"); //makes the buttons not look like a radio button and instead a normal button
                temp.getStyleClass().add("button");
                temp.setText(diff.title);
                temp.setUserData(diff); //allows the data and text to be separate
                diffToggleGroup.getToggles().add(temp);
                diffSelector.getChildren().add(temp);
            }
            play.disableProperty().bind(diffToggleGroup.selectedToggleProperty().isNull()); //disables play button when no difficulty is selected
            play.setOnAction(e -> {
                Sound.playSfx(Sound.FORWARD);
                Driver.setMenu(new LevelSurround(level, (Difficulty)diffToggleGroup.getSelectedToggle().getUserData(), ls));
            });

            leaderboard.disableProperty().bind(diffToggleGroup.selectedToggleProperty().isNull());
            leaderboard.setOnAction(e -> {
                Sound.playSfx(Sound.FORWARD);
                Driver.setMenu(new LeaderboardView((Difficulty)diffToggleGroup.getSelectedToggle().getUserData(), Driver.getMenu()));
            });


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

        HBox buttonBox = new HBox();
        buttonBox.getChildren().addAll(leaderboard,play);
        buttonBox.setSpacing(5);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        rightSide.getChildren().addAll(rightBox,buttonBox);

        super.setAlignment(Pos.CENTER_RIGHT);
        super.getChildren().add(rightSide);
    }
}
