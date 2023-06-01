package devmenu;

import java.io.File;

import gameplay.Timer;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.Difficulty;
import sound.AudioFilePlayer;

public class NotesEditor
{
    Difficulty diff;
    AudioFilePlayer music;
    Timer timer;
    public NotesEditor(Difficulty diff)
    {
        this.diff = diff;
        
        Text timerDisplay = new Text("TIMER");

        Button start = new Button("Start");
        start.setOnAction(e -> start());

        Button stop = new Button("Stop");
        stop.setOnAction(e -> stop());

        HBox main = new HBox();
        main.getChildren().addAll(timerDisplay,start,stop);

        Scene scene = new Scene(main);
        Stage primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void start()
    {
        music = new AudioFilePlayer(new File(diff.thisDir, "song.wav").toPath().toString());
        timer = new Timer(diff.bpm);
    }

    private void stop()
    {

    }
}