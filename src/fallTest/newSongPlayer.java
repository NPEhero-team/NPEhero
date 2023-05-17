package fallTest;

import javafx.event.EventHandler;

import java.awt.Insets;

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
	public void init() {
		Rectangle field = new Rectangle(50, 50, new Color(0, 0, 0, 0.7));
		field.heightProperty().bind(this.getScene().getWindow().heightProperty().multiply(0.95));
		field.widthProperty().bind(this.getScene().getWindow().widthProperty().divide(2.).add(50));
		
		TButton dButton = new TButton(Color.RED, 50, 50, 5);
		dButton.heightProperty().bind(this.getScene().getWindow().widthProperty().divide(12));
		dButton.widthProperty().bind(this.getScene().getWindow().widthProperty().divide(12));
		dButton.arcHeightProperty().bind(this.getScene().getWindow().widthProperty().divide(50));
		dButton.arcWidthProperty().bind(this.getScene().getWindow().widthProperty().divide(50));
		dButton.strokeWidthProperty().bind(this.getScene().getWindow().widthProperty().divide(210));
		//Text dButtonText = new Text("D");
		dButton.setOnKeyPressed(e -> { 
			if (e.getCode() == KeyCode.D) {
				System.out.println("D");
			}
		});
		
		TButton fButton = new TButton(Color.BLUE, 50, 50, 5);
		fButton.heightProperty().bind(this.getScene().getWindow().widthProperty().divide(12));
		fButton.widthProperty().bind(this.getScene().getWindow().widthProperty().divide(12));
		fButton.arcHeightProperty().bind(this.getScene().getWindow().widthProperty().divide(50));
		fButton.arcWidthProperty().bind(this.getScene().getWindow().widthProperty().divide(50));
		fButton.strokeWidthProperty().bind(this.getScene().getWindow().widthProperty().divide(210));
		fButton.setOnKeyPressed(e -> { 
			if (e.getCode() == KeyCode.F) {
				System.out.println("F");
			}
		});
		
		TButton sButton = new TButton(Color.GREEN, 50, 50, 5);
		sButton.heightProperty().bind(this.getScene().getWindow().widthProperty().divide(12));
		sButton.widthProperty().bind(this.getScene().getWindow().widthProperty().divide(12));
		sButton.arcHeightProperty().bind(this.getScene().getWindow().widthProperty().divide(50));
		sButton.arcWidthProperty().bind(this.getScene().getWindow().widthProperty().divide(50));
		sButton.strokeWidthProperty().bind(this.getScene().getWindow().widthProperty().divide(210));
		sButton.setOnKeyPressed(e -> { 
			if (e.getCode() == KeyCode.SPACE) {
				System.out.println("SPC");
			}
		});
		
		TButton jButton = new TButton(Color.PURPLE, 50, 50, 5);
		jButton.heightProperty().bind(this.getScene().getWindow().widthProperty().divide(12));
		jButton.widthProperty().bind(this.getScene().getWindow().widthProperty().divide(12));
		jButton.arcHeightProperty().bind(this.getScene().getWindow().widthProperty().divide(50));
		jButton.arcWidthProperty().bind(this.getScene().getWindow().widthProperty().divide(50));
		jButton.strokeWidthProperty().bind(this.getScene().getWindow().widthProperty().divide(210));
		jButton.setOnKeyPressed(e -> { 
			if (e.getCode() == KeyCode.J) {
				System.out.println("J");
			}
		});
		
		TButton kButton = new TButton(Color.YELLOW, 50, 50, 5);
		kButton.heightProperty().bind(this.getScene().getWindow().widthProperty().divide(12));
		kButton.widthProperty().bind(this.getScene().getWindow().widthProperty().divide(12));
		kButton.arcHeightProperty().bind(this.getScene().getWindow().widthProperty().divide(50));
		kButton.arcWidthProperty().bind(this.getScene().getWindow().widthProperty().divide(50));
		kButton.strokeWidthProperty().bind(this.getScene().getWindow().widthProperty().divide(210));
		kButton.setOnKeyPressed(e -> { 
			if (e.getCode() == KeyCode.K) {
				System.out.println("K");
			}
		});
		
		//StackPane dButtonComplete = new StackPane();
		//dButtonComplete.getChildren().addAll(dButtonText, dButton);
		
		HBox buttonBox = new HBox();
		
		buttonBox.setStyle("-fx-padding: 0;" + "-fx-border-style: solid inside;"
		        + "-fx-border-width: 0;" + "-fx-border-insets: 20;"
		        + "-fx-border-radius: 0;" + "-fx-border-color: black;"
		        + "-fx-background-color: black;" + "-fx-opacity: 0.67;");
		buttonBox.setAlignment(Pos.CENTER);
	    buttonBox.getChildren().addAll(dButton, fButton, sButton, jButton, kButton);
	    buttonBox.setSpacing(10);
	    
	    VBox polish = new VBox();
	    polish.prefHeightProperty().bind(this.getScene().heightProperty());
	    polish.getChildren().addAll(field);
	    polish.setAlignment(Pos.TOP_CENTER);
	    
	    VBox place = new VBox();
		place.prefWidthProperty().bind(this.getScene().widthProperty());
		place.prefHeightProperty().bind(this.getScene().heightProperty());
	    //root.setStyle("-fx-padding: 0;" + "-fx-border-style: solid inside;"
		//        + "-fx-border-width: 2;" + "-fx-border-insets: 0;"
		//        + "-fx-border-radius: 2;" + "-fx-border-color: red;");
	    place.setAlignment(Pos.BOTTOM_CENTER);
	    place.getChildren().addAll(buttonBox);
	    place.setSpacing(10);
	    
	    StackPane root = new StackPane();
	    root.getChildren().addAll(polish, place);
	    
        super.getChildren().add(root);
        
	}
}
