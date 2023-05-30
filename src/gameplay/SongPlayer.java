package gameplay;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import gui.GameOver;
import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.animation.*;
import javafx.util.*;
import main.Difficulty;
import main.ScoreController;
import sound.AudioFilePlayer;



//hi aidan here are some objects you can use
// cntrl.setScore(0) - pass in int
// cntrl.getScore() - returns int
// d.bpm - int, defined in difficulty metadata
// lvl.colors - array of colors (size 5) for the block colors
// d.notes - File, notes.txt in the difficulty folder

// gui.Driver.setMenu(new GameOver(lvl, d, p, cntrl.getScore()));

public class SongPlayer extends Pane {
	private int bpm;		//initializes the bpm of the song, to be read in from a metadata file later
	Timer timer;			//the timer that determines when notes will fall, counted in terms of the song's bpm
	final int TIME = 1500;  //delay for notes falling down the screen

	main.ScoreController scoreCounter = new ScoreController();	//used to keep track of the user's score

	HBox buttonBox = new HBox();	//used to align the buttons horizontally
	VBox place = new VBox();		//used to place the buttons within the frame

	TButton dButton = new TButton(Color.RED, 50, 50, 5);	//Initializes the button, each parameter is a placeholder that is changed later
	Queue<NoteInfo> dSends = new LinkedList<NoteInfo>(); 		//Queue that dictates when to send the notes
	ArrayList<Block> dLane = new ArrayList<Block>(); 			//Array list containing all the notes currently on the field for that lane
																//process is repeated for the following four buttons
	TButton fButton = new TButton(Color.BLUE, 50, 50, 5);
	Queue<NoteInfo> fSends = new LinkedList<NoteInfo>();
	ArrayList<Block> fLane = new ArrayList<Block>();

	TButton sButton = new TButton(Color.GREEN, 50, 50, 5);
	Queue<NoteInfo> spaceSends = new LinkedList<NoteInfo>();
	ArrayList<Block> spaceLane = new ArrayList<Block>();

	TButton jButton = new TButton(Color.PURPLE, 50, 50, 5);
	Queue<NoteInfo> jSends = new LinkedList<NoteInfo>();
	ArrayList<Block> jLane = new ArrayList<Block>();

	TButton kButton = new TButton(Color.YELLOW, 50, 50, 5);
	Queue<NoteInfo> kSends = new LinkedList<NoteInfo>();
	ArrayList<Block> kLane = new ArrayList<Block>();

