package net.sowgro.npehero.gameplay;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import net.sowgro.npehero.Driver;
import net.sowgro.npehero.levelapi.Difficulty;
import net.sowgro.npehero.levelapi.Level;
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

class KeyLane {
	Target target; //Initializes the button, each parameter is a placeholder that is changed later
	Queue<NoteInfo> sends = new LinkedList<>(); //Queue that dictates when to send the notes
	ArrayList<Block> lane = new ArrayList<>(); //Array list containing all the notes currently on the field for that lane
}

public class SongPlayer extends Pane {
	private Double bpm;		//initializes the bpm of the song, to be read in from a metadata file later
	private double songLength; //initializes the length of the song in terms of the song's bpm, to be read in later

	private EventHandler<KeyEvent> eventHandler;

	private Media song;
	private boolean songIsPlaying = false;
	private boolean missMute = false;

	private Level level;
	private Difficulty difficulty;
	private Page pane;

	Timer timer;			//the timer that determines when notes will fall, counted in terms of the song's bpm
	final int TIME = 1000;  //delay for notes falling down the screen

	ScoreController scoreCounter = new ScoreController();	//used to keep track of the user's score

	HBox buttonBox = new HBox();	//used to align the buttons horizontally
	VBox place = new VBox();		//used to place the buttons within the frame

	KeyLane[] lanes = new KeyLane[5];

	{
		lanes[0] = new KeyLane();
		lanes[0].target = new Target(Color.RED, 50, 50, 5, Control.LANE0.targetString());
		lanes[1] = new KeyLane();
		lanes[1].target = new Target(Color.BLUE, 50, 50, 5, Control.LANE1.targetString());
		lanes[2] = new KeyLane();
		lanes[2].target = new Target(Color.GREEN, 50, 50, 5, Control.LANE2.targetString());
		lanes[3] = new KeyLane();
		lanes[3].target = new Target(Color.PURPLE, 50, 50, 5, Control.LANE3.targetString());
		lanes[4] = new KeyLane();
		lanes[4].target = new Target(Color.YELLOW, 50, 50, 5, Control.LANE4.targetString());
	}

	/**
	 * Establishes what the chart for the song is going to look like
	 * @throws FileNotFoundException
	 */
	public void loadSong() throws FileNotFoundException {
		difficulty.notes.list.forEach(e -> lanes[e.lane].sends.add(new NoteInfo(e.time.get() * (bpm / 60))));
	}

	public SongPlayer(Difficulty diff, Page prev, ScoreController cntrl) {
		Sound.stopSong();
		song = diff.level.song;

		if (diff.level.background != null) {
			Driver.setBackground(diff.level.background);
		}
		bpm = 60.0;					//Reads the song's bpm from a metadata file
		level = diff.level;
		difficulty = diff;
		pane = prev;

		//System.out.println(d.bpm + " " + d.numBeats);

		if (diff.endTime != 0) {
			songLength = diff.endTime;
		}
		else {
			songLength = diff.level.song.getDuration().toSeconds();
		}
		timer = new Timer(bpm);	//Sets the timer's bpm to that of the song
		scoreCounter = cntrl;			//Uses the song's designated scoreCounter

		try {
			loadSong();			//Calls the file loading from the song's notes.txt file
		} catch (FileNotFoundException e) {
		}

		for (int i = 0; i < lanes.length; i++) {
			lanes[i].target.setColor(level.colors[i]);
			genButton(lanes[i].target);
		}

		eventHandler = e -> {
			/*
			 * The keyboard detection for the game: when a key is pressed it
			 * calls the checkNote() method for the corresponding lane
			 */
			if (e.getCode() == Control.LANE0.getKey()) {
				checkNote(lanes[0].lane, lanes[0].target);
			}
			if (e.getCode() == Control.LANE1.getKey()) {
				checkNote(lanes[1].lane, lanes[1].target);
			}
			if (e.getCode() == Control.LANE2.getKey()) {
				checkNote(lanes[2].lane, lanes[2].target);
			}
			if (e.getCode() == Control.LANE3.getKey()) {
				checkNote(lanes[3].lane, lanes[3].target);
			}
			if (e.getCode() == Control.LANE4.getKey()) {
				checkNote(lanes[4].lane, lanes[4].target);
			}
			if (e.getCode() == Control.LEGACY_PRINT.getKey()) {
				System.out.println("" + timer.time());
			}
			e.consume();
		};
		Driver.primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, eventHandler);

		buttonBox.setAlignment(Pos.CENTER);		//puts the buttons in the center of the screen
		for (KeyLane lane : lanes) { //places the buttons in the correct row order
			buttonBox.getChildren().add(lane.target);
		}
		buttonBox.setSpacing(10); //sets the space between each button

		place.prefWidthProperty().bind(super.widthProperty());		//Sets the height and with of the scene
		place.prefHeightProperty().bind(super.heightProperty());	//to natch the window
		place.getChildren().addAll(buttonBox);		//adds the buttonBox to the screen
		place.setAlignment(Pos.BOTTOM_CENTER);		//sets the alignment of the pane
		place.setSpacing(10);	

		StackPane root = new StackPane();
		root.getChildren().addAll(place);	//aligns the components within the pane

