package net.sowgro.npehero.editor;

import java.io.IOException;
import java.util.Objects;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import net.sowgro.npehero.Driver;
import net.sowgro.npehero.levelapi.Level;
import net.sowgro.npehero.main.*;

public class LevelEditor extends Page
{

    private final ValidIndicator songValid = new ValidIndicator();
    private final ValidIndicator diffsInvalid = new ValidIndicator();
    private final TextField titleEntry;
    private final TextField artistEntry;
    private final TextField descEntry;
    private final ColorPicker[] colorsPickers;
    private final Level level;

    private final HBox content = new HBox();
    private final CheckBox useCustomColors;

    record OptionEntry(String label, Node action, ValidIndicator vi) {
        OptionEntry(String label, Node action) {
            this(label, action, null);
        }
    }

    public LevelEditor(Level level, Page prev) {
        this.level = level;

        TextField folderName = new TextField();
        if (level.dir != null) {
            folderName.setText(level.dir.getName());
            folderName.setDisable(true);
        }

        titleEntry = new TextField(level.title);

        artistEntry = new TextField(level.artist);

        descEntry = new TextField(level.desc);

        useCustomColors = new CheckBox("Use Custom Colors");
        useCustomColors.setSelected(!hasNull(level.colors));

        colorsPickers = new ColorPicker[5];
        for (int i = 0; i < colorsPickers.length; i++) {
            var cp = new ColorPicker(Objects.requireNonNullElse(
                    level.colors[i],
                    Settings.defaultColors[i]
            ));
            colorsPickers[i] = cp;
            cp.disableProperty().bind(useCustomColors.selectedProperty().not());
            cp.getStyleClass().add("button");
            cp.setMinHeight(60);
            cp.setMinWidth(60);
        }

        HBox colorPickerBox = new HBox();
        colorPickerBox.getChildren().addAll(colorsPickers);
        colorPickerBox.setSpacing(10);

        VBox blockColors = new VBox(useCustomColors, colorPickerBox);
        blockColors.setSpacing(10);

        Node songFile = createFileImportBox(
                Level.SONG_FILE,
                () -> level.song,
                "Audio Files (*.wav, *.mp3, *.aac)",
                new String[]{"*.wav", "*.mp3", "*.aac"}
        );

        Node previewImage = createFileImportBox(
                Level.PREVIEW_FILE,
                () -> level.preview,
                "Image Files (*.png, *.jpg, *.gif)",
                new String[]{"*.png", "*.jpg", "*.gif"}
        );

        Node backgroundImage = createFileImportBox(
                Level.BACKGROUND_FILE,
                () -> level.background,
                "Image Files (*.png, *.jpg, *.gif)",
                new String[]{"*.png", "*.jpg", "*.gif"}
        );

        Button newDiffs = new Button("Edit difficulties");
        newDiffs.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            Driver.setMenu(new DiffList(level, this));
        });

        OptionEntry[][] optionEntries = {
                {
                        new OptionEntry("Folder", folderName),
                        new OptionEntry("Title", titleEntry),
                        new OptionEntry("Artist", artistEntry),
                        new OptionEntry("Description", descEntry),
                        new OptionEntry("Difficulties", newDiffs, diffsInvalid)
                },
                {
                        new OptionEntry("Song File", songFile, songValid),
                        new OptionEntry("Preview Image", previewImage),
                        new OptionEntry("Background Image", backgroundImage),
                        new OptionEntry("Block Colors", blockColors),
                }
        };
        HBox options = new HBox();
        for (OptionEntry[] col : optionEntries) {
            VBox colBox = new VBox();
            colBox.setSpacing(10);
            colBox.setPrefWidth(400);

            for (OptionEntry option : col) {
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
                colBox.getChildren().add(optionBox);
            }
            options.getChildren().add(colBox);
        }
        options.setSpacing(10);

        ScrollPane optionsScroll = new ScrollPane(options);
        optionsScroll.getStyleClass().remove("scroll-pane");
        optionsScroll.setFitToWidth(true);
//        optionsScroll.setPrefWidth(1100);

        HBox mainBox = new HBox();
        mainBox.getChildren().addAll(optionsScroll);
        mainBox.setSpacing(10);
        mainBox.maxHeightProperty().bind(content.heightProperty().multiply(0.75));
        mainBox.maxWidthProperty().bind(content.widthProperty().multiply(0.95));

        Button exit = new Button();
        exit.setText("Back");
        exit.setOnAction(_ -> {
            Sound.playSfx(Sound.BACKWARD);
            Driver.setMenu(prev);
        });

        HBox bottom = new HBox(exit);
        bottom.setAlignment(Pos.CENTER);
        bottom.setSpacing(10);

        VBox centerBox = new VBox();
        centerBox.getChildren().addAll(mainBox, bottom);
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
        level.title = titleEntry.getText();
        level.artist = artistEntry.getText();
        level.desc = descEntry.getText();
        for (int i = 0; i < colorsPickers.length; i++) {
            if (useCustomColors.isSelected()) {
                level.colors[i] = colorsPickers[i].getValue();
            } else {
                level.colors[i] = null;
            }
        }
        try {
            level.writeMetadata();
        } catch (IOException e) {
            e.printStackTrace(); // TODO
        }
    }

    private void update() {
        if (level.difficulties.getValidList().isEmpty()) {
            diffsInvalid.setInvalid("This level contains no valid difficulties!");
        } else {
            diffsInvalid.setValid();
        }

        if (level.song == null) {
            songValid.setInvalid("Missing song file!");
        } else {
            songValid.setValid();
        }
    }

    interface Callback {
        Object get();
    }

    private Node createFileImportBox(String filename, Callback destField, String extDesc, String[] extensions) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new ExtensionFilter(extDesc, extensions));
        Button importButton = new Button("Import");
        Button removeButton = new Button("Remove");
        importButton.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            var f = fileChooser.showOpenDialog(Driver.primaryStage);
            if (f == null) {
                return;
            }
            try {
                level.addFile(f, filename);
            } catch (IOException e) {
                e.printStackTrace(); // TODO
            }
            removeButton.setDisable(destField.get() == null);
            update();
        });
        removeButton.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            try {
                level.removeFile(filename);
            } catch (IOException e) {
                e.printStackTrace(); // TODO
            }
            removeButton.setDisable(destField.get() == null);
            update();
        });
        removeButton.setDisable(destField.get() == null);

        var b1 = new HBox(importButton, removeButton);
        b1.setSpacing(10);
        return b1;
    }

    boolean hasNull(Color[] arr) {
        for (Color color : arr) {
            if (color == null) return true;
        }
        return false;
    }
}