package net.sowgro.npehero.levelapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Leaderboard {

    public final ObservableList<LeaderboardEntry> entries = FXCollections.observableArrayList();
    private final Gson json = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    private final File file;

    public Leaderboard(File file) throws IOException{
        this.file = file;
        read();
    }

    /**
     * Adds new leaderboardEntry to list and updates json file
     * @param name: The players name
     * @param score The players score
     * @throws IOException If there is a problem updating the leaderboard file.
     */
    public void add(String name, int score) throws IOException {
        entries.add(new LeaderboardEntry(name, score, LocalDate.now().toString()));
        save();
    }

    /**
     * Writes leaderboard to json file
     * @throws IOException If there are problems writing to the file.
     */
    public void save() throws IOException {
        file.createNewFile();
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> data = json.fromJson(new FileReader(file), List.class);
        for (LeaderboardEntry cur : entries) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("name", cur.name);
            obj.put("score", cur.score);
            obj.put("date", cur.date);
            data.add(obj);
        }
        FileWriter fileWriter = new FileWriter(file);
        json.toJson(data, fileWriter);
        fileWriter.close();
    }

    /**
     * Reads in json leaderboard and assigns populates list with leaderboardEntries
     * @throws IOException If there are problems reading the file
     */
    public void read() throws IOException {
        if (!file.exists()) {
            return;
        }
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> data = json.fromJson(new FileReader(file), List.class);
        if (data == null) {
            return;
        }
        for (Map<String, Object> cur: data) {
            String name = (String) cur.getOrDefault("name", null);
            int score = (int) (double) cur.getOrDefault("score", -1);
            String date = (String) cur.getOrDefault("date", null);
            if (name == null || score == -1 || date == null) {
                System.out.println("dbg: bad entry skipped");
                continue; // discard invalid entries
            }
            entries.add(new LeaderboardEntry(name, score, date));
        }
    }
}
