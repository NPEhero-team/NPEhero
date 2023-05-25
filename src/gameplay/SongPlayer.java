package gameplay;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.animation.*;
import javafx.util.*;
import main.Difficulty;
import main.ScoreController;

public class SongPlayer extends Pane {
	Timer timer = new Timer(60);
	final int TIME = 1500; // delay for notes falling down the screen

	Score scoreCounter = new Score();

	Rectangle goalPerfect = new Rectangle();
	HBox buttonBox = new HBox();
	VBox polish = new VBox();
	VBox place = new VBox();

	TButton dButton = new TButton(Color.RED, 50, 50, 5);
	Queue<NoteInfo> dSends = new LinkedList<NoteInfo>(); // Queue that dictates when to send the notes
	ArrayList<Block> dLane = new ArrayList<Block>(); // Array list containing all the notes currently on the field

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
	 */
	public void loadSong() {
		dSends.add(new NoteInfo(4.000));
		dSends.add(new NoteInfo(4.333));
		dSends.add(new NoteInfo(4.666));
		fSends.add(new NoteInfo(5.000));
		kSends.add(new NoteInfo(5.500));
		spaceSends.add(new NoteInfo(6.000));
		jSends.add(new NoteInfo(6.000));
		jSends.add(new NoteInfo(6.250));
		dSends.add(new NoteInfo(6.500));
		jSends.add(new NoteInfo(6.750));
		spaceSends.add(new NoteInfo(7.000));
		fSends.add(new NoteInfo(7.500));
		jSends.add(new NoteInfo(7.750));
		spaceSends.add(new NoteInfo(8.000));
		fSends.add(new NoteInfo(8.500));
		jSends.add(new NoteInfo(8.500));
		dSends.add(new NoteInfo(9.000));
		spaceSends.add(new NoteInfo(9.000));
		kSends.add(new NoteInfo(9.000));
		spaceSends.add(new NoteInfo(9.500));

		kSends.add(new NoteInfo(10.000));
		dSends.add(new NoteInfo(10.000));
		kSends.add(new NoteInfo(10.333));
		fSends.add(new NoteInfo(10.333));
		kSends.add(new NoteInfo(10.666));
		spaceSends.add(new NoteInfo(10.666));
		dSends.add(new NoteInfo(11.000));
		spaceSends.add(new NoteInfo(11.000));
		dSends.add(new NoteInfo(11.333));

		jSends.add(new NoteInfo(11.333));
		dSends.add(new NoteInfo(11.666));
		kSends.add(new NoteInfo(11.666));
		spaceSends.add(new NoteInfo(12.000));
	}

	public SongPlayer(main.Level lvl, Difficulty d, Pane p, ScoreController cntrl) {
		loadSong();

		Rectangle field = new Rectangle(50, 50, new Color(0, 0, 0, 0.7));
		field.heightProperty().bind(super.heightProperty());
		field.widthProperty().bind(super.widthProperty());

		goalPerfect.heightProperty().bind(super.heightProperty().divide(32));
		goalPerfect.heightProperty().bind(super.widthProperty());

		genButton(dButton);
		genButton(fButton);
		genButton(sButton);
		genButton(jButton);
		genButton(kButton);
		
		gui.Driver.primaryStage.getScene().setOnKeyPressed(e -> {
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
			System.out.println("Score: " + scoreCounter.getScore() + "\nCombo: " + scoreCounter.getCombo() + "\n");
		});

		// buttonBox.setStyle("-fx-padding: 0;" + "-fx-border-style: solid inside;"
		// 		+ "-fx-border-width: 0;" + "-fx-border-insets: 20;"
		// 		+ "-fx-background-color: black;" + "-fx-opacity: 0.67;");
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.getChildren().addAll(dButton, fButton, sButton, jButton, kButton);
		buttonBox.setSpacing(10);

		polish.getChildren().addAll(field);
		polish.setAlignment(Pos.BASELINE_CENTER);

		place.prefWidthProperty().bind(super.widthProperty());
		place.prefHeightProperty().bind(super.heightProperty());
		place.getChildren().addAll(buttonBox);
		place.setAlignment(Pos.BOTTOM_CENTER);
		place.setSpacing(10);

		StackPane root = new StackPane();
		root.getChildren().addAll(place);

		goalPerfect.setY(dButton.getY());
		super.getChildren().addAll(root, goalPerfect);

		gameLoop.start();
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
	public void sendNote(Queue<NoteInfo> sends, ArrayList<Block> lane, double pos, Color c) {
		if (sends.peek() != null && timer.time() > sends.peek().getTime()) {
			TranslateTransition anim = new TranslateTransition(Duration.millis(TIME));

			lane.add(new Block(c, 50, 50, 5));
			int index = lane.size() - 1;
			sends.remove();
			lane.get(index).heightProperty().bind(super.widthProperty().divide(8));
			lane.get(index).widthProperty().bind(super.widthProperty().divide(8));
			lane.get(index).arcHeightProperty().bind(super.widthProperty().divide(25));
			lane.get(index).arcWidthProperty().bind(super.widthProperty().divide(25));
			lane.get(index).setX(pos);
			lane.get(index).setY(-lane.get(index).getHeight());
			anim.setByY(super.getHeight() + lane.get(index).getHeight());
			anim.setCycleCount(1);
			anim.setAutoReverse(false);
			anim.setNode(lane.get(lane.size() - 1));
			anim.play();

			anim.setOnFinished(e -> {
				if (super.getChildren().removeAll(anim.getNode())){
					scoreCounter.miss();
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
	public void genButton(TButton button) {
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
			sendNote(dSends, dLane, dButton.getLayoutX(), Color.RED);
			sendNote(fSends, fLane, fButton.getLayoutX(), Color.BLUE);
			sendNote(spaceSends, spaceLane, sButton.getLayoutX(), Color.GREEN);
			sendNote(jSends, jLane, jButton.getLayoutX(), Color.PURPLE);
			sendNote(kSends, kLane, kButton.getLayoutX(), Color.YELLOW);
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
			ft.setToValue(button.getColor());

			super.getChildren().removeAll(lane.get(getClosestNote(lane)));
			lane.remove(lane.get(getClosestNote(lane)));
			if (distance < super.getHeight() / 16) {
				ft.setFromValue(Color.WHITE);
				ft.play();
				scoreCounter.combo();
				scoreCounter.perfect();
				return 2;
			}
			if (distance < super.getHeight() / 5) {
				ft.setFromValue(Color.CYAN);
				ft.play();
				scoreCounter.combo();
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