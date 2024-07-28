package net.sowgro.npehero.levelapi;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Responsible for the list of difficulties in a level
 */
public class Difficulties {

    public final ObservableList<Difficulty> list = FXCollections.observableArrayList();
    public final HashMap<String, Exception> problems = new HashMap<>();

    private final Level level;

    /**
     * Creates a new Difficulties object
     * @param level the file path of the level
     * @throws IOException If there is a problem reading in the difficulties
     */
    public Difficulties(Level level) throws IOException {
        this.level = level;
        read();
    }

    /**
     * Loads difficulties
     * <p>
     * Creates difficulty objects out of each subfolder in the level and adds it to the list.
     * @throws IOException If there is a problem reading in the difficulties
     */
    public void read() throws IOException {
        list.clear();
        File[] fileList = level.dir.listFiles();
        if (fileList == null) {
            throw new FileNotFoundException();
        }
        for(File cur: fileList) {
            if (cur.isDirectory()) {
                try {
                    Difficulty diff = new Difficulty(cur, level);
                    list.add(diff);
                } catch (IOException e) {
                    problems.put("", e);
                    e.printStackTrace();
                }
            }
        }
        list.sort(Comparator.naturalOrder());
    }

    /**
     * Removes a difficulty
     * <p>
     * Recursively deletes the folder and removes it from the list
     * @param diff: The difficulty to remove.
     * @throws IOException If there is a problem removing the difficulty.
     */
    public void remove(Difficulty diff) throws IOException {
        File hold = diff.thisDir;
        Files.walk(hold.toPath())
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        list.remove(diff);
    }

    /**
     * Adds a difficulty
     * <p>
     * Creates the directory and required files
     * @param text The name of the directory
     * @throws IOException If there is a problem adding the level
     */
    public void add(String text) throws IOException {
        File diffDir = new File(level.dir, text.toLowerCase().replaceAll("\\W+", "-"));
        if (diffDir.exists()) {
            throw new FileAlreadyExistsException(diffDir.getName());
        }
        if (diffDir.mkdirs()) {
            Difficulty temp = new Difficulty(diffDir, level);
            temp.title = text;
            list.add(temp);
            list.sort(Comparator.naturalOrder());
        }
        else {
            throw new IOException();
        }
    }

    /**
     * Saves the order of the difficulties in the list
     * <p>
     * Updates the order variable of each difficulty in the list to match their index in the list
     * @throws IOException If there is a problem saving the difficulty's metadata file
     */
    public void saveOrder() throws IOException {
        for (Difficulty d : list) {
            d.order = list.indexOf(d);
            d.writeMetadata();
        }
    }

    /**
     * Get a list of only the valid difficulties in the level.
     * @return A list of the valid difficulties.
     */
    public List<Difficulty> getValidList() {
        ObservableList<Difficulty> validList = FXCollections.observableArrayList();
        for (Difficulty difficulty : list) {
            if (difficulty.isValid()) {
                validList.add(difficulty);
            }
        }
        return validList;
    }
}
