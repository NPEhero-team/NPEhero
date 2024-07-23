package net.sowgro.npehero.main;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

public class Difficulties {

    public ObservableList<Difficulty> list = FXCollections.observableArrayList();
    public ObservableList<Difficulty> validList = FXCollections.observableArrayList();
    {
        list.addListener((ListChangeListener<? super Difficulty>) e -> {
            validList.clear();
            for (Difficulty difficulty : list) {
                if (difficulty.isValid) {
                    validList.add(difficulty);
                }
            }
        });
    }
    Level level;

    public Difficulties(Level level) {
        this.level = level;
    }

    public void read() {
        for(File cur: level.dir.listFiles()) //iterates through all files/folders in /levels/LEVEL
        {
            if (cur.isDirectory()) //all subfolders within a level folder are difficulties
            {
                Difficulty diff = new Difficulty(cur,level);
                diff.readData();
                list.add(diff);
            }
        }
        list.sort(Comparator.naturalOrder());
    }

    /**
     * Removes the difficaulty from the filesystem then reloads the level
     * @param diff: the difficulty to be removed
     */
    public void remove(Difficulty diff)
    {
        File hold = diff.thisDir;
        try {
            Files.walk(hold.toPath())
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
            list.remove(diff);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a difficulty by creating a directory and required files
     * @param text: the name of the directory and default title
     */
    public void add(String text)
    {
        File diffDir = new File(level.dir, text);
        diffDir.mkdirs();
        Difficulty temp = new Difficulty(diffDir,level);
        temp.title = text;
        list.add(temp);
        list.sort(Comparator.naturalOrder());
    }

    public void saveOrder() {
        list.forEach(d -> d.order = list.indexOf(d));
    }






}
