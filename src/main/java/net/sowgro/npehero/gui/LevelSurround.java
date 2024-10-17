package net.sowgro.npehero.gui;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import net.sowgro.npehero.Driver;
import net.sowgro.npehero.gameplay.SongPlayer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import net.sowgro.npehero.levelapi.Difficulty;
import net.sowgro.npehero.gameplay.ScoreController;
import net.sowgro.npehero.main.Page;
import net.sowgro.npehero.main.Sound;

public class LevelSurround extends Page
{
    private final StackPane content = new StackPane();

    public LevelSurround(Difficulty difficulty, Page prev)
    {
        ScoreController sc = new ScoreController();
        SongPlayer game = new SongPlayer(difficulty, prev, sc);

        Button exit = new Button();
        exit.setText("Back");
        exit.setOnAction(_ -> {
            Driver.setMenu(prev);
            Sound.playSfx(Sound.BACKWARD);
            game.cancel();
        });

        HBox buttonBox = new HBox();
        buttonBox.getChildren().addAll(exit);
        buttonBox.setAlignment(Pos.TOP_LEFT);
        buttonBox.setSpacing(10);

        Text title = new Text();
        title.setText(difficulty.level.title);
        title.getStyleClass().add("t2");

        Text artist = new Text();
        artist.setText(difficulty.level.artist+" - "+difficulty.title);
        artist.getStyleClass().add("t3");

        VBox titleTextBox = new VBox();
        titleTextBox.setAlignment(Pos.TOP_RIGHT);
        titleTextBox.getChildren().addAll(title, artist);
        titleTextBox.getStyleClass().add("box");
        titleTextBox.setPadding(new Insets(10));

        AnchorPane topBar = new AnchorPane();
        topBar.getChildren().addAll(buttonBox,titleTextBox);
        AnchorPane.setLeftAnchor(buttonBox, 0.0);
        AnchorPane.setRightAnchor(titleTextBox, 0.0);
        AnchorPane.setTopAnchor(buttonBox, 0.0);
        AnchorPane.setTopAnchor(titleTextBox, 0.0);
        topBar.setPadding(new Insets(10));
        

        Text scoreLabel = new Text();
        scoreLabel.setText("Score:");
        scoreLabel.getStyleClass().add("t3");

        Text scoreDisplay = new Text();
        scoreDisplay.textProperty().bind(sc.score.asString());
        scoreDisplay.getStyleClass().add("t1");

        VBox scoreTextBox = new VBox();
        scoreTextBox.setAlignment(Pos.BOTTOM_LEFT);
        scoreTextBox.getChildren().addAll(scoreLabel,scoreDisplay);
        scoreTextBox.setPadding(new Insets(10));
        scoreTextBox.getStyleClass().add("box");
        scoreTextBox.minWidthProperty().bind(scoreTextBox.heightProperty());

        AnchorPane scoreBox = new AnchorPane();
        scoreBox.getChildren().add(scoreTextBox);
        AnchorPane.setLeftAnchor(scoreTextBox, 0.0);
        AnchorPane.setBottomAnchor(scoreTextBox, 0.0);
        scoreBox.setPadding(new Insets(10));

        Text comboLabel = new Text();
        comboLabel.setText("Combo:");
        comboLabel.getStyleClass().add("t3");

        Text comboDisplay = new Text();
        comboDisplay.textProperty().bind(sc.combo.asString());
        comboDisplay.getStyleClass().add("t1");

        Label comboMultiplier = new Label();
        comboMultiplier.getStyleClass().add("gray");
        comboMultiplier.textProperty().bind(sc.comboMultiplier.asString().concat("x score multiplier"));

        VBox comboTextBox = new VBox();
        comboTextBox.setAlignment(Pos.BOTTOM_RIGHT);
        comboTextBox.getChildren().addAll(comboLabel,comboDisplay, comboMultiplier);
        comboTextBox.setPadding(new Insets(10));
        comboTextBox.getStyleClass().add("box");
        comboTextBox.minWidthProperty().bind(comboTextBox.heightProperty());

        AnchorPane comboBox = new AnchorPane();
        comboBox.getChildren().add(comboTextBox);
        AnchorPane.setRightAnchor(comboTextBox, 0.0);
        AnchorPane.setBottomAnchor(comboTextBox, 0.0);
        comboBox.setPadding(new Insets(10));

        game.setMinHeight(720);
        game.setMinWidth(720 * 0.55);
        game.setMaxHeight(720);
        game.setMaxWidth(720 * 0.55);
        var scale = content.prefHeightProperty().divide(720);
        game.scaleXProperty().bind(scale);
        game.scaleYProperty().bind(scale);
        game.getStyleClass().add("box");

        BorderPane gameHolder = new BorderPane(game);
        gameHolder.maxHeightProperty().bind(content.prefHeightProperty());
        gameHolder.maxWidthProperty().bind(content.prefHeightProperty().multiply(0.55));
        gameHolder.minHeightProperty().bind(content.prefHeightProperty());
        gameHolder.minWidthProperty().bind(content.prefHeightProperty().multiply(0.55));

        var widthBind = content.widthProperty().subtract(gameHolder.widthProperty()).divide(2);
        scoreBox.prefWidthProperty().bind(widthBind);
        comboBox.prefWidthProperty().bind(widthBind);

        HBox centerBox = new HBox();
//        HBox.setHgrow(gameHolder, Priority.NEVER);
        centerBox.getChildren().addAll(comboBox, gameHolder, scoreBox);
        centerBox.setAlignment(Pos.BOTTOM_CENTER);

        content.getChildren().addAll(centerBox, topBar);

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                game.start();
            }
        }.start();
    }

    @Override
    public Pane getContent() {
        return content;
    }

    @Override
    public void onLeave() {
        Sound.playSong(Sound.MENU_SONG);
    }
}