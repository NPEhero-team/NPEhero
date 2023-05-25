package main;

import java.io.File;
import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Level 
{
    public Image preview; //optional
    private SimpleStringProperty title;
    private SimpleStringProperty artist;
    public String desc;
    public ArrayList<Difficulty> diffList = new ArrayList<Difficulty>();

    public Image background; //optional
    public Color[] colors = new Color[5];//optional, have default colors

    private JSONObject levelStuff;

    public void setColors(Color... newColors) 
    {
        colors = newColors;
    }

    //all below is required for table view
    public Level() throws ParseException
    {
        JSONParser jsonParser = new JSONParser(); //parser to read the file
		
		try(FileReader reader = new FileReader(".json"))
		{
			Object obj = jsonParser.parse(reader); 
			
			levelStuff = (JSONObject)(obj); //converts read object to a JSONObject

            title = (SimpleStringProperty)(levelStuff.get("title"));
            artist = (SimpleStringProperty)(levelStuff.get("title"));
            desc = (String)(levelStuff.get("title"));

            if(levelStuff.has("color1"))
            {
                
            }
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
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
