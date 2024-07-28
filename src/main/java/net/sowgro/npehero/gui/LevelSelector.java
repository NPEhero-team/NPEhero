package net.sowgro.npehero.gui;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import net.sowgro.npehero.Driver;
import net.sowgro.npehero.levelapi.Level;
import net.sowgro.npehero.levelapi.Levels;
import net.sowgro.npehero.main.Page;
import net.sowgro.npehero.main.Sound;

public class LevelSelector extends Page
{
    private final HBox content = new HBox();

    public LevelSelector()
    {
        //sets up table view: requires special getters, setters and constructors to work
        TableView<Level> levels = new TableView<Level>();
        
        TableColumn<Level,String> titleCol = new TableColumn<Level,String>("Title");
        TableColumn<Level,String> artistCol = new TableColumn<Level,String>("Artist");

        levels.getColumns().add(titleCol);
        levels.getColumns().add(artistCol);

        titleCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().title));
        artistCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().artist));

        levels.setItems(Levels.getValidList());

        levels.prefWidthProperty().bind(content.prefWidthProperty().multiply(0.25));
        levels.prefHeightProperty().bind(content.prefHeightProperty().multiply(0.75));
        levels.setMinWidth(300);


        Button exit = new Button();
        exit.setText("Back");
        exit.setOnAction(e -> {
            Driver.setMenu(new MainMenu());
            Sound.playSfx(Sound.BACKWARD);
        });

        VBox leftBox = new VBox();
        leftBox.setAlignment(Pos.CENTER_LEFT);
        leftBox.setSpacing(10);
        leftBox.getChildren().addAll(levels,exit);

        Pane rightBox = new Pane();
        addDetails(rightBox, levels);

        content.getChildren().addAll(leftBox, rightBox);
        content.setSpacing(10);
        content.setAlignment(Pos.CENTER);

        levels.getStyleClass().remove("list-view");
        //listens for change in selected item of the list
        levels.getSelectionModel().selectedItemProperty().addListener(_ -> addDetails(rightBox, levels));
    }

    @Override
    public Pane getContent() {
        return content;
    }

    /**
     * adds corresponding level details pane to the right side
     * @param rightBox
     * @param levels
     */
    private void addDetails(Pane rightBox, TableView<Level> levels)
    {
        VBox details = new LevelDetails(levels.getSelectionModel().getSelectedItem(), this);
        if (! rightBox.getChildren().isEmpty())
        {
            rightBox.getChildren().remove(0);
        }
        rightBox.getChildren().add(details);
        details.prefWidthProperty().bind(content.prefWidthProperty().multiply(0.37));
        details.prefHeightProperty().bind(content.prefHeightProperty());
        details.maxWidthProperty().bind(content.prefWidthProperty().multiply(0.37));
        details.maxHeightProperty().bind(content.prefHeightProperty());
    }

}
