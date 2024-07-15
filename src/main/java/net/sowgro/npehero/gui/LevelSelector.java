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
import net.sowgro.npehero.main.Level;
import net.sowgro.npehero.main.LevelController;
import net.sowgro.npehero.main.SoundController;

public class LevelSelector extends Pane
{   
    /*
     * this class is a layout class, most of its purpose is to place UI elements like Buttons within Panes like VBoxes.
     * the creation of these UI elements are mostly not commented due to their repetitive and self explanatory nature.
     * style classes are defined in the style.css file.
     */
    public LevelSelector()
    {
        //sets up table view: requires special getters, setters and constructors to work
        TableView<Level> levels = new TableView<Level>();
        
        TableColumn<Level,String> titleCol = new TableColumn<Level,String>("Title");
        TableColumn<Level,String> artistCol = new TableColumn<Level,String>("Artist");

        levels.getColumns().add(titleCol);
        levels.getColumns().add(artistCol);

        titleCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getTitle()));
        artistCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getArtist()));

        levels.setItems(LevelController.getValidLevelList());

        levels.prefWidthProperty().bind(super.prefWidthProperty().multiply(0.25)); 
        levels.prefHeightProperty().bind(super.prefHeightProperty().multiply(0.75));
        levels.setMinWidth(300);


        Button exit = new Button();
        exit.setText("Back");
        exit.setOnAction(e -> {
            Driver.setMenu(new MainMenu());
            SoundController.playSfx(SoundController.BACKWARD);
        });

        VBox leftBox = new VBox();
        leftBox.setAlignment(Pos.CENTER_LEFT);
        leftBox.setSpacing(10);
        leftBox.getChildren().addAll(levels,exit);

        Pane rightBox = new Pane();
        addDetails(rightBox, levels);


        HBox rootBox = new HBox();
        rootBox.prefWidthProperty().bind(super.prefWidthProperty()); 
        rootBox.prefHeightProperty().bind(super.prefHeightProperty());
        rootBox.getChildren().addAll(leftBox, rightBox);
        rootBox.setAlignment(Pos.CENTER);
        rootBox.setSpacing(10);

        levels.getStyleClass().remove("list-view");
        //listens for change in selected item of the list
        levels.getSelectionModel().selectedItemProperty().addListener(_ -> addDetails(rightBox, levels));
        super.getChildren().add(rootBox);
    }

    /**
     * adds corresponding level details pane to the right side
     * @param rightBox
     * @param levels
     */
    private void addDetails(Pane rightBox, TableView<Level> levels)
    {
        VBox details = new LevelDetails(levels.getSelectionModel().getSelectedItem());
        if (! rightBox.getChildren().isEmpty())
        {
            rightBox.getChildren().remove(0);
        }
        rightBox.getChildren().add(details);
        details.prefWidthProperty().bind(super.prefWidthProperty().multiply(0.37)); 
        details.prefHeightProperty().bind(super.prefHeightProperty());
        details.maxWidthProperty().bind(super.prefWidthProperty().multiply(0.37)); 
        details.maxHeightProperty().bind(super.prefHeightProperty());
    }

}
