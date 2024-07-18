package net.sowgro.npehero.devmenu;

import java.io.File;

import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import net.sowgro.npehero.Driver;
import net.sowgro.npehero.main.Difficulty;
import net.sowgro.npehero.main.Level;
import net.sowgro.npehero.main.Sound;

public class LevelEditor extends Pane
{ 
    private File selectedSong = null;
    private File selectedPreview = null;
    private File selectedBackground = null;

    /*
     * this class is a layout class, most of its purpose is to place UI elements like Buttons within Panes like VBoxes.
     * the creation of these UI elements are mostly not commented due to their repetitive and self explanatory nature.
     * style classes are defined in the style.css file.
     */
    public LevelEditor(Level level, Pane prev)
    {
        Text folderNameLabel = new Text("Folder name");
        TextField folderName = new TextField();
        if (level.dir != null) {
            folderName.setText(level.dir.getName());
            folderName.setDisable(true);
        }

        Text titleLabel = new Text("Title");
        TextField title = new TextField(level.title);

        Text artistLabel = new Text("Artist");
        TextField artist = new TextField(level.title);

        Text descLabel = new Text("Description");
        TextField desc = new TextField(level.desc);

        Text colorsLabel = new Text("Colors");

        ColorPicker[] colorsPickers = new ColorPicker[] {
                new ColorPicker(level.colors[0]),
                new ColorPicker(level.colors[1]),
                new ColorPicker(level.colors[2]),
                new ColorPicker(level.colors[3]),
                new ColorPicker(level.colors[4])
        };

        for (ColorPicker cp : colorsPickers) {
            cp.getStyleClass().add("button");
        }

        HBox colorPickerBox = new HBox();
        colorPickerBox.getChildren().addAll(colorsPickers);

        Text filesLabel = new Text("Files");

        FileChooser backgroundChooser = new FileChooser();
        backgroundChooser.getExtensionFilters().add(new ExtensionFilter("PNG", "*.png"));
        Button backgroundButton = new Button("Import background PNG");
        backgroundButton.setOnAction(e -> {selectedBackground = backgroundChooser.showOpenDialog(Driver.primaryStage);});

        FileChooser previewChooser = new FileChooser();
        previewChooser.getExtensionFilters().add(new ExtensionFilter("PNG", "*.png"));
        Button previewButton = new Button("Import preview PNG");
        previewButton.setOnAction(e -> {selectedPreview = previewChooser.showOpenDialog(Driver.primaryStage);});

        FileChooser songChooser = new FileChooser();
        songChooser.getExtensionFilters().add(new ExtensionFilter("WAV", "*.wav"));
        Button songButton = new Button("Import song WAV");
        songButton.setOnAction(e -> selectedSong = songChooser.showOpenDialog(Driver.primaryStage));

        Text diffLabel = new Text("Difficulties");

        TableView<Difficulty> diffList = new TableView<>();

        TableColumn<Difficulty,String> diffCol = new TableColumn<>("Difficulty");
        TableColumn<Difficulty,Boolean> validCol = new TableColumn<>("Valid?");

        diffList.getColumns().add(diffCol);
        diffList.getColumns().add(validCol);

        diffCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().title));
        validCol.setCellValueFactory(data -> new ReadOnlyBooleanWrapper(data.getValue().isValid));

        diffList.setItems(level.difficulties.list);

        diffList.setRowFactory( _ -> {
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
        newDiffs.setOnAction(_ -> Driver.setMenu(new DiffList(level, this)));

        diffList.setSelectionModel(null);

        Button save = new Button("Save");
        save.setOnAction(e -> { //assigns fields to values
            level.title = title.getText();
            level.artist = artist.getText();
            level.desc = desc.getText();
            level.colors[0] = colorsPickers[0].getValue();
            level.colors[1] = colorsPickers[1].getValue();
            level.colors[2] = colorsPickers[2].getValue();
            level.colors[3] = colorsPickers[3].getValue();
            level.colors[4] = colorsPickers[4].getValue();
            if (selectedBackground != null && selectedBackground.exists())
            {
                level.addFile(selectedBackground,"background.png");
            }   
            if (selectedPreview != null && selectedPreview.exists())
            {
                level.addFile(selectedPreview,"preview.png");
            }
            if (selectedSong != null)
            {
                level.addFile(selectedSong,"song.wav");
            }
            level.writeMetadata();
        });

        VBox options = new VBox();
        options.getChildren().addAll(folderNameLabel,folderName,titleLabel,title,artistLabel,artist,descLabel,desc,colorsLabel,
            colorPickerBox,filesLabel,previewButton,backgroundButton,songButton,save);
        options.setSpacing(10);

        VBox diffBox = new VBox();
        diffBox.getChildren().addAll(diffLabel,diffList,/*buttons,newDiffBox,*/ newDiffs);
        diffBox.setSpacing(10);

        HBox mainBox = new HBox();
        mainBox.getChildren().addAll(options,diffBox);
        mainBox.setSpacing(30);

        Button exit = new Button();
        exit.setText("Back");
        exit.setOnAction(e -> {
            Sound.playSfx(Sound.BACKWARD);
            Driver.setMenu(prev);
        });

        VBox centerBox = new VBox();
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setSpacing(10);
        centerBox.getChildren().addAll(mainBox,exit);
        centerBox.setMinWidth(400);

        HBox rootBox = new HBox();
        rootBox.prefWidthProperty().bind(super.prefWidthProperty());
        rootBox.prefHeightProperty().bind(super.prefHeightProperty());
        rootBox.getChildren().add(centerBox);
        rootBox.setAlignment(Pos.CENTER);

        super.getChildren().add(rootBox);
    }
}