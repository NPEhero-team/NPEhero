package net.sowgro.npehero.devmenu;

import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import net.sowgro.npehero.Driver;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import net.sowgro.npehero.gui.MainMenu;
import net.sowgro.npehero.main.Level;
import net.sowgro.npehero.main.LevelController;
import net.sowgro.npehero.main.SoundController;

public class LevelList extends Pane
{
    /*
     * this class is a layout class, most of its purpose is to place UI elements like Buttons within Panes like VBoxes.
     * the creation of these UI elements are mostly not commented due to their repetitive and self explanatory nature.
     * style classes are defined in the style.css file.
     */
    public LevelList()
    {
        //sets up table view: requires special getters, setters and constructors to work
        TableView<Level> levels = new TableView<>();

        TableColumn<Level,String> titleCol = new TableColumn<>("Title");
        TableColumn<Level,String> artistCol = new TableColumn<>("Artist");
        TableColumn<Level,Boolean> validCol = new TableColumn<>("Valid?");

        levels.getColumns().add(titleCol);
        levels.getColumns().add(artistCol);
        levels.getColumns().add(validCol);

        titleCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getTitle()));
        artistCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getArtist()));
        validCol.setCellValueFactory(data -> new ReadOnlyBooleanWrapper(data.getValue().isValid()));

        levels.setItems(LevelController.getLevelList());

        levels.setRowFactory( _ -> {
            TableRow<Level> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Level rowData = row.getItem();
                    Driver.setMenu(new LevelEditor(rowData, this));
                }
            });
            return row ;
        });

        levels.prefWidthProperty().bind(super.prefWidthProperty().multiply(0.35));
        levels.setMinWidth(400);
        levels.prefHeightProperty().bind(super.prefHeightProperty().multiply(0.75));

        Button edit = new Button("Edit");
        edit.setOnAction(e -> Driver.setMenu(new LevelEditor(levels.getSelectionModel().getSelectedItem(), this)));
        edit.setDisable(true);
        edit.disableProperty().bind(levels.getSelectionModel().selectedItemProperty().isNull());

        Button remove = new Button("Delete");
        remove.setOnAction(e -> LevelController.removeLevel(levels.getSelectionModel().getSelectedItem()));
        remove.setDisable(true);
        remove.disableProperty().bind(levels.getSelectionModel().selectedItemProperty().isNull());

        Button refresh = new Button("Refresh");
        refresh.setOnAction(e -> {
            LevelController.readData();
            levels.setItems(LevelController.getLevelList());
        });

        ToggleButton create = new ToggleButton("Create");

        VBox buttons = new VBox();
        buttons.getChildren().addAll(create, edit, remove, refresh);
        buttons.setSpacing(10);

        TextField newLevelEntry = new TextField();
        Button newLevelButton = new Button("add");

        HBox newLevel = new HBox(newLevelEntry,newLevelButton);
        Label newLevelLabel = new Label("Name of new level");
        VBox newLevelBox = new VBox(newLevelLabel, newLevel);
        newLevelBox.setSpacing(10);
        newLevelBox.getStyleClass().add("box");
        newLevelBox.setPadding(new Insets(10));

        Pane sidebar = new Pane();

        HBox main = new HBox();
        main.getChildren().addAll(levels,buttons,sidebar);
        main.setSpacing(10);

        Button exit = new Button();
        exit.setText("Back");
        exit.setOnAction(e -> {
            SoundController.playSfx(SoundController.BACKWARD);
            Driver.setMenu(new MainMenu());
        });

        VBox centerBox = new VBox();
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setSpacing(10);
        centerBox.getChildren().addAll(main,exit);
        centerBox.setMinWidth(400);

        HBox rootBox = new HBox();
        rootBox.prefWidthProperty().bind(super.prefWidthProperty());
        rootBox.prefHeightProperty().bind(super.prefHeightProperty());
        rootBox.getChildren().add(centerBox);
        rootBox.setAlignment(Pos.CENTER);

        create.setOnAction(_ -> {
            if (create.isSelected()) {
                sidebar.getChildren().add(newLevelBox);
            }
            else {
                sidebar.getChildren().remove(newLevelBox);
            }
        });

        newLevelButton.setOnAction(_ -> {
            LevelController.addLevel(newLevelEntry.getText());
            newLevelEntry.clear();
            refresh.fire();
            sidebar.getChildren().clear();
            create.setSelected(false);
        });

        super.getChildren().add(rootBox);
    }
}