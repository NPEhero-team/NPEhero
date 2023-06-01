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
        readData();
    }

    public void removeLevel(Level level)
    {
        //soon
    }

    public void renameLevel(Level level, String newName)
    {
        //soon
    }
}