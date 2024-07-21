package net.sowgro.npehero.gui;

import net.sowgro.npehero.Driver;
import net.sowgro.npehero.gameplay.SongPlayer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import net.sowgro.npehero.main.Difficulty;
import net.sowgro.npehero.main.Level;
import net.sowgro.npehero.gameplay.ScoreController;
import net.sowgro.npehero.main.Sound;

public class LevelSurround extends Pane
{   

    public LevelSurround(Level level, Difficulty difficulty, Pane prev)
    {
        ScoreController sc = new ScoreController();
        SongPlayer game = new SongPlayer(level, difficulty, prev, sc);

        Button exit = new Button();
        exit.setText("Back");
        exit.setOnAction(e -> {
            Driver.setMenu(prev);
            Sound.playSfx(Sound.BACKWARD);
            game.cancel();
        });

        HBox buttonBox = new HBox();
        buttonBox.getChildren().addAll(exit);
        buttonBox.setAlignment(Pos.TOP_LEFT);
        buttonBox.setSpacing(10);

        Text title = new Text();
        title.setText(level.title);
        title.getStyleClass().add("t2");

        Text artist = new Text();
        artist.setText(level.artist+" - "+difficulty.title);
        artist.getStyleClass().add("t3");

        VBox titleTextBox = new VBox();
        titleTextBox.setAlignment(Pos.TOP_RIGHT);
        titleTextBox.getChildren().addAll(title, artist);
        titleTextBox.getStyleClass().add("box");
        titleTextBox.setPadding(new Insets(10));

        AnchorPane topBar = new AnchorPane();
        topBar.getChildren().addAll(buttonBox,titleTextBox);
        topBar.setLeftAnchor(buttonBox, 0.0);
        topBar.setRightAnchor(titleTextBox, 0.0);
        topBar.setTopAnchor(buttonBox, 0.0);
        topBar.setTopAnchor(titleTextBox, 0.0);
        topBar.setPadding(new Insets(10));
        

        Text scoreLabel = new Text();
        scoreLabel.setText("Score:");
        scoreLabel.getStyleClass().add("t3");

        Text scoreDisplay = new Text();
        scoreDisplay.textProperty().bind(sc.scoreProperty);
        scoreDisplay.getStyleClass().add("t1");

        VBox scoreTextBox = new VBox();
        scoreTextBox.setAlignment(Pos.BOTTOM_LEFT);
        scoreTextBox.getChildren().addAll(scoreLabel,scoreDisplay);
        scoreTextBox.setPadding(new Insets(10));
        scoreTextBox.getStyleClass().add("box");
        scoreTextBox.minWidthProperty().bind(scoreTextBox.heightProperty());

        AnchorPane scoreBox = new AnchorPane();
        scoreBox.getChildren().add(scoreTextBox);
        scoreBox.setLeftAnchor(scoreTextBox, 0.0);
        scoreBox.setBottomAnchor(scoreTextBox, 0.0);
        scoreBox.setPadding(new Insets(10));

        Text comboLabel = new Text();
        comboLabel.setText("Combo:");
        comboLabel.getStyleClass().add("t3");

        Text comboDisplay = new Text();
        comboDisplay.textProperty().bind(sc.comboProperty);
        comboDisplay.getStyleClass().add("t1");

        VBox comboTextBox = new VBox();
        comboTextBox.setAlignment(Pos.BOTTOM_RIGHT);
        comboTextBox.getChildren().addAll(comboLabel,comboDisplay);
        comboTextBox.setPadding(new Insets(10));
        comboTextBox.getStyleClass().add("box");
        comboTextBox.minWidthProperty().bind(comboTextBox.heightProperty());

        AnchorPane comboBox = new AnchorPane();
        comboBox.getChildren().add(comboTextBox);
        comboBox.setRightAnchor(comboTextBox, 0.0);
        comboBox.setBottomAnchor(comboTextBox, 0.0);
        comboBox.setPadding(new Insets(10));

        game.minWidthProperty().bind(super.prefHeightProperty().multiply(0.66));
        game.minHeightProperty().bind(super.prefHeightProperty());
        game.getStyleClass().add("box");


        comboBox.minWidthProperty().bind(super.prefWidthProperty().subtract(game.minWidthProperty()).divide(2));
        scoreBox.minWidthProperty().bind(super.prefWidthProperty().subtract(game.minWidthProperty()).divide(2));

        HBox centerBox = new HBox();
        centerBox.getChildren().addAll(comboBox, game, scoreBox);
        centerBox.setAlignment(Pos.BOTTOM_CENTER);

        StackPane root = new StackPane();
        root.getChildren().addAll(centerBox, topBar);

        super.getChildren().add(root);
        root.prefWidthProperty().bind(super.prefWidthProperty());
        root.prefHeightProperty().bind(super.prefHeightProperty());

        game.start();
    }
}