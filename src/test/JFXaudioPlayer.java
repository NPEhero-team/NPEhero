package test;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class JFXaudioPlayer extends Application{
    
    public static void main(String[] args) 
    {
        launch(args);
    }
        
    @Override
    public void start(Stage primaryStage) 
    {
        // primaryStage.show();
        String musicFile = "EXAMPLE.mp3";     // For example
        Media sound = new Media(new File(musicFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }
}