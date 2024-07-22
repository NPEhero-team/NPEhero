package net.sowgro.npehero.devmenu;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javafx.geometry.Pos;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import net.sowgro.npehero.gameplay.Timer;
import net.sowgro.npehero.Driver;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import net.sowgro.npehero.main.Control;
import net.sowgro.npehero.main.Difficulty;
import net.sowgro.npehero.main.Page;
import net.sowgro.npehero.main.Sound;

public class NotesEditor extends Page
{
    Text help;
    String t1 = "Press Start to begin recording. Use the same keys. Note: existing notes will be overwritten.";
    String t2 = "Now recording. Press Stop or " + Control.LEGACY_STOP.getKey().toString() + " to finish";
    Difficulty diff;
    Timer timer;
    PrintWriter writer;

    private HBox content = new HBox();

    public NotesEditor(Difficulty diff, Page prev)
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

        VBox main = new VBox();
        main.getChildren().addAll(help,cur,start,stop);

        Button exit = new Button();
        exit.setText("Back");
        exit.setOnAction(e -> {
            Sound.playSfx(Sound.BACKWARD);
            Driver.setMenu(prev);
        });

        VBox centerBox = new VBox();
        centerBox.getChildren().addAll(main, exit);
        centerBox.setSpacing(10);
        centerBox.setAlignment(Pos.CENTER);

        content.getChildren().add(centerBox);
        content.setAlignment(Pos.CENTER);

        try {
            writer = new PrintWriter(diff.notes.getFile(), "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        Scene scene = Driver.primaryStage.getScene();
        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
			if (e.getCode() == Control.LANE0.getKey()) {
				writer.println("d"+timer);
                cur.setText("d"+timer);
			}
			if (e.getCode() == Control.LANE1.getKey()) {
				writer.println("f"+timer);
                cur.setText("f"+timer);
			}
			if (e.getCode() == Control.LANE2.getKey()) {
				writer.println("s"+timer);
                cur.setText("s"+timer);
			}
			if (e.getCode() == Control.LANE3.getKey()) {
				writer.println("j"+timer);
                cur.setText("j"+timer);
			}
			if (e.getCode() == Control.LANE4.getKey()) {
				writer.println("k"+timer);
                cur.setText("k"+timer);
			}
            if (e.getCode() == Control.LEGACY_STOP.getKey())
            {
                stop();
            }
            e.consume();
		});

        Driver.primaryStage.setOnCloseRequest(e -> stop());
    }

    @Override
    public Pane getContent() {
        return content;
    }

    private void start()
    {
        Sound.playSong(new Media(diff.level.song.toString()));
        timer = new Timer(diff.bpm);
        help.setText(t2);
    }

    private void stop()
    {
        try 
        {
            Sound.stopSong();
            diff.numBeats = (int)Double.parseDouble(timer.toString());
            timer = null;
            writer.close();
            help.setText(t1);
        }
        catch (Exception e)
        {
            //System.err.println("tried to stop but already stopped");
        }
    }
}