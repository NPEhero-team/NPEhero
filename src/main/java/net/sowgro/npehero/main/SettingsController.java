package net.sowgro.npehero.main;

import java.io.FileWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import javafx.beans.property.SimpleDoubleProperty;

public class SettingsController
{
	public static SimpleDoubleProperty effectsVol = new SimpleDoubleProperty(1);
	public static SimpleDoubleProperty musicVol = new SimpleDoubleProperty(1);
	private static File file = new File("settings.json");

	/**
	 * reads json data from settings.json
	 */
	public static void read()
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
	public static void write()
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
