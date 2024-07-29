package net.sowgro.npehero.editor;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import net.sowgro.npehero.Driver;
import net.sowgro.npehero.gui.MainMenu;
import net.sowgro.npehero.levelapi.Difficulty;
import net.sowgro.npehero.levelapi.Level;
import net.sowgro.npehero.main.Page;
import net.sowgro.npehero.main.Sound;
import net.sowgro.npehero.main.ValidIndicator;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.Collections;

public class DiffList extends Page
{
    private final Label error;
    private final HBox errorBox;
    private HBox content = new HBox();
    private Level level;

    public DiffList(Level level, Page prev)
    {
        this.level = level;
        //sets up table view: requires special getters, setters and constructors to work
        TableView<Difficulty> diffs = new TableView<>();

        TableColumn<Difficulty,String> titleCol = new TableColumn<>("Name");
        TableColumn<Difficulty,String> validCol = new TableColumn<>("Valid?");

        diffs.getColumns().add(titleCol);
        diffs.getColumns().add(validCol);

        titleCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().title));
        validCol.setCellValueFactory(data -> {
            if (data.getValue().isValid()) {
                return new ReadOnlyStringWrapper("Yes");
            }
            else {
                return new ReadOnlyStringWrapper("No");
            }
        });

        diffs.setItems(level.difficulties.list);

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

        diffs.setPrefWidth(400);
        diffs.prefHeightProperty().bind(content.prefHeightProperty().multiply(0.67));

        error = new Label();
        errorBox = new HBox(error);
        errorBox.setSpacing(10);
        errorBox.setPadding(new Insets(10));
        errorBox.getStyleClass().addAll("box", "red");
        errorBox.setOnMouseClicked(_ -> {
            // TODO
            Driver.setMenu(new ErrorList(level.difficulties.problems, this));
        });
        refresh();

        Button edit = new Button("Edit");
        edit.setOnAction(e -> {
            Sound.playSfx(Sound.FORWARD);
            Driver.setMenu(new DiffEditor(diffs.getSelectionModel().getSelectedItem(), this));
        });
        edit.setDisable(true);
        edit.disableProperty().bind(diffs.getSelectionModel().selectedItemProperty().isNull());

        Button remove = new Button("Delete");
        remove.setOnAction(e -> {
            Sound.playSfx(Sound.FORWARD);
            try {
                level.difficulties.remove(diffs.getSelectionModel().getSelectedItem());
            } catch (IOException ex) {
                ex.printStackTrace();
                Driver.setMenu(new ErrorDisplay("Failed to remove difficulty\n"+e, this));
            }
        });
        remove.setDisable(true);
        remove.disableProperty().bind(diffs.getSelectionModel().selectedItemProperty().isNull());

        Button refresh = new Button("Refresh");
        refresh.setOnAction(e -> {
            Sound.playSfx(Sound.FORWARD);
            try {
                level.difficulties.read();
            } catch (IOException ex) {
                // TODO
            }
//            diffs.setItems(level.difficulties.list.sorted());
            diffs.refresh();
            refresh();
        });

        ToggleButton create = new ToggleButton("Create");

        Button moveUp = new Button("Move Up");
        moveUp.disableProperty().bind(diffs.getSelectionModel().selectedItemProperty().isNull());
        moveUp.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            Difficulty diff = diffs.getSelectionModel().selectedItemProperty().get();
            ObservableList<Difficulty> diffList = level.difficulties.list;
            int oldIndex = diffList.indexOf(diff);
            if (oldIndex <= 0) {
                return;
            }
            Collections.swap(diffList, oldIndex, oldIndex-1);
            try {
                level.difficulties.saveOrder();
            } catch (IOException e) {
                e.printStackTrace();
                Driver.setMenu(new ErrorDisplay("Failed to move difficulty\n"+e,this));
            }
        });

        Button moveDown = new Button("Move Down");
        moveDown.disableProperty().bind(diffs.getSelectionModel().selectedItemProperty().isNull());
        moveDown.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            Difficulty diff = diffs.getSelectionModel().selectedItemProperty().get();
            ObservableList<Difficulty> diffList = level.difficulties.list;
            int oldIndex = diffList.indexOf(diff);
            if (oldIndex >= diffList.size()-1) {
                return;
            }
            Collections.swap(diffList, oldIndex, oldIndex+1);
            try {
                level.difficulties.saveOrder();
            } catch (IOException e) {
                e.printStackTrace();
                Driver.setMenu(new ErrorDisplay("Failed to move difficulty\n"+e,this));
            }
        });

        VBox buttons = new VBox();
        buttons.getChildren().addAll(create, edit, remove, moveUp, moveDown, refresh);
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
        main.getChildren().addAll(new VBox(diffs, errorBox),buttons, sidebar);
        main.setSpacing(10);

        Button exit = new Button();
        exit.setText("Back");
        exit.setOnAction(e -> {
            Sound.playSfx(Sound.BACKWARD);
            Driver.setMenu(prev);
        });

        create.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            if (create.isSelected()) {
                sidebar.getChildren().add(newLevelBox);
            }
            else {
                sidebar.getChildren().remove(newLevelBox);
            }
        });

        newLevelButton.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            try {
                level.difficulties.add(newLevelEntry.getText());
            } catch (FileAlreadyExistsException e) {
                Driver.setMenu(new ErrorDisplay("Failed to add level\nA difficulty already exists with the folder name " + e.getFile(), this));
            } catch (IOException e) {
                Driver.setMenu(new ErrorDisplay("Failed to add level\n"+e, this));
                e.printStackTrace();
            }
            newLevelEntry.clear();
            refresh.fire();
            sidebar.getChildren().clear();
            create.setSelected(false);
        });

        VBox centerBox = new VBox();
        centerBox.getChildren().addAll(main, exit);
        centerBox.setSpacing(10);
        centerBox.setAlignment(Pos.CENTER);

        content.getChildren().add(centerBox);
        content.setAlignment(Pos.CENTER);
    }

    @Override
    public Pane getContent() {
        return content;
    }

    public void refresh() {
        error.setText("Failed to load " + level.difficulties.problems.size() + " difficulty(s)");
        if (level.difficulties.problems.isEmpty()) {
            errorBox.setVisible(false);
            errorBox.setManaged(false);
        } else {
            errorBox.setVisible(true);
            errorBox.setManaged(true);
        }
    }
}