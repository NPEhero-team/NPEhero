package main;

import java.io.File;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Difficulty 
{
    public String title;
    private ObservableList<LeaderboardEntry> leaderboard = FXCollections.observableArrayList();
    public File notes;
    public int bpm;
    public File song;
    public int numBeats;

    public void parseMetadata(File file) {
        //hi zach put json reader stuff here
        title = "placeholderDiff";
    }

    public void parseLeaderboard(File file) {
        //and here
        leaderboard.add(new LeaderboardEntry("placeholderScore", 0, "0/0/0"));
    }

    public void addToLeaderboard(String name, int score) {
        leaderboard.add(new LeaderboardEntry(name, score, ""+LocalDate.now())); //do not delete this tho its not a placeholder
        //and make this write to the json also
    }

    public ObservableList<LeaderboardEntry> getLeaderboard() {
        return leaderboard;
    }
}
