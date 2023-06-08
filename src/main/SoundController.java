package main;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;

import gui.Driver;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class SoundController 
{
    public MediaPlayer songMediaPlayer;
    public MediaPlayer sfxMediaPlayer;
    private HashMap<String,MediaPlayer> effects = new HashMap<>();
    private File mainMenuSong = Paths.get("resources/fairyfountain.wav").toFile();
    

    /**
     * creates a new sound controller and starts playing the main menu music
     */
    public SoundController() 
    {
        effects.put("hit", new MediaPlayer(new Media(Paths.get("resources/hit.wav").toUri().toString())));
        effects.put("miss", new MediaPlayer(new Media(Paths.get("resources/miss.wav").toUri().toString())));
        effects.put("forward", new MediaPlayer(new Media(Paths.get("resources/forward.wav").toUri().toString())));
        effects.put("backward", new MediaPlayer(new Media(Paths.get("resources/backward.wav").toUri().toString())));
        effects.forEach((key,value) -> {
            value.volumeProperty().bind(Driver.settingsController.effectsVol);
        });
        playMenuSong();
    }

    /**
     * plays the song that is passed in.
     * @param songFile: the song
     */
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

    /**
     * plays the main menu song
     */
    public void playMenuSong()
    {
        playSong(mainMenuSong);
        songMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        songMediaPlayer.play();
    }

    /**
     * stops the currently playing song
     */
    public void endSong()
    {
        if (songMediaPlayer != null)
        {
            songMediaPlayer.stop();
        }
    }

    /**
     * plays a sound effect
     * for the volume slider to take affect each mediaplayer needs to be pre loaded. 
     * this rewinds and played the proper mediaplayer for the sound
     * @param preset: a string of the name of the sound. possible sounds assigned in the constructor
     */
    public void playSfx(String preset)
    {
        effects.get(preset).seek(new Duration(0));
        effects.get(preset).play();
    }
}