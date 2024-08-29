package net.sowgro.npehero.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import net.sowgro.npehero.Driver;
import net.sowgro.npehero.levelapi.Difficulty;
import net.sowgro.npehero.levelapi.Level;
import net.sowgro.npehero.main.Sound;

public class LevelDetails extends VBox
{
    /**
     * Create a LevelDetails Pane
     * @param level: the selected level on the right side
     */
    public LevelDetails(Level level, LevelSelector ls)
    {
        BorderPane rightBox = new BorderPane();
        rightBox.prefWidthProperty().bind(super.widthProperty());
        rightBox.prefHeightProperty().bind(super.heightProperty().multiply(0.75));
//        rightBox.setMinWidth(350);
        rightBox.getStyleClass().add("box");
        rightBox.setPadding(new Insets(5));

        Button play = new Button();
        play.setDisable(true);
        play.setText("Play");

        Button leaderboard = new Button();
        leaderboard.setDisable(true);
        leaderboard.setText("Leaderboard");

        if (level == null) { //if no level is selected from the list on the left
            Label desc = new Label();
            desc.setText("Select a level from the left pane");
            desc.getStyleClass().add("t3");
            rightBox.setCenter(desc);
        }
        else {
            Text title = new Text();
            title.setText(level.title);
            title.getStyleClass().add("t1");

            Text artist = new Text();
            artist.setText(level.artist);
            artist.getStyleClass().add("t2");

            TextFlow titleFlow = new TextFlow(title);
            titleFlow.setLineSpacing(0);
            TextFlow artistFlow = new TextFlow(artist);
            VBox titleArtistDesc = new VBox(titleFlow, artistFlow);
            if (level.desc != null && !level.desc.isEmpty()) {
                Text desc = new Text();
                desc.setText(level.desc);
                desc.getStyleClass().add("t3");
                TextFlow descFlow = new TextFlow(desc);
                descFlow.setPadding(new Insets(20, 0, 0, 0));
                titleArtistDesc.getChildren().add(descFlow);
            }
            titleArtistDesc.setSpacing(-5);

            ImageView imageView = new ImageView();
            Image image = level.preview;
            imageView.setImage(image);
            imageView.fitWidthProperty().bind(rightBox.widthProperty().multiply(0.5));
            imageView.setPreserveRatio(true);
            VBox imageHolder = new VBox(imageView);
            imageHolder.setAlignment(Pos.CENTER);

            FlowPane diffSelector = new FlowPane();
            diffSelector.setAlignment(Pos.CENTER);
            ToggleGroup diffToggleGroup = new ToggleGroup(); //allows only one to be selected at a time
            for (Difficulty diff : level.difficulties.getValidList()) //adds a button for each diff
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
                Driver.setMenu(new LeaderboardView((Difficulty)diffToggleGroup.getSelectedToggle().getUserData(), ls));
            });

            HBox diffBox = new HBox();
            diffSelector.prefWidthProperty().bind(diffBox.widthProperty());
            diffBox.getChildren().add(diffSelector);

            BorderPane details = new BorderPane();
            details.setCenter(imageHolder);
            details.setBottom(diffBox);
            details.setTop(titleArtistDesc);
            details.setPadding(new Insets(5));

            ScrollPane detailsScroll = new ScrollPane(details);
            detailsScroll.prefHeightProperty().bind(rightBox.prefHeightProperty());
            detailsScroll.prefWidthProperty().bind(rightBox.prefWidthProperty());
            detailsScroll.getStyleClass().remove("scroll-pane");
            detailsScroll.setFitToWidth(true);
            details.minHeightProperty().bind(detailsScroll.heightProperty());
            detailsScroll.setFitToHeight(true);

            rightBox.setCenter(detailsScroll);
            rightBox.setPadding(new Insets(5));
        }

        HBox buttonBox = new HBox();
        buttonBox.getChildren().addAll(leaderboard,play);
        buttonBox.setSpacing(5);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        super.setSpacing(10);
        super.setAlignment(Pos.CENTER_RIGHT);
        super.getChildren().addAll(rightBox,buttonBox);
    }
}
