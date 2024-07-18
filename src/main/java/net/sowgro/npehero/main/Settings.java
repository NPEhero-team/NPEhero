package net.sowgro.npehero.main;

import java.io.File;
import java.io.IOException;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Settings
{
	public static SimpleDoubleProperty effectsVol = new SimpleDoubleProperty(1);
	public static SimpleDoubleProperty musicVol = new SimpleDoubleProperty(1);
	public static SimpleBooleanProperty enableMenuMusic = new SimpleBooleanProperty(true);

	private static final JSONFile jsonFile = new JSONFile(new File("settings.json"));

	/**
	 * Reads json data from settings.json
	 */
	public static void read()
	{
        try {
            jsonFile.read();
        } catch (Exception e) {
			e.printStackTrace();
            System.out.println("Error reading settings.json");
        }
        effectsVol.set(jsonFile.getDouble("effectsVol", 1));
		musicVol.set(jsonFile.getDouble("musicVol", 1));
		enableMenuMusic.set(jsonFile.getBoolean("enableMenuMusic", true));
	}

	/**
	 * Writes json data to settings.json
	 */
	public static void save()
	{
		jsonFile.set("effectsVol", effectsVol.get());
		jsonFile.set("musicVol", musicVol.get());
		jsonFile.set("enableMenuMusic", enableMenuMusic.get());
		try {
			jsonFile.write();
		}
		catch (IOException e) {
			System.out.println("Error writing settings.json");
		}
	}
}
