package net.sowgro.npehero.levelapi;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Stores all the notes for a difficulty.
 */
public class Notes {

    private final File file;
    private final Difficulty diff;

    public ListProperty<Note> list = new SimpleListProperty<>(FXCollections.observableArrayList());

    /**
     * Create a new Notes object
     * @param file The notes.txt file
     * @param diff The difficulty these notes belong to
     * @throws IOException If there is a problem reading the notes file
     */
    public Notes(File file, Difficulty diff) throws IOException {
        this.file = file;
        this.diff = diff;
        readFile();
    }

    /**
     * Read notes.txt and add the notes to the list
     * @throws IOException if there is a problem reading the file.
     */
    public void readFile() throws IOException {
        if (!file.exists()) {
            return;
        }
        Scanner scan = new Scanner(file);
        while (scan.hasNext()) {
            String input = scan.next();
            int lane = switch (input.charAt(0)) {
                case 'd' -> 0;
                case 'f' -> 1;
                case 's' -> 2;
                case 'j' -> 3;
                case 'k' -> 4;
                default -> -1;
            };
            if (lane == -1) {
                continue;
            }
            double time = Double.parseDouble(input.substring(1));

            if (diff.bpm != 0.0) {
                time = beatToSecond(time, diff.bpm);
            }
            list.add(new Note(time, lane));
        }
    }

    /**
     * Writes the notes to notes.txt
     * @throws IOException If there is a problem writing to the file.
     */
    public void writeFile() throws IOException{
        var _ = file.createNewFile();
        PrintWriter writer = new PrintWriter(file, StandardCharsets.UTF_8);
        for (Note note : list) {
            Character lane = switch (note.lane) {
                case 0 -> 'd';
                case 1 -> 'f';
                case 2 -> 's';
                case 3 -> 'j';
                case 4 -> 'k';
                default -> null;
            };
            if (lane == null) {
                continue;
            }
            writer.println(lane + "" + note.time.get());
        }
        writer.close();
    }

    /**
     * Converts a beat to a second using the levels bpm
     * @param beat The beat to convert to seconds
     * @param bpm The beats per minute to use for conversion
     * @return The time in seconds the beat was at
     */
    public static double beatToSecond(double beat, double bpm) {
        return beat/(bpm/60);
    }

    /**
     * Performs a deep copy of the notes list.
     * @return a new list of notes with the same notes.
     */
    public ListProperty<Note> deepCopyList() {
        ListProperty<Note> ret = new SimpleListProperty<>(FXCollections.observableArrayList());
        list.forEach(e -> ret.add(new Note(e)));
        return ret;
    }
}
