package net.sowgro.npehero.main;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;
import net.sowgro.npehero.Driver;

public class Settings
{
	public static final Color[] DEFAULT_DEFAULT_COLORS = {Color.RED,Color.BLUE,Color.GREEN,Color.PURPLE,Color.YELLOW};

	public static SimpleDoubleProperty effectsVol = new SimpleDoubleProperty(1);
	public static SimpleDoubleProperty musicVol = new SimpleDoubleProperty(1);
	public static SimpleBooleanProperty enableMenuMusic = new SimpleBooleanProperty(true);
	public static SimpleDoubleProperty guiScale = new SimpleDoubleProperty(1);
	public static Color[] defaultColors = Arrays.copyOf(DEFAULT_DEFAULT_COLORS, 5);
	public static boolean forceDefaultColors = false;
	public static double gameOpacity = 0.0;

	private static final Gson jsonParser = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
	private static final File jsonFile = new File(Driver.BASE_DIR, "settings.json");

	/**
	 * Reads json data from settings.json
	 */
	public static void read() throws Exception {
		@SuppressWarnings("unchecked")
		Map<String, Object> data = jsonParser.fromJson(new FileReader(jsonFile), Map.class);
		if (data == null) {
			return;
		}
        effectsVol.set((Double) data.getOrDefault("effectsVol", 1.0));
		musicVol.set((Double) data.getOrDefault("musicVol", 1.0));
		enableMenuMusic.set((Boolean) data.getOrDefault("enableMenuMusic", true));
		guiScale.set((Double) data.getOrDefault("guiScale", 1.0));
		defaultColors[0] = colorGetOrDefault(data, "colorOverride1", defaultColors[0]);
		defaultColors[1] = colorGetOrDefault(data, "colorOverride2", defaultColors[1]);
		defaultColors[2] = colorGetOrDefault(data, "colorOverride3", defaultColors[2]);
		defaultColors[3] = colorGetOrDefault(data, "colorOverride4", defaultColors[3]);
		defaultColors[4] = colorGetOrDefault(data, "colorOverride5", defaultColors[4]);
		forceDefaultColors = (Boolean) data.getOrDefault("enableColorOverride", false);
		gameOpacity = (Double) data.getOrDefault("gameOpacity", gameOpacity);
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
		data.put("colorOverride1", defaultColors[0].toString());
		data.put("colorOverride2", defaultColors[1].toString());
		data.put("colorOverride3", defaultColors[2].toString());
		data.put("colorOverride4", defaultColors[3].toString());
		data.put("colorOverride5", defaultColors[4].toString());
		data.put("enableColorOverride", forceDefaultColors);
		data.put("gameOpacity", gameOpacity);
		FileWriter fileWriter = new FileWriter(jsonFile);
		jsonParser.toJson(data, fileWriter);
		fileWriter.close();
	}

	public static Color colorGetOrDefault(Map<String, Object> source, String key, Color defaultValue) {
		try {
			return Color.web((String) source.getOrDefault(key, defaultValue));
		} catch (Exception e) {
			return null;
		}
	}
}
