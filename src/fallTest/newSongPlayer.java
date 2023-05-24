package fallTest;

import javafx.event.EventHandler;

import java.awt.Insets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javafx.beans.property.DoubleProperty;
import javafx.event.*;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.animation.*;
import javafx.animation.KeyFrame;
import javafx.util.*;

public class newSongPlayer extends Pane
{
	Timer timer = new Timer();
    final int TIME = 1500;		//delay for notes falling down the screen

	Rectangle goalPerfect = new Rectangle();
	HBox buttonBox = new HBox();
	VBox polish = new VBox();
	VBox place = new VBox();

	TButton dButton = new TButton(Color.RED, 50, 50, 5);
	Queue<NoteInfo> dSends = new LinkedList<NoteInfo>();         //Queue that dictates when to send the notes
	ArrayList<Block> dLane = new ArrayList<Block>();     //Array list containing all the notes currently on the field

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
        dSends.add(new NoteInfo(4000));
        dSends.add(new NoteInfo(4333));
        dSends.add(new NoteInfo(4666));
        fSends.add(new NoteInfo(5000));
        kSends.add(new NoteInfo(5500)); 
        spaceSends.add(new NoteInfo(6000));
        jSends.add(new NoteInfo(6000));
        jSends.add(new NoteInfo(6250));
        dSends.add(new NoteInfo(6500));
        jSends.add(new NoteInfo(6750));
        spaceSends.add(new NoteInfo(7000));
        fSends.add(new NoteInfo(7500));
        jSends.add(new NoteInfo(7750));
        spaceSends.add(new NoteInfo(8000));
        fSends.add(new NoteInfo(8500));
        jSends.add(new NoteInfo(8500));
        dSends.add(new NoteInfo(9000));
        spaceSends.add(new NoteInfo(9000));
        kSends.add(new NoteInfo(9000));
        spaceSends.add(new NoteInfo(9500));
        
