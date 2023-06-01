package devmenu;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.stage.Stage;
import main.Difficulty;
import main.Level;

public class LevelEditor
{
    public LevelEditor(Level level)
    {
        Text folderNameLabel = new Text("Folder name");
        TextField folderName = new TextField(level.thisDir.getName());

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

        ObservableList diffList2 = FXCollections.observableArrayList();
        diffList2.addAll(level.getDiffList());
        ListView<Difficulty> diffList = new ListView();
        diffList.setItems(diffList2);
        diffList.setOnMouseClicked(e -> new DiffEditor(diffList.getSelectionModel().getSelectedItem()));

        Text diffLabel = new Text("Difficulties");

        TextField newDiff = new TextField("new");
        Button newDiffButton = new Button("add");
        newDiffButton.setOnAction(e -> level.addDiff(newDiff.getText()));
        HBox newDiffBox = new HBox();
        newDiffBox.getChildren().addAll(newDiff,newDiffButton);

        Button save = new Button("Save");
        save.setOnAction(e -> {
            level.setTitle(title.getText());
            level.setArtist(artist.getText());
            level.desc = desc.getText();
            level.colors[0] = c1.getValue();
            level.colors[1] = c2.getValue();
            level.colors[2] = c3.getValue();
            level.colors[3] = c4.getValue();
            level.colors[4] = c5.getValue();
            level.writeMetadata();
        });

        VBox main = new VBox();
        main.getChildren().addAll(folderNameLabel,folderName,titleLabel,title,artistLabel,artist,descLabel,desc,colorsLabel,c1,c2,c3,c4,c5,diffLabel,diffList,newDiffBox,save);
        
        Stage primaryStage = new Stage();
        Scene scene = new Scene(main);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}