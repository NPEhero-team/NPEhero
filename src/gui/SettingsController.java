package gui;
import org.json.simple.JSONObject;
import java.util.Map;
import java.util.HashMap;

public class SettingsController 
{
	private int effectsVol;
	private int musicVol;
	private boolean fullscreen;
	
	public SettingsController()
	{
		readFile();
	}
	
	public void saveAndWrite(int newEffVol, int newMusVol, boolean isFull)
	{
		effectsVol = newEffVol;
		musicVol = newMusVol;
		fullscreen = isFull;
		
		
	}
	
	public void readFile()
	{
		
	}
	
}
