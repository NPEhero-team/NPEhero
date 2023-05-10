package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LevelSelector extends Scene
{
    private static VBox centerMenu2 = new VBox();
    
    public LevelSelector(Stage primaryStage)
    {
        super(centerMenu2,800,600);
        primaryStage.setTitle("NPE Hero - Level Selector");
        ListView<String> levels = new ListView<String>();
        ObservableList<String> levelList= FXCollections.observableArrayList ("Test Level 1", "Test Level 2", "Test Level 3", "Test Level 4");
        levels.setItems(levelList);
        centerMenu2.setAlignment(Pos.CENTER);
        centerMenu2.getChildren().add(levels);
    }

}
