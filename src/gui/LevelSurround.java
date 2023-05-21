package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import main.Level;

public class LevelSurround extends Pane
{
    //will have param (Level l)
    public LevelSurround(Level level, String diff2, Pane prev)
    {
        Button exit = new Button();
        exit.setText("Exit");
        exit.setOnAction(e -> Driver.setMenu(prev));

        Button pause = new Button();
        pause.setText("Pause");

        HBox buttonBox = new HBox();
        buttonBox.getChildren().addAll(exit,pause);
        buttonBox.setAlignment(Pos.TOP_LEFT);
        buttonBox.setSpacing(10);

        Text title = new Text();
        title.setText(level.title);
        title.setFill(Color.WHITE);
        title.setStyle("-fx-font-size: 30;");

        Text artist = new Text();
        artist.setText(level.aritst+" - "+diff2);
        artist.setFill(Color.WHITE);

        VBox titleTextBox = new VBox();
        titleTextBox.setAlignment(Pos.TOP_RIGHT);
        titleTextBox.getChildren().addAll(title, artist);

        BorderPane topBar = new BorderPane();
        topBar.setLeft(buttonBox);
        topBar.setRight(titleTextBox);
        topBar.setPadding(new Insets(10));
        

        Text scoreLabel = new Text();
        scoreLabel.setText("Score:");
        scoreLabel.setFill(Color.WHITE);

        Text score = new Text();
        score.setText("100000");
        score.setFill(Color.WHITE);
        score.setStyle("-fx-font-size: 50;");

        VBox scoreTextBox = new VBox();
        scoreTextBox.setAlignment(Pos.BOTTOM_LEFT);
        scoreTextBox.getChildren().addAll(scoreLabel,score);
        scoreTextBox.setPadding(new Insets(10));


        Text comboLabel = new Text();
        comboLabel.setText("Combo:");
        comboLabel.setFill(Color.WHITE);

        Text combo = new Text();
        combo.setText("100000");
        combo.setFill(Color.WHITE);
        combo.setStyle("-fx-font-size: 50;");

        VBox comboTextBox = new VBox();
        comboTextBox.setAlignment(Pos.BOTTOM_RIGHT);
        comboTextBox.getChildren().addAll(comboLabel,combo);
        comboTextBox.setPadding(new Insets(10));

        Pane game = new Pane();
        game.minWidthProperty().bind(super.prefHeightProperty().multiply(0.66));
        game.minHeightProperty().bind(super.prefHeightProperty());
        game.getStyleClass().add("textBox");

        HBox centerBox = new HBox();
        centerBox.getChildren().addAll(comboTextBox,game, scoreTextBox);
        centerBox.setAlignment(Pos.BOTTOM_CENTER);

        StackPane root = new StackPane();
        root.getChildren().addAll(centerBox, topBar);

        super.getChildren().add(root);
        root.prefWidthProperty().bind(super.prefWidthProperty());
        root.prefHeightProperty().bind(super.prefHeightProperty());
    }
}