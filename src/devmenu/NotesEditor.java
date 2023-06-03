package devmenu;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import gameplay.Timer;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
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
    Text help;
    String t1 = "Press Start to begin recording. Use the same keys.";
    String t2 = "Now recording. Press Stop or ESC to finish";
    MediaPlayer mediaPlayer;
    Difficulty diff;
    AudioFilePlayer music;
    Timer timer;
    PrintWriter writer;
    public NotesEditor(Difficulty diff) throws FileNotFoundException, UnsupportedEncodingException
    {
        this.diff = diff;

        help = new Text(t1);

        Text cur = new Text("-----");

        Button start = new Button("Start");
        start.setOnAction(e -> start());
        start.setFocusTraversable(false);

        Button stop = new Button("Stop");
        stop.setOnAction(e -> stop());
        stop.setFocusTraversable(false);

        Media song = new Media(diff.level.song.toURI().toString());
        mediaPlayer = new MediaPlayer(song);
        new MediaView(mediaPlayer);

        VBox main = new VBox();
        main.getChildren().addAll(help,cur,start,stop);

        Scene scene = new Scene(main);
        Stage primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.show();

        writer = new PrintWriter(diff.notes, "UTF-8");

        scene.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.D) {
				writer.println("d"+timer);
                cur.setText("d"+timer);
			}
			if (e.getCode() == KeyCode.F) {
				writer.println("f"+timer);
                cur.setText("f"+timer);
			}
			if (e.getCode() == KeyCode.SPACE) {
				writer.println("s"+timer);
                cur.setText("s"+timer);
			}
			if (e.getCode() == KeyCode.J) {
				writer.println("j"+timer);
                cur.setText("j"+timer);
			}
			if (e.getCode() == KeyCode.K) {
				writer.println("k"+timer);
                cur.setText("k"+timer);
			}
            if (e.getCode() == KeyCode.ESCAPE)
            {
                stop();
            }
		});

        primaryStage.setOnCloseRequest(e -> stop());
    }

    private void start()
    {
        mediaPlayer.play();
        timer = new Timer(diff.bpm);
        help.setText(t2);
    }

    private void stop()
    {
        try {
        mediaPlayer.stop();
        diff.numBeats = (int)timer.time();
        timer = null;
        writer.close();
        help.setText(t1);
        }
        catch (Exception e)
        {
        }
    }
}