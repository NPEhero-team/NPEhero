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

public class Difficulty implements Comparable<Difficulty>
{
    public File thisDir;
    public String title = "Unnamed";
    private ObservableList<LeaderboardEntry> leaderboard = FXCollections.observableArrayList();
    public File notes;
    public Double bpm = 0.0;
    public int numBeats;
    public Level level;
    public boolean isValid = false;
    public int priority = 0;
    
    /**
     * Creates a new Difficulty and gives it a file path
     * @param newDir: The file path of the Difficulty
     */
    public Difficulty(File newDir, Level level)
    {
        thisDir = newDir;
        this.level = level;
    }

    public void readData()
    {
        boolean isValid1 = true;
        if (new File(thisDir, "metadata.json").exists())
        {
            if (!parseMetadata())
            {
                isValid1 = false;
            }
        }
        else
        {
            System.err.println(thisDir+" is missing metadata.json");
            isValid1 = false;
        }

        if (new File(thisDir, "leaderboard.json").exists())
        {
            if (!parseLeaderboard())
            {
                isValid1 = false;
            }
        }
        else
        {
            System.err.println(thisDir+" is missing leaderboard.json");
            isValid1 = false;
        }

        if (new File(thisDir, "notes.txt").exists())
        {
            notes = new File(thisDir, "notes.txt");
        }
        else
        {
            System.err.println(thisDir+" is missing notes.txt");
            isValid1 = false;
        }

        if (bpm == 0.0)
        {
            System.err.println(thisDir+" is missing a bpm");
            isValid1 = false;
        }

        if (numBeats == 0)
        {
            System.err.println(thisDir+" is missing the number of beats");
            isValid1 = false;
        }

        isValid = isValid1;
    }

    /**
     * Reads in json metadata and assigns values to variables
     */
    public boolean parseMetadata()
    {
        boolean isValid = true;
        File file = new File(thisDir, "metadata.json");
        JSONParser jsonParser = new JSONParser(); //parser to read the file
		
		try(FileReader reader = new FileReader(file))
		{
			Object obj = jsonParser.parse(reader); 
			JSONObject diffStuff = (JSONObject)(obj); //converts read object to a JSONObject
            
            if (diffStuff.containsKey("title"))
            {
                title = (String) diffStuff.get("title");
            }
            else
            {
                System.err.println(file+" is missing properety title");
                isValid = false;
            }

            if (diffStuff.containsKey("bpm"))
            {
                bpm = Double.parseDouble(diffStuff.get("bpm")+"");
            }
            else
            {
                System.err.println(file+" is missing properety bpm");
                isValid = false;
            }

            if (diffStuff.containsKey("numBeats"))
            {
                numBeats = Integer.parseInt(diffStuff.get("numBeats")+"");
            }
            else
            {
                System.err.println(file+" is missing properety numBeats");
                isValid = false;
            }

            if (diffStuff.containsKey("priority"))
            {
                priority = Integer.parseInt(diffStuff.get("priority")+"");

            }
            else
            {
                System.err.println(file+" is missing properety priority");
                isValid = false;
            }
		}
		catch (Exception e)
        {
            e.printStackTrace();
            isValid = false;
        }
        return isValid;
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
            obj.put("priority", priority);
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
    public boolean parseLeaderboard()
    {
        boolean isValid = true;
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
            isValid = false;
            e.printStackTrace();
        }
        return isValid;
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

    public boolean isValid() {
        return isValid;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int compareTo(Difficulty d) {
        return priority - d.priority;
    }
}
