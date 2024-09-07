package net.sowgro.npehero.editor;

import java.io.IOException;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import net.sowgro.npehero.Driver;
import net.sowgro.npehero.levelapi.Difficulty;
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
    private final TableView<Difficulty> diffList;
    Level level;

    private HBox content = new HBox();

    public LevelEditor(Level level, Page prev)
    {
        this.level = level;
        Label folderNameLabel = new Label("Folder name");
        TextField folderName = new TextField();
        if (level.dir != null) {
            folderName.setText(level.dir.getName());
            folderName.setDisable(true);
        }

        Label titleLabel = new Label("Title");
        titleEntry = new TextField(level.title);

        Label artistLabel = new Label("Artist");
        artistEntry = new TextField(level.artist);

        Label descLabel = new Label("Description");
        descEntry = new TextField(level.desc);

        Label colorsLabel = new Label("Colors");
        colorsPickers = new ColorPicker[] {
                new ColorPicker(level.colors[0]),
                new ColorPicker(level.colors[1]),
                new ColorPicker(level.colors[2]),
                new ColorPicker(level.colors[3]),
                new ColorPicker(level.colors[4])
        };
        for (ColorPicker cp : colorsPickers) {
            cp.getStyleClass().add("button");
            cp.setMinHeight(60);
            cp.setMinWidth(60);
        }

        HBox colorPickerBox = new HBox();
        colorPickerBox.getChildren().addAll(colorsPickers);
        colorPickerBox.setSpacing(10);

        Label backgroundLabel = new Label("Background Image");
        FileChooser backgroundChooser = new FileChooser();
        backgroundChooser.getExtensionFilters().add(new ExtensionFilter("Image Files (*.png, *.jpg, *.gif)", "*.png", "*.jpg", "*.gif"));
        Button backgroundImport = new Button("Import");
        Button backgroundRemove = new Button("Remove");
        backgroundImport.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            var f = backgroundChooser.showOpenDialog(Driver.primaryStage);
            if (f == null) {
                return;
            }
            try {
                level.addFile(f, Level.BACKGROUND_FILE);
            } catch (IOException e) {
                // TODO
                e.printStackTrace();
            }
            backgroundRemove.setDisable(level.background == null);
        });
        backgroundRemove.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            try {
                level.removeFile(Level.BACKGROUND_FILE);
            } catch (IOException e) {
                // TODO
                e.printStackTrace();
            }
            backgroundRemove.setDisable(level.background == null);
        });
        backgroundRemove.setDisable(level.background == null);

        Label previewLabel = new Label("Preview Image");
        FileChooser previewChooser = new FileChooser();
        previewChooser.getExtensionFilters().add(new ExtensionFilter("Image Files (*.png, *.jpg, *.gif)", "*.png", "*.jpg", "*.gif"));
        Button previewImport = new Button("Import");
        Button previewRemove = new Button("Remove");
        previewImport.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            var f = previewChooser.showOpenDialog(Driver.primaryStage);
            if (f == null) {
                return;
            }
            try {
                level.addFile(f, Level.PREVIEW_FILE);
            } catch (IOException e) {
                e.printStackTrace(); // TODO
            }
            previewRemove.setDisable(level.preview == null);
        });
        previewRemove.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            try {
                level.removeFile(Level.PREVIEW_FILE);
            } catch (IOException e) {
                e.printStackTrace(); // TODO
            }
            previewRemove.setDisable(level.preview == null);
        });
        previewRemove.setDisable(level.preview == null);

        HBox songLabel = new HBox(new Label("Song File"), songValid);
        FileChooser songChooser = new FileChooser();
        songChooser.getExtensionFilters().add(new ExtensionFilter("Audio Files (*.wav, *.mp3, *.aac)", "*.wav", "*.mp3", "*.aac"));
        Button songImport = new Button("Import");
        Button songRemove = new Button("Remove");
        songImport.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            var f = songChooser.showOpenDialog(Driver.primaryStage);
            try {
                level.addFile(f, Level.SONG_FILE);
            } catch (IOException e) {
                e.printStackTrace(); // TODO
            }
            songRemove.setDisable(level.song == null);
        });
        songRemove.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            try {
                level.removeFile(Level.SONG_FILE);
            } catch (IOException e) {
                e.printStackTrace(); // TODO
            }
            songRemove.setDisable(level.song == null);
        });
        songRemove.setDisable(level.song == null);

        HBox diffLabel = new HBox(new Label("Difficulties"), diffsInvalid);
        diffLabel.setSpacing(5);


        diffList = new TableView<>();

        TableColumn<Difficulty,String> diffCol = new TableColumn<>("Difficulty");
        TableColumn<Difficulty,String> validCol = new TableColumn<>("Valid?");

        diffCol.prefWidthProperty().bind(diffList.widthProperty().multiply(0.45));
        validCol.prefWidthProperty().bind(diffList.widthProperty().multiply(0.45));

        diffList.getColumns().add(diffCol);
        diffList.getColumns().add(validCol);

        diffCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().title));
        validCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().isValid() ? "Yes" : "No"));
        diffList.setItems(level.difficulties.list);

        diffList.setRowFactory(_ -> {
            TableRow<Difficulty> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Difficulty rowData = row.getItem();
                    Driver.setMenu(new DiffEditor(rowData, this));
                }
            });
            return row ;
        });

        Button newDiffs = new Button("Edit difficulties");
        newDiffs.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            Driver.setMenu(new DiffList(level, this));
        });

        diffList.setSelectionModel(null);


        var b1 = new HBox(songImport, songRemove);
        b1.setSpacing(10);
        var b2 = new HBox(previewImport, previewRemove);
        b2.setSpacing(10);
        var b3 = new HBox(backgroundImport, backgroundRemove);
        b3.setSpacing(10);
        VBox optionsBox = new VBox(folderNameLabel, folderName, titleLabel, titleEntry, artistLabel, artistEntry, descLabel, descEntry,
                songLabel, b1, previewLabel, b2, backgroundLabel, b3, colorsLabel, colorPickerBox/*, new Separator(Orientation.HORIZONTAL), save*/);
        optionsBox.setSpacing(10);
