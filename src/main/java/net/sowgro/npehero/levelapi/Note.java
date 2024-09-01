package net.sowgro.npehero.levelapi;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * A note represents a moment in the song when the player should hit a key
 * <p>
 * The key corresponding to the lane the note is in should be pressed
 */
public class Note {

    public final DoubleProperty time = new SimpleDoubleProperty();
    public final int lane;

    /**
     * Creates a new note
     * @param time The time the player should hit the note.
     * @param lane The lane the note belongs to.
     */
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

    public double getTime() {
        return time.get();
    }
}
