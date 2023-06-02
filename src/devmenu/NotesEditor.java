package devmenu;

import java.io.File;
import gameplay.Timer;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.Difficulty;
import sound.AudioFilePlayer;

public class NotesEditor
{
    MediaPlayer mediaPlayer;
    Difficulty diff;
    AudioFilePlayer music;
    Timer timer;
    public NotesEditor(Difficulty diff)
    {
        this.diff = diff;

        Button start = new Button("Start");
        start.setOnAction(e -> start());

        Button stop = new Button("Pause");
        stop.setOnAction(e -> stop());

        Button print = new Button("print");
        print.setOnAction(e -> System.out.println(timer.time()));

        Media song = new Media(diff.level.song.toURI().toString());
        mediaPlayer = new MediaPlayer(song);
        new MediaView(mediaPlayer);

        VBox main = new VBox();
        main.getChildren().addAll(start,stop,print);

        Scene scene = new Scene(main);
        Stage primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void start()
    {
        mediaPlayer.play();
        
        timer = new Timer(diff.bpm);
    }

    private void stop()
    {
        mediaPlayer.pause();
    }
}