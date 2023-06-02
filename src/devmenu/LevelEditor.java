package devmenu;

import java.io.File;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import main.Difficulty;
import main.Level;

public class LevelEditor
{ 
    private File selectedSong = null;
    private File selectedPreview = null;
    private File selectedBackground = null;

    /*
     * this class is a layout class, most of its purpose is to place UI elements like Buttons within Panes like VBoxes.
     * the creation of these UI elements are mostly not commented due to their repetitive and self explanatory nature.
     * style classes are defined in the style.css file.
     */
    public LevelEditor(Level level)
    {
        Stage primaryStage = new Stage();

        Text folderNameLabel = new Text("Folder name");
        TextField folderName = new TextField(level.thisDir.getName());
        folderName.setDisable(true);

        Text titleLabel = new Text("Title");
        TextField title = new TextField(level.getTitle());

        Text artistLabel = new Text("Artist");
        TextField artist = new TextField(level.getArtist());

        Text descLabel = new Text("Description");
        TextField desc = new TextField(level.desc);

        Text colorsLabel = new Text("Colors (Left to right)");
        ColorPicker c1 = new ColorPicker(level.colors[0]);
        ColorPicker c2 = new ColorPicker(level.colors[1]);
        ColorPicker c3 = new ColorPicker(level.colors[2]);
        ColorPicker c4 = new ColorPicker(level.colors[3]);
        ColorPicker c5 = new ColorPicker(level.colors[4]);

        Text filesLabel = new Text("Files");

        FileChooser backgroundChooser = new FileChooser();
        backgroundChooser.getExtensionFilters().add(new ExtensionFilter("PNG", "*.png"));
        Button backgroundButton = new Button("Import background PNG");
        backgroundButton.setOnAction(e -> {selectedBackground = backgroundChooser.showOpenDialog(primaryStage);});

        FileChooser previewChooser = new FileChooser();
        previewChooser.getExtensionFilters().add(new ExtensionFilter("PNG", "*.png"));
        Button previewButton = new Button("Import preview PNG");
        previewButton.setOnAction(e -> {selectedPreview = previewChooser.showOpenDialog(primaryStage);});

        FileChooser songChooser = new FileChooser();
        songChooser.getExtensionFilters().add(new ExtensionFilter("WAV", "*.wav"));
        Button songButton = new Button("Import song WAV");
        songButton.setOnAction(e -> selectedSong = songChooser.showOpenDialog(primaryStage));

        Text diffLabel = new Text("Difficulties");

        ObservableList<Difficulty> diffList2 = FXCollections.observableArrayList();
        diffList2.addAll(level.getDiffList());
        ListView<Difficulty> diffList = new ListView<Difficulty>();
        diffList.setItems(diffList2);

        Button edit = new Button("Edit");
        edit.setOnAction(e -> new DiffEditor(diffList.getSelectionModel().getSelectedItem()));

        Button remove = new Button("Delete");
        remove.setOnAction(e -> level.removeDiff(diffList.getSelectionModel().getSelectedItem()));

        Button refresh = new Button("Refresh");
        refresh.setOnAction(e -> {
            diffList2.clear();
            diffList2.addAll(level.getDiffList());
            diffList.setItems(diffList2);
        });

        HBox buttons = new HBox();
        buttons.getChildren().addAll(edit,remove,refresh);

        TextField newDiff = new TextField("new");
        Button newDiffButton = new Button("add");
        newDiffButton.setOnAction(e -> level.addDiff(newDiff.getText()));
        HBox newDiffBox = new HBox();
        newDiffBox.getChildren().addAll(newDiff,newDiffButton);

        Button save = new Button("Save");
        save.setOnAction(e -> { //asigns feilds to values
            level.setTitle(title.getText());
            level.setArtist(artist.getText());
            level.desc = desc.getText();
            level.colors[0] = c1.getValue();
            level.colors[1] = c2.getValue();
            level.colors[2] = c3.getValue();
            level.colors[3] = c4.getValue();
            level.colors[4] = c5.getValue();
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

        VBox main = new VBox();
        main.getChildren().addAll(folderNameLabel,folderName,titleLabel,title,artistLabel,artist,descLabel,desc,colorsLabel,
            c1,c2,c3,c4,c5,filesLabel,previewButton,backgroundButton,songButton,diffLabel,diffList,buttons,newDiffBox,save);
    
        Scene scene = new Scene(main);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}