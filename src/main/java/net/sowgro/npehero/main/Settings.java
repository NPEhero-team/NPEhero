package net.sowgro.npehero.main;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import net.sowgro.npehero.Driver;

public class Settings
{
	public static SimpleDoubleProperty effectsVol = new SimpleDoubleProperty(1);
	public static SimpleDoubleProperty musicVol = new SimpleDoubleProperty(1);
	public static SimpleBooleanProperty enableMenuMusic = new SimpleBooleanProperty(true);
	public static SimpleDoubleProperty guiScale = new SimpleDoubleProperty(1);

	private static final Gson jsonParser = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
	private static final File jsonFile = new File(Driver.BASE_DIR, "settings.json");

	/**
	 * Reads json data from settings.json
	 */
	public static void read() throws Exception {
		@SuppressWarnings("unchecked")
		Map<String, Object> data = jsonParser.fromJson(new FileReader(jsonFile), Map.class);
		if (data == null) {
			data = new HashMap<>();
		}
        effectsVol.set((Double) data.getOrDefault("effectsVol", 1.0));
		musicVol.set((Double) data.getOrDefault("musicVol", 1.0));
		enableMenuMusic.set((Boolean) data.getOrDefault("enableMenuMusic", true));
		guiScale.set((Double) data.getOrDefault("guiScale", 1.0));
	}

	/**
	 * Writes json data to settings.json
	 */
	public static void save() throws IOException {
		Map<String, Object> data = new HashMap<>();
		data.put("effectsVol", effectsVol.get());
		data.put("musicVol", musicVol.get());
		data.put("enableMenuMusic", enableMenuMusic.get());
		data.put("guiScale", guiScale.get());
		FileWriter fileWriter = new FileWriter(jsonFile);
		jsonParser.toJson(data, fileWriter);
		fileWriter.close();
	}
}
