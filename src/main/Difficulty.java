package main;

import java.io.File;
import java.io.FileNotFoundException;
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
    public String title;
    private ObservableList<LeaderboardEntry> leaderboard = FXCollections.observableArrayList();
    public File notes;
    public int bpm;
    public File song;
    public int numBeats;
    private File leaderboardFile;
    

    public void parseMetadata(File file) throws FileNotFoundException, IOException 
    {
        JSONParser jsonParser = new JSONParser(); //parser to read the file
		
		try(FileReader reader = new FileReader(file))
		{
			Object obj = jsonParser.parse(reader); 
			JSONObject diffStuff = (JSONObject)(obj); //converts read object to a JSONObject
            
            title = (String) diffStuff.get("title");
            bpm = (int)(diffStuff.get("bpm"));           
            numBeats = (int)(diffStuff.get("numBeats"));  
            
		}
		catch (Exception e)
        {
            System.out.println("Error in json file "+file);
            //e.printStackTrace();
        }
    }

    public void parseLeaderboard(File file) throws FileNotFoundException, IOException 
    {
        leaderboardFile = file;
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
            System.out.println("Error in json file "+leaderboardFile);
            //e.printStackTrace();
        }
    }

    public void addToLeaderboard(String name, int score) 
    {
        leaderboard.add(new LeaderboardEntry(name, score, ""+LocalDate.now())); //do not delete this tho its not a placeholder
        
        try (FileWriter fileWriter = new FileWriter(leaderboardFile)) 
		{
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

    public ObservableList<LeaderboardEntry> getLeaderboard() 
    {
        return leaderboard;
    }
}
