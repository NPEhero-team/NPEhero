package main;

import java.io.File;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Difficulty 
{
    public String title;
    public ObservableList<LeaderboardEntry> leaderboard = FXCollections.observableArrayList();
    public File notes;

    public String toString()
    {
        return title;
    }
}
