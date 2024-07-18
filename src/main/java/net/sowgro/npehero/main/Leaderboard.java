package net.sowgro.npehero.main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class Leaderboard {

    public ObservableList<LeaderboardEntry> entries = FXCollections.observableArrayList();
//    private final JSONFile jsonFile;
    private File file;

    public Leaderboard(File file) {
//        jsonFile = new JSONFile(file);
        this.file = file;
    }

    /**
     * Adds new leaderboardEntry to list and updates json file
     * @param name: The players name
     * @param score The players score
     */
    public void add(String name, int score) {
        new LeaderboardEntry(name, score, ""+ LocalDate.now());
    }

    /**
     * Writes leaderboard to json file
     */
    public void save()
    {
        FileWriter fileWriter;
        try
        {
            fileWriter = new FileWriter(file);
            //write the settings JSONObject instance to the file
            JSONArray jsonArray = new JSONArray();
            for (LeaderboardEntry cur: entries)
            {
                JSONObject obj = new JSONObject();
                obj.put("name", cur.getName());
                obj.put("score", cur.getScore());
                obj.put("date",cur.getDate());
                jsonArray.add(obj);
            }
            jsonArray.writeJSONString(fileWriter);
            fileWriter.flush();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads in json leaderboard and assigns populates list with leaderboardEntries
     */
    public boolean parseLeaderboard()
    {
        boolean isValid = true;
        JSONParser jsonParser = new JSONParser(); //parser to read the file

        try(FileReader reader = new FileReader(file))
        {
            Object obj = jsonParser.parse(reader);

            JSONArray leaderboardStuff = (JSONArray)(obj); //converts read object to a JSONArray

            for (Object cur: leaderboardStuff)
            {
                JSONObject cur2 = (JSONObject) cur;

                String name = (String) cur2.get("name");
                int score = Integer.parseInt(""+cur2.get("score"));
                String date = (String) cur2.get("date");
                entries.add(new LeaderboardEntry(name, score, date));
            }
        }
        catch (Exception e)
        {
            isValid = false;
            e.printStackTrace();
        }
        return isValid;
    }


}
