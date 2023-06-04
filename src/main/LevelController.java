package main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LevelController
{
    File thisDir = new File("levels");
    private static ObservableList<Level> levelList;
    private static ObservableList<Level> validLevelList;

    /**
     * Creates a levelController, which holds all the levels
     */
    public LevelController()
    {
        readData();
    }

    /**
     * Reads contents of folder and creates cooresponding levels
     */
    public void readData()
    {
        levelList = FXCollections.observableArrayList();
        validLevelList = FXCollections.observableArrayList();
        for (File cur: thisDir.listFiles()) //iterates through all files/folders in levels
        {
            Level level = new Level(cur);
            level.readData();
            levelList.add(level);
            if (level.isValid())
            {
                validLevelList.add(level);
            }
        }
    }

    /**
     * Adds a level to the list by creating a directory and required files
     * @param text: the name of the directory and default title
     */
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

    /**
     * Removes level from the filesystem then reloads this levelController
     * @param level: the level to be removed
     */
    public void removeLevel(Level level)
    {
        File hold = level.thisDir;
        levelList.remove(level);

        try {
            Files.walk(hold.toPath())
            .sorted(Comparator.reverseOrder())
            .map(Path::toFile)
            .forEach(File::delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
        readData();
    }

    public static ObservableList<Level> getLevelList() {
        return levelList;
    }

    public static ObservableList<Level> getValidLevelList() {
        return validLevelList;
    }
}