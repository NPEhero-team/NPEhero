package net.sowgro.npehero.main;

import net.sowgro.npehero.Driver;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;

/**
 * An ergonomic JSON API wrapper inspired by the Bukkit YAML API
 */
public class JSONFile {

    private final File file;
    private JSONObject jsonObject = new JSONObject();

    public JSONFile(File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.file = file;
    }

    public String getString(String key, String def) {
        if (!jsonObject.containsKey(key)) {
            return def;
        }
        return jsonObject.get(key).toString();
    }

    public int getInt(String key, int def) {
        if (!jsonObject.containsKey(key)) {
            return def;
        }
        try {
            return Integer.parseInt(jsonObject.get(key).toString());
        }
        catch (NumberFormatException e) {
            return def;
        }
    }

    public double getDouble(String key, double def) {
        if (jsonObject.containsKey(key)) {
            try {
                return Double.parseDouble(jsonObject.get(key).toString());
            }
            catch (NumberFormatException e) {
                return def;
            }
        }
        else {
            return def;
        }
    }

    public boolean getBoolean(String key, boolean def) {
        if (!jsonObject.containsKey(key)) {
            return def;
        }
        try {
            return Boolean.parseBoolean(jsonObject.get(key).toString());
        }
        catch (NumberFormatException e) {
            return def;
        }
    }

    public void set(String key, Object value) {
        if (value == null) {
            return;
        }
        jsonObject.put(key, value);
    }

    public boolean containsKey(String key) {
        return jsonObject.containsKey(key);
    }

    public void read() throws Exception {
        try {
            if (file.length() == 0) {
                return;
            }
            FileReader fileReader = new FileReader(file);
            jsonObject = (JSONObject) new JSONParser().parse(fileReader);
        }
        catch (Exception e) {
            throw e;
        }
    }

    public void write() throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        jsonObject.writeJSONString(fileWriter);
        fileWriter.close();
    }

}
