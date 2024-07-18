package net.sowgro.npehero.main;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.spec.ECField;

import org.json.simple.JSONObject;

public class Difficulty implements Comparable<Difficulty>
{
    public File thisDir;
    public Level level;

    public String title = "Unnamed";
    public Double bpm = 0.0;
    public int numBeats;
    public int priority = 0;

    public Leaderboard leaderboard;
    public Notes notes;

    public boolean isValid = true;

    private final JSONFile metadataYaml;
    
    /**
     * Creates a new Difficulty and gives it a file path
     * @param newDir: The file path of the Difficulty
     */
    public Difficulty(File newDir, Level level) {
        thisDir = newDir;
        this.level = level;
        metadataYaml = new JSONFile(new File(thisDir, "metadata.json"));
    }

    public void readData() {
        try {
            metadataYaml.read();
        }
        catch (Exception e) {
            System.err.println(level.title + "/" + title + ": Failed to read metadata.json");
        }

        title = metadataYaml.getString("title", title);
        bpm = metadataYaml.getDouble("bpm", bpm);
        numBeats = metadataYaml.getInt("numBeats", numBeats);
        priority = metadataYaml.getInt("priority", priority);

        notes = new Notes(new File(thisDir, "notes.txt"), this);
        leaderboard = new Leaderboard(new File(thisDir, "leaderboard.json"));

        validate();

    }

    public void validate() {
        isValid = true;

        if (notes.list.isEmpty()) {
            isValid = false;
        }

        if (numBeats == 0) {
            isValid = false;
        }
    }

    /**
     * Writes metadata to json file
     */
    public void write() {
        metadataYaml.set("title", title);
        metadataYaml.set("numBeats", numBeats);
        metadataYaml.set("priority", priority);

        try {
            metadataYaml.write();
        } 
        catch (IOException e) {
            System.err.println(level.title + "/" + title + ": Failed to write metadata.json");
        }
    }

    @Override
    public int compareTo(Difficulty d) {
        return priority - d.priority;
    }
}
