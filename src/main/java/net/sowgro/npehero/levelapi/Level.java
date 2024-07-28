package net.sowgro.npehero.levelapi;

import java.io.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;

import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Map;

public class Level implements Comparable<Level>{

    public final File dir;

    public String title = "Unnamed";
    public String artist = "Unknown";
    public String desc;
    public Color[] colors = {Color.RED,Color.BLUE,Color.GREEN,Color.PURPLE,Color.YELLOW};
    public Image preview;
    public Image background;
    public Media song;

    public Difficulties difficulties;

    private final File jsonFile;
    private final Gson jsonParser = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

    /**
     * Creates a new level
     * @param newDir The path of the Level
     * @throws IOException If there is a problem reading the metadata file or loading the difficulties
     */
    public Level(File newDir) throws IOException
    {
        dir = newDir;
        jsonFile = new File(dir, "metadata.json");
        readFiles();
        readMetadata();
        difficulties = new Difficulties(this);
    }

    /**
     * Check for a song file, background file and preview image file
     */
    public void readFiles() {

        File[] fileList = dir.listFiles();
        if (fileList == null) {
            return;
        }
        for (File file : fileList) {
            String fileName = file.getName();
            if (fileName.contains("song")) {
                song = new Media(file.toURI().toString());
            }
            else if (fileName.contains("background")) {
                background = new Image(file.toURI().toString());
            }
            else if (fileName.contains("preview")) {
                preview = new Image(file.toURI().toString());
            }
        }

    }

    /**
     * Read in metadata file
     * @throws IOException If there is a problem reading the file
     */
    public void readMetadata() throws IOException {
        if (!jsonFile.exists()) {
            return;
        }
        Map<String, Object> data = jsonParser.fromJson(new FileReader(jsonFile), Map.class);
        title = (String) data.getOrDefault("title", title);
        artist = (String) data.getOrDefault("artist", artist);
        desc = (String) data.getOrDefault("desc", desc);
        colors[0] = Color.web((String) data.getOrDefault("color1", colors[0].toString()));
        colors[1] = Color.web((String) data.getOrDefault("color2", colors[1].toString()));
        colors[2] = Color.web((String) data.getOrDefault("color3", colors[2].toString()));
        colors[3] = Color.web((String) data.getOrDefault("color4", colors[3].toString()));
        colors[4] = Color.web((String) data.getOrDefault("color5", colors[4].toString()));
    }

    /**
     * Checks if the level is valid.
     * <p>
     * A valid level has a song file and 1 or more valid difficulties
     * @return True if the level is valid
     */
    public boolean isValid() {
        if (song == null) {
            return false;
        }

        if (difficulties.getValidList().isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * Writes metadata to json file
     * @throws IOException If there is a problem writing to the file.
     */
    public void writeMetadata() throws IOException {
        jsonFile.createNewFile();
        Map<String, Object> data = jsonParser.fromJson(new FileReader(jsonFile), Map.class);
        data.put("title", title);
        data.put("artist", artist);
        data.put("desc", desc);
        data.put("color1",colors[0].toString());
        data.put("color2",colors[1].toString());
        data.put("color3",colors[2].toString());
        data.put("color4",colors[3].toString());
        data.put("color5",colors[4].toString());
        FileWriter fileWriter = new FileWriter(jsonFile);
        jsonParser.toJson(data, fileWriter);
        fileWriter.close();
    }


    /**
     * Copies a file into the level directory with the name provided. The extension will be inherited from the source file
     * @param source: the file to be copied
     * @param name: the new file name EXCLUDING the extension.
     * @throws IOException If there is a problem adding the file
     */
    public void addFile(File source, String name) throws IOException {
        name = name + "." + getFileExtension(source);
        Files.copy(source.toPath(), new File(dir, name).toPath(), StandardCopyOption.REPLACE_EXISTING);
        readFiles();
    }

    @Override
    public int compareTo(Level other) {
        return title.compareTo(other.title);
    }

    /**
     * Get the extension of a file.
     * @param file The file to return the extension of
     * @return The extension of the file in the format "*.ext"
     */
    public String getFileExtension(File file) {
        return file.getName().substring(file.getName().lastIndexOf('.') + 1);
    }
}
