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
import net.sowgro.npehero.levelapi.Difficulty;
import net.sowgro.npehero.levelapi.LeaderboardEntry;
import net.sowgro.npehero.main.*;

public class LeaderboardView extends Page
{
    private HBox content = new HBox();

    public LeaderboardView(Difficulty diff, Page prev)
    {
        //sets up table view: requires java bean getters, setters and constructors to work
        TableView<LeaderboardEntry> scores = new TableView<>();

        TableColumn<LeaderboardEntry, String> nameCol = new TableColumn<>("Name");
        TableColumn<LeaderboardEntry, String> scoreCol = new TableColumn<>("Score");
        TableColumn<LeaderboardEntry, String> dateCol = new TableColumn<>("Date");

        nameCol.prefWidthProperty().bind(scores.widthProperty().multiply(0.45));
        scoreCol.prefWidthProperty().bind(scores.widthProperty().multiply(0.23));
        dateCol.prefWidthProperty().bind(scores.widthProperty().multiply(0.27));

        scores.getColumns().add(nameCol);
        scores.getColumns().add(scoreCol);
        scores.getColumns().add(dateCol);

        nameCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().name));
        scoreCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().score + ""));
        dateCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().date));

        scores.setItems(diff.leaderboard.entries);

        scores.getStyleClass().add("unselectable");

        scores.prefWidthProperty().bind(content.prefWidthProperty().multiply(0.30));
        scores.prefHeightProperty().bind(content.prefHeightProperty().multiply(0.75));

        scoreCol.setSortType(SortType.DESCENDING);
        scores.getSortOrder().add(scoreCol);

        Button exit = new Button();
        exit.setText("Back");
        exit.setOnAction(e -> {
            Sound.playSfx(Sound.BACKWARD);
            Driver.setMenu(prev);
        });

        VBox centerBox = new VBox();
        centerBox.getChildren().addAll(scores, exit);
        centerBox.setSpacing(10);
        centerBox.setAlignment(Pos.CENTER);

        content.getChildren().add(centerBox);
        content.setAlignment(Pos.CENTER);
    }

    @Override
    public Pane getContent() {
        return content;
    }
}
