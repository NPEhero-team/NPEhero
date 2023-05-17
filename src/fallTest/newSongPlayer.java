package fallTest;

import javafx.event.EventHandler;

import java.awt.Insets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

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

public class newSongPlayer extends Pane
{
    Timer time = new Timer();
    
    private double dLaneX = 0;

    
	public void init() {
	    Queue<NoteInfo> dSends = new LinkedList<NoteInfo>();         //Queue that dictates when to send the notes
	    ArrayList<Block> dLane = new ArrayList<Block>();     //Array list containing all the notes currently on the field
	    
		Rectangle field = new Rectangle(50, 50, new Color(0, 0, 0, 0.7));
		field.heightProperty().bind(this.getScene().getWindow().heightProperty().multiply(0.95));
		field.widthProperty().bind(this.getScene().getWindow().widthProperty().divide(2.7).add(50));
		TButton dButton = new TButton(Color.RED, 50, 50, 5);
		genButton(dButton);
		/*dButton.setOnKeyPressed(e -> { 
			if (e.getCode() == KeyCode.D) {
				System.out.println("D");
			}
		});*/
		
		TButton fButton = new TButton(Color.BLUE, 50, 50, 5);
		genButton(fButton);
		
		TButton sButton = new TButton(Color.GREEN, 50, 50, 5);
		genButton(sButton);
		
		TButton jButton = new TButton(Color.PURPLE, 50, 50, 5);
		genButton(jButton);
		
		TButton kButton = new TButton(Color.YELLOW, 50, 50, 5);
		genButton(kButton);
		

		HBox buttonBox = new HBox();
		buttonBox.setStyle("-fx-padding: 0;" + "-fx-border-style: solid inside;"
		        + "-fx-border-width: 0;" + "-fx-border-insets: 20;"
		        + "-fx-background-color: black;" + "-fx-opacity: 0.67;");
		buttonBox.setAlignment(Pos.CENTER);
	    buttonBox.getChildren().addAll(dButton, fButton, sButton, jButton, kButton);
	    buttonBox.setSpacing(10);
	    
	    VBox polish = new VBox();
	    polish.getChildren().addAll(field);
	    polish.setAlignment(Pos.TOP_CENTER);
	    
	    VBox place = new VBox();
		place.prefWidthProperty().bind(this.getScene().widthProperty());
		place.prefHeightProperty().bind(this.getScene().heightProperty());
	    place.setAlignment(Pos.BOTTOM_CENTER);
	    place.getChildren().addAll(buttonBox);
	    place.setSpacing(10);
	    
	    StackPane root = new StackPane();
	    root.getChildren().addAll(polish, place);
	    
        super.getChildren().add(root);
        
	    sendNote(dLane);
	}
	
	public void sendNote(ArrayList<Block> lane) {
		lane.add(new Block(Color.PINK, 50, 50, 5));
		lane.get(lane.size()-1).heightProperty().bind(this.getScene().getWindow().widthProperty().divide(16));
		lane.get(lane.size()-1).widthProperty().bind(this.getScene().getWindow().widthProperty().divide(16));
		lane.get(lane.size()-1).arcHeightProperty().bind(this.getScene().getWindow().widthProperty().divide(50));
		lane.get(lane.size()-1).arcWidthProperty().bind(this.getScene().getWindow().widthProperty().divide(50));
		super.getChildren().add(lane.get(lane.size()-1));
	}
	
	public void genButton(TButton button) {
		button.heightProperty().bind(this.getScene().getWindow().widthProperty().divide(16));
		button.widthProperty().bind(this.getScene().getWindow().widthProperty().divide(16));
		button.arcHeightProperty().bind(this.getScene().getWindow().widthProperty().divide(50));
		button.arcWidthProperty().bind(this.getScene().getWindow().widthProperty().divide(50));
		button.strokeWidthProperty().bind(this.getScene().getWindow().widthProperty().divide(210));
	}
}
