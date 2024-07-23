package net.sowgro.npehero.main;

import java.io.File;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Level implements Comparable<Level>{

    public File dir;

    public String title = "Unnamed";
    public String artist = "Unknown";
    public String desc;
    public Color[] colors = {Color.RED,Color.BLUE,Color.GREEN,Color.PURPLE,Color.YELLOW};//optional, have default colors

    public Image preview; //optional
    public Image background; //optional
    public Media song;

    public Difficulties difficulties;

    private JSONFile metadataJson;

    /**
     * Creates a new level
     * @param newDir: The path of the Level
     */
    public Level(File newDir)
    {
        dir = newDir;
        metadataJson = new JSONFile(new File(dir, "metadata.json"));
        difficulties = new Difficulties(this);
        difficulties.read();
        readData();
    }

    /**
     * Check for a song file, background file and preview image file
     * Parse metadata.json
     */
    public void readData() {

        var fileList = dir.listFiles();
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

        try {
            metadataJson.read();
        }
        catch (Exception e) {
            // TODO
        }
        title = metadataJson.getString("title", title);
        artist = metadataJson.getString("artist", artist);
        desc = metadataJson.getString("desc", desc);
        colors[0] = Color.web(metadataJson.getString("color1", colors[0].toString()));
        colors[1] = Color.web(metadataJson.getString("color2", colors[1].toString()));
        colors[2] = Color.web(metadataJson.getString("color3", colors[2].toString()));
        colors[3] = Color.web(metadataJson.getString("color4", colors[3].toString()));
        colors[4] = Color.web(metadataJson.getString("color5", colors[4].toString()));
    }

    /**
     * Checks if the level is valid.
     * A valid level has a song file and 1 or more valid difficulties
     * @return true if the level is valid
     */
    public boolean isValid() {
        if (song == null) {
//            System.out.println(dir +" is missing song file");
            return false;
        }

        if (difficulties.getValidList().isEmpty()) {
//            System.out.println(dir +" contains no valid difficulties");
            return false;
        }
        return true;
    }

    /**
     * Writes metadata to json file
     */
    public void writeMetadata()
    {
        metadataJson.set("title", title);
        metadataJson.set("artist", artist);
        metadataJson.set("desc", desc);
        metadataJson.set("color1",colors[0].toString());
        metadataJson.set("color2",colors[1].toString());
        metadataJson.set("color3",colors[2].toString());
        metadataJson.set("color4",colors[3].toString());
        metadataJson.set("color5",colors[4].toString());
        try {
            metadataJson.write();
        }
        catch (IOException e) {
            // TODO
        }
    }


    /**
     * Copies a file into the level directory
     * @param newFile: the file to be copied
     * @param name: the new file name
     * @throws IOException if there was an error adding the file
     */
    public void addFile(File newFile, String name) throws IOException {
        Files.copy(newFile.toPath(), new File(dir, name).toPath(), StandardCopyOption.REPLACE_EXISTING);
        readData();
    }

    @Override
    public int compareTo(Level other) {
        return title.compareTo(other.title);
    }
}
