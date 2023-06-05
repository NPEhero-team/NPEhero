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
    private HashMap<String,File> presets = new HashMap<>();
    private File mainMenuSong = new File("src/assets/MenuMusicPlaceholder.wav");

    public SoundController() 
    {
        presets.put("forward", new File("src/assets/MenuForward.wav"));
        presets.put("backward", new File("src/assets/MenuBackward.wav"));
        presets.put("hit", new File("src/assets/Hitsound.wav"));
        presets.put("miss", new File("src/assets/Miss.wav"));
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

    private void playMenuSong()
    {
        playSong(mainMenuSong);
        songMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        songMediaPlayer.play();
    }

    public void endSong()
    {
        playMenuSong();
    }

    public void playSfx(File sfxFile) 
    {
        if (sfxMediaPlayer != null)
        {
            sfxMediaPlayer.stop();
        }
        Media sound = new Media(sfxFile.toURI().toString());
        sfxMediaPlayer = new MediaPlayer(sound);
        sfxMediaPlayer.volumeProperty().bind(Driver.settingsController.effectsVol); //not working yet
        sfxMediaPlayer.play();
    }

    public void playSfx(String preset)
    {
        playSfx(presets.get(preset));
    }
}