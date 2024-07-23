package net.sowgro.npehero.main;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.input.KeyCode;

import java.io.*;
import java.util.*;

import static java.util.Map.entry;

public enum Control {

    LANE0           ("Lane 1", KeyCode.D),
    LANE1           ("Lane 2", KeyCode.F),
    LANE2           ("Lane 3", KeyCode.SPACE),
    LANE3           ("Lane 4", KeyCode.J),
    LANE4           ("Lane 5", KeyCode.K),
    DELETE_NOTE     ("Delete Note", KeyCode.DELETE),
    NOTE_UP         ("Move Note Up", KeyCode.EQUALS),
    NOTE_DOWN       ("Move Note Down", KeyCode.MINUS),
    SCROLL_LOCK     ("Scroll Lock", KeyCode.L),
    PLAY_PAUSE      ("Play / Pause", KeyCode.P),
    CLEAR_SELECTION ("Clear Selection", KeyCode.ESCAPE),
    SELECT_ALL      ("Select All", KeyCode.S),
    LEGACY_PRINT    ("Print Time", KeyCode.Q),
    LEGACY_STOP     ("Stop Edit", KeyCode.ESCAPE);

    public final String label;
    public final KeyCode defaultKey;
    public final ObjectProperty<KeyCode> keyProperty = new SimpleObjectProperty<>();

    public static final List<Map.Entry<String, List<Control>>> sections = List.of(
                entry("Gameplay",      List.of(LANE0, LANE1, LANE2, LANE3, LANE4)),
                entry("Editor",        List.of(DELETE_NOTE, NOTE_UP, NOTE_DOWN, SCROLL_LOCK, PLAY_PAUSE, CLEAR_SELECTION, SELECT_ALL)),
                entry("Legacy Editor", List.of(LEGACY_PRINT, LEGACY_STOP))
            );

    private static final JSONFile jsonFile = new JSONFile(new File("controls.json"));

    Control(String label, KeyCode key) {
        this.label = label;
        this.defaultKey = key;
        this.keyProperty.set(key);
    }

    public KeyCode getKey() {
        return keyProperty.get();
    }

    public void setKey(KeyCode key) {
        keyProperty.set(key);
    }

    /**
     * The key KeyCode represented as 2 or fewer characters for use in a block target.
     * @return the resulting string
     */
    public String targetString() {
        KeyCode key = getKey();
        return switch (key) {
            case UP    -> "↑";
            case DOWN  -> "↓";
            case LEFT  -> "←";
            case RIGHT -> "→";
            case SPACE -> "_";
            case EQUALS -> "+";
            case MINUS -> "-";
            case null  -> " ";
            default    -> {
                String s = key.toString();
                if (s.length() > 2) {
                    yield s.charAt(0) + ".";
                }
                else {
                    yield s;
                }
            }
        };
    }

    public static void writeToFile() {
        for (Control control : Control.values()) {
            jsonFile.set(control.toString(), control.getKey().toString());
        }
        
        try {
            jsonFile.write();
        }
        catch (IOException e) {
            System.err.println("Error writing to controls.json");
        }
    }

    public static void readFromFile() {
        try {
            jsonFile.read();
        }
        catch (Exception e) {
            System.err.println("Error reading from controls.json");
        }
        
        for (Control control : Control.values()) {
            if (jsonFile.containsKey(control.toString())) {
                control.setKey(KeyCode.valueOf(jsonFile.getString(control.toString(), null)));
            }
        }
    }

}
