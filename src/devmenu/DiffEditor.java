package devmenu;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.Difficulty;

public class DiffEditor
{
    /*
     * this class is a layout class, most of its purpose is to place UI elements like Buttons within Panes like VBoxes.
     * the creation of these UI elements are mostly not commented due to their repetitive and self explanatory nature.
     * style classes are defined in the style.css file.
     */
    public DiffEditor(Difficulty diff)
    {
        Stage primaryStage = new Stage();

        Text folderNameLabel = new Text("Folder name (ordered alphabetically)");
        TextField folderName = new TextField(diff.thisDir.getName());
        folderName.setDisable(true);

        Text titleLabel = new Text("Title");
        TextField title = new TextField(diff.title);

        Text bpmLabel = new Text("BPM");
        TextField bpm = new TextField(diff.bpm+"");

        Text numBeatsLabel = new Text("Number of beats");
        TextField numBeats = new TextField(diff.numBeats+"");

        Button editNotes = new Button("Edit notes");
        editNotes.setOnAction(e -> new NotesEditor(diff));

        Button editScores = new Button("Edit leaderboard");

        Button save = new Button("Save");
        save.setOnAction(e -> { //assigns text feilds to values
            diff.title = title.getText();
            diff.bpm = Double.parseDouble(bpm.getText());
            diff.numBeats = Integer.parseInt(numBeats.getText());
            diff.writeMetadata();
        });

        VBox main = new VBox();
        main.getChildren().addAll(folderNameLabel,folderName,titleLabel,title,bpmLabel,bpm,numBeatsLabel,numBeats,editNotes,editScores,save);
        Scene scene = new Scene(main);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}