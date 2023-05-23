package main;

import java.io.File;
import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Level 
{
    public Image preview; //optional
    private SimpleStringProperty title;
    private SimpleStringProperty artist;
    public String desc;
    public ArrayList<Difficulty> diffList = new ArrayList<Difficulty>();

    public Image background; //optional
    public Color[] colors; //optional, have default colors

    public void setColors(Color... newColors) 
    {
        colors = newColors;
    }

    //all below is required for table view
    public Level(String title, String artist)
    {
        this.title = new SimpleStringProperty(title);
        this.artist = new SimpleStringProperty(artist);
    }

    public String getTitle() {
        return title.get();
    }

    public String getArtist() {
        return artist.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public void setArtist(String artist) {
        this.artist.set(artist);
    }
}
