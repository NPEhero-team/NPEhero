package net.sowgro.npehero.gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import net.sowgro.npehero.Driver;
import net.sowgro.npehero.gameplay.ScoreController;
import net.sowgro.npehero.main.ErrorDisplay;
import net.sowgro.npehero.levelapi.Difficulty;
import net.sowgro.npehero.levelapi.Level;
import net.sowgro.npehero.main.Page;
import net.sowgro.npehero.main.Sound;

import java.io.IOException;

public class GameOver extends Page
{
    private final Label yourScore;
    HBox content = new HBox();
    ScoreController score2;

    public GameOver(Level level, Difficulty diff, Page prev, ScoreController score2)
    {
        this.score2 = score2;

        Label topText = new Label();
        topText.setText("LEVEL COMPLETE");
        topText.getStyleClass().add("t11");

        Label levelName = new Label();
        levelName.setText(level.title);
        levelName.getStyleClass().add("t2");

        Label levelArtist = new Label();
        levelArtist.setText(level.artist+" - "+diff.title);
        levelArtist.getStyleClass().add("t3");

        VBox levelDetailsBox = new VBox();
        levelDetailsBox.getChildren().addAll(levelName,levelArtist);
        levelDetailsBox.getStyleClass().add("box");
        levelDetailsBox.setPadding(new Insets(10));


        var scoreLabel = new BorderPane();
        scoreLabel.setLeft(new Label("Final Score"));
        scoreLabel.setRight(new Label("Max Score"));

        ScoreController maxScoreController = new ScoreController();
        for (int i = 0; i < diff.notes.list.size(); i++) {
            maxScoreController.perfect();
        }

        var score = new BorderPane(new Label("/"));
        yourScore = new Label("0");
        score.setLeft(yourScore);
        score.setRight( new Label(maxScoreController.score.get() + ""));
//        score.getStyleClass().add("t2");
        score.setStyle("-fx-font-size: 30;");

        VBox scoreBox = new VBox();
        scoreBox.getStyleClass().add("box");
        scoreBox.getChildren().addAll(scoreLabel,score);
        scoreBox.setPadding(new Insets(10));


        Text nameLabel = new Text();
        nameLabel.setText("Leaderboard entry");
        nameLabel.getStyleClass().add("t3");

        TextField name = new TextField();
        name.setText("name");

        Button save = new Button();
        save.setText("Add");
        save.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            save.setDisable(true);
            name.setDisable(true);
            try {
                diff.leaderboard.add(name.getText(), score2.score.get());
            } catch (IOException e) {
                Driver.setMenu(new ErrorDisplay("Failed to save score to leaderboard", e, this));
            }
        });

        BorderPane b = new BorderPane();
        b.setRight(save);
        b.setCenter(name);

        VBox nameBox = new VBox();
        nameBox.getChildren().addAll(nameLabel,b);
        nameBox.getStyleClass().add("box");
        nameBox.setSpacing(10);
        nameBox.setPadding(new Insets(10));


        Button exit = new Button();
        exit.setText("Back");
        exit.setOnAction(_ -> {
            Sound.playSfx(Sound.BACKWARD);
            Driver.setMenu(prev);
        });

        Button replay = new Button();
        replay.setText("Replay");
        replay.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            Driver.setMenu(new LevelSurround(diff, prev));
        });

        BorderPane buttonBox = new BorderPane();
        buttonBox.setLeft(exit);
        buttonBox.setRight(replay);

        VBox centerBox = new VBox();
        centerBox.getChildren().addAll(topText,levelDetailsBox,scoreBox,nameBox,buttonBox);
        centerBox.setSpacing(10);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPrefWidth(450);
        centerBox.setMaxWidth(450);

        content.getChildren().add(centerBox);
        content.setAlignment(Pos.CENTER);
    }

    @Override
    public void onView() {
        // score count up animation
        Timeline tl = new Timeline();
        int stepSize = score2.score.get() / 20;
        for (int i = 1; i <= 20; i++) {
            int i1 = i;
            tl.getKeyFrames().add(new KeyFrame(
                    Duration.seconds(i / 20.0 + 0.5),
                    _ -> yourScore.setText(i1 * stepSize + "")
            ));
        }
        tl.play();

    }

    @Override
    public Pane getContent() {
        return content;
    }
}
