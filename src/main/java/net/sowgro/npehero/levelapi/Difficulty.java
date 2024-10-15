package net.sowgro.npehero.levelapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a difficulty
 * Responsible for the data in metadata.yml
 */
public class Difficulty implements Comparable<Difficulty>
{
    public final File thisDir;
    public final Level level;

    public String title = "Unnamed";
    public Double bpm = 0.0;
    public double endTime = 0;
    public int order = 0;

    public final Leaderboard leaderboard;
    public final Notes notes;

    private final Gson jsonParser = new GsonBuilder().serializeNulls().setPrettyPrinting().setNumberToNumberStrategy(ToNumberPolicy.DOUBLE).create();
    private final File jsonFile;

    /**
     * Creates a new Difficulty
     * @param newDir: The file path of the Difficulty
     * @throws IOException If there are any problems reading the metadata or leaderboard files
     */
    public Difficulty(File newDir, Level level) throws IOException {
        thisDir = newDir;
        this.level = level;
        jsonFile = new File(thisDir, "metadata.json");
        readMetadata();
        notes = new Notes(new File(thisDir, "notes.txt"), this); // needs metadata first
        leaderboard = new Leaderboard(new File(thisDir, "leaderboard.json"));
    }

    /**
     * Read in the data from metadata.json
     * @throws IOException If there are any problems loading the file.
     */
    public void readMetadata() throws IOException {
        if (!jsonFile.exists()) {
            return;
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> data = jsonParser.fromJson(new FileReader(jsonFile), Map.class);
        if (data == null) {
            return;
        }

        title = (String) data.getOrDefault("title", title);
        bpm = (Double) data.getOrDefault("bpm", bpm);
        endTime = (double) data.getOrDefault("endTime", endTime);
        if (endTime == 0) {
            int tmp = (int) (double) data.getOrDefault("numBeats", 0.0);
            if (tmp != 0) {
                endTime = Notes.beatToSecond(tmp, bpm);
            }
        }
        order = (int) (double) data.getOrDefault("priority", (double) order);
    }

    /**
     * Checks the validity of the difficulty
     * <p>
     * A valid difficulty has at least one note
     * @return True if the difficulty is valid
     */
    public boolean isValid() {
        return !notes.list.isEmpty();
    }

    /**
     * Writes metadata to json file
     * @throws IOException If there is a problem writing to the file
     */
    public void writeMetadata() throws IOException {
        if (!jsonFile.exists() && !jsonFile.createNewFile()) {
            throw new IOException("Could not create file " + jsonFile.getAbsolutePath());
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> data = jsonParser.fromJson(new FileReader(jsonFile), Map.class); // start with previous values
        if (data == null) {
            data = new HashMap<>();
        }
        data.put("title", title);
        data.put("endTime", endTime);
        data.put("priority", order);
        FileWriter fileWriter = new FileWriter(jsonFile);
        jsonParser.toJson(data, fileWriter);
        fileWriter.close();
    }

    @Override
    public int compareTo(Difficulty d) {
        return order - d.order;
    }
}
