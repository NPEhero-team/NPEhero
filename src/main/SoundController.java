package main;

import java.io.File;
import java.util.HashMap;

import gui.Driver;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundController 
{
    public MediaPlayer songMediaPlayer;
    public MediaPlayer sfxMediaPlayer;
    private HashMap<String,MediaPlayer> effects = new HashMap<>();
    private File mainMenuSong = new File("src/assets/fairyfountain.wav");

    public SoundController() 
    {
        effects.put("hit", new MediaPlayer(new Media(new File("src/assets/hit.wav").toURI().toString())));
        effects.put("miss", new MediaPlayer(new Media(new File("src/assets/miss.wav").toURI().toString())));
        effects.put("forward", new MediaPlayer(new Media(new File("src/assets/forward.wav").toURI().toString())));
        effects.put("backward", new MediaPlayer(new Media(new File("src/assets/backward.wav").toURI().toString())));
        effects.forEach((key,value) -> {
            value.volumeProperty().bind(Driver.settingsController.effectsVol);
        });
        playMenuSong();
    }

    public void playSong(File songFile)
    {
        if (songMediaPlayer != null)
        {
            songMediaPlayer.stop();
        }
        Media song = new Media(songFile.toURI().toString());
        songMediaPlayer = new MediaPlayer(song);
        songMediaPlayer.volumeProperty().bind(Driver.settingsController.musicVol);
        songMediaPlayer.play();
    }

    public void playMenuSong()
    {
        playSong(mainMenuSong);
        songMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        songMediaPlayer.play();
    }

    public void endSong()
    {
        if (songMediaPlayer != null)
        {
            songMediaPlayer.stop();
        }
    }

    public void playSfx(String preset)
    {
        effects.get(preset).stop();
        effects.get(preset).play();
    }
}