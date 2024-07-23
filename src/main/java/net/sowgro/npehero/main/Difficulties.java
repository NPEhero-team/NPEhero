package net.sowgro.npehero.main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

public class Difficulties {

    public ObservableList<Difficulty> list = FXCollections.observableArrayList();
    Level level;

    public Difficulties(Level level) {
        this.level = level;
    }

    public void read() {
        list.clear();
        File[] fileList = level.dir.listFiles();
        if (fileList == null) {
            return;
        }
        for(File cur: fileList) //iterates through all files/folders in /levels/LEVEL
        {
            if (cur.isDirectory()) //all subfolders within a level folder are difficulties
            {
                Difficulty diff = new Difficulty(cur,level);
                list.add(diff);
            }
        }
        list.sort(Comparator.naturalOrder());
    }

    /**
     * Removes the difficaulty from the filesystem then reloads the level
     * @param diff: the difficulty to be removed
     */
    public void remove(Difficulty diff) throws IOException
    {
        File hold = diff.thisDir;
        Files.walk(hold.toPath())
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        list.remove(diff);
    }

    /**
     * Adds a difficulty by creating a directory and required files
     * @param text: the name of the directory and default title
     */
    public void add(String text) throws IOException {
        File diffDir = new File(level.dir, text);
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

    public void saveOrder() {
        for (Difficulty d : list) {
            d.order = list.indexOf(d);
            d.write();
        }
    }

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
