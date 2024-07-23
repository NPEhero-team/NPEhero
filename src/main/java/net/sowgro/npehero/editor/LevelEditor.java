package net.sowgro.npehero.editor;

import java.io.File;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import net.sowgro.npehero.Driver;
import net.sowgro.npehero.main.*;

public class LevelEditor extends Page
{
    private HBox content = new HBox();

    private File selectedSong = null;
    private File selectedPreview = null;
    private File selectedBackground = null;

    public LevelEditor(Level level, Page prev)
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
        TextField artist = new TextField(level.artist);

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

        ValidIndicator songValid = new ValidIndicator();
        if (level.song == null) {
            songValid.setInvalid("Missing song file!");
        }
        HBox filesLabel = new HBox(new Text("Files"), songValid);

        FileChooser backgroundChooser = new FileChooser();
        backgroundChooser.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        Button backgroundButton = new Button("Background Image");
        backgroundButton.setOnAction(_ -> selectedBackground = backgroundChooser.showOpenDialog(Driver.primaryStage));

        FileChooser previewChooser = new FileChooser();
        previewChooser.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        Button previewButton = new Button("Preview Image");
        previewButton.setOnAction(_ -> selectedPreview = previewChooser.showOpenDialog(Driver.primaryStage));

        FileChooser songChooser = new FileChooser();
        songChooser.getExtensionFilters().add(new ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"));
        Button songButton = new Button("Song file");
        songButton.setOnAction(_ -> selectedSong = songChooser.showOpenDialog(Driver.primaryStage));


        ValidIndicator diffsInvalid = new ValidIndicator();
        if (level.difficulties.validList.isEmpty()) {
            diffsInvalid.setInvalid("This level contains no valid difficulties!");
        }
        HBox diffLabel = new HBox(new Text("Difficulties"), diffsInvalid);
        diffLabel.setSpacing(5);


        TableView<Difficulty> diffList = new TableView<>();

        TableColumn<Difficulty,String> diffCol = new TableColumn<>("Difficulty");
        TableColumn<Difficulty,String> validCol = new TableColumn<>("Valid?");

        diffList.getColumns().add(diffCol);
        diffList.getColumns().add(validCol);

        diffCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().title));
        validCol.setCellValueFactory(data -> {
            if (data.getValue().isValid) {
                return new ReadOnlyStringWrapper("Yes");
            }
            else {
                return new ReadOnlyStringWrapper("No");
            }
        });

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
            if (selectedBackground != null && selectedBackground.exists()) {
                level.addFile(selectedBackground,"background." + getFileExtension(selectedBackground));
            }   
            if (selectedPreview != null && selectedPreview.exists()) {
                level.addFile(selectedPreview,"preview." + getFileExtension(selectedPreview));
            }
            if (selectedSong != null) {
                level.addFile(selectedSong,"song." + getFileExtension(selectedSong));
            }
            level.writeMetadata();
        });

        VBox left = new VBox(filesLabel, songButton, previewButton, backgroundButton, colorsLabel, colorPickerBox);
        left.setSpacing(10);
        left.setPrefWidth(300);

        VBox center = new VBox(folderNameLabel,folderName,titleLabel,title,artistLabel,artist,descLabel,desc);
        center.setSpacing(10);
        center.setPrefWidth(300);

        VBox right = new VBox(diffLabel,diffList,newDiffs);
        right.setSpacing(10);
        center.setPrefWidth(300);

        HBox mainBox = new HBox();
        mainBox.getChildren().addAll(left, center, right);
        mainBox.setSpacing(30);

        Button exit = new Button();
        exit.setText("Back");
        exit.setOnAction(e -> {
            Sound.playSfx(Sound.BACKWARD);
            Driver.setMenu(prev);
        });

        HBox bottom = new HBox(save, exit);
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

    /**
     * Get the extension of a file.
     * @param file The file to return the extension of
     * @return The extension of the file in the format "*.ext"
     */
    public String getFileExtension(File file) {
        return file.getName().substring(file.getName().lastIndexOf('.') + 1);
    }
}