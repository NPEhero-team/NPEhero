package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Settings extends Pane
{
    public Settings()
    {
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
                Driver.setBackground("assets/trees.png");
            }
        });

        Button exit = new Button();
        exit.setText("Exit");
        exit.setOnAction(e -> Driver.switchMenu("MainMenu"));

        VBox options = new VBox();
        options.setAlignment(Pos.CENTER);
        options.getChildren().addAll(t1,musicVol,t2,sfxVol,devMenu,exit);
        options.minWidthProperty().bind(super.widthProperty()); 
        options.minHeightProperty().bind(super.heightProperty());
        super.getChildren().add(options);
    }
    
}
