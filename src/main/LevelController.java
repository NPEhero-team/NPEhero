package main;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

public class LevelController
{
    public static ObservableList<Level> levelList = FXCollections.observableArrayList();

    public LevelController()
    {
        Level testLevel = new Level();
        testLevel.title = "test level class";
        testLevel.desc = "this level is being used to test the LevelController class";
        testLevel.aritst = "testArtist";
        testLevel.setColors(Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.PURPLE);
        testLevel.diffList.add("Hello");
        testLevel.diffList.add("Easy");
        testLevel.diffList.add("Med");
        levelList.add(testLevel);

        Level testLevel2 = new Level();
        testLevel2.title = "another one";
        testLevel2.desc = "it can say something else too";
        testLevel2.aritst = "testArtist2";
        testLevel2.setColors(Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.PURPLE);
        testLevel2.diffList.add("Hard");
        testLevel2.diffList.add("Easy");
        testLevel2.diffList.add("Med");
        testLevel2.diffList.add("insane+++");
        levelList.add(testLevel2);
        
    }

}