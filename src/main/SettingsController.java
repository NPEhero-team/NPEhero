package main;

import java.io.FileWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import javafx.beans.property.SimpleDoubleProperty;

public class SettingsController 
{
	public SimpleDoubleProperty effectsVol = new SimpleDoubleProperty(1);
	public SimpleDoubleProperty musicVol = new SimpleDoubleProperty(1);
	private File file = new File("settings.json");

	public SettingsController()
	{
		read();
	}
	
	/**
	 * reads json data from settings.json
	 */
	public void read()
	{
		JSONParser jsonParser = new JSONParser(); //parser to read the file
		try(FileReader reader = new FileReader(file))
		{
			Object obj = jsonParser.parse(reader); 
			JSONObject settings = new JSONObject();
			settings = (JSONObject)(obj); //converts read object to a JSONObject
			
			effectsVol.set(Double.parseDouble(settings.get("effectsVol")+""));
			musicVol.set(Double.parseDouble(settings.get("musicVol")+""));
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}		
	}

	/**
	 * writes json data to settings.json
	 */
	public void write()
	{
		FileWriter fileWriter;
		try
		{
			fileWriter = new FileWriter(file);
			JSONObject obj = new JSONObject();
			obj.put("musicVol", musicVol.getValue());
			obj.put("effectsVol", effectsVol.getValue());
            obj.writeJSONString(fileWriter);
            fileWriter.flush();
        } 
		catch (IOException e) {
            e.printStackTrace();
        }
	}
}
