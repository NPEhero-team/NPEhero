package net.sowgro.npehero.main;

import java.io.File;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Level 
{
    public File dir;

    public String title = "Unnamed";
    public String artist = "Unknown";
    public String desc;
    public Color[] colors = {Color.RED,Color.BLUE,Color.GREEN,Color.PURPLE,Color.YELLOW};//optional, have default colors

    public Image preview; //optional
    public Image background; //optional
    public File song;

    public Difficulties difficulties;

    public boolean isValid = true;

    private JSONFile metadataJson;

    /**
     * Creates a new level and gives it a file path
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

    public void readData() {

        if (new File(dir, "song.wav").exists()) {
            song = new File(dir,"song.wav");
        }

        if (new File(dir, "background.png").exists()) {
            background = new Image(new File(dir,"background.png").toURI().toString());
        }

        if (new File(dir, "preview.png").exists()) {
            preview = new Image(new File(dir,"preview.png").toURI().toString());
        }

        parseMetadata();
        validate();
    }

    public void validate() {
        if (song == null) {
            System.err.println(dir +" is missing song.wav");
        }

        if (difficulties.validList.isEmpty()) {
            System.err.println(dir +" contains no valid difficulties");
            isValid = false;
        }
    }

    /**
     * Reads in json metadata and assigns values to variables
     */
    public void parseMetadata()
    {
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
     */
    public void addFile(File newFile, String name)
    {
        try {
            Files.copy(newFile.toPath(), new File(dir, name).toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        readData();
    }
}
