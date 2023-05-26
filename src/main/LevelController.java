package main;

import java.io.File;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

public class LevelController
{
    public static ObservableList<Level> levelList = FXCollections.observableArrayList();

    public LevelController()
    {
        for (File curFileInLevels: new File("levels").listFiles()) //iterates through all files/folders in src/assets/levels
        {
            Level level = new Level();
            for(File curFileInCurLevel: curFileInLevels.listFiles()) //iterates through all files/folders in src/assets/levels/LEVEL
            {
                if (curFileInCurLevel.isDirectory()) //all subfolders within a level folder are difficulties
                {
                    Difficulty diff = new Difficulty();
                    for(File curFileInCurDiff: curFileInCurLevel.listFiles()) //iterates through all files/folders in src/assets/levels/LEVEL/DIFFICULTY
                    {
                        if (curFileInCurDiff.getName().equals("metadata.json"))
                        {
                            diff.parseMetadata(curFileInCurDiff);
                        }
                        if (curFileInCurDiff.getName().equals("leaderboard.json"))
                        {
                            diff.parseLeaderboard(curFileInCurDiff);
                        }
                        if (curFileInCurDiff.getName().equals("notes.txt"))
                        {
                            diff.notes = curFileInCurDiff;
                        }
                    }
                    level.diffList.add(diff);
                }

                if (curFileInCurLevel.getName().equals("preview.png"))
                {
                    level.preview = new Image(curFileInCurLevel.toURI().toString());
                }

                if (curFileInCurLevel.getName().equals("metadata.json"))
                {
                    level.parseMetadata(curFileInCurLevel);
                }

                if (curFileInCurLevel.getName().equals("background.png"))
                {
                    level.background = new Image(curFileInCurLevel.toURI().toString());
                }
            }
            levelList.add(level);
        }
    }
}