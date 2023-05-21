package gui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Settings extends Pane
{
    public Settings()
    {
        Text t1 = new Text();
        t1.setText("Music Volume");
        t1.setFill(Color.WHITE);

        Slider musicVol = new Slider();
        musicVol.setMax(100);
        musicVol.setMin(0);

        Text t2 = new Text();
        t2.setText("Sound Effects Volume");
        t2.setFill(Color.WHITE);

        Slider sfxVol = new Slider();
        sfxVol.setMax(100);
        sfxVol.setMin(0);

        Button fullscreen = new Button();
        fullscreen.setText("Toggle Fullscreen (F11)");
        fullscreen.getStyleClass().remove("toggle-button");
        fullscreen.getStyleClass().add("button");
        fullscreen.getStyleClass().add("custom-radio-button");
        fullscreen.setOnAction(e -> Driver.primaryStage.setFullScreen(!Driver.primaryStage.isFullScreen()));

        Button devMenu = new Button();
        devMenu.setText("Debug Menu");
        devMenu.setOnAction(e -> new DebugMenu());

        Button exit = new Button();
        exit.setText("Exit");
        exit.setOnAction(e -> Driver.setMenu(new MainMenu()));

        VBox options = new VBox();
        options.setSpacing(10);
        options.setAlignment(Pos.CENTER);
        options.getChildren().addAll(t1,musicVol,t2,sfxVol,fullscreen,devMenu,exit);
        options.maxWidthProperty().bind(super.prefWidthProperty().multiply(0.25)); 
        options.setMinWidth(400);
        options.prefHeightProperty().bind(super.prefHeightProperty());

        HBox rootBox = new HBox();
        rootBox.prefWidthProperty().bind(super.prefWidthProperty()); 
        rootBox.prefHeightProperty().bind(super.prefHeightProperty());
        rootBox.getChildren().add(options);
        rootBox.setAlignment(Pos.CENTER);
        super.getChildren().add(rootBox);
    }
}
