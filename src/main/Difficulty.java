package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;

import javax.lang.model.util.ElementScanner14;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Difficulty 
{
    public String title;
    private ObservableList<LeaderboardEntry> leaderboard;
    public File notes;
    public int bpm = 28;
    public File song;
    public int numBeats;
    public JSONObject diffStuff;
    public JSONObject leaderboardStuff;

    public void parseMetadata(File file) {
        JSONParser jsonParser = new JSONParser(); //parser to read the file
		
		try(FileReader reader = new FileReader(file))
		{
			Object obj = jsonParser.parse(reader); 
			
			diffStuff = (JSONObject)(obj); //converts read object to a JSONObject
            
            title = (String) diffStuff.get("title");
            bpm = (int) diffStuff.get("bpm");
            numBeats = (int) diffStuff.get("numBeats");            
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} catch (org.json.simple.parser.ParseException e) 
        {
            e.printStackTrace();
        } 

    }

    public void parseLeaderboard(File file) {
        //and here
        //leaderboard.add(new LeaderboardEntry("placeholderScore", 0, "0/0/0"));

        String tracker = "leaderBoardEntry1"; //you gotta follow this format in ascending order in order for the file to be read properly
        //basically this tracker is going to be used as the changing key name to be read in from the file. the substring later on is to be used to increase its value.
        boolean jsonHasNext = true;
        JSONParser jsonParser = new JSONParser(); //parser to read the file
		
		try(FileReader reader = new FileReader(file))
		{
            Object obj = jsonParser.parse(reader); 
			
			leaderboardStuff = (JSONObject)(obj); //converts read object to a JSONObject
            while(jsonHasNext)
            {
                if(leaderboardStuff.containsKey(tracker))
                {
                    int num = (tracker.charAt(tracker.length()-1)) + 1; //get the number at the end of tracker and increase it by one
                    tracker = tracker.substring(0, tracker.length()-1) + num; //substring tracker to take off the end number and add the new end number.

                    //read in the actual leaderboard stuff now
                }
                else
                {
                    jsonHasNext = false;
                }
            }
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} catch (org.json.simple.parser.ParseException e) 
        {
            e.printStackTrace();
        } 

    }

    public void addToLeaderboard(String name, int score) {
        leaderboard.add(new LeaderboardEntry(name, score, ""+LocalDate.now())); //do not delete this tho its not a placeholder
        //and make this write to the json also
    }

    public ObservableList<LeaderboardEntry> getLeaderboard() {
        return leaderboard;
    }
}
