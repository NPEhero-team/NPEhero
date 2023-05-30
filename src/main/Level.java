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
    public Color[] colors;//optional, have default colors
    public Color c1;
    public Color c2;
    public Color c3;
    public Color c4;
    public Color c5;

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

            if(( levelStuff).containsKey("color1")) //check for custom colors in a hexadecimal format
            {
                colors = new Color[5];

                c1 = Color.web((String)(levelStuff.get("color1"))); //read in all the custom colors
                c2 = Color.web((String)(levelStuff.get("color2")));
                c3 = Color.web((String)(levelStuff.get("color3")));
                c4 = Color.web((String)(levelStuff.get("color4")));
                c5 = Color.web((String)(levelStuff.get("color5")));

                colors[0] = c1;
                colors[1] = c2;
                colors[2] = c3;
                colors[3] = c4;
                colors[4] = c5;
            }
            else
            {
                colors = new Color[]{Color.RED,Color.BLUE,Color.GREEN,Color.PURPLE,Color.YELLOW};
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
