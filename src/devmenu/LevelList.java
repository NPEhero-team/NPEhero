package devmenu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import gui.Driver;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.Level;

public class LevelList 
{
    Stage primaryStage = new Stage();
    public LevelList()
    {
        //sets up table view: requires special getters, setters and constructors to work
        TableView<Level> levels = new TableView<Level>();

        TableColumn<Level,String> titleCol = new TableColumn<Level,String>("Title");
        TableColumn<Level,String> artistCol = new TableColumn<Level,String>("Artist");

        levels.getColumns().add(titleCol);
        levels.getColumns().add(artistCol);

        titleCol.setCellValueFactory(new PropertyValueFactory<Level, String>("title"));
        artistCol.setCellValueFactory(new PropertyValueFactory<Level, String>("artist"));

        levels.setItems(Driver.levelController.levelList);
        levels.setOnMouseClicked(e -> new LevelEditor(levels.getSelectionModel().getSelectedItem()));

        Button refresh = new Button("Refresh");
        refresh.setOnAction(e -> levels.setItems(Driver.levelController.levelList));

        TextField newLevel = new TextField("new");
        Button newLevelButton = new Button("add");
        newLevelButton.setOnAction(e -> Driver.levelController.addLevel(newLevel.getText()));
        HBox newLevelBox = new HBox();
        newLevelBox.getChildren().addAll(newLevel,newLevelButton);

        VBox main = new VBox();
        main.getChildren().addAll(levels,refresh,newLevelBox);
        Scene scene = new Scene(main);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}