package net.sowgro.npehero.gameplay;

import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import net.sowgro.npehero.Driver;
import net.sowgro.npehero.levelapi.Difficulty;
import net.sowgro.npehero.levelapi.Level;
import net.sowgro.npehero.levelapi.Note;
import net.sowgro.npehero.levelapi.Notes;
import net.sowgro.npehero.main.*;
import net.sowgro.npehero.gui.GameOver;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.animation.*;
import javafx.util.*;

public class SongPlayer extends HBox {

    static class Lane {
        Target target; //Initializes the button, each parameter is a placeholder that is changed later
        ArrayList<Block> blocks = new ArrayList<>(); //Array list containing all the notes currently on the field for that lane
        Pane pane = new Pane();
    }

    private final int FALL_TIME = 1;  // delay for notes falling down the screen (seconds)
    private final double START_DELAY = 1;  // seconds

    private final Level level;
    private final Media song;
    private final ScoreController scoreCounter;

    private final EventHandler<KeyEvent> eventHandler;
    private final Timeline timeline;

    private boolean done = false;
    private final Lane[] lanes = new Lane[5];

    public SongPlayer(Difficulty diff, Page prev, ScoreController scoreController) {
        this.level = diff.level;
        this.song = diff.level.song;
        this.scoreCounter = scoreController; // Uses the song's designated scoreCounter
        double songLength = diff.endTime != 0 ? diff.endTime : diff.level.song.getDuration().toSeconds();
        Notes notes = diff.notes;

        Sound.stopSong();
        if (level.background != null) {
            Driver.setBackground(level.background);
        }
        // create targets
        for (int i = 0; i < lanes.length; i++) {
            lanes[i] = new Lane();
            var tmp = new Target(level.colors[i], Control.lanes[i].targetString());
            bindTarget(tmp);
            lanes[i].target = tmp;
        }

        // create timeline
        timeline = new Timeline();
        for (Note note : notes.list) {
            // schedule each note to send at its time
            KeyFrame kf = new KeyFrame(Duration.seconds(note.getTime() + START_DELAY), _ -> sendNote(note));
            timeline.getKeyFrames().add(kf);
        }
        // schedule the song to start after the delay
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(FALL_TIME + START_DELAY), _ -> {
            if (!done) {
                Sound.playSong(song);
            }
        }));
        // schedule the game over screen to show at the end
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(songLength + FALL_TIME + START_DELAY), _ -> {
            Driver.setMenu(new GameOver(level, diff, prev, scoreCounter));
            cancel();
        }));

        // handle keyboard input
        eventHandler = e -> {
            for (int i = 0; i < lanes.length; i++) {
                if (e.getCode() == Control.lanes[i].getKey()) {
                    checkNote(lanes[i]);
                    e.consume();
                }
            }
            if (e.getCode() == Control.LEGACY_PRINT.getKey()) {
                System.out.println("" + timeline.getCurrentTime());
                e.consume();
            }
        };
        Driver.primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, eventHandler);

        // layout
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.BOTTOM_CENTER);		//puts the buttons in the center of the screen
        for (Lane lane : lanes) { //places the buttons in the correct row order
            Line line = new Line();
            line.setStroke(lane.target.getFillColor());
            line.setStrokeWidth(2);
            line.setStartY(0);
            line.endYProperty().bind(lane.target.layoutYProperty().subtract(2));
            VBox back = new VBox(line, lane.target);
            back.setAlignment(Pos.BOTTOM_CENTER);
            StackPane stackPane = new StackPane(back, lane.pane);
            buttonBox.getChildren().add(stackPane);
        }
        buttonBox.spacingProperty().bind(super.heightProperty().multiply(20/1080.0));
        super.getChildren().add(buttonBox);
        super.setPadding(new Insets(0, 20, 10, 20));
        super.setAlignment(Pos.BOTTOM_CENTER);
    }

    /**
     * Checks if a note should be sent at the current time, and sends the note if it
     * needs to be
     */
    public void sendNote(Note note) {
        Lane lane = lanes[note.lane];

        Block block = new Block(lane.target.getColor(), note);
        block.setCache(true);
        block.setCacheHint(CacheHint.SPEED);
        block.xProperty().bind(lane.pane.widthProperty().subtract(block.widthProperty()).divide(2));
        block.yProperty().bind(block.heightProperty().negate());
        bindBlock(block);

        lane.blocks.add(block);

        TranslateTransition anim = new TranslateTransition(Duration.seconds(FALL_TIME + 0.105));
        anim.setInterpolator(Interpolator.LINEAR);
        anim.byYProperty().bind(lane.pane.heightProperty().add(block.getHeight()).add(75));
        anim.setNode(block);
        anim.play();
        anim.setOnFinished(_ -> {
            if (lane.pane.getChildren().remove(block) && !done) {
                scoreCounter.miss();
                Sound.playSfx(Sound.MISS);
                FillTransition ft = new FillTransition(Duration.millis(500), lane.target.rect);
                ft.setFromValue(Color.RED);
                ft.setToValue(lane.target.getFillColor());
                ft.play();
            }
        });
        lane.pane.getChildren().add(block);
    }

    /**
     * Binds properties of the target to the screen
     * @param target The target to bind
     */
    private void bindTarget(Target target) {
        bindBlock(target.rect);
		target.rect.strokeWidthProperty().bind(super.heightProperty().multiply(4/1080.0));
    }

    private void bindBlock(Rectangle block) {
        var sizeBind = super.heightProperty().multiply(87/1080.0);
        block.heightProperty().bind(sizeBind);
        block.widthProperty().bind(sizeBind);
        var arcBind = super.heightProperty().multiply(20/1080.0);
        block.arcHeightProperty().bind(arcBind);
        block.arcWidthProperty().bind(arcBind);
    }

    /**
     * starts the gameLoop, a periodic background task runner that runs the methods within it 60 times every second
     */
    public void start() {
        timeline.play();
    }

    /**
     * Stops the gameLoop
     */
    public void cancel() {
        Driver.primaryStage.removeEventFilter(KeyEvent.KEY_PRESSED, eventHandler);
        done = true;
        Sound.stopSong();
        Sound.playSong(Sound.MENU_SONG);
        Driver.setMenuBackground();
        timeline.stop();
        timeline.getKeyFrames().clear(); // for some reason other instances of Timeline will have these keyframes if I don't clear.
    }

    /**
     * returns the pos in the lane array of the closest note to the goal
     * @param searchLane The list of blocks to search in
     * @return the position of the note
     */
    private int getClosestNote(ArrayList<Block> searchLane) {
        int pos = 0;

        for (int i = 0; i < searchLane.size(); i++) {
            if (distanceToTarget(searchLane.get(i)) < distanceToTarget(searchLane.get(pos))) {
                pos = i;
            }
        }
        return pos;
    }

    /**
     * Returns the distance to the goal of the given note
     * @param note Note to check the distance of
     * @return The distance between the note and the target in pixels
     */
    private double distanceToTarget(Block note) {
        return Math.abs((super.getHeight() - note.getTranslateY() + note.getHeight()/2) - lanes[0].target.rect.getLayoutY());
    }

    /**
     * When the player hits the key, checks the quality of the hit
     * @param lane the lane checking for a hit
     * @return 2 for a perfect hit, 1 for a good hit, 0 for a miss, and -1 if there are no notes to hit
     */
    private int checkNote(Lane lane) {
        ArrayList<Block> blocks = lane.blocks;
        if (blocks.isEmpty() || done) {
            return -1;
        }
        double distance = distanceToTarget(blocks.get(getClosestNote(blocks)));
        if (distance < super.getHeight() / 3) {

            FillTransition ft = new FillTransition(Duration.millis(500), lane.target.rect);
            ft.setToValue(lane.target.getFillColor());

            lane.pane.getChildren().removeAll(blocks.get(getClosestNote(blocks)));
            blocks.remove(blocks.get(getClosestNote(blocks)));
            if (distance < super.getHeight() / 12) {
                ft.setFromValue(Color.WHITE);
                ft.play();
                scoreCounter.perfect();
                Sound.playSfx(Sound.HIT);
                return 2;
            }
            if (distance < super.getHeight() / 4) {
                ft.setFromValue(Color.CYAN);
                ft.play();
                scoreCounter.good();
                Sound.playSfx(Sound.HIT);
                return 1;
            }
            ft.setFromValue(Color.RED);
            ft.play();
            scoreCounter.miss();
            Sound.playSfx(Sound.MISS);
            return 0;
        }
        return -1;
    }

}