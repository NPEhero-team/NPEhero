package gui;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainMenu extends Scene
{
    private static Pane root = new Pane();
    public MainMenu(Stage primaryStage)
    {
        super(root,800,600);
        primaryStage.setTitle("NPE Hero - Main menu");

        Text title = new Text();
        title.setText("NPE Hero");
        title.setFont(new Font(48));
        //set color

        Button play = new Button();
        play.setText("Play");
        play.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            {
                primaryStage.setScene(new LevelSelector(primaryStage));
            }
        });

        Button settings = new Button();
        settings.setText("Settings");
        settings.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            {
                primaryStage.setScene(new Settings(primaryStage));
            }
        });

        Button leaderboard = new Button();
        leaderboard.setText("Leaderboard");
        leaderboard.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            {
                primaryStage.setScene(new Leaderboard(primaryStage));
            }
        });

        VBox centerMenu = new VBox();
        centerMenu.getChildren().addAll(title, play, settings, leaderboard);
        centerMenu.minWidthProperty().bind(primaryStage.widthProperty()); 
        centerMenu.minHeightProperty().bind(primaryStage.heightProperty());
        centerMenu.setAlignment(Pos.CENTER);

        root.getChildren().add(centerMenu);
    }
}
