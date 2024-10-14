package net.sowgro.npehero.editor;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import net.sowgro.npehero.Driver;
import net.sowgro.npehero.levelapi.Difficulty;
import net.sowgro.npehero.levelapi.Level;
import net.sowgro.npehero.main.ErrorDisplay;
import net.sowgro.npehero.main.ErrorList;
import net.sowgro.npehero.main.Page;
import net.sowgro.npehero.main.Sound;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.Collections;

public class DiffList extends Page
{
    private final Button error;
    private final HBox content = new HBox();
    private final Level level;

    public DiffList(Level level, Page prev)
    {
        this.level = level;
        //sets up table view: requires special getters, setters and constructors to work
        TableView<Difficulty> diffs = new TableView<>();

        TableColumn<Difficulty,String> titleCol = new TableColumn<>("Name");
        TableColumn<Difficulty,String> validCol = new TableColumn<>("Valid?");

        titleCol.prefWidthProperty().bind(diffs.widthProperty().multiply(0.5));
        validCol.prefWidthProperty().bind(diffs.widthProperty().multiply(0.4));

        diffs.getColumns().add(titleCol);
        diffs.getColumns().add(validCol);

        titleCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().title));
        validCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().isValid() ? "Yes" : "No"));

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

        error = new Button();
        error.getStyleClass().add("red");
        error.setOnAction(_ -> {
            // TODO
            Driver.setMenu(new ErrorList(level.difficulties.problems, this));
        });
        refresh();

        Button edit = new Button("Edit");
        edit.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            Driver.setMenu(new DiffEditor(diffs.getSelectionModel().getSelectedItem(), this));
        });
        edit.setDisable(true);
        edit.disableProperty().bind(diffs.getSelectionModel().selectedItemProperty().isNull());

        Button remove = new Button("Delete");
        remove.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            try {
                level.difficulties.remove(diffs.getSelectionModel().getSelectedItem());
            } catch (IOException ex) {
                Driver.setMenu(new ErrorDisplay("Failed to remove difficulty", ex, this));
            }
        });
        remove.setDisable(true);
        remove.disableProperty().bind(diffs.getSelectionModel().selectedItemProperty().isNull());

        Button refresh = new Button("Refresh");
        refresh.setOnAction(_ -> {
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
                Driver.setMenu(new ErrorDisplay("Failed to move difficulty", e,this));
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
                Driver.setMenu(new ErrorDisplay("Failed to move difficulty", e,this));
            }
        });

        VBox buttons = new VBox();
        buttons.getChildren().addAll(create, edit, remove, moveUp, moveDown, refresh);
        buttons.setSpacing(10);

        BorderPane bp = new BorderPane();
        bp.setTop(buttons);
        bp.setBottom(error);

        HBox main = new HBox();
        main.getChildren().addAll(diffs, bp);
        main.setSpacing(10);
        main.prefHeightProperty().bind(content.prefHeightProperty().multiply(0.67));
        diffs.prefHeightProperty().bind(main.heightProperty());

        Button exit = new Button();
        exit.setText("Back");
        exit.setOnAction(_ -> {
            Sound.playSfx(Sound.BACKWARD);
            Driver.setMenu(prev);
        });

        create.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            FolderNameEntry.StringToVoidLambda next = (String name) -> {
                try {
                    Difficulty d = level.difficulties.add(name);
                    Driver.setMenu(new DiffEditor(d, new DiffList(level, prev)));
                } catch (FileAlreadyExistsException e) {
                    Driver.setMenu(new ErrorDisplay("Failed to add difficulty\nA difficulty already exists with the folder name " + e.getFile(), this));
                } catch (IOException e) {
                    Driver.setMenu(new ErrorDisplay("Failed to add difficulty", e, this));
                }
            };
            Driver.setMenu(new FolderNameEntry("difficulty", this, next));
        });

        VBox centerBox = new VBox();
        centerBox.getChildren().addAll(main, exit);
        centerBox.setSpacing(10);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.maxWidthProperty().bind(content.widthProperty().multiply(0.95));

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
            error.setVisible(false);
            error.setManaged(false);
        } else {
            error.setVisible(true);
            error.setManaged(true);
        }
    }
}