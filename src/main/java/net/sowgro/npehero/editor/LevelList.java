package net.sowgro.npehero.editor;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import net.sowgro.npehero.Driver;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import net.sowgro.npehero.gui.MainMenu;
import net.sowgro.npehero.levelapi.Level;
import net.sowgro.npehero.levelapi.Levels;
import net.sowgro.npehero.main.Page;
import net.sowgro.npehero.main.Sound;

import java.awt.*;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

public class LevelList extends Page
{
    private HBox content = new HBox();

    private final Label error;
    private final HBox errorBox;

    public LevelList()
    {
        //sets up table view: requires special getters, setters and constructors to work
        TableView<Level> levels = new TableView<>();

        TableColumn<Level,String> titleCol = new TableColumn<>("Title");
        TableColumn<Level,String> artistCol = new TableColumn<>("Artist");
        TableColumn<Level,String> validCol = new TableColumn<>("Valid?");

        titleCol.prefWidthProperty().bind(levels.widthProperty().multiply(0.4));
        artistCol.prefWidthProperty().bind(levels.widthProperty().multiply(0.4));
        validCol.prefWidthProperty().bind(levels.widthProperty().multiply(0.15));

        levels.getColumns().add(titleCol);
        levels.getColumns().add(artistCol);
        levels.getColumns().add(validCol);

        titleCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().title));
        artistCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().artist));
        validCol.setCellValueFactory(data -> {
            if (data.getValue().isValid()) {
                return new ReadOnlyStringWrapper("Yes");
            }
            else {
                return new ReadOnlyStringWrapper("No");
            }
        });

        levels.setItems(Levels.list);

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
        levels.setPrefWidth(600);
        levels.prefHeightProperty().bind(content.prefHeightProperty().multiply(0.75));

        error = new Label();
        errorBox = new HBox(error);
        errorBox.setSpacing(10);
        errorBox.setPadding(new Insets(10));
        errorBox.getStyleClass().addAll("box", "red");
        errorBox.setOnMouseClicked(_ -> {
            // TODO
            Driver.setMenu(new ErrorList(Levels.problems, this));
        });
        refresh();


        Button edit = new Button("Edit");
        edit.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            Driver.setMenu(new LevelEditor(levels.getSelectionModel().getSelectedItem(), this));
        });
        edit.setDisable(true);
        edit.disableProperty().bind(levels.getSelectionModel().selectedItemProperty().isNull());

        Button remove = new Button("Delete");
        remove.setOnAction(e -> {
            Sound.playSfx(Sound.FORWARD);
            try {
                Levels.remove(levels.getSelectionModel().getSelectedItem());
            } catch (IOException ex) {
                Driver.setMenu(new ErrorDisplay("Failed to remove this level\n"+e.toString(), this));
                ex.printStackTrace();
            }
        });
        remove.setDisable(true);
        remove.disableProperty().bind(levels.getSelectionModel().selectedItemProperty().isNull());

        Button refresh = new Button("Refresh");
        refresh.setOnAction(e -> {
            Sound.playSfx(Sound.FORWARD);
            try {
                Levels.readData();
            } catch (IOException ex) {
                Driver.setMenu(new ErrorDisplay("Failed to load levels: Level folder is missing\n"+e.toString(), this));
                ex.printStackTrace();
            }
            levels.setItems(Levels.list);
        });

        Button create = new Button("Create");

        Button viewFolder = new Button("Open Folder");
        viewFolder.setOnAction(_ -> new Thread(() -> {
            Sound.playSfx(Sound.FORWARD);
            try {
                Desktop.getDesktop().open(Levels.dir);
            } catch (IOException ex) {
                Driver.setMenu(new ErrorDisplay("Failed to open folder\n"+ex, this));
            }
        }).start());

        VBox buttons = new VBox();
        buttons.getChildren().addAll(create, edit, remove, refresh, viewFolder);
        buttons.setSpacing(10);



        HBox main = new HBox();
        main.getChildren().addAll(new VBox(levels, errorBox),buttons);
        main.setSpacing(10);

        Button exit = new Button();
        exit.setText("Back");
        exit.setOnAction(_ -> {
            Sound.playSfx(Sound.BACKWARD);
            Driver.setMenu(new MainMenu());
        });

        VBox centerBox = new VBox();
        centerBox.getChildren().addAll(main, exit);
        centerBox.setSpacing(10);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.maxWidthProperty().bind(content.widthProperty().multiply(0.95));

        content.getChildren().add(centerBox);
        content.setAlignment(Pos.CENTER);

        create.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            FolderNameEntry.StringToVoidLambda next = (String name) -> {
                try {
                    Level l = Levels.add(name);
                    Driver.setMenu(new LevelEditor(l, new LevelList()));
                } catch (FileAlreadyExistsException e) {
                    Driver.setMenu(new ErrorDisplay("Failed to add level\nA level already exists with the folder name '" + e.getFile() + "'", this));
                } catch (IOException e) {
                    Driver.setMenu(new ErrorDisplay("Failed to create level\n"+e, this));
                }
            };
            Driver.setMenu(new FolderNameEntry("level", this, next));
        });
    }

    @Override
    public Pane getContent() {
        return content;
    }

    public void refresh() {
        error.setText("Failed to load " + Levels.problems.size() + " level(s)");
        if (Levels.problems.isEmpty()) {
            errorBox.setVisible(false);
            errorBox.setManaged(false);
        } else {
            errorBox.setVisible(true);
            errorBox.setManaged(true);
        }
    }
}