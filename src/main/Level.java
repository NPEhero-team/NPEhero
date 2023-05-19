package main;

import java.io.File;
import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Level 
{
    public Image preview;
    public String title;
    public String aritst;
    public String desc;
    public ArrayList<String> diffList = new ArrayList<String>();

    public Image background;
    public Color[] colors;

    public void setColors(Color... newColors)
    {
        colors = newColors;
    }

    public String toString()
    {
        return title+" - "+aritst;
    }
}
