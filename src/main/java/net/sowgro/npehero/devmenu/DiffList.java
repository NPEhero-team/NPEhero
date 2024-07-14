package net.sowgro.npehero.devmenu;

import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import net.sowgro.npehero.Driver;
import net.sowgro.npehero.main.Difficulty;
import net.sowgro.npehero.main.Level;

public class DiffList extends Pane
{
    /*
     * this class is a layout class, most of its purpose is to place UI elements like Buttons within Panes like VBoxes.
     * the creation of these UI elements are mostly not commented due to their repetitive and self explanatory nature.
     * style classes are defined in the style.css file.
     */
    public DiffList(Level level, Pane prev)
    {
        //sets up table view: requires special getters, setters and constructors to work
        TableView<Difficulty> diffs = new TableView<>();

        TableColumn<Difficulty,String> titleCol = new TableColumn<>("Name");
        TableColumn<Difficulty,Boolean> validCol = new TableColumn<>("Valid?");

        diffs.getColumns().add(titleCol);
        diffs.getColumns().add(validCol);

        titleCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getTitle()));
        validCol.setCellValueFactory(data -> new ReadOnlyBooleanWrapper(data.getValue().isValid()));

        diffs.setItems(level.getDiffList());

        diffs.setRowFactory( _ -> {
            TableRow<Difficulty> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Difficulty rowData = row.getItem();
                    Driver.setMenu(new DiffEditor(rowData, this));
                }
            });
            return row ;
        });

//        diffs.prefWidthProperty().bind(super.prefWidthProperty().multiply(0.35));
        diffs.setPrefWidth(400);
        diffs.prefHeightProperty().bind(super.prefHeightProperty().multiply(0.67));

        Button edit = new Button("Edit");
        edit.setOnAction(e -> Driver.setMenu(new DiffEditor(diffs.getSelectionModel().getSelectedItem(), this)));
        edit.setDisable(true);
        edit.disableProperty().bind(diffs.getSelectionModel().selectedItemProperty().isNull());

        Button remove = new Button("Delete");
        remove.setOnAction(e -> level.removeDiff(diffs.getSelectionModel().getSelectedItem()));
        remove.setDisable(true);
        remove.disableProperty().bind(diffs.getSelectionModel().selectedItemProperty().isNull());

        Button refresh = new Button("Refresh");
        refresh.setOnAction(e -> {
            level.readData();
            diffs.setItems(level.getDiffList());
        });

        ToggleButton create = new ToggleButton("Create");

        VBox buttons = new VBox();
        buttons.getChildren().addAll(create, edit, remove, refresh);
        buttons.setSpacing(10);

        TextField newLevelEntry = new TextField();
        Button newLevelButton = new Button("add");

        HBox newLevel = new HBox(newLevelEntry,newLevelButton);
        Label newLevelLabel = new Label("Name of new difficulty");
        VBox newLevelBox = new VBox(newLevelLabel, newLevel);
        newLevelBox.setSpacing(10);
        newLevelBox.getStyleClass().add("box");
        newLevelBox.setPadding(new Insets(10));

        Pane sidebar = new Pane();

        HBox main = new HBox();
        main.getChildren().addAll(diffs,buttons, sidebar);
        main.setSpacing(10);

        Button exit = new Button();
        exit.setText("Back");
        exit.setOnAction(e -> {
            Driver.soundController.playSfx("backward");
            Driver.setMenu(prev);
        });

        VBox centerBox = new VBox();
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setSpacing(10);
        centerBox.getChildren().addAll(main,exit);

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
            level.addDiff(newLevelEntry.getText());
            newLevelEntry.clear();
            refresh.fire();
            sidebar.getChildren().clear();
            create.setSelected(false);
        });

        super.getChildren().add(rootBox);
    }

}