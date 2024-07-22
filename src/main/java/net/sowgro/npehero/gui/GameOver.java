package net.sowgro.npehero.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import net.sowgro.npehero.Driver;
import net.sowgro.npehero.main.Difficulty;
import net.sowgro.npehero.main.Level;
import net.sowgro.npehero.main.Page;
import net.sowgro.npehero.main.Sound;

public class GameOver extends Page
{
    HBox content = new HBox();

    public GameOver(Level level, Difficulty diff, Page prev, int score2)
    {
        Text topText = new Text();
        topText.setText("Level Complete");
        topText.getStyleClass().add("t11");

        Text levelName = new Text();
        levelName.setText(level.title);
        levelName.getStyleClass().add("t2");

        Text levelArtist = new Text();
        levelArtist.setText(level.artist+" - "+diff.title);
        levelArtist.getStyleClass().add("t3");

        VBox levelDetailsBox = new VBox();
        levelDetailsBox.getChildren().addAll(levelName,levelArtist);
        levelDetailsBox.getStyleClass().add("box");
        levelDetailsBox.setPadding(new Insets(5));


        Text scoreLabel = new Text();
        scoreLabel.setText("Final score");
        scoreLabel.getStyleClass().add("t3");

        Text score = new Text();
        score.setText(score2+"");
        score.getStyleClass().add("t2");
        score.setStyle("-fx-font-size: 30;");

        VBox scoreBox = new VBox();
        scoreBox.getStyleClass().add("box");
        scoreBox.getChildren().addAll(scoreLabel,score);
        scoreBox.setPadding(new Insets(5));


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
            diff.leaderboard.add(name.getText(), score2);
        });

        BorderPane b = new BorderPane();
        b.setRight(save);
        b.setCenter(name);

        VBox nameBox = new VBox();
        nameBox.getChildren().addAll(nameLabel,b);
        nameBox.getStyleClass().add("box");
        nameBox.setSpacing(5);
        nameBox.setPadding(new Insets(5));


        Button exit = new Button();
        exit.setText("Back");
        exit.setOnAction(e -> {
            Sound.playSfx(Sound.BACKWARD);
            Driver.setMenu(prev);
        });

        Button replay = new Button();
        replay.setText("Replay");
        replay.setOnAction(e -> {
            Sound.playSfx(Sound.FORWARD);
            Driver.setMenu(new LevelSurround(level, diff, prev));
        });

        BorderPane buttonBox = new BorderPane();
        buttonBox.setLeft(exit);
        buttonBox.setRight(replay);

        VBox centerBox = new VBox();
        centerBox.getChildren().addAll(topText,levelDetailsBox,scoreBox,nameBox,buttonBox);
        centerBox.setSpacing(10);
        centerBox.setAlignment(Pos.CENTER);

        content.getChildren().add(centerBox);
        content.setAlignment(Pos.CENTER);
    }

    @Override
    public Pane getContent() {
        return content;
    }
}