        kSends.add(new NoteInfo(10000));
        dSends.add(new NoteInfo(10000));
        kSends.add(new NoteInfo(10333));
        fSends.add(new NoteInfo(10333));
        kSends.add(new NoteInfo(10666));
        spaceSends.add(new NoteInfo(10666));
        dSends.add(new NoteInfo(11000));
        spaceSends.add(new NoteInfo(11000));
        dSends.add(new NoteInfo(11333));
        jSends.add(new NoteInfo(11333));
        dSends.add(new NoteInfo(11666));
        kSends.add(new NoteInfo(11666));
        spaceSends.add(new NoteInfo(12000));
    }
    
	public void init() {	    
		loadSong();
		
		Rectangle field = new Rectangle(50, 50, new Color(0, 0, 0, 0.7));
		field.heightProperty().bind(this.getScene().getWindow().heightProperty());
		field.widthProperty().bind(this.getScene().getWindow().widthProperty().divide(2.7).add(50));

		goalPerfect.heightProperty().bind(this.getScene().getWindow().heightProperty().divide(32));
		goalPerfect.heightProperty().bind(this.getScene().getWindow().widthProperty());

		genButton(dButton);
		genButton(fButton);
		genButton(sButton);
		genButton(jButton);
		genButton(kButton);
		
		this.getScene().setOnKeyPressed(e -> { 
			if (e.getCode() == KeyCode.D) {
				System.out.println("D");
				if (dLane.size() > 0 && distanceToGoal(dLane.get(getClosestNote(dLane))) < ()) {
					super.getChildren().removeAll(dLane.get(getClosestNote(dLane)));
					dLane.remove(dLane.get(getClosestNote(dLane)));
					System.out.println("Hit");
				}
			}
			if (e.getCode() == KeyCode.F) {
				System.out.println("F");
				if (fLane.size() > 0 && fLane.get(getClosestNote(fLane)).intersects(buttonBox.getBoundsInLocal())) {
					super.getChildren().removeAll(fLane.get(getClosestNote(fLane)));
					fLane.remove(fLane.get(getClosestNote(fLane)));
					System.out.println("Hit");
				}
			}
			if (e.getCode() == KeyCode.SPACE) {
				System.out.println("SPC");
				if (spaceLane.size() > 0 && spaceLane.get(getClosestNote(spaceLane)).intersects(buttonBox.getBoundsInLocal())) {
					super.getChildren().removeAll(spaceLane.get(getClosestNote(spaceLane)));
					spaceLane.remove(spaceLane.get(getClosestNote(spaceLane)));
					System.out.println("Hit");
				}
			}
			if (e.getCode() == KeyCode.J) {
				System.out.println("J");
				if (jLane.size() > 0 && jLane.get(getClosestNote(jLane)).intersects(buttonBox.getBoundsInLocal())) {
					super.getChildren().removeAll(jLane.get(getClosestNote(jLane)));
					jLane.remove(jLane.get(getClosestNote(jLane)));
					System.out.println("Hit");
				}
			}
			if (e.getCode() == KeyCode.K) {
				System.out.println("K");
				if (kLane.size() > 0 && kLane.get(getClosestNote(kLane)).intersects(buttonBox.getBoundsInLocal())) {
					super.getChildren().removeAll(kLane.get(getClosestNote(kLane)));
					kLane.remove(kLane.get(getClosestNote(kLane)));
					System.out.println("Hit");
				}
			}
		});

		buttonBox.setStyle("-fx-padding: 0;" + "-fx-border-style: solid inside;"
		        + "-fx-border-width: 0;" + "-fx-border-insets: 20;"
		        + "-fx-background-color: black;" + "-fx-opacity: 0.67;");
		buttonBox.setAlignment(Pos.CENTER);
	    buttonBox.getChildren().addAll(dButton, fButton, sButton, jButton, kButton);
	    buttonBox.setSpacing(10);
	    
	    polish.getChildren().addAll(field);
	    polish.setAlignment(Pos.BASELINE_CENTER);
	    
		place.prefWidthProperty().bind(this.getScene().widthProperty());
		place.prefHeightProperty().bind(this.getScene().heightProperty());
	    place.getChildren().addAll(buttonBox);
		place.setAlignment(Pos.BOTTOM_CENTER);
	    place.setSpacing(10);
	    
	    StackPane root = new StackPane();
	    root.getChildren().addAll(polish, place);

		goalPerfect.setY(dButton.getY());
        super.getChildren().addAll(root, goalPerfect);

		gameLoop.start();
	}
	
	public void sendNote(Queue<NoteInfo> sends, ArrayList<Block> lane, double pos, Color c) {
		if (sends.peek() != null && timer.time() > sends.peek().getTime()) {
			TranslateTransition anim = new TranslateTransition(Duration.millis(TIME));

			lane.add(new Block(c, 50, 50, 5));
			int index = lane.size()-1;
			sends.remove();
			lane.get(lane.size()-1).heightProperty().bind(this.getScene().getWindow().widthProperty().divide(16));
			lane.get(lane.size()-1).widthProperty().bind(this.getScene().getWindow().widthProperty().divide(16));
			lane.get(lane.size()-1).arcHeightProperty().bind(this.getScene().getWindow().widthProperty().divide(50));
			lane.get(lane.size()-1).arcWidthProperty().bind(this.getScene().getWindow().widthProperty().divide(50));
			lane.get(lane.size()-1).setX(pos);
			lane.get(index).setY(-lane.get(index).getHeight());
			anim.setByY(this.getScene().getHeight() + lane.get(index).getHeight());
			anim.setCycleCount(1);
			anim.setAutoReverse(false);
			anim.setNode(lane.get(lane.size()-1));
			anim.play();

			anim.setOnFinished(e -> {
				super.getChildren().removeAll(anim.getNode());
			});
			super.getChildren().add(lane.get(lane.size()-1));
		}
	}
	

	public void genButton(TButton button) {
		button.heightProperty().bind(this.getScene().getWindow().widthProperty().divide(16));
		button.widthProperty().bind(this.getScene().getWindow().widthProperty().divide(16));
		button.arcHeightProperty().bind(this.getScene().getWindow().widthProperty().divide(50));
		button.arcWidthProperty().bind(this.getScene().getWindow().widthProperty().divide(50));
		button.strokeWidthProperty().bind(this.getScene().getWindow().widthProperty().divide(210));
	}

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

	private int getClosestNote(ArrayList<Block> searchLane) {
        int pos = 0;
        
        for (int i=0; i<searchLane.size(); i++) {
            if (distanceToGoal(searchLane.get(i)) < distanceToGoal(searchLane.get(pos))) {
                pos = i;
            }
        }
        return pos;
    }

	private double distanceToGoal(Block note) {
		return Math.abs(note.getY()-dButton.getY());
	}

	private void checkForNote(ArrayList<Block> lane) {
		if (lane.size() > 0 && distanceToGoal(lane.get(getClosestNote(lane))) < 50) {
			super.getChildren().removeAll(lane.get(getClosestNote(lane)));
			lane.remove(lane.get(getClosestNote(lane)));
			System.out.println("Hit");
		}
	}
}