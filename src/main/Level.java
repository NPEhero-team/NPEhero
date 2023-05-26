package main;

import java.io.File;
import java.util.ArrayList;
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
    private String title;
    private String artist;
    public String desc;
    public ArrayList<Difficulty> diffList = new ArrayList<Difficulty>();

    public Image background; //optional
    public Color[] colors = new Color[5];//optional, have default colors

    private JSONObject levelStuff;

    public void setColors(Color... newColors) 
    {
        colors = newColors;
    }

    public String getTitle() 
    {
        return title;
    }

    public String getArtist() 
    {
        return artist;
    }

    public void parseMetadata(File file) 
    {
        JSONParser jsonParser = new JSONParser(); //parser to read the file
		
		try(FileReader reader = new FileReader(file))
		{
			Object obj = jsonParser.parse(reader); 
			
			levelStuff = (JSONObject)(obj); //converts read object to a JSONObject

            title = (String)(levelStuff.get("title"));
            artist = (String)(levelStuff.get("artist"));
            desc = (String)(levelStuff.get("desc"));

            if(( levelStuff).containsKey("color1"))
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
        catch (ParseException e) 
        {
            e.printStackTrace();
        }
    }
}
