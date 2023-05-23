package main;

import java.util.Map;
import java.util.HashMap;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.beans.property.SimpleIntegerProperty;

public class SettingsController 
{
	public SimpleIntegerProperty effectsVol = new SimpleIntegerProperty(0);
	public SimpleIntegerProperty musicVol = new SimpleIntegerProperty(0);
	private boolean fullscreen;
	private JSONObject settings;
	
	public void saveAndWrite(int newEffVol, int newMusVol, boolean isFull)
	{
		
	}
	
	public void readFile() throws ParseException
	{
		JSONParser jsonParser = new JSONParser(); //parser to read the file
		
		try(FileReader reader = new FileReader("settings.json"))
		{
			Object obj = jsonParser.parse(reader); 
			
			settings = (JSONObject)(obj); //converts read object to a JSONObject
			
			effectsVol.set((int) settings.get("effectsVol"));
			musicVol.set((int) settings.get("musicVol"));
			fullscreen = (boolean) settings.get("fullscreen");
		}
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}
	
}
