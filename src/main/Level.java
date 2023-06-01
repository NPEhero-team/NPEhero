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

import devmenu.LevelList;
import gui.Driver;

public class Level 
{
    public File thisDir;
    private String title = "Unnamed";
    private String artist = "Unknown";
    private ArrayList<Difficulty> diffList;

    public Image preview; //optional
    public String desc = "No description";
    public Image background; //optional
    public Color[] colors = {Color.RED,Color.BLUE,Color.GREEN,Color.PURPLE,Color.YELLOW};//optional, have default colors

    public Level(File dir)
    {
        thisDir = dir;
    }

    public void readData()
    {
        diffList = new ArrayList<Difficulty>();
        for(File cur: thisDir.listFiles()) //iterates through all files/folders in src/assets/levels/LEVEL
        {
            if (cur.isDirectory()) //all subfolders within a level folder are difficulties
            {
                Difficulty diff = new Difficulty(cur);
                diff.readData();
                diffList.add(diff);
            }

            if (cur.getName().equals("preview.png"))
            {
                preview = new Image(cur.toURI().toString());
            }

            if (cur.getName().equals("metadata.json"))
            {
                parseMetadata();
            }

            if (cur.getName().equals("background.png"))
            {
                background = new Image(cur.toURI().toString());
            }
        }
    }

    public void parseMetadata() 
    {
        JSONParser jsonParser = new JSONParser(); //parser to read the file
		
		try(FileReader reader = new FileReader(new File(thisDir, "metadata.json")))
		{
			Object obj = jsonParser.parse(reader); 
			JSONObject levelStuff = new JSONObject();
			levelStuff = (JSONObject)(obj); //converts read object to a JSONObject

            title = (String)(levelStuff.get("title"));
            artist = (String)(levelStuff.get("artist"));
            desc = (String)(levelStuff.get("desc"));

            if(( levelStuff).containsKey("color1")) //check for custom colors in a hexadecimal format
            {
                colors = new Color[5];

                colors[0] = Color.web((String)(levelStuff.get("color1"))); //read in all the custom colors
                colors[1] = Color.web((String)(levelStuff.get("color2")));
                colors[2] = Color.web((String)(levelStuff.get("color3")));
                colors[3] = Color.web((String)(levelStuff.get("color4")));
                colors[4] = Color.web((String)(levelStuff.get("color5")));
            }
            else
            {
                colors = new Color[]{Color.RED,Color.BLUE,Color.GREEN,Color.PURPLE,Color.YELLOW};
            }
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
    }

    public void writeMetadata()
    {
        FileWriter fileWriter;
        try 
        {
            fileWriter = new FileWriter(new File(thisDir, "metadata.json"));
            JSONObject obj = new JSONObject();
            obj.put("title", title);
            obj.put("artist", artist);
            obj.put("desc", desc);
            obj.put("color1",colors[0].toString());
            obj.put("color2",colors[1].toString());
            obj.put("color3",colors[2].toString());
            obj.put("color4",colors[3].toString());
            obj.put("color5",colors[4].toString());
            obj.writeJSONString(fileWriter);
            fileWriter.flush();
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Difficulty> getDiffList()
    {
        return diffList;
    }

    public void addDiff(String text) 
    {
        File diffDir = new File(thisDir, text);
        diffDir.mkdirs();
        File metadataDir = new File(diffDir, "metadata.json");
        try {
            metadataDir.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Difficulty temp = new Difficulty(diffDir);
        temp.title = text;
        temp.writeMetadata();
        readData();
    }

    public String getTitle() 
    {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() 
    {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
