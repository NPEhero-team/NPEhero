package net.sowgro.npehero.main;



import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;

/**
 * An ergonomic JSON API wrapper inspired by the Bukkit YAML API
 */
public class JSONFile {

    private final File file;
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectNode writeNode = objectMapper.createObjectNode();
    JsonNode readNode;

    public JSONFile(File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.file = file;
    }

    public String getString(String key, String def) {
        if (!readNode.has(key)) {
            return def;
        }
        return readNode.get(key).asText();
    }

    public int getInt(String key, int def) {
        if (!readNode.has(key)) {
            return def;
        }
        try {
            return Integer.parseInt(readNode.get(key).asText());
        }
        catch (NumberFormatException e) {
            return def;
        }
    }

    public double getDouble(String key, double def) {
        if (!readNode.has(key)) {
            return def;
        }
        try {
            return Double.parseDouble(readNode.get(key).asText());
        }
        catch (NumberFormatException e) {
            return def;
        }
    }

    public boolean getBoolean(String key, boolean def) {
        if (!readNode.has(key)) {
            return def;
        }
        try {
            return Boolean.parseBoolean(readNode.get(key).asText());
        }
        catch (NumberFormatException e) {
            return def;
        }
    }

    public void set(String key, String value) {
        if (value == null) {
            return;
        }
        writeNode.put(key, value);
    }

    public void set(String key, int value) {
        writeNode.put(key, value);
    }

    public void set(String key, double value) {
        writeNode.put(key, value);
    }

    public void set(String key, boolean value) {
        writeNode.put(key, value);
    }

    public boolean containsKey(String key) {
        return writeNode.has(key);
    }

    public void read() throws Exception {
        if (file.length() == 0) {
            readNode = objectMapper.createObjectNode();
            return;
        }
        readNode = objectMapper.readTree(file);
    }

    public void write() throws IOException {
        objectMapper.writeValue(file, writeNode);
    }

}
