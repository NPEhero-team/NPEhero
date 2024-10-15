package net.sowgro.npehero.editor;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import net.sowgro.npehero.Driver;
import net.sowgro.npehero.gui.LeaderboardView;
import net.sowgro.npehero.gui.LevelSurround;
import javafx.scene.layout.VBox;
import net.sowgro.npehero.levelapi.Difficulty;
import net.sowgro.npehero.main.*;

import java.io.IOException;

public class DiffEditor extends Page
{
    private final TextField titleEntry;
    private final Difficulty diff;
    private final ValidIndicator validNotes = new ValidIndicator();

    private final HBox content = new HBox();

    record OptionEntry(String label, Node action, ValidIndicator vi) {
        OptionEntry(String label, Node action) {
            this(label, action, null);
        }
    }

    public DiffEditor(Difficulty diff, Page prev) {
        this.diff = diff;

        TextField folderName = new TextField(diff.thisDir.getName());
        folderName.setDisable(true);

        titleEntry = new TextField(diff.title);

        Button clearLeaderboard = new Button("Clear");
        clearLeaderboard.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            diff.leaderboard.entries.clear();
            try {
                diff.leaderboard.save();
            } catch (IOException e) {
                Driver.setMenu(new ErrorDisplay("Failed to clear the leaderboard:\n"+e, this));
            }
            clearLeaderboard.setDisable(diff.leaderboard.entries.isEmpty());
        });
        clearLeaderboard.setDisable(diff.leaderboard.entries.isEmpty());

        Button viewLeaderboard = new Button("View");
        viewLeaderboard.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            Driver.setMenu(new LeaderboardView(diff, this));
        });

        HBox leaderboardActions = new HBox(viewLeaderboard, clearLeaderboard);
        leaderboardActions.setSpacing(10);

        Button playLevel = new Button("Play");
        playLevel.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            if (diff.isValid() && diff.level.isValid()) {
                Driver.setMenu(new LevelSurround(diff, this));
            }
            else {
                Driver.setMenu(new ErrorDisplay("This Level is not valid!\nCheck that all required fields are populated.", this));
            }
        });

        Button editNotes = new Button("Edit Notes");
        editNotes.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            if (diff.level.song == null) {
                Driver.setMenu(new ErrorDisplay("You must import a song file before editing the notes!", this));
                return;
            }
//            if (diff.bpm != 0.0) {
//                Driver.setMenu(new ErrorDisplay(
//                        "Note:\nThe new notes editor does not support bpm and beat based songs. If you continue, the notes will be converted.",
//                        this,
//                        new NotesEditor2(diff, this)
//                ));
//                return;
//            }
            Driver.setMenu(new NotesEditor2(diff, this));
        });

        Button oldEditNotes = new Button("Edit legacy");
        oldEditNotes.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            Driver.setMenu(new ErrorDisplay(
                "Warning: \nThe legacy editor will overwrite all existing notes!",
                this,
                new NotesEditor(diff, this))
            );
        });

        HBox noteActions = new HBox(playLevel, editNotes/*, oldEditNotes*/);
        noteActions.setSpacing(10);

        OptionEntry[] optionsEntries = {
                new OptionEntry("Folder name", folderName),
                new OptionEntry("Title", titleEntry),
                new OptionEntry("Scores", leaderboardActions),
                new OptionEntry("Notes", noteActions, validNotes),
        };
        VBox options = new VBox();
        for (OptionEntry option : optionsEntries) {
            Label label = new Label(option.label);
            HBox labelBox = new HBox(label);
            labelBox.setSpacing(5);
            if (option.vi != null) {
                labelBox.getChildren().add(option.vi);
            }
            VBox optionBox = new VBox(labelBox, option.action);
            optionBox.setPadding(new Insets(10));
            optionBox.setSpacing(5);
            optionBox.getStyleClass().add("box");
            options.getChildren().add(optionBox);
        }
        options.setSpacing(10);

        ScrollPane optionsScroll = new ScrollPane(options);
        optionsScroll.getStyleClass().remove("scroll-pane");
        optionsScroll.setFitToWidth(true);
        optionsScroll.setPrefWidth(500);

        HBox main = new HBox();
        main.getChildren().addAll(optionsScroll);
        main.setSpacing(10);
        main.maxWidthProperty().bind(content.widthProperty().multiply(0.95));

        Button exit = new Button();
        exit.setText("Back");
        exit.setOnAction(_ -> {
            Sound.playSfx(Sound.BACKWARD);
            Driver.setMenu(prev);
        });

        HBox bottom = new HBox(exit);
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

    @Override
    public void onView() {
        update();
    }

    @Override
    public void onLeave() {
        diff.title = titleEntry.getText();
        try {
            diff.writeMetadata();
        } catch (IOException e) {
            e.printStackTrace(); //TODO
        }
    }

    private void update() {
        if (diff.notes.list.isEmpty()) {
            validNotes.setInvalid("This difficulty does not contain any notes!");
        } else {
            validNotes.setValid();
        }
    }
}