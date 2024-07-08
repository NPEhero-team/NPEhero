package net.sowgro.npehero.devmenu;

import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import net.sowgro.npehero.Driver;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.sowgro.npehero.main.Level;
import net.sowgro.npehero.main.LevelController;

public class LevelList 
{
    /*
     * this class is a layout class, most of its purpose is to place UI elements like Buttons within Panes like VBoxes.
     * the creation of these UI elements are mostly not commented due to their repetitive and self explanatory nature.
     * style classes are defined in the style.css file.
     */
    public LevelList()
    {
        //sets up table view: requires special getters, setters and constructors to work
        TableView<Level> levels = new TableView<Level>();

        TableColumn<Level,String> titleCol = new TableColumn<Level,String>("Title");
        TableColumn<Level,String> artistCol = new TableColumn<Level,String>("Artist");
        TableColumn<Level,Boolean> validCol = new TableColumn<>("Valid?");

        levels.getColumns().add(titleCol);
        levels.getColumns().add(artistCol);
        levels.getColumns().add(validCol);

        titleCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getTitle()));
        artistCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getArtist()));
        validCol.setCellValueFactory(data -> new ReadOnlyBooleanWrapper(data.getValue().isValid()));

        levels.setItems(LevelController.getLevelList());


        Button edit = new Button("Edit");
        edit.setOnAction(e -> new LevelEditor(levels.getSelectionModel().getSelectedItem()));

        Button remove = new Button("Delete");
        remove.setOnAction(e -> Driver.levelController.removeLevel(levels.getSelectionModel().getSelectedItem()));

        Button refresh = new Button("Refresh");
        refresh.setOnAction(e -> {
            Driver.levelController.readData();
            levels.setItems(LevelController.getLevelList());
        });

        HBox buttons = new HBox();
        buttons.getChildren().addAll(edit,remove,refresh);

        TextField newLevel = new TextField("new");
        Button newLevelButton = new Button("add");
        newLevelButton.setOnAction(e -> Driver.levelController.addLevel(newLevel.getText()));
        HBox newLevelBox = new HBox();
        newLevelBox.getChildren().addAll(newLevel,newLevelButton);

        VBox main = new VBox();
        main.getChildren().addAll(levels,buttons,newLevelBox);
        Scene scene = new Scene(main, 400, 400);
        Stage primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}