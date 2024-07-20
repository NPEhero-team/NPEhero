package net.sowgro.npehero.devmenu;

import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
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
import net.sowgro.npehero.main.Sound;
import net.sowgro.npehero.main.ValidIndicator;

public class DiffEditor extends Pane
{
    Difficulty diff;
    ScrollPane scroll;

    public Pane prev;

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
            if (diff.level.song == null) {
                Driver.setMenu(new ErrorDisplay("You must import a song file before editing the notes!", this));
            }
            else {
                Driver.setMenu(new NotesEditor2(diff, this));
            }
        });

        Button oldEditNotes = new Button("Edit notes (legacy)");
        oldEditNotes.setOnAction(_ -> {
            Driver.setMenu(new ErrorDisplay(
                    "Warning: \nThe legacy editor will overwrite all existing notes!",
                    this,
                    new NotesEditor(diff, this))
            );
        });

        Button editScores = new Button("Clear leaderboard");
        editScores.setOnAction(e -> diff.leaderboard.entries.clear());

        Button playLevel = new Button("Play level");
        playLevel.setOnAction(e -> {
            if (diff.isValid && diff.level.isValid) {
                Driver.setMenu(new LevelSurround(diff.level, diff, this));
            }
            else {
                Driver.setMenu(new ErrorDisplay("This Level is not valid!\nCheck that all required fields\nare populated.", this));
            }
        });

        Button save = new Button("Save");
        save.setOnAction(e -> { //assigns text fields to values
            diff.title = title.getText();
//            diff.bpm = Double.parseDouble(bpm.getText());
//            diff.numBeats = Integer.parseInt(numBeats.getText());
            diff.priority = Integer.parseInt(priority.getText());
            diff.write();
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

        VBox notePreview = new VBox();

        ValidIndicator validNotes = new ValidIndicator();
        if (diff.notes.list.isEmpty()) {
            validNotes.setInvalid("This difficulty does not contain any notes!");
        }
        HBox notesLabel = new HBox(new Label("Notes"), validNotes);
        notePreview.getChildren().addAll(notesLabel, scroll, editNotes, oldEditNotes);
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
            Sound.playSfx(Sound.BACKWARD);
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