package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Settings extends Scene
{
    private static Pane primaryPane = new Pane();

    public Settings(Stage primaryStage)
    {
        super(primaryPane, 800, 600);
        primaryStage.setTitle("NPE Hero - Main menu");
        Scene root = super.getRoot().getScene();

        Text t1 = new Text();
        t1.setText("Music Volume");

        Slider musicVol = new Slider();
        musicVol.setMax(100);
        musicVol.setMin(0);

        Text t2 = new Text();
        t2.setText("Sound Effects Volume");

        Slider sfxVol = new Slider();
        sfxVol.setMax(100);
        sfxVol.setMin(0);

        Button devMenu = new Button();
        devMenu.setText("Debug Menu");
        devMenu.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            {
                System.out.println("not yet implimented");
            }
        });

        Button exit = new Button();
        exit.setText("Exit");
        exit.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            {
                primaryStage.setScene(root);
            }
        });

        VBox options = new VBox();
        options.setAlignment(Pos.CENTER);
        options.getChildren().addAll(t1,musicVol,t2,sfxVol,devMenu,exit);
        options.minWidthProperty().bind(primaryStage.widthProperty()); 
        options.minHeightProperty().bind(primaryStage.heightProperty());
        primaryPane.getChildren().add(options);
    }
    
}
