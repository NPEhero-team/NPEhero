package net.sowgro.npehero.editor;

import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import net.sowgro.npehero.Driver;
import net.sowgro.npehero.gameplay.Block;
import net.sowgro.npehero.gui.LevelSurround;
import javafx.scene.layout.VBox;
import net.sowgro.npehero.levelapi.Difficulty;
import net.sowgro.npehero.levelapi.Note;
import net.sowgro.npehero.main.*;

import java.io.IOException;

public class DiffEditor extends Page
{
    Difficulty diff;
    ScrollPane scroll;

    HBox content = new HBox();

    public Page prev;

    public DiffEditor(Difficulty diff, Page prev) {
        this.diff = diff;
        this.prev = prev;

        Label folderNameLabel = new Label("Folder name");
        TextField folderName = new TextField(diff.thisDir.getName());
        folderName.setDisable(true);

        Label titleLabel = new Label("Title");
        TextField title = new TextField(diff.title);

        Button editNotes = new Button("Edit notes");
        editNotes.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            if (diff.level.song == null) {
                Driver.setMenu(new ErrorDisplay("You must import a song file before editing the notes!", this));
                return;
            }
            if (diff.bpm != 0.0) {
                Driver.setMenu(new ErrorDisplay(
                        "Note:\nThe new notes editor does not support bpm and beat based songs. If you continue, the notes will be converted.",
                        this,
                        new NotesEditor2(diff, this)
                ));
                return;
            }
            Driver.setMenu(new NotesEditor2(diff, this));
        });

        Button oldEditNotes = new Button("Edit notes (legacy)");
        oldEditNotes.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            Driver.setMenu(new ErrorDisplay(
                "Warning: \nThe legacy editor will overwrite all existing notes!",
                this,
                new NotesEditor(diff, this))
            );
        });

        Label scoresLable = new Label("Scores");
        Button editScores = new Button("Clear leaderboard");
        editScores.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            diff.leaderboard.entries.clear();
            try {
                diff.leaderboard.save();
            } catch (IOException e) {
                Driver.setMenu(new ErrorDisplay("Failed to clear the leaderboard:\n"+e, this));
            }
        });

        Button playLevel = new Button("Play level");
        playLevel.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            if (diff.isValid() && diff.level.isValid()) {
                Driver.setMenu(new LevelSurround(diff, this));
            }
            else {
                Driver.setMenu(new ErrorDisplay("This Level is not valid!\nCheck that all required fields\nare populated.", this));
            }
        });

        Button save = new Button("Save");
        save.setOnAction(_ -> { //assigns text fields to values
            Sound.playSfx(Sound.FORWARD);
            diff.title = title.getText();
//            diff.bpm = Double.parseDouble(bpm.getText());
//            diff.numBeats = Integer.parseInt(numBeats.getText());
            try {
                diff.writeMetadata();
            } catch (IOException e) {
                //TODO
                throw new RuntimeException(e);
            }
        });

        HBox scrollContent = new HBox();
        scroll = new ScrollPane(scrollContent);
        scroll.setFitToWidth(true);
        scroll.getStyleClass().remove("scroll-pane");
        scroll.getStyleClass().add("box");
//        scroll.setPrefHeight(400);
//        System.out.println("dbg"+scroll.heightProperty());
        // TODO scroll.heightProperty is 0 here until the window is resized, idk what to do
        scroll.prefWidthProperty().bind(scroll.heightProperty().multiply(0.66));

        Pane[] lanes = new Pane[5];
        for (int i = 0; i < lanes.length; i++) {
            lanes[i] = new Pane();
        }

        scrollContent.getChildren().addAll(lanes);
        scrollContent.setSpacing(5);
        scrollContent.setAlignment(Pos.CENTER);

        diff.notes.list.forEach(n -> lanes[n.lane].getChildren().add(drawBlock(n)));

        VBox notePreview = new VBox();

        ValidIndicator validNotes = new ValidIndicator();
        if (diff.notes.list.isEmpty()) {
            validNotes.setInvalid("This difficulty does not contain any notes!");
        }
        HBox notesLabel = new HBox(new Label("Notes"), validNotes);
        Pane scrollHolder = new Pane(scroll);
        scroll.prefHeightProperty().bind(scrollHolder.heightProperty());
        scrollHolder.setPrefHeight(400);
        notePreview.getChildren().addAll(notesLabel, scrollHolder, editNotes, oldEditNotes);
        notePreview.setSpacing(10);

        VBox left = new VBox();
        left.getChildren().addAll(folderNameLabel,folderName,titleLabel,title,scoresLable,editScores,playLevel);
        left.setSpacing(10);

        HBox main = new HBox();
        main.getChildren().addAll(left, notePreview);
        main.setSpacing(30);

        Button exit = new Button();
        exit.setText("Back");
        exit.setOnAction(_ -> {
            Sound.playSfx(Sound.BACKWARD);
            Driver.setMenu(prev);
        });

        HBox bottom = new HBox(exit,save);
        bottom.setSpacing(10);
        bottom.setAlignment(Pos.CENTER);

        VBox centerBox = new VBox();
        centerBox.getChildren().addAll(main, bottom);
        centerBox.setSpacing(10);
        centerBox.setAlignment(Pos.CENTER);

        content.getChildren().add(centerBox);
        content.setAlignment(Pos.CENTER);
    }

    @Override
    public Pane getContent() {
        return content;
    }

    // Duplicates of NotesEditor2 methods, should be made generic and combined
    private Block drawBlock(Note n) {
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

    private DoubleBinding secondToScreenPos(DoubleBinding second) {
        return scroll.heightProperty().multiply(second).multiply(0.9);
    }
}