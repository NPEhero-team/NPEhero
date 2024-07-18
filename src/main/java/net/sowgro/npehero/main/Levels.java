package net.sowgro.npehero.main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class Levels
{
    private static final File dir = new File("levels");
    public static ObservableList<Level> list = FXCollections.observableArrayList();
    public static ObservableList<Level> validList = FXCollections.observableArrayList();
    static {
        list.addListener((ListChangeListener<? super Level>) e -> {
            validList.clear();
            for (Level level : list) {
                if (level.isValid) {
                    validList.add(level);
                }
            }
        });
    }

    /**
     * Reads contents of folder and creates cooresponding levels
     */
    public static void readData()
    {
        for (File cur: dir.listFiles()) //iterates through all files/folders in levels
        {
            Level level = new Level(cur);
            level.readData();
            list.add(level);
        }
    }

    /**
     * Adds a level to the list by creating a directory and required files
     * @param text: the name of the directory and default title
     */
    public static void add(String text)
    {
        File levelDir = new File(dir,text);
        levelDir.mkdirs();
        Level temp = new Level(levelDir);
        temp.title = text;
        list.add(temp);
    }

    /**
     * Removes level from the filesystem then reloads this levelController
     * @param level: the level to be removed
     */
    public static void remove(Level level)
    {
        File hold = level.dir;
        list.remove(level);

        try {
            Files.walk(hold.toPath())
            .sorted(Comparator.reverseOrder())
            .map(Path::toFile)
            .forEach(File::delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
        list.remove(level);
    }
}