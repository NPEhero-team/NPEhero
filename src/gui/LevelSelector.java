package gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import main.Level;

public class LevelSelector extends Pane
{   
    public LevelSelector()
    {
        ListView<Level> levels = new ListView<Level>();
        levels.setItems(main.LevelController.levelList);
        levels.prefWidthProperty().bind(super.prefWidthProperty().multiply(0.25)); 
        levels.prefHeightProperty().bind(super.prefHeightProperty().multiply(0.75));
        levels.setMinWidth(275);

        Button exit = new Button();
        exit.setText("Exit");
        exit.setOnAction(e -> Driver.setMenu(new MainMenu()));

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
        levels.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Level>() {

			@Override
			public void changed(ObservableValue<? extends Level> arg0, Level arg1, Level arg2) {
				addDetails(rightBox, levels);
			}	
		});
        super.getChildren().add(rootBox);
    }

    private void addDetails(Pane rightBox, ListView<Level> levels)
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
