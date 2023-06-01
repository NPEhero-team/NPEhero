package main;

import java.io.File;
import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LevelController
{
    File thisDir = new File("levels");
    public static ObservableList<Level> levelList;

    public LevelController()
    {
        readData();
    }

    public void readData()
    {
        levelList = FXCollections.observableArrayList();
        for (File cur: thisDir.listFiles()) //iterates through all files/folders in levels
        {
            Level level = new Level(cur);
            level.readData();
            levelList.add(level);
        }
    }

    public void addLevel(String text) 
    {
        File levelDir = new File(thisDir,text);
        levelDir.mkdirs();
        File metadataDir = new File(levelDir, "metadata.json");
        try 
        {
            metadataDir.createNewFile();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        Level temp = new Level(levelDir);
        temp.setTitle(text);
        temp.writeMetadata();
        readData();
    }
}