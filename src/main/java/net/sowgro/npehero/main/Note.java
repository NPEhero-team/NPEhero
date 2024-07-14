package net.sowgro.npehero.main;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * A note represents a moment in the song when the player should hit a key
 * The key corresponding to the lane the note is in should be pressed
 */
public class Note {

    public DoubleProperty time = new SimpleDoubleProperty();
    public int lane;

    public Note(double time, int lane) {
        this.time.set(time);
        this.lane = lane;
    }

    /**
     * Copy constructor
     * @param other the note to copy from
     */
    public Note(Note other) {
        this.lane = other.lane;
        this.time.set(other.time.get());
    }
}
