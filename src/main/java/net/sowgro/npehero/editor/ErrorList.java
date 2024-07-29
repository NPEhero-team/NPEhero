package net.sowgro.npehero.editor;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import net.sowgro.npehero.Driver;
import net.sowgro.npehero.gui.SettingsEditor;
import net.sowgro.npehero.main.Control;
import net.sowgro.npehero.main.Page;
import net.sowgro.npehero.main.Sound;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ErrorList extends Page {

    private HBox content = new HBox();

    public ErrorList(Map<String, Exception> list, Page prev) {

        GridPane controls = new GridPane();
        ScrollPane scrollPane = new ScrollPane(controls);
        scrollPane.getStyleClass().remove("scroll-pane");
        scrollPane.getStyleClass().add("box");
        scrollPane.setPadding(new Insets(10));
        controls.setPadding(new Insets(10));
        controls.setVgap(20);
        controls.setHgap(40);

        scrollPane.setPrefWidth(700);
        scrollPane.prefHeightProperty().bind(content.prefHeightProperty().multiply(0.75));

        Button exit = new Button();
        exit.setText("Back");
        exit.setOnAction(e -> {
            Sound.playSfx(Sound.BACKWARD);
            Driver.setMenu(prev);
        });

        VBox centerBox = new VBox();
        centerBox.getChildren().addAll(scrollPane, exit);
        centerBox.setSpacing(10);
        centerBox.setAlignment(Pos.CENTER);

        content.getChildren().add(centerBox);
        content.setAlignment(Pos.CENTER);

        int i = 0;
        for (Map.Entry<String, Exception> error : list.entrySet()) {
            // label
            Label label = new Label(error.getKey());
            controls.add(label, 0, i);

            // reset button
            Button resetButton = new Button("View Error");
            resetButton.setOnAction(_ -> Driver.setMenu(new ErrorDisplay("E: \n"+error.getValue(), this)));
            controls.add(resetButton, 1, i);
            i++;
        }
    }

    @Override
    public Pane getContent() {
        return content;
    }
}
