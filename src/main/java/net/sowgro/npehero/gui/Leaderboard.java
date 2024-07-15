package net.sowgro.npehero.gui;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import net.sowgro.npehero.Driver;
import net.sowgro.npehero.main.Difficulty;
import net.sowgro.npehero.main.LeaderboardEntry;
import net.sowgro.npehero.main.Level;
import net.sowgro.npehero.main.SoundController;

public class Leaderboard extends Pane
{
    /*
     * this class is a layout class, most of its purpose is to place UI elements like Buttons within Panes like VBoxes.
     * the creation of these UI elements are mostly not commented due to their repetitive and self explanatory nature.
     * style classes are defined in the style.css file.
     */
    public Leaderboard(Level level, Difficulty diff, Pane prev)
    {
        //sets up table view: requires java bean getters, setters and constructors to work
        TableView<LeaderboardEntry> scores = new TableView<LeaderboardEntry>();

        TableColumn<LeaderboardEntry, String> nameCol = new TableColumn<LeaderboardEntry, String>("Name");
        TableColumn<LeaderboardEntry, String> scoreCol = new TableColumn<LeaderboardEntry, String>("Score");
        TableColumn<LeaderboardEntry, String> dateCol = new TableColumn<LeaderboardEntry, String>("Date");

        scores.getColumns().add(nameCol);
        scores.getColumns().add(scoreCol);
        scores.getColumns().add(dateCol);

        nameCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getName()));
        scoreCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getScore() + ""));
        dateCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getDate()));

        scores.setItems(diff.getLeaderboard());

        scores.getStyleClass().add("unselectable");

        scores.prefWidthProperty().bind(super.prefWidthProperty().multiply(0.25)); 
        scores.prefHeightProperty().bind(super.prefHeightProperty().multiply(0.75));

        scoreCol.setSortType(SortType.DESCENDING);
        scores.getSortOrder().add(scoreCol);

        Button exit = new Button();
        exit.setText("Back");
        exit.setOnAction(e -> {
            SoundController.playSfx(SoundController.BACKWARD);
            Driver.setMenu(prev);
        });

        VBox centerBox = new VBox();
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setSpacing(10);
        centerBox.getChildren().addAll(scores,exit);
        centerBox.setMinWidth(400);

        HBox rootBox = new HBox();
        rootBox.prefWidthProperty().bind(super.prefWidthProperty()); 
        rootBox.prefHeightProperty().bind(super.prefHeightProperty());
        rootBox.getChildren().add(centerBox);
        rootBox.setAlignment(Pos.CENTER);

        super.getChildren().add(rootBox);
    }
}
