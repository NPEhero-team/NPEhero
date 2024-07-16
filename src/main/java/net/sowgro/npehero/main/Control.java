package net.sowgro.npehero.main;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.input.KeyCode;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;

public enum Control {

    LANE0           ("Lane 0", KeyCode.D),
    LANE1           ("Lane 1", KeyCode.F),
    LANE2           ("Lane 2", KeyCode.SPACE),
    LANE3           ("Lane 3", KeyCode.J),
    LANE4           ("Lane 4", KeyCode.K),
    DELETE_NOTE     ("Delete note", KeyCode.DELETE),
    NOTE_UP         ("Move note up", KeyCode.EQUALS),
    NOTE_DOWN       ("Move note down", KeyCode.MINUS),
    SCROLL_LOCK     ("Scroll lock", KeyCode.L),
    PLAY_PAUSE      ("Play / Pause", KeyCode.P),
    CLEAR_SELECTION ("Clear selection", KeyCode.ESCAPE),
    SELECT_ALL      ("Select All", KeyCode.S),
    LEGACY_PRINT    ("Print time (Legacy)", KeyCode.Q),
    LEGACY_STOP     ("Stop edit (Legacy)", KeyCode.ESCAPE);

    public final String label;
    public final KeyCode defaultKey;
    public final ObjectProperty<KeyCode> keyProperty = new SimpleObjectProperty<>();

    private static final String fileName = "controls.json";

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
        try {
            File file = new File("controls.json");
            FileWriter fileWriter = new FileWriter(file);
            JSONObject jsonObject = new JSONObject();
            for (Control control : Control.values()) {
                jsonObject.put(control.toString(), control.getKey().toString());
            }
            jsonObject.writeJSONString(fileWriter);
            fileWriter.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readFromFile() {
        File file = new File("controls.json");
        JSONParser jsonParser = new JSONParser();

        try {
            FileReader reader = new FileReader(file);
            Object obj = jsonParser.parse(reader);
            JSONObject jsonObject = (JSONObject)(obj);
            for (Control control : Control.values()) {
                if (jsonObject.containsKey(control.toString())) {
                    control.setKey(KeyCode.valueOf((String) jsonObject.get(control.toString())));
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
