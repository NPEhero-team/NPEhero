package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import main.Level;

public class GameOver extends Pane
{
    public GameOver(int score2, Pane lastMenu, Level level, String diff)
    {
        Text topText = new Text();
        topText.setText("Level Complete");
        topText.setFill(Color.WHITE);
        topText.setStyle("-fx-font-size: 50;");


        Text levelName = new Text();
        levelName.setText(level.title);
        levelName.setFill(Color.WHITE);
        levelName.setStyle("-fx-font-size: 30;");

        Text levelArtist = new Text();
        levelArtist.setText(level.aritst+" - "+diff);
        levelArtist.setFill(Color.WHITE);

        VBox levelDetailsBox = new VBox();
        levelDetailsBox.getChildren().addAll(levelName,levelArtist);
        levelDetailsBox.getStyleClass().add("textBox");
        levelDetailsBox.setPadding(new Insets(5));


        Text scoreLabel = new Text();
        scoreLabel.setText("Final score");
        scoreLabel.setFill(Color.WHITE);

        Text score = new Text();
        score.setText(score2+"");
        score.setFill(Color.WHITE);
        score.setStyle("-fx-font-size: 30;");

        VBox scoreBox = new VBox();
        scoreBox.getStyleClass().add("textBox");
        scoreBox.getChildren().addAll(scoreLabel,score);
        scoreBox.setPadding(new Insets(5));


        Text nameLabel = new Text();
        nameLabel.setText("Leaderboard entry");
        nameLabel.setFill(Color.WHITE);

        TextField name = new TextField();
        name.getStyleClass().remove("text-feild");
        name.getStyleClass().add("custom-radio-button");
        name.setText("name");

        Button save = new Button();
        save.setText("Add");
        save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                save.setDisable(true);
                name.setDisable(true);
            }
        });

        BorderPane b = new BorderPane();
        b.setRight(save);
        b.setCenter(name);

        VBox nameBox = new VBox();
        nameBox.getChildren().addAll(nameLabel,b);
        nameBox.getStyleClass().add("textBox");
        nameBox.setSpacing(5);
        nameBox.setPadding(new Insets(5));


        Button exit = new Button();
        exit.setText("Exit");
        exit.setOnAction(e -> Driver.setMenu(lastMenu));

        Button replay = new Button();
        replay.setText("Replay");
        replay.setOnAction(e -> Driver.setMenu(new LevelSurround(level, diff, lastMenu)));

        BorderPane buttonBox = new BorderPane();
        buttonBox.setLeft(exit);
        buttonBox.setRight(replay);


        VBox centerBox = new VBox();
        centerBox.getChildren().addAll(topText,levelDetailsBox,scoreBox,nameBox,buttonBox);
        centerBox.setSpacing(10);
        centerBox.setAlignment(Pos.CENTER);

        HBox rootBox = new HBox();
        rootBox.getChildren().add(centerBox);
        rootBox.setAlignment(Pos.CENTER);
        rootBox.prefWidthProperty().bind(super.prefWidthProperty()); 
        rootBox.prefHeightProperty().bind(super.prefHeightProperty());

        super.getChildren().add(rootBox);
    }
}
