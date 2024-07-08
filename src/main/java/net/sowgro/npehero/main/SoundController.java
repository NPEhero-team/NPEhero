package net.sowgro.npehero.main;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import net.sowgro.npehero.Driver;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class SoundController 
{
    public MediaPlayer songMediaPlayer;
    public MediaPlayer sfxMediaPlayer;
    private final HashMap<String,MediaPlayer> effects = new HashMap<>();
    private final File mainMenuSong;

    {
        try {
            mainMenuSong = new File(Driver.getResource("fairyfountain.wav").toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * creates a new sound controller and starts playing the main menu music
     */
    public SoundController() 
    {
        effects.put("hit", new MediaPlayer(new Media(Driver.getResource("hit.wav").toString())));
        effects.put("miss", new MediaPlayer(new Media(Driver.getResource("miss.wav").toString())));
        effects.put("forward", new MediaPlayer(new Media(Driver.getResource("forward.wav").toString())));
        effects.put("backward", new MediaPlayer(new Media(Driver.getResource("backward.wav").toString())));
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
        if (!mainMenuSong.exists()) {
            System.out.println("NOT EXIST " + mainMenuSong.getAbsolutePath());
            return;
        }
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
     * for the volume slider to take effect each mediaplayer needs to be preloaded.
     * this rewinds and played the proper mediaplayer for the sound
     * @param preset: a string of the name of the sound. possible sounds assigned in the constructor
     */
    public void playSfx(String preset)
    {
        effects.get(preset).seek(new Duration(0));
        effects.get(preset).play();
    }
}