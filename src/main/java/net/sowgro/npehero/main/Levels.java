package net.sowgro.npehero.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class Levels
{
    private static final File dir = new File("levels");
    public static ObservableList<Level> list = FXCollections.observableArrayList();

    /**
     * Reads contents of the levels folder and creates a level form each subfolder
     * All subfolders in the levels folder are assumed to be levels
     * @throws FileNotFoundException when the levels folder is not present
     */
    public static void readData() throws FileNotFoundException
    {
        list.clear();
        File[] fileList = dir.listFiles();
        if (fileList == null) {
            throw new FileNotFoundException();
        }
        for (File file: fileList)
        {
            Level level = new Level(file);
            level.readData();
            list.add(level);
        }
        list.sort(Comparator.naturalOrder());
    }

    /**
     * Creates a subfolder in the levels folder for the new level then creates the level with it
     * @param text: the name of the directory and default title
     * @throws IOException if there was an error adding the level
     */
    public static void add(String text) throws IOException
    {
        File levelDir = new File(dir,text);
        if (levelDir.mkdirs()) {
            Level temp = new Level(levelDir);
            temp.title = text;
            list.add(temp);
        }
        else {
            throw new IOException();
        }
    }

    /**
     * Removes level from the filesystem then reloads this levelController
     * @param level: the level to be removed
     * @throws IOException if there was an error deleting the level
     */
    public static void remove(Level level) throws IOException
    {
        File hold = level.dir;
        list.remove(level);

        // delete files recursively
        // TODO clean this up
        Files.walk(hold.toPath())
            .sorted(Comparator.reverseOrder())
            .map(Path::toFile)
            .forEach(File::delete);

        list.remove(level);
    }

    public static ObservableList<Level> getValidList() {
        ObservableList<Level> validList = FXCollections.observableArrayList();
        for (Level level : list) {
            if (level.isValid()) {
                validList.add(level);
            }
        }
        return validList;
    }
}