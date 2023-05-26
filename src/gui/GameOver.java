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
import javafx.scene.text.Text;
import main.Difficulty;
import main.Level;

public class GameOver extends Pane
{
    /*
     * this class is a layout class, most of its purpose is to place UI elements like Buttons within Panes like VBoxes.
     * the creation of these UI elements are mostly not commented due to their repetitive and self explanatory nature.
     * style classes are defined in the style.css file.
     */
    public GameOver(Level level, Difficulty diff, Pane lastMenu, int score2)
    {
        Text topText = new Text();
        topText.setText("Level Complete");
        topText.getStyleClass().add("t1");

        Text levelName = new Text();
        levelName.setText(level.getTitle());
        levelName.getStyleClass().add("t2");

        Text levelArtist = new Text();
        levelArtist.setText(level.getArtist()+" - "+diff);
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
        name.getStyleClass().remove("text-feild");
        name.getStyleClass().add("button");
        name.setText("name");

        Button save = new Button();
        save.setText("Add");
        save.setOnAction(new EventHandler<ActionEvent>() { //this is the same as the "e ->" thing but it allows more than one line to be added 
            @Override
            public void handle(ActionEvent event) {
                save.setDisable(true);
                name.setDisable(true);
                diff.addToLeaderboard(name.getText(), score2);
            }
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