		super.getChildren().addAll(root);	//puts all of the combonents in the pane to be rendered
	}

	/**
	 * Checks if a note should be sent at the current time, and sends the note if it
	 * needs to be
	 * 
	 * @param sends the queue to check
	 * @param lane  the lane to send the note to
	 */
	public void sendNote(Queue<NoteInfo> sends, ArrayList<Block> lane, Target button) {
		if (sends.peek() != null && timer.time() > sends.peek().getTime()-(1000*(bpm/60000.0))) {
			TranslateTransition anim = new TranslateTransition(Duration.millis(TIME+105));

			lane.add(new Block(button.getColor(), 50, 50, 5));
			int index = lane.size() - 1;
			sends.remove();
			lane.get(index).setCache(true); //added by tbone to try to improve performance
			lane.get(index).setCacheHint(CacheHint.SPEED); //this too
			lane.get(index).heightProperty().bind(super.widthProperty().divide(8));
			lane.get(index).widthProperty().bind(super.widthProperty().divide(8));
			lane.get(index).arcHeightProperty().bind(super.widthProperty().divide(25));
			lane.get(index).arcWidthProperty().bind(super.widthProperty().divide(25));
			lane.get(index).setX(button.getLayoutX());
			lane.get(index).setY(-lane.get(index).getHeight());
			anim.setInterpolator(Interpolator.LINEAR);
			anim.setByY(super.getHeight() + lane.get(index).getHeight() + 75);
			anim.setCycleCount(1);
			anim.setAutoReverse(false);
			anim.setNode(lane.get(lane.size() - 1));
			anim.play();

			anim.setOnFinished(e -> {
				if (super.getChildren().removeAll(anim.getNode())){
					scoreCounter.miss(missMute);
					FillTransition ft = new FillTransition(Duration.millis(500), button.rect);
					ft.setFromValue(Color.RED);
					ft.setToValue(button.getFillColor());
					ft.play();
				}
			});
			super.getChildren().add(lane.get(lane.size() - 1));
		}
	}

	/**
	 * Sets up the given button
	 * 
	 * @param button
	 */
	private void genButton(Target button) {
		button.rect.heightProperty().bind(super.widthProperty().divide(8));
		button.rect.widthProperty().bind(super.widthProperty().divide(8));
		button.rect.arcHeightProperty().bind(super.widthProperty().divide(25));
		button.rect.arcWidthProperty().bind(super.widthProperty().divide(25));
		button.rect.strokeWidthProperty().bind(super.widthProperty().divide(120));
	}

	/**
	 * The background test that is run on every frame of the game
	 */
	AnimationTimer gameLoop = new AnimationTimer() {

		@Override
		public void handle(long arg0) {
			for (KeyLane lane : lanes) {
				sendNote(lane.sends, lane.lane, lane.target);
			}

			if (timer.time() > songLength) {
				Driver.setMenu(new GameOver(level, difficulty, pane, scoreCounter.getScore()));
				cancel();
			}
			if (!songIsPlaying && timer.time() > 0.0) {
				songIsPlaying = true;
				Sound.playSong(song);
			}
		}
	};

	//starts the gameLoop, a periodic backround task runner that runs the methods within it 60 times every second
	public void start() 
	{
		gameLoop.start();
	}

	/**
	 * Stops the gameloop
	 * @throws LineUnavailableException
	 * @throws IOException
	 * @throws UnsupportedAudioFileException
	 */
	public void cancel() {
		Driver.primaryStage.removeEventFilter(KeyEvent.KEY_PRESSED, eventHandler);
		missMute = true;
		Sound.stopSong();
		Sound.playSong(Sound.MENU_SONG);
		Driver.setMenuBackground();
		gameLoop.stop();
	}

	/**
	 * returns the pos in the lane array of the closest note to the goal
	 * 
	 * @param searchLane
	 * @return the position of the note
	 */
	private int getClosestNote(ArrayList<Block> searchLane) {
		int pos = 0;

		for (int i = 0; i < searchLane.size(); i++) {
			if (distanceToGoal(searchLane.get(i)) < distanceToGoal(searchLane.get(pos))) {
				pos = i;
			}
		}
		return pos;
	}

	/**
	 * Returns the distance to the goal of the given note
	 * 
	 * @param note
	 * @return
	 */
	private double distanceToGoal(Block note) {
		return Math.abs((super.getHeight() - note.getTranslateY() + note.getHeight()/2) - lanes[0].target.rect.getLayoutY());
	}

	/**
	 * When the player hits the key, checks the quality of the hit
	 * @param lane the lane checking for a hit
	 * @param button the button checking for a hit
	 * @return 2 for a perfect hit, 1 for a good hit, 0 for a miss, and -1 if there are no notes to hit
	 */
	private int checkNote(ArrayList<Block> lane, Target button) {
		if (lane.size() != 0 && super.isVisible())
		{
			double distance = distanceToGoal(lane.get(getClosestNote(lane)));
			if (lane.size() > 0 && distance < super.getHeight() / 3) {

				FillTransition ft = new FillTransition(Duration.millis(500), button.rect);
				ft.setToValue(button.getFillColor());

				super.getChildren().removeAll(lane.get(getClosestNote(lane)));
				lane.remove(lane.get(getClosestNote(lane)));
				if (distance < super.getHeight() / 12) {
					ft.setFromValue(Color.WHITE);
					ft.play();
					scoreCounter.perfect();
					return 2;
				}
				if (distance < super.getHeight() / 4) {
					ft.setFromValue(Color.CYAN);
					ft.play();
					scoreCounter.good();
					return 1;
				}
				ft.setFromValue(Color.RED);
				ft.play();
				scoreCounter.miss(false);
				return 0;
			}
		}	
		return -1;
	}

}