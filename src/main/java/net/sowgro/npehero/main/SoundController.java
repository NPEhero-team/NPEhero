package net.sowgro.npehero.main;

import java.io.File;
import java.util.HashMap;

import javafx.scene.media.AudioClip;
import net.sowgro.npehero.Driver;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundController 
{
    public static MediaPlayer songMediaPlayer;

    public static final Media MENUSONG = new Media(Driver.getResource("fairyfountain.wav").toString());

    public static final AudioClip HIT      = new AudioClip(Driver.getResource("hit.wav").toString());
    public static final AudioClip MISS     = new AudioClip(Driver.getResource("miss.wav").toString());
    public static final AudioClip FORWARD  = new AudioClip(Driver.getResource("forward.wav").toString());
    public static final AudioClip BACKWARD = new AudioClip(Driver.getResource("backward.wav").toString());

    /**
     * plays the song that is passed in.
     * @param song the song to play
     */
    public static void playSong(Media song)
    {
        if (songMediaPlayer != null)
        {
            songMediaPlayer.stop();
        }
        songMediaPlayer = new MediaPlayer(song);
        songMediaPlayer.volumeProperty().bind(SettingsController.musicVol);
        songMediaPlayer.play();
    }

    /**
     * stops the currently playing song
     */
    public static void endSong()
    {
        if (songMediaPlayer != null)
        {
            songMediaPlayer.stop();
        }
    }

    /**
     * plays a sound effect
     * @param clip the sound to play
     */
    public static void playSfx(AudioClip clip)
    {
        clip.volumeProperty().bind(SettingsController.effectsVol);
        clip.play();
    }
}