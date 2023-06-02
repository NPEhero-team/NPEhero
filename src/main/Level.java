package main;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

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
    public File song;

    /**
     * Creates a new level and gives it a file path
     * @param newDir: The path of the Level
     */
    public Level(File newDir)
    {
        thisDir = newDir;
    }

    /**
     * Checks for files in the level folder and runs cooresponding actions
     */
    public void readData()
    {
        diffList = new ArrayList<Difficulty>();
        for(File cur: thisDir.listFiles()) //iterates through all files/folders in src/assets/levels/LEVEL
        {
            if (cur.isDirectory()) //all subfolders within a level folder are difficulties
            {
                Difficulty diff = new Difficulty(cur,this);
                diff.readData();
                diffList.add(diff);
            }
            if (cur.getName().equals("metadata.json"))
            {
                parseMetadata();
            }
            if (cur.getName().equals("preview.png"))
            {
                preview = new Image(cur.toURI().toString());
            }
            if (cur.getName().equals("background.png"))
            {
                background = new Image(cur.toURI().toString());
            }
            if (cur.getName().equals("song.wav"))
            {
                song = cur;
            }
        }
    }

    /**
     * Reads in json metadata and assigns values to variables
     */
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

    /**
     * Writes metadata to json file
     */
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

    /**
     * Adds a difficulty by creating a directory and required files
     * @param text: the name of the directory and default title
     */
    public void addDiff(String text) 
    {
        File diffDir = new File(thisDir, text);
        diffDir.mkdirs();
        File metadataDir = new File(diffDir, "metadata.json");
        File leaderboardDir = new File(diffDir, "leaderboard.json");
        File notesDir = new File(diffDir, "notes.txt");
        try {
            metadataDir.createNewFile();
            leaderboardDir.createNewFile();
            notesDir.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Difficulty temp = new Difficulty(diffDir,this);
        temp.title = text;
        temp.writeMetadata();
        temp.writeLeaderboard();
        readData();
    }

    /**
     * Removes the difficaulty from the filesystem then reloads the level
     * @param diff: the difficulty to be removed
     */
    public void removeDiff(Difficulty diff)
    {
        File hold = diff.thisDir;
        diffList.remove(diff);

        try {
            Files.walk(hold.toPath())
            .sorted(Comparator.reverseOrder())
            .map(Path::toFile)
            .forEach(File::delete);
        } catch (IOException e) {
            e.printStackTrace();
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
            Files.copy(newFile.toPath(), new File(thisDir, name).toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        readData();
    }

    public ArrayList<Difficulty> getDiffList()
    {
        return diffList;
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
