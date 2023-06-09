package devmenu;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import gui.Driver;
import gui.LevelSurround;
import gui.MainMenu;
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

        Text numBeatsLabel = new Text("Number of beats (set by notes editor)");
        TextField numBeats = new TextField(diff.numBeats+"");

        Text priorityLabel = new Text("priority (lower first)");
        TextField priority = new TextField(diff.priority+"");

        Button editNotes = new Button("Edit notes");
        editNotes.setOnAction(e -> {
            try {
                new NotesEditor(diff);
            } catch (FileNotFoundException | UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
        });

        Button editScores = new Button("Clear leaderboard");
        editScores.setOnAction(e -> diff.getLeaderboard().clear());

        Button playLevel = new Button("Launch level");
        playLevel.setOnAction(e -> Driver.setMenu(new LevelSurround(diff.level, diff, new MainMenu())));

        Button refresh = new Button("Refresh");
        refresh.setOnAction( e -> {
            numBeats.setText(diff.numBeats+"");
        });

        Button save = new Button("Save");
        save.setOnAction(e -> { //assigns text feilds to values
            diff.title = title.getText();
            diff.bpm = Double.parseDouble(bpm.getText());
            diff.numBeats = Integer.parseInt(numBeats.getText());
            diff.priority = Integer.parseInt(priority.getText());
            diff.writeMetadata();
        });

        VBox main = new VBox();
        main.getChildren().addAll(folderNameLabel,folderName,titleLabel,title,bpmLabel,bpm,numBeatsLabel,numBeats,refresh,priorityLabel,priority,editNotes,editScores,playLevel,save);
        Scene scene = new Scene(main);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}