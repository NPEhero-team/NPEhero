package gui;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class MainMenu extends Pane
{
    public MainMenu()
    {

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(50.0);
        dropShadow.setColor(Color.WHITE);
        dropShadow.setBlurType(BlurType.GAUSSIAN);
        
        Text title = new Text();
        title.setText("NPE Hero");
        title.setFont(new Font(125));
        title.setEffect(dropShadow);
        title.setFill(Color.WHITE);

        Button play = new Button();
        play.setText("Play");
        play.setOnAction(e -> Driver.switchMenu("LevelSelector"));

        Button settings = new Button();
        settings.setText("Settings");
        settings.setOnAction(e -> Driver.switchMenu("Settings"));

        Button leaderboard = new Button();
        leaderboard.setText("Leaderboard");
        leaderboard.setOnAction(e -> Driver.switchMenu("Leaderboard"));

        //Border border = new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));

        VBox buttonBox = new VBox();
        buttonBox.getChildren().addAll(play, settings, leaderboard);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(10);
        //buttonBox.setBorder(border);

        VBox centerBox = new VBox();
        centerBox.setAlignment(Pos.CENTER);
        centerBox.getChildren().addAll(title, buttonBox);
        centerBox.setSpacing(10);
        //centerBox.setBorder(border);

        VBox rootBox = new VBox();
        rootBox.minWidthProperty().bind(super.widthProperty()); 
        rootBox.minHeightProperty().bind(super.heightProperty());
        rootBox.setAlignment(Pos.CENTER);
        rootBox.getChildren().add(centerBox);
        //rootBox.setBorder(border);
        super.getChildren().add(rootBox);
        
    }
}
