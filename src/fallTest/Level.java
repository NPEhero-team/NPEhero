package fallTest;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
 
// will eventually extend pane
public class Level extends Pane {
   
 
    public Level() {

        BorderPane background = new BorderPane();
        Scene game = new Scene(background, 800, 600);
        GridPane blockGrid = new GridPane();
        blockGrid.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        background.setCenter(blockGrid);
        
        blockGrid.maxWidthProperty().bind(game.heightProperty().multiply(0.53));
        blockGrid.maxHeightProperty().bind(game.heightProperty());
        System.out.println(game.heightProperty());
        
        //blockGrid.setMaxHeight(700);
        //blockGrid.setMaxWidth(500);
        //blockGrid.setScaleX(1);
        //blockGrid.setScaleY(1);
        //blockGrid.scaleXProperty().bind(game.heightProperty().divide(1000));
        //blockGrid.scaleYProperty().bind(game.heightProperty().divide(1000));
        //blockGrid.hgapProperty().bind(blockGrid.heightProperty().divide(11));
        //blockGrid.vgapProperty().bind(blockGrid.widthProperty().divide(21));
        blockGrid.setVgap(10);
        blockGrid.setHgap(25);

        int blocksize = 50;
        //AnchorPane b11 = new AnchorPane();
        //b11.getChildren().add(new Block(Color.ORANGE, b11, 10));
        Block b1 = new Block(Color.RED, blocksize, blocksize, 10);
        //b1.heightProperty().bind(blockGrid.heightProperty().divide((blockGrid.getColumnCount()*2)+1));
        //b1.widthProperty().bind(blockGrid.widthProperty().divide((blockGrid.getRowCount()*2)+1));

        Block b2 = new Block(Color.BLUE, blocksize, blocksize, 10);
        //b2.heightProperty().bind(blockGrid.heightProperty().divide(21));
        //b2.widthProperty().bind(blockGrid.widthProperty().divide(11));

        Block b3 = new Block(Color.GREEN, blocksize, blocksize, 10);
        //b3.heightProperty().bind(blockGrid.heightProperty().divide(21));
        //b3.widthProperty().bind(blockGrid.widthProperty().divide(11));

        Block b4 = new Block(Color.YELLOW, blocksize, blocksize, 10);
        //b4.heightProperty().bind(blockGrid.heightProperty().divide(21));
        //b4.widthProperty().bind(blockGrid.widthProperty().divide(11));

        Pane test = new Pane();

        Block b5 = new Block(Color.ORANGE, blocksize, blocksize, 10);
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(200.0);
        dropShadow.setColor(Color.ORANGE);
        dropShadow.setBlurType(BlurType.GAUSSIAN);
        test.getChildren().add(b5);
        test.setEffect(dropShadow);

       

        blockGrid.add(b1, 0, 0);
        blockGrid.add(b2, 1, 0);
        blockGrid.add(b3, 0, 1);
        blockGrid.add(b4, 1, 1);
        blockGrid.add(test, 1, 2);

        Button btn = new Button();
        btn.setText("Test");
        btn.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            {
                System.out.println("test");
            }
        });

        background.setLeft(btn);

       
        
    }
}