	/**
	 * Establishes what the chart for the song is going to look like
	 * @throws FileNotFoundException
	 */
	public void loadSong(File notes) throws FileNotFoundException {
		Scanner scan = new Scanner(new File(notes.getPath()));	//file reader for reading in the notes from a notes.txt file
		try{
			while (scan.hasNext()) {
				String input = scan.next();
				if (input.charAt(0) == 'd') {
					dSends.add(new NoteInfo(Double.parseDouble(input.substring(1))));
				}
				else if (input.charAt(0) == 'f') {
					fSends.add(new NoteInfo(Double.parseDouble(input.substring(1))));
				}
				else if (input.charAt(0) == 's') {
					spaceSends.add(new NoteInfo(Double.parseDouble(input.substring(1))));
				}
				else if (input.charAt(0) == 'j') {
					jSends.add(new NoteInfo(Double.parseDouble(input.substring(1))));
				}
				else if (input.charAt(0) == 'k') {
					kSends.add(new NoteInfo(Double.parseDouble(input.substring(1))));
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	public SongPlayer(main.Level lvl, Difficulty d, Pane p, ScoreController cntrl) {
		bpm = d.bpm;					//Reads the song's bpm from a metadata file
		timer = new Timer(120);	//Sets the timer's bpm to that of the song
		scoreCounter = cntrl;			//Uses the song's designated scoreCounter

		try {
			loadSong(d.notes);			//Calls the file loading from the song's notes.txt file
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		dButton.setColor(lvl.colors[0]);	//sets the color of the five buttons to be
		fButton.setColor(lvl.colors[1]);	//the colors outlined in the songs metadata
		sButton.setColor(lvl.colors[2]);	//file
		jButton.setColor(lvl.colors[3]);
		kButton.setColor(lvl.colors[4]);
		genButton(dButton);		//binds the size of each button to the screen, so that
		genButton(fButton);		//they are dynamically resizeable
		genButton(sButton);
		genButton(jButton);
		genButton(kButton);
		
		gui.Driver.primaryStage.getScene().setOnKeyPressed(e -> {
			/**
			 * The keyboard detection for the game: when a key is pressed it
			 * calls the checkNote() method for the corresponding lane
			 */
			if (e.getCode() == KeyCode.D) {
				checkNote(dLane, dButton);
			}
			if (e.getCode() == KeyCode.F) {
				checkNote(fLane, fButton);
			}
			if (e.getCode() == KeyCode.SPACE) {
				checkNote(spaceLane, sButton);
			}
			if (e.getCode() == KeyCode.J) {
				checkNote(jLane, jButton);
			}
			if (e.getCode() == KeyCode.K) {
				checkNote(kLane, kButton);
			}
			//prints the user's current score and combo, for debugging purposes
			System.out.println("Score: " + scoreCounter.getScore() + "\nCombo: " + scoreCounter.getCombo() + "\n");
		});

		buttonBox.setAlignment(Pos.CENTER);		//puts the buttons in the center of the screen
		buttonBox.getChildren().addAll(dButton, fButton, sButton, jButton, kButton);	//places the buttons in the correct row order
		buttonBox.setSpacing(10); //sets the space between each button

		place.prefWidthProperty().bind(super.widthProperty());		//Sets the height and with of the scene
		place.prefHeightProperty().bind(super.heightProperty());	//to natch the window
		place.getChildren().addAll(buttonBox);		//adds the buttonBox to the screen
		place.setAlignment(Pos.BOTTOM_CENTER);		//sets the alignment of the pane
		place.setSpacing(10);	

		StackPane root = new StackPane();
		root.getChildren().addAll(place);	//aligns the components within the pane

		super.getChildren().addAll(root);	//puts all of the combonents in the pane to be rendered

		sound.AudioFilePlayer music = new AudioFilePlayer("src/assets/TestSync120bpm.wav");
		music.play();
		gameLoop.start();		//starts the gameLoop, a periodic backround task runner that runs the methods within it 60 times every second
	}

	/**
	 * Checks if a note should be sent at the current time, and sends the note if it
	 * needs to be
	 * 
	 * @param sends the queue to check
	 * @param lane  the lane to send the note to
	 * @param pos   the x pos of the note to be sent
	 * @param c     the color of the sent note
	 */
	public void sendNote(Queue<NoteInfo> sends, ArrayList<Block> lane, TButton button) {
		if (sends.peek() != null && timer.time() > sends.peek().getTime()-(TIME*bpm/60000)) {
			TranslateTransition anim = new TranslateTransition(Duration.millis(TIME));

			lane.add(new Block(button.getColor(), 50, 50, 5));
			int index = lane.size() - 1;
			sends.remove();
			lane.get(index).heightProperty().bind(super.widthProperty().divide(8));
			lane.get(index).widthProperty().bind(super.widthProperty().divide(8));
			lane.get(index).arcHeightProperty().bind(super.widthProperty().divide(25));
			lane.get(index).arcWidthProperty().bind(super.widthProperty().divide(25));
			lane.get(index).setX(button.getLayoutX());
			lane.get(index).setY(-lane.get(index).getHeight());
			anim.setByY(super.getHeight() + lane.get(index).getHeight());
			anim.setCycleCount(1);
			anim.setAutoReverse(false);
			anim.setNode(lane.get(lane.size() - 1));
			anim.play();

			anim.setOnFinished(e -> {
				if (super.getChildren().removeAll(anim.getNode())){
					scoreCounter.miss();
					FillTransition ft = new FillTransition(Duration.millis(500), button);
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
	private void genButton(TButton button) {
		button.heightProperty().bind(super.widthProperty().divide(8));
		button.widthProperty().bind(super.widthProperty().divide(8));
		button.arcHeightProperty().bind(super.widthProperty().divide(25));
		button.arcWidthProperty().bind(super.widthProperty().divide(25));
		button.setStrokeWidth(3);
	}

	/**
	 * The background test that is run on every frame of the game
	 */
	AnimationTimer gameLoop = new AnimationTimer() {

		@Override
		public void handle(long arg0) {
			sendNote(dSends, dLane, dButton);
			sendNote(fSends, fLane, fButton);
			sendNote(spaceSends, spaceLane, sButton);
			sendNote(jSends, jLane, jButton);
			sendNote(kSends, kLane, kButton);
		}
	};

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
		return Math.abs((super.getHeight() - note.getTranslateY()) - dButton.getY());
	}

	/**
	 * When the player hits the key, checks the quality of the hit
	 * @param lane the lane checking for a hit
	 * @param button the button checking for a hit
	 * @return 2 for a perfect hit, 1 for a good hit, 0 for a miss, and -1 if there are no notes to hit
	 */
	private int checkNote(ArrayList<Block> lane, TButton button) {
		double distance = distanceToGoal(lane.get(getClosestNote(lane)));
		if (lane.size() > 0 && distance < super.getHeight() / 3) {

			FillTransition ft = new FillTransition(Duration.millis(500), button);
			ft.setToValue(button.getFillColor());

			super.getChildren().removeAll(lane.get(getClosestNote(lane)));
			lane.remove(lane.get(getClosestNote(lane)));
			if (distance < super.getHeight() / 16) {
				ft.setFromValue(Color.WHITE);
				ft.play();
				scoreCounter.perfect();
				return 2;
			}
			if (distance < super.getHeight() / 5) {
				ft.setFromValue(Color.CYAN);
				ft.play();
				scoreCounter.good();
				return 1;
			}
			ft.setFromValue(Color.RED);
			ft.play();
			scoreCounter.miss();
			return 0;
		}
		return -1;
	}

}