package main;

import java.io.File;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Difficulty 
{
    public String title;
    public ObservableList<LeaderboardEntry> leaderboard = FXCollections.observableArrayList();
    public File notes;

    public void parseMetadata(File file) {
        title = "placeholderDiff";
    }

    public void parseLeaderboard(File file) {
        //and here
        leaderboard.add(new LeaderboardEntry("placeholderScore", 0, "0/0/0"));
    }
}
