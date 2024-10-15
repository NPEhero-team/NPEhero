package net.sowgro.npehero.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.input.KeyCode;
import net.sowgro.npehero.Driver;

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
    NOTE_UP         ("Move Note Up", KeyCode.UP),
    NOTE_DOWN       ("Move Note Down", KeyCode.DOWN),
    SCROLL_LOCK     ("Scroll Lock", KeyCode.L),
    PLAY_PAUSE      ("Play / Pause", KeyCode.P),
    CLEAR_SELECTION ("Clear Selection", KeyCode.ESCAPE),
    SELECT_ALL      ("Select All", KeyCode.S),
    SELECT_MULTIPLE ("Select Multiple (Hold)", KeyCode.CONTROL),
    LEGACY_PRINT    ("Print Time", KeyCode.Q),
    LEGACY_STOP     ("Stop Edit", KeyCode.ESCAPE);

    public static final List<Map.Entry<String, List<Control>>> sections = List.of(
                entry("Gameplay",      List.of(LANE0, LANE1, LANE2, LANE3, LANE4)),
                entry("Editor",        List.of(DELETE_NOTE, NOTE_UP, NOTE_DOWN, SCROLL_LOCK, PLAY_PAUSE, CLEAR_SELECTION, SELECT_ALL, SELECT_MULTIPLE)),
                entry("Legacy Editor", List.of(LEGACY_PRINT, LEGACY_STOP))
            );

    public static final Control[] lanes = {
            LANE0,
            LANE1,
            LANE2,
            LANE3,
            LANE4,
    };

    private static final File file = new File(Driver.BASE_DIR, "controls.json");
    private static final Gson jsonParser = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

    public static void writeToFile() throws IOException {
        Map<String, Object> data = new HashMap<>();
        for (Control control : Control.values()) {
            data.put(control.toString(), control.getKey().toString());
        }
        FileWriter fileWriter = new FileWriter(file);
        jsonParser.toJson(data, fileWriter);
        fileWriter.close();
    }

    public static void readFromFile() throws Exception {
        if (!file.exists()) {
            return;
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> data = jsonParser.fromJson(new FileReader(file), Map.class);
        if (data == null) {
            data = new HashMap<>();
        }
        for (Control control : Control.values()) {
            if (data.containsKey(control.toString())) {
                control.setKey(KeyCode.valueOf((String) data.getOrDefault(control.toString(), null)));
            }
        }
    }

    public final String label;
    public final KeyCode defaultKey;
    public final ObjectProperty<KeyCode> keyProperty = new SimpleObjectProperty<>();

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
}
