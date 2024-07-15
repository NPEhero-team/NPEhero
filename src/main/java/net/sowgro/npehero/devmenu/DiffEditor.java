package net.sowgro.npehero.devmenu;

import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import net.sowgro.npehero.Driver;
import net.sowgro.npehero.gameplay.Block;
import net.sowgro.npehero.gui.LevelSurround;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import net.sowgro.npehero.main.Difficulty;
import net.sowgro.npehero.main.Note;
import net.sowgro.npehero.main.SoundController;

public class DiffEditor extends Pane
{
    Difficulty diff;
    ScrollPane scroll;

    public Pane prev;
    /*
     * this class is a layout class, most of its purpose is to place UI elements like Buttons within Panes like VBoxes.
     * the creation of these UI elements are mostly not commented due to their repetitive and self explanatory nature.
     * style classes are defined in the style.css file.
     */
    public DiffEditor(Difficulty diff, Pane prev)
    {
        this.diff = diff;
        this.prev = prev;

        Text folderNameLabel = new Text("Folder name");
        TextField folderName = new TextField(diff.thisDir.getName());
        folderName.setDisable(true);

        Text titleLabel = new Text("Title");
        TextField title = new TextField(diff.title);

        Text priorityLabel = new Text("Order (lower first)");
        TextField priority = new TextField(diff.priority+"");

        Button editNotes = new Button("Edit notes");
        editNotes.setOnAction(e -> {
            Driver.setMenu(new NotesEditor2(diff, this));
        });

        Button oldEditNotes = new Button("Edit notes (legacy)");
        oldEditNotes.setOnAction(_ -> {
            try {
                Driver.setMenu(new NotesEditor(diff, this));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        Button editScores = new Button("Clear leaderboard");
        editScores.setOnAction(e -> diff.getLeaderboard().clear());

        Button playLevel = new Button("Play level");
        playLevel.setOnAction(e -> Driver.setMenu(new LevelSurround(diff.level, diff, this)));

        Button save = new Button("Save");
        save.setOnAction(e -> { //assigns text fields to values
            diff.title = title.getText();
//            diff.bpm = Double.parseDouble(bpm.getText());
//            diff.numBeats = Integer.parseInt(numBeats.getText());
            diff.priority = Integer.parseInt(priority.getText());
            diff.writeMetadata();
        });

        HBox content = new HBox();
        ScrollPane scroll = new ScrollPane(content);
        this.scroll = scroll;
        scroll.getStyleClass().remove("scroll-pane");
        scroll.getStyleClass().add("box");
        scroll.setPrefHeight(400);

        Pane[] lanes = new Pane[5];
        for (int i = 0; i < lanes.length; i++) {
            lanes[i] = new Pane();
        }

        content.getChildren().addAll(lanes);

        diff.notes.list.forEach(n -> lanes[n.lane].getChildren().add(drawNote(n)));

        VBox notePreview = new VBox(scroll);
        notePreview.getChildren().addAll(editNotes,oldEditNotes);
        notePreview.setSpacing(10);

        VBox left = new VBox();
        left.getChildren().addAll(folderNameLabel,folderName,titleLabel,title,priorityLabel,priority,editScores,playLevel,save);
        left.setSpacing(10);

        HBox main = new HBox();
        main.getChildren().addAll(left, notePreview);
        main.setSpacing(30);

        Button exit = new Button();
        exit.setText("Back");
        exit.setOnAction(e -> {
            SoundController.playSfx(SoundController.BACKWARD);
            Driver.setMenu(prev);
        });

        VBox centerBox = new VBox();
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setSpacing(10);
        centerBox.getChildren().addAll(main,exit);
        centerBox.setMinWidth(400);

        HBox rootBox = new HBox();
        rootBox.prefWidthProperty().bind(super.prefWidthProperty());
        rootBox.prefHeightProperty().bind(super.prefHeightProperty());
        rootBox.getChildren().add(centerBox);
        rootBox.setAlignment(Pos.CENTER);

        super.getChildren().add(rootBox);
    }

    // Duplicates of NotesEditor2 methods, should be made generic and combined
    private Block drawNote(Note n) {
        Color color = diff.level.colors[n.lane];
        Block b = new Block(color,20, 20, 5, false, n);
        b.heightProperty().bind(scroll.widthProperty().divide(8));
        b.widthProperty().bind(scroll.widthProperty().divide(8));
        b.arcHeightProperty().bind(scroll.widthProperty().divide(25));
        b.arcWidthProperty().bind(scroll.widthProperty().divide(25));
        b.strokeWidthProperty().bind(scroll.widthProperty().divide(120));
        b.layoutYProperty().bind(secondToScreenPos(n.time.add(0)));
        return b;
    }

    private DoubleBinding secondToScreenPos(double second) {
        return scroll.heightProperty().multiply(second * 0.9);
    }

    private DoubleBinding secondToScreenPos(DoubleBinding second) {
        return scroll.heightProperty().multiply(second).multiply(0.9);
    }
}