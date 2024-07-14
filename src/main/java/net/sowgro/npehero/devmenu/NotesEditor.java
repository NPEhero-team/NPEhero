package net.sowgro.npehero.devmenu;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import net.sowgro.npehero.gameplay.Timer;
import net.sowgro.npehero.Driver;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import net.sowgro.npehero.main.Difficulty;

public class NotesEditor extends Pane
{
    Text help;
    String t1 = "Press Start to begin recording. Use the same keys. Note: existing notes will be overwritten.";
    String t2 = "Now recording. Press Stop or ESC to finish";
    Difficulty diff;
    Timer timer;
    PrintWriter writer;
    public NotesEditor(Difficulty diff, Pane prev) throws FileNotFoundException, UnsupportedEncodingException
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
            Driver.soundController.playSfx("backward");
            Driver.setMenu(prev);
        });

        VBox centerBox = new VBox();
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setSpacing(10);
        centerBox.getChildren().addAll(main,exit);
        centerBox.setMinWidth(400);

        HBox rootBox = new HBox();
        rootBox.prefWidthProperty().bind(super.prefWidthProperty());
        rootBox.prefHeightProperty().bind(super.prefHeightProperty());
        rootBox.getChildren().add(centerBox);
        rootBox.setAlignment(Pos.CENTER);

        super.getChildren().add(rootBox);

        writer = new PrintWriter(diff.notes.getFile(), "UTF-8");

        Scene scene = Driver.primaryStage.getScene();
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

        Driver.primaryStage.setOnCloseRequest(e -> stop());
    }

    private void start()
    {
        Driver.soundController.playSong(diff.level.song);
        timer = new Timer(diff.bpm);
        help.setText(t2);
    }

    private void stop()
    {
        try 
        {
            Driver.soundController.endSong();
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