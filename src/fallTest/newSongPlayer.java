package fallTest;

import javafx.event.EventHandler;
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class newSongPlayer extends Pane
{

	public newSongPlayer() {
		/*TButton dButton = new TButton(Color.RED, 50, 50, 5);
		System.out.println(super.widthProperty());
		dButton.heightProperty().bind(super.widthProperty().divide(1));
		dButton.widthProperty().bind(super.widthProperty().divide(1));
		
		HBox buttonBox = new HBox();
	    buttonBox.getChildren().addAll(dButton);
	    buttonBox.setAlignment(Pos.CENTER);
	    buttonBox.setSpacing(10);
	    
        super.getChildren().add(buttonBox);
	    //buttonBox.setBorder(border);*/
	}
	
	public void init() {
		TButton dButton = new TButton(Color.RED, 50, 50, 5);
		dButton.heightProperty().bind(this.getScene().getWindow().heightProperty().add(this.getScene().getWindow().widthProperty()).divide(20));
		dButton.widthProperty().bind(this.getScene().getWindow().heightProperty().add(this.getScene().getWindow().widthProperty()).divide(20));
		dButton.arcHeightProperty().bind(this.getScene().getWindow().heightProperty().add(this.getScene().getWindow().widthProperty()).divide(100));
		dButton.arcWidthProperty().bind(this.getScene().getWindow().heightProperty().add(this.getScene().getWindow().widthProperty()).divide(100));
		dButton.strokeWidthProperty().bind(this.getScene().getWindow().heightProperty().add(this.getScene().getWindow().widthProperty()).divide(420));
		dButton.setOnKeyPressed(e -> { 
			if (e.getCode() == KeyCode.D) {
				System.out.println("D");
			}
		});
		
		TButton fButton = new TButton(Color.BLUE, 50, 50, 5);
		fButton.heightProperty().bind(this.getScene().getWindow().heightProperty().add(this.getScene().getWindow().widthProperty()).divide(20));
		fButton.widthProperty().bind(this.getScene().getWindow().heightProperty().add(this.getScene().getWindow().widthProperty()).divide(20));
		fButton.arcHeightProperty().bind(this.getScene().getWindow().heightProperty().add(this.getScene().getWindow().widthProperty()).divide(100));
		fButton.arcWidthProperty().bind(this.getScene().getWindow().heightProperty().add(this.getScene().getWindow().widthProperty()).divide(100));
		fButton.strokeWidthProperty().bind(this.getScene().getWindow().heightProperty().add(this.getScene().getWindow().widthProperty()).divide(420));
		fButton.setOnKeyPressed(e -> { 
			if (e.getCode() == KeyCode.F) {
				System.out.println("F");
			}
		});
		
		TButton sButton = new TButton(Color.GREEN, 50, 50, 5);
		sButton.heightProperty().bind(this.getScene().getWindow().heightProperty().add(this.getScene().getWindow().widthProperty()).divide(20));
		sButton.widthProperty().bind(this.getScene().getWindow().heightProperty().add(this.getScene().getWindow().widthProperty()).divide(20));
		sButton.arcHeightProperty().bind(this.getScene().getWindow().heightProperty().add(this.getScene().getWindow().widthProperty()).divide(100));
		sButton.arcWidthProperty().bind(this.getScene().getWindow().heightProperty().add(this.getScene().getWindow().widthProperty()).divide(100));
		sButton.strokeWidthProperty().bind(this.getScene().getWindow().heightProperty().add(this.getScene().getWindow().widthProperty()).divide(420));
		sButton.setOnKeyPressed(e -> { 
			if (e.getCode() == KeyCode.SPACE) {
				System.out.println("SPC");
			}
		});
		
		TButton jButton = new TButton(Color.PURPLE, 50, 50, 5);
		jButton.heightProperty().bind(this.getScene().getWindow().heightProperty().add(this.getScene().getWindow().widthProperty()).divide(20));
		jButton.widthProperty().bind(this.getScene().getWindow().heightProperty().add(this.getScene().getWindow().widthProperty()).divide(20));
		jButton.arcHeightProperty().bind(this.getScene().getWindow().heightProperty().add(this.getScene().getWindow().widthProperty()).divide(100));
		jButton.arcWidthProperty().bind(this.getScene().getWindow().heightProperty().add(this.getScene().getWindow().widthProperty()).divide(100));
		jButton.strokeWidthProperty().bind(this.getScene().getWindow().heightProperty().add(this.getScene().getWindow().widthProperty()).divide(420));
		jButton.setOnKeyPressed(e -> { 
			if (e.getCode() == KeyCode.J) {
				System.out.println("J");
			}
		});
		
		TButton kButton = new TButton(Color.YELLOW, 50, 50, 5);
		kButton.heightProperty().bind(this.getScene().getWindow().heightProperty().add(this.getScene().getWindow().widthProperty()).divide(20));
		kButton.widthProperty().bind(this.getScene().getWindow().heightProperty().add(this.getScene().getWindow().widthProperty()).divide(20));
		kButton.arcHeightProperty().bind(this.getScene().getWindow().heightProperty().add(this.getScene().getWindow().widthProperty()).divide(100));
		kButton.arcWidthProperty().bind(this.getScene().getWindow().heightProperty().add(this.getScene().getWindow().widthProperty()).divide(100));
		kButton.strokeWidthProperty().bind(this.getScene().getWindow().heightProperty().add(this.getScene().getWindow().widthProperty()).divide(420));
		kButton.setOnKeyPressed(e -> { 
			if (e.getCode() == KeyCode.K) {
				System.out.println("K");
			}
		});
		
		
		HBox buttonBox = new HBox();
		buttonBox.setAlignment(Pos.CENTER_LEFT);
	    buttonBox.getChildren().addAll(dButton, fButton, sButton, jButton, kButton);
	    buttonBox.setSpacing(10);
	    
	    VBox root = new VBox();
	    root.setAlignment(Pos.CENTER);
	    root.getChildren().addAll(buttonBox);
	    root.setSpacing(10);
	    
	    
        super.getChildren().add(root);
	}
}
