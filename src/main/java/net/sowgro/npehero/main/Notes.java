package net.sowgro.npehero.main;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Notes {
    private File file;
    private Difficulty diff;

    public ListProperty<Note> list = new SimpleListProperty<>(FXCollections.observableArrayList());

    public Notes(File f, Difficulty diff) {
        this.file = f;
        this.diff = diff;
        readFile();
    }

    public void readFile() {
        Scanner scan = null;	//file reader for reading in the notes from a notes.txt file
        try {
            scan = new Scanner(file);
        }
        catch (FileNotFoundException e) {
            // TODO handle error
            throw new RuntimeException(e);
        }

        while (scan.hasNext()) {
            String input = scan.next();

            int lane = switch (input.charAt(0)) {
                case 'd' -> 0;
                case 'f' -> 1;
                case 's' -> 2;
                case 'j' -> 3;
                case 'k' -> 4;
                default  -> -1;
            };

            if (lane == -1) {
                // TODO handle error
                continue;
            }

            double time = Double.parseDouble(input.substring(1));
            if (diff.bpm != 0.0) {
                time = beatToSecond(time, diff.bpm);
            }
            list.add(new Note(time, lane));
        }
    }

    public void writeFile() {
        try (PrintWriter writer = new PrintWriter(diff.notes.getFile(), StandardCharsets.UTF_8)) {
            for (Note note : list) {
                char lane = switch (note.lane) {
                    case 0 -> 'd';
                    case 1 -> 'f';
                    case 2 -> 's';
                    case 3 -> 'j';
                    case 4 -> 'k';
                    default -> 'e';
                };

                if (lane == 'e') {
                    // TODO handle error
                }

                writer.println(lane + note.time.get());

            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private double beatToSecond(double beat, double bpm) {
        return beat/(bpm/60);
    }

    public File getFile() {
        return file;
    }

    public ListProperty<Note> deepCopyList() {
        ListProperty<Note> ret = new SimpleListProperty<>(FXCollections.observableArrayList());
        list.forEach(e -> ret.add(new Note(e)));
        return ret;
    }
}
