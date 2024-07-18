package net.sowgro.npehero.main;

import javafx.scene.media.AudioClip;
import net.sowgro.npehero.Driver;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Sound
{
    public static MediaPlayer songMediaPlayer;

    public static final Media MENU_SONG = new Media(Driver.getResource("fairyfountain.wav").toString());

    public static final AudioClip HIT      = new AudioClip(Driver.getResource("hit.wav").toString());
    public static final AudioClip MISS     = new AudioClip(Driver.getResource("miss.wav").toString());
    public static final AudioClip FORWARD  = new AudioClip(Driver.getResource("forward.wav").toString());
    public static final AudioClip BACKWARD = new AudioClip(Driver.getResource("backward.wav").toString());

    /**
     * plays the song that is passed in.
     * @param song The song to play.
     */
    public static void playSong(Media song)
    {
        if (songMediaPlayer != null) {
            songMediaPlayer.stop();
        }
        songMediaPlayer = new MediaPlayer(song);
        if (song == MENU_SONG) {
            songMediaPlayer.muteProperty().bind(Settings.enableMenuMusic.not());
        }
        songMediaPlayer.volumeProperty().bind(Settings.musicVol);
        songMediaPlayer.play();
    }

    /**
     * Stops the currently playing song.
     */
    public static void stopSong()
    {
        if (songMediaPlayer != null)
        {
            songMediaPlayer.stop();
        }
    }

    /**
     * Plays a sound effect.
     * @param clip The sound effect to play.
     */
    public static void playSfx(AudioClip clip)
    {
        clip.volumeProperty().bind(Settings.effectsVol);
        clip.play();
    }
}