package main;

import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class SettingsController 
{
	public SimpleDoubleProperty effectsVol = new SimpleDoubleProperty(1);
	public SimpleDoubleProperty musicVol = new SimpleDoubleProperty(1);
	private JSONObject settings;
	
	public void read() throws ParseException
	{
		JSONParser jsonParser = new JSONParser(); //parser to read the file
		
		try(FileReader reader = new FileReader("settings.json"))
		{
			Object obj = jsonParser.parse(reader); 
			
			settings = (JSONObject)(obj); //converts read object to a JSONObject
			
			effectsVol.set((double) settings.get("effectsVol"));
			musicVol.set((double) settings.get("musicVol"));
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
				
	}

	public void write(int newEffVol, int newMusVol)
	{
		settings.put("musicVol", newMusVol);
		settings.put("effectsVol", newEffVol);
		try (FileWriter file = new FileWriter("settings.json")) 
		{
            //write the settings JSONObject instance to the file
            file.write(settings.toJSONString()); 
            file.flush();
 
        } 
		catch (IOException e) {
            e.printStackTrace();
        }
	}
}
