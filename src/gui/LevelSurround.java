package gui;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import gameplay.SongPlayer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.Difficulty;
import main.Level;
import main.ScoreController;

public class LevelSurround extends Pane
{   
    /*
     * this class is a layout class, most of its purpose is to place UI elements like Buttons within Panes like VBoxes.
     * the creation of these UI elements are mostly not commented due to their repetitive and self explanatory nature.
     * style classes are defined in the style.css file.
     */
    public LevelSurround(Level level, Difficulty difficulty, Pane prev)
    {
        ScoreController sc = new ScoreController();
        SongPlayer game = new SongPlayer(level, difficulty, prev, sc);

        Button exit = new Button();
        exit.setText("Back");
        exit.setOnAction(e -> {
            Driver.setMenu(prev);
            Driver.soundController.playSfx("backward");
            game.cancel();
        });

        HBox buttonBox = new HBox();
        buttonBox.getChildren().addAll(exit);
        buttonBox.setAlignment(Pos.TOP_LEFT);
        buttonBox.setSpacing(10);

        Text title = new Text();
        title.setText(level.getTitle());
        title.getStyleClass().add("t2");

        Text artist = new Text();
        artist.setText(level.getArtist()+" - "+difficulty.title);
        artist.getStyleClass().add("t3");

        VBox titleTextBox = new VBox();
        titleTextBox.setAlignment(Pos.TOP_RIGHT);
        titleTextBox.getChildren().addAll(title, artist);

        BorderPane topBar = new BorderPane();
        topBar.setLeft(buttonBox);
        topBar.setRight(titleTextBox);
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

        game.minWidthProperty().bind(super.prefHeightProperty().multiply(0.66));
        game.minHeightProperty().bind(super.prefHeightProperty());
        game.getStyleClass().add("box");


        comboTextBox.minWidthProperty().bind(super.prefWidthProperty().subtract(game.minWidthProperty()).divide(2));
        scoreTextBox.minWidthProperty().bind(super.prefWidthProperty().subtract(game.minWidthProperty()).divide(2));

        HBox centerBox = new HBox();
        centerBox.getChildren().addAll(comboTextBox, game, scoreTextBox);
        centerBox.setAlignment(Pos.BOTTOM_CENTER);

        StackPane root = new StackPane();
        root.getChildren().addAll(centerBox, topBar);

        super.getChildren().add(root);
        root.prefWidthProperty().bind(super.prefWidthProperty());
        root.prefHeightProperty().bind(super.prefHeightProperty());

        //for debug menu
        Button addScore = new Button();
        addScore.setText(level.getTitle() + " addscore");
        addScore.setOnAction(e -> sc.setScore(sc.getScore()+1));
        Driver.debug.addButton(addScore);

        Button addCombo = new Button();
        addCombo.setText(level.getTitle() + " addcombo");
        addCombo.setOnAction(e -> sc.setCombo(sc.getCombo()+1));
        Driver.debug.addButton(addCombo);

        Button printD = new Button();
        printD.setText(level.getTitle() + " print debug");
        printD.setOnAction(e -> sc.print());
        Driver.debug.addButton(printD);

        Button testfinish = new Button();
        testfinish.setText(level.getTitle() + "launch game end");
        testfinish.setOnAction(e -> Driver.setMenu(new GameOver(level, difficulty, prev, sc.getScore())));
        Driver.debug.addButton(testfinish);

        game.start();
    }
}