package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class LevelController
{
    public static ObservableList<Level> levelList = FXCollections.observableArrayList();

    public LevelController()
    {

        Difficulty d1 = new Difficulty();
        d1.title = "Easy";
        LeaderboardEntry lb = new LeaderboardEntry("t-bone", 1000, "DATE");
        //lb.setName("t-bone");
        //lb.setScore(1000);
        d1.leaderboard.add(lb);

        Difficulty d2 = new Difficulty();
        d2.title = "Medium";
        Difficulty d3 = new Difficulty();
        d3.title = "Hard";
        Difficulty d4 = new Difficulty();
        d4.title = "Expert";
        Difficulty d5 = new Difficulty();
        d5.title = "Impossible";

        Level testLevel = new Level("test level class","testArtist");
        //testLevel.setTitle("test level class");
        testLevel.desc = "this level is being used to test the LevelController class";
        //testLevel.setAritst("testArtist");
        testLevel.setColors(Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.PURPLE);
        testLevel.diffList.add(d1);
        testLevel.diffList.add(d2);
        levelList.add(testLevel);

        Level testLevel2 = new Level("another one", "testArtist2");
        //testLevel2.setTitle("another one");
        testLevel2.desc = "it can say something else too";
        //testLevel2.setAritst("testArtist2");
        testLevel2.setColors(Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.PURPLE);
        testLevel2.diffList.add(d2);
        testLevel2.diffList.add(d3);
        testLevel2.diffList.add(d4);
        testLevel2.preview = new Image("assets/pico.png");
        levelList.add(testLevel2);
         
        
    }

    public void readInLevels()
    {
        //oh boy time for a flowchart
    } 



}