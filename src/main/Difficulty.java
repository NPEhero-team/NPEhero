package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;

import javax.lang.model.util.ElementScanner14;

import org.json.simple.JSONArray;
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
    public LeaderboardEntry[] fileLeaderboard;
    private String filepath;
    

    public void parseMetadata(File file) 
    {
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

        JSONParser jsonParser = new JSONParser(); //parser to read the file

        filepath = file.getName();
        
        System.out.println(file);

		try(FileReader reader = new FileReader(file))
		{
            Object obj = jsonParser.parse(reader); 
			
			leaderboardStuff = (JSONObject)(obj); //converts read object to a JSONArray
            
            fileLeaderboard = (LeaderboardEntry[]) leaderboardStuff.get("leaderboard");

            leaderboard.addAll(fileLeaderboard);
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

    public void addToLeaderboard(String name, int score) 
    {
        leaderboard.add(new LeaderboardEntry(name, score, ""+LocalDate.now())); //do not delete this tho its not a placeholder
        
        try (FileWriter fileWriter = new FileWriter(filepath)) 
		{
            //write the settings JSONObject instance to the file 
            fileWriter.write(((JSONArray) leaderboard).toJSONString()); 
            fileWriter.flush();
 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<LeaderboardEntry> getLeaderboard() {
        return leaderboard;
    }
}
