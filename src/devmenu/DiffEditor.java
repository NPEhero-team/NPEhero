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
    public DiffEditor(Difficulty diff)
    {
        Text folderNameLabel = new Text("Folder name (ordered alphabetically)");
        TextField folderName = new TextField(diff.thisDir.getName());

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
        save.setOnAction(e -> {
            diff.title = title.getText();
            diff.bpm = Integer.parseInt(bpm.getText());
            diff.numBeats = Integer.parseInt(numBeats.getText());
            if(! diff.thisDir.getName().equals(folderName.getText()));
            {
                //will rename
            }
            diff.writeMetadata();
        });

        VBox main = new VBox();
        main.getChildren().addAll(folderNameLabel,folderName,titleLabel,title,bpmLabel,bpm,numBeatsLabel,numBeats,editNotes,editScores,save);
        Scene scene = new Scene(main);
        Stage primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}