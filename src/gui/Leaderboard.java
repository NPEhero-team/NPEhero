package gui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import main.Difficulty;
import main.LeaderboardEntry;
import main.Level;

public class Leaderboard extends Pane
{
    /*
     * this class is a layout class, most of its purpose is to place UI elements like Buttons within Panes like VBoxes.
     * the creation of these UI elements are mostly not commented due to their repetitive and self explanatory nature.
     * style classes are defined in the style.css file.
     */
    public Leaderboard(Level level, Difficulty diff, Pane prev)
    {
        //sets up table view: requires special getters, setters and constructors to work
        TableView<LeaderboardEntry> scores = new TableView<LeaderboardEntry>();

        TableColumn<LeaderboardEntry, String> nameCol = new TableColumn<LeaderboardEntry, String>("Name");
        TableColumn<LeaderboardEntry, String> scoreCol = new TableColumn<LeaderboardEntry, String>("Score");
        TableColumn<LeaderboardEntry, String> dateCol = new TableColumn<LeaderboardEntry, String>("Date");
        //scoreCol.minWidthProperty().bind(scores.widthProperty().subtract(nameCol.widthProperty()));

        scores.getColumns().add(nameCol);
        scores.getColumns().add(scoreCol);
        scores.getColumns().add(dateCol);

        nameCol.setCellValueFactory(new PropertyValueFactory<LeaderboardEntry, String>("name"));
        scoreCol.setCellValueFactory(new PropertyValueFactory<LeaderboardEntry, String>("score"));
        dateCol.setCellValueFactory(new PropertyValueFactory<LeaderboardEntry, String>("date"));

        scores.setItems(diff.getLeaderboard());

        scores.getStyleClass().add("unselectable");

        scores.prefWidthProperty().bind(super.prefWidthProperty().multiply(0.25)); 
        scores.prefHeightProperty().bind(super.prefHeightProperty().multiply(0.75));


        Button exit = new Button();
        exit.setText("Back");
        exit.setOnAction(e -> Driver.setMenu(prev));

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
