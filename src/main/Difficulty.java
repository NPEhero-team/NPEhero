package main;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Difficulty 
{
    public File thisDir;
    public String title = "Unnamed";
    private ObservableList<LeaderboardEntry> leaderboard = FXCollections.observableArrayList();
    public File notes;
    public int bpm;
    public int numBeats;
    
    /**
     * Creates a new Difficulty and gives it a file path
     * @param newDir: The file path of the Difficulty
     */
    public Difficulty(File newDir)
    {
        thisDir = newDir;
    }

    /**
     * Checks for files in the difficulty folder and runs cooresponding actions
     */
    public void readData()
    {
        for(File cur: thisDir.listFiles()) //iterates through all files/folders in src/assets/levels/LEVEL/DIFFICULTY
        {
            if (cur.getName().equals("metadata.json"))
            {
                parseMetadata();
            }
            if (cur.getName().equals("leaderboard.json"))
            {
                parseLeaderboard();
            }
            if (cur.getName().equals("notes.txt"))
            {
                notes = cur;
            }
        }
    }

    /**
     * Reads in json metadata and assigns values to variables
     */
    public void parseMetadata()
    {
        File file = new File(thisDir, "metadata.json");
        JSONParser jsonParser = new JSONParser(); //parser to read the file
		
		try(FileReader reader = new FileReader(file))
		{
			Object obj = jsonParser.parse(reader); 
			JSONObject diffStuff = (JSONObject)(obj); //converts read object to a JSONObject
            
            title = (String) diffStuff.get("title");
            bpm = Integer.parseInt(diffStuff.get("bpm")+"");
            numBeats = Integer.parseInt(diffStuff.get("numBeats")+"");
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
            File file = new File(thisDir, "metadata.json");
            fileWriter = new FileWriter(file);
            JSONObject obj = new JSONObject();
            obj.put("title", title);
            obj.put("bpm", bpm);
            obj.put("numBeats", numBeats);
            obj.writeJSONString(fileWriter);
            fileWriter.flush();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    /**
     * Reads in json leaderboard and assigns populates list with leaderboardEntries
     */
    public void parseLeaderboard()
    {
        File file = new File(thisDir, "leaderboard.json");
        JSONParser jsonParser = new JSONParser(); //parser to read the file

		try(FileReader reader = new FileReader(file))
		{
            Object obj = jsonParser.parse(reader); 
			
			JSONArray leaderboardStuff = (JSONArray)(obj); //converts read object to a JSONArray

            for (Object cur: leaderboardStuff)
            {
                JSONObject cur2 = (JSONObject) cur;

                String name = (String) cur2.get("name");
                int score = Integer.parseInt(""+cur2.get("score"));
                String date = (String) cur2.get("date");
                leaderboard.add(new LeaderboardEntry(name, score, date));
            }
		}
		catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Writes leaderboard to json file
     */
    public void writeLeaderboard()
    {
        FileWriter fileWriter;
        try
		{
            File file = new File(thisDir, "leaderboard.json");
            fileWriter = new FileWriter(file);
            //write the settings JSONObject instance to the file 
            JSONArray jsonArray = new JSONArray();
            for (LeaderboardEntry cur: leaderboard)
            {
                JSONObject obj = new JSONObject();
                obj.put("name", cur.getName());
                obj.put("score", cur.getScore());
                obj.put("date",cur.getDate());
                jsonArray.add(obj);
            }
            jsonArray.writeJSONString(fileWriter);
            fileWriter.flush();
 
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds new leaderboardEntry to list and updates json file
     * @param name: the players name
     * @param score the players score
     */
    public void addToLeaderboard(String name, int score) 
    {
        leaderboard.add(new LeaderboardEntry(name, score, ""+LocalDate.now())); //do not delete this tho its not a placeholder
        writeLeaderboard();
    }

    public ObservableList<LeaderboardEntry> getLeaderboard() 
    {
        return leaderboard;
    }

    public String toString()
    {
        return title;
    }
}