//        left.setPrefWidth(300);
//        optionsBox.setPadding(new Insets(5));

        ScrollPane leftScroll = new ScrollPane(optionsBox);
        leftScroll.getStyleClass().remove("scroll-pane");
//        leftScroll.getStyleClass().add("box");
//        leftScroll.setPadding(new Insets(5));
        leftScroll.setFitToWidth(true);
        leftScroll.setPrefWidth(400);

//        VBox center = new VBox();
//        center.setSpacing(10);
//        center.setPrefWidth(300);

        VBox right = new VBox(diffLabel, diffList,newDiffs);
        right.setSpacing(10);
//        right.setPrefWidth(325);

        Label optionsLable = new Label("Options");
        VBox left = new VBox(/*optionsLable, */leftScroll);
        left.setSpacing(10);

        HBox mainBox = new HBox();
        mainBox.getChildren().addAll(left, right);
        mainBox.setSpacing(10);
        mainBox.prefHeightProperty().bind(content.heightProperty().multiply(0.75));
        mainBox.maxWidthProperty().bind(content.widthProperty().multiply(0.95));

        Button exit = new Button();
        exit.setText("Back");
        exit.setOnAction(e -> {
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
        diffList.refresh();
        validate();
    }

    @Override
    public void onLeave() {
        level.title = titleEntry.getText();
        level.artist = artistEntry.getText();
        level.desc = descEntry.getText();
        level.colors[0] = colorsPickers[0].getValue();
        level.colors[1] = colorsPickers[1].getValue();
        level.colors[2] = colorsPickers[2].getValue();
        level.colors[3] = colorsPickers[3].getValue();
        level.colors[4] = colorsPickers[4].getValue();
        try {
            level.writeMetadata();
        } catch (IOException ex) {
            // TODO
        }
        validate();
    }

    public void validate() {
        if (level.difficulties.getValidList().isEmpty()) {
            diffsInvalid.setInvalid("This level contains no valid difficulties!");
        } else {
            diffsInvalid.setValid();
        }
        if (level.song == null) {
            songValid.setInvalid("Missing song file!");
        }
    }
}