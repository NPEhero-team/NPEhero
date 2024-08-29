package net.sowgro.npehero.gui;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
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
        TableView<Level> levels = new TableView<>();
        
        TableColumn<Level,String> titleCol = new TableColumn<>("Title");
        TableColumn<Level,String> artistCol = new TableColumn<>("Artist");

        titleCol.prefWidthProperty().bind(levels.widthProperty().multiply(0.5));
        artistCol.prefWidthProperty().bind(levels.widthProperty().multiply(0.45));

        levels.getColumns().add(titleCol);
        levels.getColumns().add(artistCol);

        titleCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().title));
        artistCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().artist));

        levels.setItems(Levels.getValidList());
        levels.prefHeightProperty().bind(content.heightProperty().multiply(0.75));

        Button exit = new Button();
        exit.setText("Back");
        exit.setOnAction(_ -> {
            Driver.setMenu(new MainMenu());
            Sound.playSfx(Sound.BACKWARD);
        });

        VBox leftBox = new VBox();
        leftBox.setAlignment(Pos.CENTER_LEFT);
        leftBox.setSpacing(10);
        leftBox.getChildren().addAll(levels,exit);

        BorderPane rightBox = new BorderPane(new LevelDetails(levels.getSelectionModel().getSelectedItem(), this));
        rightBox.prefHeightProperty().bind(content.heightProperty());

        HBox centerBox = new HBox(leftBox, rightBox);
        centerBox.setSpacing(10);
        content.getChildren().addAll(centerBox);
        content.setAlignment(Pos.CENTER);
        leftBox.prefWidthProperty().bind(centerBox.widthProperty().multiply(0.4));
        rightBox.prefWidthProperty().bind(centerBox.widthProperty().multiply(0.6));
        centerBox.setPrefWidth(1200);
        centerBox.maxWidthProperty().bind(content.widthProperty().multiply(0.95));

        levels.getStyleClass().remove("list-view");
        //listens for change in selected item of the list
        levels.getSelectionModel().selectedItemProperty().addListener(_ -> {
            rightBox.setCenter(new LevelDetails(levels.getSelectionModel().getSelectedItem(), this));
        });
    }

    @Override
    public Pane getContent() {
        return content;
    }

}
