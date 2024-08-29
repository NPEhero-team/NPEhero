package net.sowgro.npehero.levelapi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Stores a list of all the levels
 */
public class Levels {

    public static final ObservableList<Level> list = FXCollections.observableArrayList();
    public static final HashMap<String, Exception> problems = new HashMap<>();

    public static final File dir = new File("levels");

    /**
     * Reads contents of the levels folder and creates a level form each subfolder
     * <p>
     * All subfolders in the levels folder are assumed to be levels
     * @throws FileNotFoundException If the levels folder is missing.
     * @throws IOException If there is a problem reading in the levels.
     */
    public static void readData() throws IOException {
        list.clear();
        File[] fileList = dir.listFiles();
        if (fileList == null) {
            throw new FileNotFoundException();
        }
        for (File file: fileList) {
            try {
                Level level = new Level(file);
                list.add(level);
            } catch (IOException e) {
                problems.put(file.getName(), e);
                e.printStackTrace();
            }
        }
        list.sort(Comparator.naturalOrder());
    }

    /**
     * Creates a subfolder in the levels folder for the new level then creates the level with it
     * @param text: the name of the directory and default title
     * @throws IOException if there was an error adding the level
     */
    public static void add(String text) throws IOException {
        File levelDir = new File(dir, text.toLowerCase().replaceAll("\\W+", "-"));
        if (levelDir.exists()) {
            throw new FileAlreadyExistsException(levelDir.getName());
        }
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
     * @throws IOException If there is a problem deleting the level
     */
    public static void remove(Level level) throws IOException {
        File hold = level.dir;
        Files.walk(hold.toPath())
            .sorted(Comparator.reverseOrder())
            .map(Path::toFile)
            .forEach(File::delete);
        list.remove(level);
    }

    /**
     * Gets a list of only the valid levels.
     * @return A list of the valid levels.
     */
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