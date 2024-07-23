package net.sowgro.npehero.editor;

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
import net.sowgro.npehero.main.Levels;
import net.sowgro.npehero.main.Page;
import net.sowgro.npehero.main.Sound;

public class LevelList extends Page
{
    private HBox content = new HBox();

    public LevelList()
    {
        //sets up table view: requires special getters, setters and constructors to work
        TableView<Level> levels = new TableView<>();

        TableColumn<Level,String> titleCol = new TableColumn<>("Title");
        TableColumn<Level,String> artistCol = new TableColumn<>("Artist");
        TableColumn<Level,String> validCol = new TableColumn<>("Valid?");

        levels.getColumns().add(titleCol);
        levels.getColumns().add(artistCol);
        levels.getColumns().add(validCol);

        titleCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().title));
        artistCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().artist));
        validCol.setCellValueFactory(data -> {
            if (data.getValue().isValid) {
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

        Button edit = new Button("Edit");
        edit.setOnAction(e -> Driver.setMenu(new LevelEditor(levels.getSelectionModel().getSelectedItem(), this)));
        edit.setDisable(true);
        edit.disableProperty().bind(levels.getSelectionModel().selectedItemProperty().isNull());

        Button remove = new Button("Delete");
        remove.setOnAction(e -> Levels.remove(levels.getSelectionModel().getSelectedItem()));
        remove.setDisable(true);
        remove.disableProperty().bind(levels.getSelectionModel().selectedItemProperty().isNull());

        Button refresh = new Button("Refresh");
        refresh.setOnAction(e -> {
            Levels.readData();
            levels.setItems(Levels.list);
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
            Sound.playSfx(Sound.BACKWARD);
            Driver.setMenu(new MainMenu());
        });

        VBox centerBox = new VBox();
        centerBox.getChildren().addAll(main, exit);
        centerBox.setSpacing(10);
        centerBox.setAlignment(Pos.CENTER);

        content.getChildren().add(centerBox);
        content.setAlignment(Pos.CENTER);

        create.setOnAction(_ -> {
            if (create.isSelected()) {
                sidebar.getChildren().add(newLevelBox);
            }
            else {
                sidebar.getChildren().remove(newLevelBox);
            }
        });

        newLevelButton.setOnAction(_ -> {
            Levels.add(newLevelEntry.getText());
            newLevelEntry.clear();
            refresh.fire();
            sidebar.getChildren().clear();
            create.setSelected(false);
        });
    }

    @Override
    public Pane getContent() {
        return content;
    }
}