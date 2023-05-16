package main;

import java.io.File;
import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Level 
{
    private Color[] colors;
    private Image background;
    private Image preview;
    private String text;
    private String desc;
    //private ArrayList<String>();

    //google "varargs" to see how this works
    public void setColors(Color... newColors)
    {
        colors = newColors;
    }

    //INCOMPLETE
}
